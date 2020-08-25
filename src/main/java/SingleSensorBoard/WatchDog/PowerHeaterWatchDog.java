package SingleSensorBoard.WatchDog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import SingleSensorBoard.Commands.HeaterCommands;
import SingleSensorBoard.ModeHeater;
import core.ATask;
import core.TaskManager;

public class PowerHeaterWatchDog implements PropertyChangeListener {

    private double _powerThreshold;
    private ModeHeater _heater;
    private TaskManager _TM;
    private HeaterCommands _Command;

    public boolean flagOn = false;

    public PowerHeaterWatchDog(ModeHeater heater, HeaterCommands Command, TaskManager TM, double powerThreshold) {
        _powerThreshold = powerThreshold;
        _heater = heater;
        _TM = TM;
        _Command = Command;
    }

    public void SetPowerThreshold(double powerThreshold) {
        _powerThreshold = powerThreshold;
    }

    public double getPowerThreshold() {
        return _powerThreshold;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (flagOn && evt.getPropertyName().equals("FinishedLoop"))
            if (_heater.getPower().lastElement().getY() > _powerThreshold) {
                _heater.getFeedBackController().reset();
                _heater.setFeedbakON(false);

                _TM.addTask(new ATask() {
                    @Override
                    public void execution() {
                        _Command.SetVoltageHeater(0);
                        JOptionPane.showMessageDialog(null,
                                "Wuf! Power on heater exceeding threshold. Turned off the FeedBack of the heater and setted voltage to zero.",
                                "PowerHeaterWatchDog", JOptionPane.WARNING_MESSAGE);
                    }
                });
            }
    }
}
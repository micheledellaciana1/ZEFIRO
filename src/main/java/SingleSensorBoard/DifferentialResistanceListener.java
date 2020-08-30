package SingleSensorBoard;

import java.util.Vector;

import SingleSensorBoard.Commands.VoltAmpMeterCommands;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import core.LoopManager;

public class DifferentialResistanceListener implements PropertyChangeListener {

    private boolean _flagON = false;

    private Vector<Point2D> _differentialResistance;

    private double _VoltageIncrement;

    private double _previousVoltage = 0;
    private double _previousCurrent = 0;

    private boolean _flagChangeVoltage = true;

    private VoltAmpMeterCommands _Commands;
    private ModeVoltAmpMeter _voltAmpMeter;
    public int millisDelay = 1000;
    private long _timeChangedVoltageFall;

    public PropertyChangeSupport ChangeSupport = new PropertyChangeSupport(this);

    public DifferentialResistanceListener(VoltAmpMeterCommands Commands, ModeVoltAmpMeter voltAmpMeter) {
        _Commands = Commands;
        _voltAmpMeter = voltAmpMeter;

        _differentialResistance = new Vector<Point2D>();
        _VoltageIncrement = 0.1;
    }

    public void setFlagON(boolean ON) {
        ChangeSupport.firePropertyChange("flagON", _flagON, ON);
        _flagON = ON;
    }

    public boolean getFlagON() {
        return _flagON;
    }

    public void setVoltageIncrement(double VoltageIncrement) {
        _VoltageIncrement = VoltageIncrement;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (_flagON && evt.getPropertyName().equals("FinishedLoop"))
            try {
                if (_flagChangeVoltage) {
                    _Commands.SetVoltageFall(_Commands.GetVoltageFall() + _VoltageIncrement);
                    _VoltageIncrement = -_VoltageIncrement;
                    _timeChangedVoltageFall = System.currentTimeMillis();
                    _flagChangeVoltage = false;
                    return;
                }

                if (System.currentTimeMillis() - _timeChangedVoltageFall < millisDelay)
                    return;

                float time = (float) ((System.currentTimeMillis() - LoopManager.startingTime) * 0.001);
                double VoltageFall = _voltAmpMeter.getVoltage().lastElement().getY();
                double Current = _voltAmpMeter.getCurrent().lastElement().getY();
                _flagChangeVoltage = true;

                double diffResistance = 0;
                if ((Current - _previousCurrent) != 0)
                    diffResistance = (VoltageFall - _previousVoltage) / (Current - _previousCurrent);
                _differentialResistance.add(new Point2D.Double(time, diffResistance));

                _previousVoltage = VoltageFall;
                _previousCurrent = Current;

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public Vector<Point2D> getDifferentialResitance() {
        return _differentialResistance;
    }

    public void EraseData() {
        _differentialResistance.clear();
        _VoltageIncrement = Math.abs(_VoltageIncrement);
        _flagChangeVoltage = true;
    }
}
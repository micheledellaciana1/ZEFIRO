package SingleSensorBoard.Menu;

import java.util.AbstractList;
import java.util.ArrayList;

import SingleSensorBoard.SingleSensorBoard;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import SingleSensorBoard.ModeChamberTemperature;
import SingleSensorBoard.ModeHeater;
import SingleSensorBoard.Commands.HeaterCommands;
import SingleSensorBoard.Commands.SingleBoardCommands;
import core.ATask;
import core.TaskManager;
import core.themal.LookUpTable;

import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CalibrateHeater implements PropertyChangeListener {

    private double _alpha;
    private double _beta;
    private ModeHeater _heater;
    private long _start;
    private double _oldPeriod;
    private ModeChamberTemperature _chamberTemperature;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("FinishedLoop")) {
            if (System.currentTimeMillis() - _start < 2000) {
                return;
            }

            ArrayList<Double> Resistance = new ArrayList<Double>();
            ArrayList<Double> Temperature = new ArrayList<Double>();

            double T0 = _chamberTemperature.getData().lastElement().getY();

            if (ValueIsStable(_heater.getResistance(), 10)) {
                double heaterResistance = _heater.getResistance().lastElement().getY();

                for (double T = 0; T < 2000; T += 0.1) {
                    Resistance.add(heaterResistance * (1 + (T - T0) * _alpha + (T - T0) * (T - T0) * _beta));
                    Temperature.add(T);
                }

                _heater.setLUT(new LookUpTable(Resistance, Temperature));
                _heater.ChangeSupport.removePropertyChangeListener(this);
                _heater.setPeriod(_oldPeriod);
            }
        }
    }

    public CalibrateHeater(double alpha, double beta, final TaskManager TM, final HeaterCommands commands,
            final ModeHeater heater, final ModeChamberTemperature chamberTemperature) {
        _oldPeriod = heater.getMillisPeriod();
        heater.setPeriod(2000);

        _alpha = alpha;
        _beta = beta;
        _heater = heater;
        _chamberTemperature = chamberTemperature;

        TM.addTask(new ATask() {
            @Override
            public void execution() {
                commands.SetVoltageHeater(0);
            }
        });

        _start = System.currentTimeMillis();
        _heater.setFeedbakON(false);
        _heater.ChangeSupport.addPropertyChangeListener(this);
    }

    static private boolean ValueIsStable(AbstractList<Point2D> values, int NPoints) {
        if (values.size() < NPoints)
            return false;

        SimpleRegression RO = new SimpleRegression(true);
        for (int i = values.size() - NPoints; i < values.size(); i++)
            RO.addData(values.get(i).getX(), values.get(i).getY());
        RegressionResults results = RO.regress();

        double slope = results.getParameterEstimate(1);
        double errSlope = results.getStdErrorOfEstimate(1);

        if ((slope - errSlope) * (slope + errSlope) <= 0)
            return true;
        return false;
    }
}
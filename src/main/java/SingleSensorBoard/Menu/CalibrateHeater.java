package SingleSensorBoard.Menu;

import java.util.AbstractList;
import java.util.ArrayList;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import SingleSensorBoard.ModeHeater;
import SingleSensorBoard.Commands.ICommands;
import core.ATask;
import core.TaskManager;
import core.themal.LookUpTable;

import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CalibrateHeater implements PropertyChangeListener {

    private double _T0;
    private double _alpha;
    private double _beta;
    private ModeHeater _heater;
    private long _start;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("FinishedLoop")) {
            if (System.currentTimeMillis() - _start < 1000) {
                return;
            }

            ArrayList<Double> Resistance = new ArrayList<Double>();
            ArrayList<Double> Temperature = new ArrayList<Double>();

            if (ValueIsStable(_heater.getResistance(), 100)) {
                double heaterResistance = _heater.getResistance().lastElement().getY();
                for (double T = 0; T < 2000; T += 0.1) {
                    Resistance.add(heaterResistance * (1 + (T - _T0) * _alpha + (T - _T0) * (T - _T0) * _beta));
                    Temperature.add(T);
                }

                _heater.setLUT(new LookUpTable(Resistance, Temperature));
                _heater.ChangeSupport.removePropertyChangeListener(this);
            }
        }
    }

    public CalibrateHeater(double T0, double alpha, double beta, final TaskManager TM, final ICommands commands,
            final ModeHeater heater) {
        _T0 = T0;
        _alpha = alpha;
        _beta = beta;
        _heater = heater;

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
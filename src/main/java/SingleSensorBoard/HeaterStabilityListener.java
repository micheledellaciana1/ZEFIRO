package SingleSensorBoard;

import java.util.AbstractList;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HeaterStabilityListener implements PropertyChangeListener {

    private ModeHeater _heater;
    private int _dimWindows;
    private Boolean _heaterIsStable = null;
    public PropertyChangeSupport ChangeSupport = new PropertyChangeSupport(this);

    public HeaterStabilityListener(ModeHeater Heater, int dimWindows) {
        _heater = Heater;
        _dimWindows = dimWindows;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("FinishedLoop"))
            if (readyToAcquire(_dimWindows)) {
                ChangeSupport.firePropertyChange("HeaterChangedStability", _heaterIsStable, Boolean.TRUE);
                _heaterIsStable = true;
            } else {
                ChangeSupport.firePropertyChange("HeaterChangedStability", _heaterIsStable, Boolean.FALSE);
                _heaterIsStable = false;
            }
    }

    private boolean readyToAcquire(int NPoints) {
        if (TemperatureIsStable(_heater.getTemperature(), NPoints))
            return true;
        return false;
    }

    private boolean TemperatureIsStable(AbstractList<Point2D> temperatures, int NPoints) {
        if (temperatures.size() < NPoints)
            return false;

        double centralTime = temperatures.get(temperatures.size() - NPoints / 2).getX();
        SimpleRegression RO = new SimpleRegression(true);
        for (int i = temperatures.size() - NPoints; i < temperatures.size(); i++)
            RO.addData(temperatures.get(i).getX() - centralTime, temperatures.get(i).getY());

        RegressionResults results = RO.regress();

        double slope = results.getParameterEstimate(1);
        double errSlope = results.getStdErrorOfEstimate(1);
        double intercept = results.getParameterEstimate(0);
        double errIntercept = results.getStdErrorOfEstimate(0);

        if ((slope - errSlope) * (slope + errSlope) <= 0)
            if ((intercept - _heater.getFeedBackController().getTarget() - errIntercept)
                    * (intercept - _heater.getFeedBackController().getTarget() + errIntercept) <= 0)
                return true;
        return false;
    }
}
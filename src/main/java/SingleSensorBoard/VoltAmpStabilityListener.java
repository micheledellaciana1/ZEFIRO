package SingleSensorBoard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.AbstractList;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.awt.geom.*;

public class VoltAmpStabilityListener implements PropertyChangeListener {

    private ModeVoltAmpMeter _voltAmpMeter;
    private int _dimWIndows;
    private Boolean _VoltAmpIsStable = null;
    public PropertyChangeSupport ChangeSupport = new PropertyChangeSupport(this);

    public VoltAmpStabilityListener(ModeVoltAmpMeter voltAmpMeter, int dimWIndows) {
        _dimWIndows = dimWIndows;
        _voltAmpMeter = voltAmpMeter;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("FinishedLoop")) {
            if (readyToAcquire(_dimWIndows)) {
                ChangeSupport.firePropertyChange("VoltAmpChangedStability", _VoltAmpIsStable, Boolean.TRUE);
                _VoltAmpIsStable = true;
            } else {
                ChangeSupport.firePropertyChange("VoltAmpChangedStability", _VoltAmpIsStable, Boolean.FALSE);
                _VoltAmpIsStable = false;
            }
        }
    }

    private boolean readyToAcquire(int NPoints) {
        if (_voltAmpMeter.getSize() > NPoints)
            if (ValueIsStable(_voltAmpMeter.getVoltage(), NPoints))
                if (ValueIsStable(_voltAmpMeter.getCurrent(), NPoints))
                    return true;
        return false;
    }

    private boolean ValueIsStable(AbstractList<Point2D> values, int NPoints) {
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
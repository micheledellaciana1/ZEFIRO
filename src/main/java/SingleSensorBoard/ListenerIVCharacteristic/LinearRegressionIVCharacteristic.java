package SingleSensorBoard.ListenerIVCharacteristic;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import SingleSensorBoard.IVCharacteristic;
import core.LoopManager;

public class LinearRegressionIVCharacteristic implements PropertyChangeListener {

    private ArrayList<Vector<Point2D>> _datas;
    private IVCharacteristic _ivCharacteristic;

    public LinearRegressionIVCharacteristic(IVCharacteristic ivCharacteristic) {
        _datas = new ArrayList<Vector<Point2D>>();
        _datas.add(new Vector<Point2D>());
        _datas.add(new Vector<Point2D>());
        _ivCharacteristic = ivCharacteristic;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("FinishedIVCharacteristic")) {
            float time = (float) ((System.currentTimeMillis() - LoopManager.startingTime) * 0.001);

            SimpleRegression RO = new SimpleRegression(true);
            for (int i = 0; i < _ivCharacteristic.getActualCharacteristic().size(); i++)
                RO.addData(_ivCharacteristic.getActualCharacteristic().get(i).getX(),
                        _ivCharacteristic.getActualCharacteristic().get(i).getY());

            RegressionResults result = RO.regress();

            _datas.get(0).add(new Point2D.Double(time, result.getParameterEstimate(0)));
            _datas.get(1).add(new Point2D.Double(time, result.getParameterEstimate(1)));

            // System.out.println(result.getParameterEstimate(0));
            // System.out.println(result.getParameterEstimate(1));
        }
    }

    public Vector<Point2D> getIntercept() {
        return _datas.get(0);
    }

    public Vector<Point2D> getSlope() {
        return _datas.get(1);
    }

    public void EraseData() {
        for (Vector<Point2D> vector : _datas) {
            vector.clear();
        }
    }
}
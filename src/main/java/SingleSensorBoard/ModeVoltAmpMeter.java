package SingleSensorBoard;

import java.util.*;

import SingleSensorBoard.Commands.ICommands;
import core.*;

import java.awt.geom.Point2D;

public class ModeVoltAmpMeter extends AMultipleDataStream {
    private ICommands _Commands;

    public ModeVoltAmpMeter(String name, long period, ICommands Commands) {
        super(name, period, 3);
        _Commands = Commands;
    }

    @Override
    public ArrayList<Double> acquireData() {
        ArrayList<Double> data = new ArrayList<Double>();
        double voltage = _Commands.measureVoltageFall();
        double current = _Commands.measureCurrent();
        double resistance = voltage / current;

        data.add(voltage);
        data.add(current);
        data.add(resistance);

        return data;
    }

    public Vector<Point2D> getVoltage() {
        return _datas.get(0);
    }

    public Vector<Point2D> getCurrent() {
        return _datas.get(1);
    }

    public Vector<Point2D> getResistance() {
        return _datas.get(2);
    }
}
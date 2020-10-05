package SingleSensorBoard;

import java.util.*;

import SingleSensorBoard.Commands.VoltAmpMeterCommands;
import core.*;

import java.awt.geom.Point2D;

public class ModeVoltAmpMeter extends AMultipleDataStream {
    private VoltAmpMeterCommands _Commands;

    public ModeVoltAmpMeter(String name, long period, VoltAmpMeterCommands Commands) {
        super(name, period, 3);
        _Commands = Commands;
    }

    @Override
    public ArrayList<Double> acquireData() {
        ArrayList<Double> data = new ArrayList<Double>();

        double voltage = 0;
        double current = 0;
        double resistance = 0;

        voltage = _Commands.measureVoltageFall();
        current = _Commands.measureCurrent();

        data.add(voltage);
        data.add(current);

        if (current != 0) {
            resistance = voltage / current;
            data.add(resistance);
        } else {
            data.add(Double.MAX_VALUE);
        }

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
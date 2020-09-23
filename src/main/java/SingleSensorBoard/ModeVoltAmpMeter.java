package SingleSensorBoard;

import java.util.*;

import SingleSensorBoard.Commands.VoltAmpMeterCommands;
import core.*;

import java.awt.geom.Point2D;

public class ModeVoltAmpMeter extends AMultipleDataStream {
    private VoltAmpMeterCommands _Commands;
    public int numberOfAverange = 10;

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

        int count1 = 0;
        int count2 = 0;

        for (int i = 0; i < numberOfAverange; i++) {
            voltage += _Commands.measureVoltageFall();
            current += _Commands.measureCurrent();
            count1++;
            if (current != 0) {
                resistance += voltage / current;
                count2++;
            }
        }

        data.add(voltage / count1);
        data.add(current / count1);
        data.add(resistance / count2);

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
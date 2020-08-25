package SingleSensorBoard.Commands;

import core.SerialBuffer;

/**
 * VoltAmpMeterCommands
 */
public class VoltAmpMeterCommands extends ACommands {
    public VoltAmpMeterCommands(SerialBuffer serial) {
        super(serial);
    }

    private double _VoltageFallSetted;

    public void SetVoltageFall(double value) {
        _VoltageFallSetted = value;
        if (!isASimulation) {
            _serial.println("ApplyVoltageFallEvent");
            _serial.println(Double.toString(value));
        }
    }

    public double GetVoltageFall() {
        return _VoltageFallSetted;
    }

    private long _AvernageTimeADC;

    public void setAverangeTimeADC(long value) {
        _AvernageTimeADC = value;
        if (!isASimulation) {
            _serial.println("EventAverangeTimeADC");
            _serial.println(Long.toString(value));
        }
    }

    public long getAverangeTimeADC() {
        return _AvernageTimeADC;
    }

    public double measureVoltageFall() {
        if (isASimulation)
            return _VoltageFallSetted;

        _serial.println("ReadVoltageFallEvent");
        String VoltageFall = null;
        while (VoltageFall == null)
            VoltageFall = _serial.readLine();
        return Double.valueOf(VoltageFall);
    }

    public double measureCurrent() {
        if (isASimulation)
            return _VoltageFallSetted;

        _serial.println("ReadCurrentEvent");
        String Current = null;
        while (Current == null)
            Current = _serial.readLine();
        return Double.valueOf(Current);
    }

    private boolean flagAutorangeAmpMeter = false;

    public void setAutorangeAmpMeter(boolean flag) {
        flagAutorangeAmpMeter = flag;

        if (!isASimulation)
            if (flag)
                _serial.println("EventEnableAmpMeterCCAutoRange");
            else
                _serial.println("EventDisableAmpMeterCCAutoRange");
    }

    public boolean getAutorangeAmpMeter() {
        return flagAutorangeAmpMeter;
    }

    public void setAmpMeterRange(int IndexRange) {
        if (!isASimulation) {
            _serial.println("EventSelectResistance");
            _serial.println(Integer.toString(IndexRange));
        }
    }

    public int getAmpMeterRange() { // todo
        int IndexRange = 0;
        return IndexRange;
    }

    public double getAmpMeterResistor() {
        return 0;
    }

    @Override
    public void Reset() {
        SetVoltageFall(0);
        setAutorangeAmpMeter(true);
    }
}
package SingleSensorBoard.Commands;

import core.SerialBuffer;

public class HeaterCommands extends ACommands {
    public HeaterCommands(SerialBuffer serial) {
        super(serial);
    }

    private double _VoltageHeaterSetted;

    public void SetVoltageHeater(double value) {
        _VoltageHeaterSetted = value;
        if (!isASimulation) {
            _serial.println("EventSetHeaterVoltage");
            _serial.println(Double.toString(value));
        }
    }

    public double GetVoltageHeater() {
        return _VoltageHeaterSetted;
    }

    public double measureResistanceHeater() {
        if (isASimulation)
            return 0 + Math.random() * 0.1;
        ;

        _serial.println("EventMeasureHeaterResistance");
        String resistanceHeater = null;
        while (resistanceHeater == null)
            resistanceHeater = _serial.readLine();

        return Double.valueOf(resistanceHeater);
    }

    public double measurePowerHeater() {
        if (isASimulation)
            return 0 + Math.random() * 0.1;
        ;

        return 0;
        /*
         * _serial.println("EventGetPowerHeater"); String PowerHeater = null; while
         * (PowerHeater == null) PowerHeater = _serial.readLine();
         * 
         * return Double.valueOf(PowerHeater);
         */
    }

    @Override
    public void Reset() {
        SetVoltageHeater(0);
    }
}
package SingleSensorBoard.Commands;

import core.SerialBuffer;

public class TempHumidityCommands extends ACommands {

    public TempHumidityCommands(SerialBuffer serial) {
        super(serial);
    }

    public double getChamberHumidity() {
        if (isASimulation)
            return 0 + Math.random() * 0.1;;
        _serial.println("EventMeasureRHChamber");
        String ChamberHumidity = null;
        while (ChamberHumidity == null)
            ChamberHumidity = _serial.readLine();
        return Double.valueOf(ChamberHumidity);
    }

    public double getChamberTemperature() {
        if (isASimulation)
            return 0 + Math.random() * 0.1;;

        _serial.println("EventMeasureBoardTemperature");
        String ChamberTemperature = null;
        while (ChamberTemperature == null)
            ChamberTemperature = _serial.readLine();
        return Double.valueOf(ChamberTemperature);
    }
}
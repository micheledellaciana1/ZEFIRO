package SingleSensorBoard;

import SingleSensorBoard.Commands.TempHumidityCommands;
import core.ADataStream;

public class ModeChamberTemperature extends ADataStream {

    private TempHumidityCommands _Commands;

    public ModeChamberTemperature(TempHumidityCommands Commands, String name, double period) {
        super(name, period);
        _Commands = Commands;
    }

    @Override
    public double acquireData() {
        return _Commands.getChamberTemperature();
    }
}
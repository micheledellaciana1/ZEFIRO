package SingleSensorBoard;

import SingleSensorBoard.Commands.TempHumidityCommands;
import core.ADataStream;

public class ModeChamberHumidity extends ADataStream {

    private TempHumidityCommands _Commands;

    public ModeChamberHumidity(TempHumidityCommands Commands, String name, double period) {
        super(name, period);
        _Commands = Commands;
    }

    @Override
    public double acquireData() {
        return _Commands.getChamberHumidity();
    }
}
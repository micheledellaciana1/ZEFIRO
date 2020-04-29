package SingleSensorBoard;

import SingleSensorBoard.Commands.ICommands;
import core.ADataStream;

public class ModeChamberHumidity extends ADataStream {

    private ICommands _Commands;

    public ModeChamberHumidity(ICommands Commands, String name, double period) {
        super(name, period);
        _Commands = Commands;
    }

    @Override
    public double acquireData() {
        return _Commands.getChamberHumidity();
    }
}
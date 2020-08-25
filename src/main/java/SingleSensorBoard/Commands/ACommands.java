package SingleSensorBoard.Commands;

import core.SerialBuffer;

public class ACommands {

    public boolean isASimulation;
    protected SerialBuffer _serial;

    public ACommands() {
        isASimulation = true;
    }

    public ACommands(SerialBuffer serial) {
        isASimulation = true;
        _serial = serial;
    }

    public void Reset() {
    }
}
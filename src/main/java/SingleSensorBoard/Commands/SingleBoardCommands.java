package SingleSensorBoard.Commands;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;

import core.SerialBuffer;

public class SingleBoardCommands extends ACommands {
	private HeaterCommands _HeaterCommands;
	private VoltAmpMeterCommands _VoltAmpMeterCommands;
	private TempHumidityCommands _TempHumidityCommands;

	SerialBuffer _serial;

	public HeaterCommands getHeaterCommands() {
		return _HeaterCommands;
	}

	public VoltAmpMeterCommands getVoltAmpMeterCommands() {
		return _VoltAmpMeterCommands;
	}

	public TempHumidityCommands getTempHumidityCommands() {
		return _TempHumidityCommands;
	}

	public SingleBoardCommands() {
		super();

		SerialPort port = null;

		if (SerialPort.getCommPorts().length == 0) {
			_serial = null;
			_HeaterCommands = new HeaterCommands(_serial);
			_VoltAmpMeterCommands = new VoltAmpMeterCommands(_serial);
			_TempHumidityCommands = new TempHumidityCommands(_serial);
			return;
		}

		if (SerialPort.getCommPorts().length == 1)
			port = SerialPort.getCommPorts()[Integer.valueOf(0)];

		if (SerialPort.getCommPorts().length > 1) {
			while (true) {
				String labelPanel = "Input Port index: \n Ports available: \n";
				for (int i = 0; i < SerialPort.getCommPorts().length; i++) {
					labelPanel += Integer.toString(i) + ". " + SerialPort.getCommPorts()[i].getSystemPortName() + "\n";
				}

				String Input = JOptionPane.showInputDialog(null, labelPanel);

				try {
					port = SerialPort.getCommPorts()[Integer.valueOf(Input)];
					break;
				} catch (Exception e) {
				}
			}
		}

		port.openPort();
		port.setBaudRate(9600);
		_serial = new SerialBuffer(port);
		_HeaterCommands = new HeaterCommands(_serial);
		_HeaterCommands.isASimulation = false;
		_VoltAmpMeterCommands = new VoltAmpMeterCommands(_serial);
		_VoltAmpMeterCommands.isASimulation = false;
		_TempHumidityCommands = new TempHumidityCommands(_serial);
		_TempHumidityCommands.isASimulation = false;
		isASimulation = false;
		_serial.EstablishConnection();

		Reset();
	}

	private boolean flagExternlFeedback = false;

	public void setFeedbackExternal(boolean flag) { // todo
		flagExternlFeedback = flag;
	}

	private boolean flagSumExternalSignal = false;

	public void setSumInputWithExternalSignal(boolean flag) { // todo
		flagSumExternalSignal = flag;
	}

	public boolean getFeedbackExternal() {
		return flagExternlFeedback;
	}

	public boolean getSumInputWithExternalSignal() { // todo
		return flagSumExternalSignal;
	}

	@Override
	public void Reset() {
		_HeaterCommands.Reset();
		_TempHumidityCommands.Reset();
		_VoltAmpMeterCommands.Reset();

		setFeedbackExternal(false);
		setSumInputWithExternalSignal(false);
	}

	public void closeDevice() {
		Reset();
		if (!isASimulation)
			_serial.println("EventCloseSerial");
	}
}

package SingleSensorBoard.Commands;

import com.fazecast.jSerialComm.SerialPort;

import core.SerialBuffer;

public class SingleBoardCommands implements ICommands {
	private static SingleBoardCommands _instance = new SingleBoardCommands();
	SerialBuffer _serial;

	static public SingleBoardCommands getInstance() {
		return _instance;
	}

	private SingleBoardCommands() {
	}

	@Override
	public void initCommands() {
		SerialPort port = SerialPort.getCommPorts()[0];
		port.setBaudRate(230400);
		_serial = new SerialBuffer(port);

		_serial.EstablishConnection();

		ResetDevice();
	}

	private double _VoltageFall = 0;

	public void SetVoltageFall(double value) {
		_VoltageFall = value;
		_serial.println("ApplyVoltageFallEvent");
		_serial.println(Double.toString(value));
	}

	public double GetVoltageFall() {
		return _VoltageFall;
	}

	public void setAverangeTimeADC(long value) {
		_serial.println("EventAverangeTimeADC");
		_serial.println(Long.toString(value));
	}

	public double measureVoltageFall() {
		_serial.println("ReadVoltageFallEvent");
		String VoltageFall = null;
		while (VoltageFall == null)
			VoltageFall = _serial.readLine();
		return Double.valueOf(VoltageFall);
	}

	public double measureCurrent() {
		_serial.println("ReadCurrentEvent");
		String Current = null;
		while (Current == null)
			Current = _serial.readLine();
		return Double.valueOf(Current);
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

	public boolean getSumInputWithExternalSignal() {
		return flagSumExternalSignal;
	}

	private double _VoltageHeater = 0;

	public void SetVoltageHeater(double value) {
		_serial.println("SetSupplyValueEvent");
		_serial.println("0"); // todo
		_serial.println(Double.toString(value));
	}

	public double GetVoltageHeater() {
		return _VoltageHeater;
	}

	public double measureResistanceHeater() {
		_serial.println("MeasureValueResistorEvent");
		_serial.println("0"); // todo
		String resistanceHeater = null;
		while (resistanceHeater == null)
			resistanceHeater = _serial.readLine();
		return Double.valueOf(resistanceHeater);
	}

	public double measurePowerHeater() { // todo
		return _VoltageHeater;
	}

	private boolean flagAutorangeAmpMeter = false;

	public void setAutorangeAmpMeter(boolean flag) {
		flagAutorangeAmpMeter = flag;
		if (flag)
			_serial.println("EventEnaleAmpMeterCCAutoRange");
		else
			_serial.println("EventDisableAmpMeterCCAutoRange");
	}

	public boolean getAutorangeAmpMeter() {
		return flagAutorangeAmpMeter;
	}

	public void setAmpMeterRange(int IndexRange) {
		_serial.println("EventSelectResistance");
		_serial.println(Integer.toString(IndexRange));
	}

	public int getAmpMeterRange() { // todo
		int IndexRange = 0;
		return IndexRange;
	}

	public double getChamberHumidity() { // todo
		return 0.;
	}

	public double getChamberTemperature() { // todo
		return 0.;
	}

	public void ResetDevice() {
		SetVoltageFall(0);
		SetVoltageHeater(0);
		setAutorangeAmpMeter(true);
		setFeedbackExternal(false);
		setSumInputWithExternalSignal(false);
	}
}

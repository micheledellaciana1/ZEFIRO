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
		SerialPort port = SerialPort.getCommPorts()[0];
		port.setBaudRate(230400);
		_serial = new SerialBuffer(port);

		_serial.EstablishConnection();

		ResetDevice();
	}

	private double _VoltageFallSetted;

	@Override
	public void SetVoltageFall(double value) {
		_VoltageFallSetted = value;
		_serial.println("ApplyVoltageFallEvent");
		_serial.println(Double.toString(value));
	}

	@Override
	public double GetVoltageFall() {
		return _VoltageFallSetted;
	}

	private long _AvernageTimeADC;

	@Override
	public void setAverangeTimeADC(long value) {
		_AvernageTimeADC = value;
		_serial.println("EventAverangeTimeADC");
		_serial.println(Long.toString(value));
	}

	@Override
	public long getAverangeTimeADC() {
		return _AvernageTimeADC;
	}

	@Override
	public double measureVoltageFall() {
		_serial.println("ReadVoltageFallEvent");
		String VoltageFall = null;
		while (VoltageFall == null)
			VoltageFall = _serial.readLine();
		return Double.valueOf(VoltageFall);
	}

	@Override
	public double measureCurrent() {
		_serial.println("ReadCurrentEvent");
		String Current = null;
		while (Current == null)
			Current = _serial.readLine();
		return Double.valueOf(Current);
	}

	private boolean flagExternlFeedback = false;

	@Override
	public void setFeedbackExternal(boolean flag) { // todo
		flagExternlFeedback = flag;
	}

	private boolean flagSumExternalSignal = false;

	@Override
	public void setSumInputWithExternalSignal(boolean flag) { // todo
		flagSumExternalSignal = flag;
	}

	@Override
	public boolean getFeedbackExternal() {
		return flagExternlFeedback;
	}

	@Override
	public boolean getSumInputWithExternalSignal() { // todo
		return flagSumExternalSignal;
	}

	private double _VoltageHeaterSetted;

	@Override
	public void SetVoltageHeater(double value) {
		_VoltageHeaterSetted = value;
		_serial.println("SetSupplyValueEvent");
		_serial.println("0"); // todo
		_serial.println(Double.toString(value));
	}

	@Override
	public double GetVoltageHeater() {
		return _VoltageHeaterSetted;
	}

	@Override
	public double measureResistanceHeater() {
		_serial.println("MeasureValueResistorEvent");
		_serial.println("0"); // todo
		String resistanceHeater = null;
		while (resistanceHeater == null)
			resistanceHeater = _serial.readLine();
		return Double.valueOf(resistanceHeater);
	}

	@Override
	public double measurePowerHeater() { // todo
		return 0;
	}

	private boolean flagAutorangeAmpMeter = false;

	@Override
	public void setAutorangeAmpMeter(boolean flag) {
		flagAutorangeAmpMeter = flag;
		if (flag)
			_serial.println("EventEnaleAmpMeterCCAutoRange");
		else
			_serial.println("EventDisableAmpMeterCCAutoRange");
	}

	@Override
	public boolean getAutorangeAmpMeter() {
		return flagAutorangeAmpMeter;
	}

	@Override
	public void setAmpMeterRange(int IndexRange) {
		_serial.println("EventSelectResistance");
		_serial.println(Integer.toString(IndexRange));
	}

	@Override
	public int getAmpMeterRange() { // todo
		int IndexRange = 0;
		return IndexRange;
	}

	@Override
	public double getChamberHumidity() { // todo
		return 0.;
	}

	@Override
	public double getChamberTemperature() { // todo
		return 0.;
	}

	@Override
	public double getAmpMeterResistor() {
		return 0;
	}

	@Override
	public void ResetDevice() {
		SetVoltageFall(0);
		SetVoltageHeater(0);
		setAutorangeAmpMeter(true);
		setFeedbackExternal(false);
		setSumInputWithExternalSignal(false);
	}

	@Override
	public void closeDevice() { // todo
	}
}

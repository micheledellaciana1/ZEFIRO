package SingleSensorBoard.Commands;

public class SimCommands implements ICommands {
	private static SimCommands _instance = new SimCommands();

	static public SimCommands getInstance() {
		return _instance;
	}

	private SimCommands() {
	}

	@Override
	public void initCommands() {
	}

	private double _VoltageFall = 0;

	public void SetVoltageFall(double value) { // todo
		_VoltageFall = value;
	}

	public double GetVoltageFall() {
		return _VoltageFall;
	}

	public void setAverangeTimeADC(long value) { // todo
	}

	public double measureVoltageFall() { // todo
		return _VoltageFall + Math.random() * 0.01;
	}

	public double measureCurrent() { // todo
		return _VoltageFall + Math.random() * 0.01;
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
	private double T0 = 0; // todo

	public void SetVoltageHeater(double value) { // todo
		_VoltageHeater = value;
		T0 = System.nanoTime();
	}

	public double GetVoltageHeater() {
		return _VoltageHeater;
	}

	public double measureResistanceHeater() { // todo
		double resistance = 0;
		return 0.5 * _VoltageHeater * Math.exp(-1. / ((System.nanoTime() - T0) * 1e-5)) + 12 + Math.random() * 0.5;
	}

	public double measurePowerHeater() { // todo
		return _VoltageHeater;
	}

	private boolean flagAutorangeAmpMeter = true;

	public void setAutorangeAmpMeter(boolean flag) { // todo
		flagAutorangeAmpMeter = flag;
	}

	public boolean getAutorangeAmpMeter() {
		return flagAutorangeAmpMeter;
	}

	private int _IndexRange = 0;

	public void setAmpMeterRange(int IndexRange) { // todo
		_IndexRange = IndexRange;
	}

	public int getAmpMeterRange() { // todo
		return _IndexRange;
	}

	public double getAmpMeterResistor() { // todo
		return Math.pow(10, 2 + _IndexRange);
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

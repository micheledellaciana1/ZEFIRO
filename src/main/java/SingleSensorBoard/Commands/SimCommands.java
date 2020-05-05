package SingleSensorBoard.Commands;

/*
Simulate a resistor with Resistance of 10 KOhm and heater with zero thermal mass
*/

public class SimCommands implements ICommands {
	private static SimCommands _instance = new SimCommands();

	static public SimCommands getInstance() {
		return _instance;
	}

	private SimCommands() {
	}

	private double _VoltageFallSetted = 0;

	@Override
	public void SetVoltageFall(double value) {
		_VoltageFallSetted = value;
	}

	@Override
	public double GetVoltageFall() {
		return _VoltageFallSetted;
	}

	private long _AverangeTimeADC = 0;

	@Override
	public void setAverangeTimeADC(long value) {
		_AverangeTimeADC = value;
	}

	@Override
	public long getAverangeTimeADC() {
		return _AverangeTimeADC;
	}

	@Override
	public double measureVoltageFall() {
		return _VoltageFallSetted + Math.random() * 0.05;
	}

	@Override
	public double measureCurrent() {
		return (_VoltageFallSetted + Math.random() * 0.05) / 10;
	}

	private boolean flagExternlFeedback = false;

	@Override
	public void setFeedbackExternal(boolean flag) {
		flagExternlFeedback = flag;
	}

	private boolean flagSumExternalSignal = false;

	@Override
	public void setSumInputWithExternalSignal(boolean flag) {
		flagSumExternalSignal = flag;
	}

	@Override
	public boolean getFeedbackExternal() {
		return flagExternlFeedback;
	}

	@Override
	public boolean getSumInputWithExternalSignal() {
		return flagSumExternalSignal;
	}

	private double _VoltageHeaterSetted = 0;

	@Override
	public void SetVoltageHeater(double value) {
		_VoltageHeaterSetted = value;
	}

	@Override
	public double GetVoltageHeater() {
		return _VoltageHeaterSetted;
	}

	@Override
	public double measureResistanceHeater() {
		return 0.1 * _VoltageHeaterSetted + Math.random() * 0.1 + 12;
	}

	@Override
	public double measurePowerHeater() {
		return _VoltageHeaterSetted;
	}

	private boolean flagAutorangeAmpMeter = true;

	@Override
	public void setAutorangeAmpMeter(boolean flag) {
		flagAutorangeAmpMeter = flag;
	}

	@Override
	public boolean getAutorangeAmpMeter() {
		return flagAutorangeAmpMeter;
	}

	private int _IndexRange = 0;

	@Override
	public void setAmpMeterRange(int IndexRange) {
		_IndexRange = IndexRange;
	}

	@Override
	public int getAmpMeterRange() {
		return _IndexRange;
	}

	@Override
	public double getAmpMeterResistor() {
		return Math.pow(10, 2 + _IndexRange);
	}

	@Override
	public double getChamberHumidity() {
		return 55 + Math.random() * 5;
	}

	@Override
	public double getChamberTemperature() { // todo
		return 24 + Math.random() * 0.1;
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
	public void closeDevice() {
		// TODO Auto-generated method stub
	}
}

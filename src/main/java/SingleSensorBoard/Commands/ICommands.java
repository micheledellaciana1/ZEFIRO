package SingleSensorBoard.Commands;

public interface ICommands {

    public void SetVoltageFall(double value);

    public double GetVoltageFall();

    public void setAverangeTimeADC(long value);

    public long getAverangeTimeADC();

    public double measureVoltageFall();

    public double measureCurrent();

    public void setFeedbackExternal(boolean flag);

    public void setSumInputWithExternalSignal(boolean flag);

    public boolean getFeedbackExternal();

    public boolean getSumInputWithExternalSignal();

    public void SetVoltageHeater(double value);

    public double GetVoltageHeater();

    public double measureResistanceHeater();

    public double measurePowerHeater();

    public void setAutorangeAmpMeter(boolean flag);

    public boolean getAutorangeAmpMeter();

    public void setAmpMeterRange(int IndexRange);

    public int getAmpMeterRange();

    public double getAmpMeterResistor();

    public double getChamberHumidity();

    public double getChamberTemperature();

    public void ResetDevice();

    public void closeDevice();
}
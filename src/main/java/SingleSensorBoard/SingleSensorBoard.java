package SingleSensorBoard;

import java.util.Vector;

import org.jfree.chart.ChartPanel;
import java.awt.event.*;

import SingleSensorBoard.Commands.SingleBoardCommands;
import SingleSensorBoard.ListenerIVCharacteristic.LinearRegressionIVCharacteristic;
import SingleSensorBoard.Menu.*;
import SingleSensorBoard.WatchDog.PowerHeaterWatchDog;
import core.*;

import java.awt.geom.Point2D;

public class SingleSensorBoard extends ChartFrame {

	static private SingleSensorBoard _instance = new SingleSensorBoard(new ModChartPanel(), "ZEFIRO");
	static private TaskManager _TMInstace;
	static private SingleBoardCommands _commands;
	static private ModeVoltAmpMeter _voltAmpMeter;
	static private ModeHeater _heater;
	static private IVCharacteristic _ivCharacteristic;
	static private ITCharacteristic _itCharacteristic;
	static private LinearRegressionIVCharacteristic _LRIVCharacteristic;
	static private PowerHeaterWatchDog _PHWatchDog;
	static private ModeChamberHumidity _ChamberHumidity;
	static private ModeChamberTemperature _ChamberTemperature;
	static private DifferentialResistanceListener _differentialResistance;
	static private MenuEditorSingleSensorBoard _MenuEditor;

	private SingleSensorBoard(ChartPanel panel, String Title) {
		super(panel, Title);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				_TMInstace.addTask(new ATask() {
					@Override
					public void execution() {
						ResetUI();
						_commands.closeDevice();
						System.out.println("Closing serial...");

						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}

						System.exit(0);
					}
				});
			}
		});

		_TMInstace = new TaskManager("TMSingleSensorBoard");
		_commands = new SingleBoardCommands();
		_voltAmpMeter = new ModeVoltAmpMeter("VoltAmpMeter", 250, _commands.getVoltAmpMeterCommands());
		_heater = new ModeHeater("Heater", 250, _commands.getHeaterCommands());
		_ivCharacteristic = new IVCharacteristic(_commands.getVoltAmpMeterCommands(), _voltAmpMeter);
		_itCharacteristic = new ITCharacteristic(_voltAmpMeter, _heater);
		_LRIVCharacteristic = new LinearRegressionIVCharacteristic(_ivCharacteristic);
		_PHWatchDog = new PowerHeaterWatchDog(_heater, _commands.getHeaterCommands(), _TMInstace, 0);
		_ChamberHumidity = new ModeChamberHumidity(_commands.getTempHumidityCommands(), "ChamberHumidity", 250);
		_ChamberTemperature = new ModeChamberTemperature(_commands.getTempHumidityCommands(), "ChamberTemperature",
				250);
		_differentialResistance = new DifferentialResistanceListener(_commands.getVoltAmpMeterCommands(),
				_voltAmpMeter);
		_MenuEditor = new MenuEditorSingleSensorBoard(this, _commands.getHeaterCommands(),
				_commands.getVoltAmpMeterCommands(), _commands.getTempHumidityCommands(), _commands, _TMInstace);

		_heater.ChangeSupport.addPropertyChangeListener(_PHWatchDog);
		_ivCharacteristic.ChangeSupport.addPropertyChangeListener(_LRIVCharacteristic);

		_commands.Reset();
	}

	public void buildMenuBar() {
		setJMenuBar(_MenuEditor.constructMenuBar());
	}

	static public SingleSensorBoard getInstance() {
		return _instance;
	}

	static public TaskManager getTaskManager() {
		return _TMInstace;
	}

	static public SingleBoardCommands getCommands() {
		return _commands;
	}

	static public ModeVoltAmpMeter getVoltAmpMeter() {
		return _voltAmpMeter;
	}

	static public ModeHeater getHeater() {
		return _heater;
	}

	static public DifferentialResistanceListener getDifferentialResistanceListener() {
		return _differentialResistance;
	}

	static public IVCharacteristic getIVCharacteristic() {
		return _ivCharacteristic;
	}

	static public ITCharacteristic getITCharacteristic() {
		return _itCharacteristic;
	}

	static public LinearRegressionIVCharacteristic getLRIVCharacteristic() {
		return _LRIVCharacteristic;
	}

	static public PowerHeaterWatchDog getPHWatchDog() {
		return _PHWatchDog;
	}

	static public ModeChamberHumidity getChamberHumidity() {
		return _ChamberHumidity;
	}

	static public ModeChamberTemperature getChamberTemperature() {
		return _ChamberTemperature;
	}

	static public MenuEditorSingleSensorBoard getMenuEditor() {
		return _MenuEditor;
	}

	public void displayDifferentialResistanceVsTime() {
		clearData();
		addSeries(_differentialResistance.getDifferentialResitance(), "Diff. Resistance");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Diff. Resistance [KOhm]");
	}

	public void displayVoltageVsTime() {
		clearData();
		addSeries(_voltAmpMeter.getVoltage(), "Voltage");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Voltage [V]");
	}

	public void displayCurrentVsTime() {
		clearData();
		addSeries(_voltAmpMeter.getCurrent(), "Current");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Current [mA]");
	}

	public void displayResistanceVsTime() {
		clearData();
		addSeries(_voltAmpMeter.getResistance(), "Resistance");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Resistance [KOhm]");
	}

	public void displayTemperatureVsTime() {
		clearData();
		addSeries(_heater.getTemperature(), "Temperature");
		addSeries(_heater.getTargetTemperature(), "Target Temperature");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Temperature [°C]");
	}

	public void displayResistanceHeaterVsTime() {
		clearData();
		addSeries(_heater.getResistance(), "Resistance");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Resistance [Ohm]");
	}

	public void displayPowerVsTime() {
		clearData();
		addSeries(_heater.getPower(), "Power");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Power [au]");
	}

	public void displayVoltageHeaterVsTime() {
		clearData();
		addSeries(_heater.getVoltageHeater(), "Voltage");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Voltage [au]");
	}

	public void displayChamberHumidity() {
		clearData();
		addSeries(_ChamberHumidity.getData(), "Humidity");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Humidity [%]");
	}

	public void displayChambertTemperature() {
		clearData();
		addSeries(_ChamberTemperature.getData(), "Chamber Temperature");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Temperature [°C]");
	}

	public void displaySlopeLinearRegressionIVCharacteristic() {
		clearData();
		addSeries(_LRIVCharacteristic.getSlope(), "Slope linear Regression IV Characteristic");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Slope [1/KOhm]");
	}

	public void displayInterceptLinearRegressionIVCharacteristic() {
		clearData();
		addSeries(_LRIVCharacteristic.getIntercept(), "Intercept linear Regression IV Characteristic");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Time [S]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Intercept [mA]");
	}

	public void displayIVCharacteristic() {
		clearData();
		addSeries(_ivCharacteristic.getActualCharacteristic(), "IVCharacteristic");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Voltage [V]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Current [mA]");
	}

	public void displayIVCharacteristicHistorical() {
		clearData();

		Vector<Point2D> CharacteristicHistory = new Vector<Point2D>();

		for (int i = 0; i < _ivCharacteristic.getoldCharacteristics().size(); i++)
			for (int j = 0; j < _ivCharacteristic.getoldCharacteristics().get(i).size(); j++)
				CharacteristicHistory.add(_ivCharacteristic.getoldCharacteristics().get(i).get(j));

		for (int i = 0; i < _ivCharacteristic.getActualCharacteristic().size(); i++)
			CharacteristicHistory.add(_ivCharacteristic.getActualCharacteristic().get(i));

		addSeries(CharacteristicHistory, "IVCharacteristicHistorical");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Voltage [V]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Current [mA]");
	}

	public void displayITCharacteristic() {
		clearData();
		addSeries(_itCharacteristic.getActualCharacteristic(), "ITCharacteristic");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Temperature [°C]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Current [mA]");
	}

	public void displayArreniusPlot() {
		clearData();
		addSeries(_itCharacteristic.getActualArreniusPlot(), "ArreniusPlot");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("1/Temperature [1/°C]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Current [mA]");
	}

	public void displayITCharacteristicHistorical() {
		clearData();

		Vector<Point2D> CharacteristicHistory = new Vector<Point2D>();

		for (int i = 0; i < _itCharacteristic.getoldCharacteristics().size(); i++)
			for (int j = 0; j < _itCharacteristic.getoldCharacteristics().get(i).size(); j++)
				CharacteristicHistory.add(_itCharacteristic.getoldCharacteristics().get(i).get(j));

		for (int i = 0; i < _itCharacteristic.getActualCharacteristic().size(); i++)
			CharacteristicHistory.add(_itCharacteristic.getActualCharacteristic().get(i));

		addSeries(CharacteristicHistory, "ITCharacteristicHistorical");
		_panel.getChart().getXYPlot().getDomainAxis().setLabel("Temperature [°C]");
		_panel.getChart().getXYPlot().getRangeAxis().setLabel("Current [mA]");
	}

	public void clearEveryChart() {
		_heater.EraseData();
		_voltAmpMeter.EraseData();
		_ChamberHumidity.EraseData();
		_ChamberTemperature.EraseData();
		_ivCharacteristic.EraseData();
		_itCharacteristic.EraseData();
		_LRIVCharacteristic.EraseData();
		_differentialResistance.EraseData();
	}

	public void ResetUI() {
		_heater.EraseData();
		_voltAmpMeter.EraseData();
		_ChamberHumidity.EraseData();
		_ChamberTemperature.EraseData();
		_ivCharacteristic.EraseData();
		_itCharacteristic.EraseData();
		_LRIVCharacteristic.EraseData();
		_differentialResistance.EraseData();
		_differentialResistance.setFlagON(false);
		_itCharacteristic.setFlagON(false);
		_ivCharacteristic.setFlagON(false);
		_heater.setFeedbakON(false);
		_heater.setTargetTemperature(0);
	}
}
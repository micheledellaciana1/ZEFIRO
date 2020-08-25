package SingleSensorBoard.Menu;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import SingleSensorBoard.*;
import SingleSensorBoard.Commands.HeaterCommands;
import SingleSensorBoard.Commands.SingleBoardCommands;
import SingleSensorBoard.Commands.TempHumidityCommands;
import SingleSensorBoard.Commands.VoltAmpMeterCommands;
import core.ATask;
import core.ChartFrame;
import core.LoopManager;
import core.MenuEditorChartFrame;
import core.TaskManager;

public class MenuEditorSingleSensorBoard extends MenuEditorChartFrame {

	protected HeaterCommands _HeaterCommands;
	protected VoltAmpMeterCommands _VoltAmpMeterCommands;
	protected TempHumidityCommands _TempHumidityCommands;
	protected SingleBoardCommands _SingleBoardCommands;
	protected TaskManager _TM;

	public MenuEditorSingleSensorBoard(ChartFrame chart, HeaterCommands HCommands, VoltAmpMeterCommands VACommands,
			TempHumidityCommands THCommands, SingleBoardCommands SBCommands, TaskManager TM) {
		super(chart);
		_HeaterCommands = HCommands;
		_VoltAmpMeterCommands = VACommands;
		_TempHumidityCommands = THCommands;
		_SingleBoardCommands = SBCommands;
		_TM = TM;
	}

	public JMenuBar constructMenuBar() {
		JMenuBar menu = new JMenuBar();
		menu.add(BuildFileMenu());
		menu.add(BuildDisplayMenu());
		menu.add(BuildSetMenu());
		menu.add(BuildAdvancedMenu());
		return menu;
	}

	protected JMenu BuildDisplayMenuMeasurment() {
		JMenu menu = new JMenu("Meausurement");

		menu.add(new AbstractAction("Voltage") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayVoltageVsTime();
			}
		});

		menu.add(new AbstractAction("Current") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayCurrentVsTime();
			}
		});

		menu.add(new AbstractAction("Resistance") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayResistanceVsTime();
			}
		});

		return menu;
	}

	protected JMenu BuildDiffResistance() {
		JMenu menu = new JMenu("Diff. Resistance");
		menu.add(new AbstractAction("Diff. Resistance") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayDifferentialResistanceVsTime();
			}
		});

		return menu;
	}

	protected JMenu BuildDisplayIVCharacteristic() {
		JMenu menu = new JMenu("IV-Characteristic>");

		menu.add(new AbstractAction("Characteristic") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayIVCharacteristic();
			}
		});

		menu.add(new AbstractAction("CharacteristicHistorical") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayIVCharacteristicHistorical();
			}
		});

		menu.add(new AbstractAction("Slope") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displaySlopeLinearRegressionIVCharacteristic();
			}
		});

		menu.add(new AbstractAction("Intercept") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayInterceptLinearRegressionIVCharacteristic();
			}
		});

		return menu;
	}

	protected JMenu BuildDisplayITCharacteristic() {
		JMenu menu = new JMenu("IT-Characteristic>");

		menu.add(new AbstractAction("Characteristic") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayITCharacteristic();
			}
		});

		menu.add(new AbstractAction("CharacteristicHistorical") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayITCharacteristicHistorical();
			}
		});

		return menu;
	}

	protected JMenu BuildDisplayMenuHeater() {
		JMenu menu = new JMenu("Heater");

		menu.add(new AbstractAction("Temperature") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayTemperatureVsTime();
			}
		});

		menu.add(new AbstractAction("Resistance") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayResistanceHeaterVsTime();
			}
		});

		menu.add(new AbstractAction("Power") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayPowerVsTime();
			}
		});

		menu.add(new AbstractAction("Voltage") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayVoltageHeaterVsTime();
			}
		});

		return menu;

	}

	protected JMenu BuildDisplayChamberMenu() {
		JMenu menu = new JMenu("Chamber");

		menu.add(new AbstractAction("Temperature") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayChambertTemperature();
			}
		});

		menu.add(new AbstractAction("Humidity") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SingleSensorBoard.getInstance().displayChamberHumidity();
			}
		});

		return menu;
	}

	protected JMenu BuildDisplayMenu() {
		JMenu menu = new JMenu("Display");

		menu.add(BuildDisplayMenuMeasurment());
		menu.add(BuildDisplayMenuHeater());
		menu.add(BuildDisplayChamberMenu());
		menu.add(BuildDisplayIVCharacteristic());
		menu.add(BuildDisplayITCharacteristic());
		menu.add(BuildDiffResistance());
		menu.add(BuildDuplicateView());

		return menu;
	}

	protected JMenu BuildSetMenu() {
		JMenu menu = new JMenu("Set");

		menu.add(BuildSetMenuMeasurment());
		menu.add(BuildSetMenuHeater(SingleSensorBoard.getHeater()));
		menu.add(BuildSetMenuIVCharacteristic());
		menu.add(BuildSetMenuITCharacteristic());
		menu.add(BuildSetMenuDiffResistance());

		return menu;
	}

	protected JMenu BuildSetMenuMeasurment() {
		JMenu menu = new JMenu("Measurement");

		menu.add(new AbstractAction("Voltage fall") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog("Set Voltage fall:");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							_VoltAmpMeterCommands.SetVoltageFall(Double.valueOf(answer));
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		return menu;
	}

	protected JMenu BuildSetMenuHeater(final ModeHeater heater) {
		JMenu menu = new JMenu("Heater");
		menu.add(new AbstractAction("Temperature") {
			@Override
			public void actionPerformed(ActionEvent e) {
				String answer = JOptionPane.showInputDialog("Set Temperature");
				try {
					heater.getFeedBackController().set_target_value(Double.valueOf(answer));
				} catch (Exception _e) {
					if (verbose) {
						JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
						_e.printStackTrace();
					}
				}
			}
		});

		menu.add(new AbstractAction("Voltage") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog("Set Voltage");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							_HeaterCommands.SetVoltageHeater(Double.valueOf(answer));
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		menu.add(BuildFeedbackMenu(heater));
		menu.add(BuildCalibrationHeaterMenu(heater));

		return menu;
	}

	protected JMenu BuildFeedbackMenu(final ModeHeater heater) {
		JMenu menu = new JMenu("Feedback");

		// Add feedbackOnOff
		final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
		final AbstractAction action = new AbstractAction("ON") {
			@Override
			public void actionPerformed(ActionEvent e) {
				heater.setFeedbakON(checkbox.getState());
			}
		};

		checkbox.setAction(action);
		menu.add(checkbox);

		// Add a MenuListener that check the status off checkbox when menu is selected
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (checkbox.getState() != heater.getFeedbakON())
					checkbox.setState(heater.getFeedbakON());
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		menu.add(new ActionSetFeedbackParameters("Parameters", heater.getFeedBackController().getParameters(0) * 10,
				heater.getFeedBackController().getParameters(1) * 10,
				heater.getFeedBackController().getParameters(2) * 10,
				heater.getFeedBackController().getParameters(3) * 10, heater));

		return menu;
	}

	protected JMenu BuildSetMenuIVCharacteristic() {
		final IVCharacteristic _ivCharacteristic = SingleSensorBoard.getIVCharacteristic();
		final DifferentialResistanceListener _diffRes = SingleSensorBoard.getDifferentialResistanceListener();
		final VoltAmpStabilityListener _VoltAmpStabilityListener = SingleSensorBoard.getVoltAmpStabilityListener();

		JMenu menu = new JMenu("IV-Characteristic");

		// Add feedbackOnOff
		final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
		final AbstractAction action = new AbstractAction("ON") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkbox.getState() == true) {
					// remove listeners of ivCharacteristic
					_diffRes.setFlagON(false);
					_VoltAmpStabilityListener.ChangeSupport.removePropertyChangeListener(_diffRes);
					_VoltAmpStabilityListener.ChangeSupport.addPropertyChangeListener(_ivCharacteristic);
				} else {
					_VoltAmpStabilityListener.ChangeSupport.removePropertyChangeListener(_ivCharacteristic);
				}

				_ivCharacteristic.setFlagON(checkbox.getState());
			}
		};

		checkbox.setAction(action);
		menu.add(checkbox);

		// Add a MenuListener that check the status off checkbox when menu is selected
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (checkbox.getState() != _ivCharacteristic.getFlagON())
					checkbox.setState(_ivCharacteristic.getFlagON());
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		menu.add(new AbstractAction("Voltage Range") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog("Set Voltage Range: <Min> <Max> <Step>");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							String[] values = answer.split(" ");
							_ivCharacteristic.define_VPATH(Double.valueOf(values[0]), Double.valueOf(values[1]),
									Double.valueOf(values[2]));
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		return menu;
	}

	protected JMenu BuildSetMenuDiffResistance() {
		final IVCharacteristic _ivCharacteristic = SingleSensorBoard.getIVCharacteristic();
		final DifferentialResistanceListener _diffRes = SingleSensorBoard.getDifferentialResistanceListener();
		final VoltAmpStabilityListener _VoltAmpStabilityListener = SingleSensorBoard.getVoltAmpStabilityListener();

		JMenu menu = new JMenu("Diff. Resistance");

		// Add feedbackOnOffs
		final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
		final AbstractAction action = new AbstractAction("ON") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkbox.getState() == true) {
					// remove listeners of ivCharacteristic
					_ivCharacteristic.setFlagON(false);
					_VoltAmpStabilityListener.ChangeSupport.removePropertyChangeListener(_ivCharacteristic);
					//
					_VoltAmpStabilityListener.ChangeSupport.addPropertyChangeListener(_diffRes);
				} else {
					_VoltAmpStabilityListener.ChangeSupport.removePropertyChangeListener(_diffRes);
				}

				_diffRes.setFlagON(checkbox.getState());
			}
		};

		checkbox.setAction(action);
		menu.add(checkbox);

		// Add a MenuListener that check the status off checkbox when menu is selected
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (checkbox.getState() != _diffRes.getFlagON())
					checkbox.setState(_diffRes.getFlagON());
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		menu.add(new AbstractAction("Voltage Increment") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog("Set Voltage increment: <Voltage increment>");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							_diffRes.setVoltageIncrement(Double.valueOf(answer));
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		return menu;
	}

	protected JMenu BuildSetMenuITCharacteristic() {
		final ITCharacteristic _itCharacteristic = SingleSensorBoard.getITCharacteristic();
		final HeaterStabilityListener _HeaterStabilityListener = SingleSensorBoard.getHeaterStabilityListener();
		final VoltAmpStabilityListener _VoltAmpStabilityListener = SingleSensorBoard.getVoltAmpStabilityListener();

		JMenu menu = new JMenu("IT-Characteristic");

		// Add feedbackOnOff
		final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
		final AbstractAction action = new AbstractAction("ON") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkbox.getState() == true) {
					_HeaterStabilityListener.ChangeSupport.addPropertyChangeListener(_itCharacteristic);
					_VoltAmpStabilityListener.ChangeSupport.addPropertyChangeListener(_itCharacteristic);
				} else {
					_HeaterStabilityListener.ChangeSupport.removePropertyChangeListener(_itCharacteristic);
					_VoltAmpStabilityListener.ChangeSupport.removePropertyChangeListener(_itCharacteristic);
				}

				_itCharacteristic.setFlagON(checkbox.getState());
			}
		};

		checkbox.setAction(action);
		menu.add(checkbox);

		// Add a MenuListener that check the status off checkbox when menu is selected
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (checkbox.getState() != _itCharacteristic.getFlagON())
					checkbox.setState(_itCharacteristic.getFlagON());
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		menu.add(new AbstractAction("Temperature Range") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog("Set Temperature Range: <Min> <Max> <Step>");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							String[] values = answer.split(" ");
							_itCharacteristic.define_TPATH(Double.valueOf(values[0]), Double.valueOf(values[1]),
									Double.valueOf(values[2]));
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		return menu;
	}

	protected JMenu BuildFileMenu() {
		JMenu menu = new JMenu("File");

		menu.add(BuildChartPropertyMenu());
		menu.add(BuildExportMenu());

		menu.add(new AbstractAction("Clear Chart") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {

					@Override
					public void execution() {
						int answer = JOptionPane.showConfirmDialog(null, "Are you sure to erase all data?", "Clean all",
								JOptionPane.YES_NO_OPTION);
						try {
							switch (answer) {
								case 0:
									LoopManager.startingTime = System.currentTimeMillis();
									SingleSensorBoard.getInstance().clearEveryChart();
									break;
								case 1:
									break;
							}
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		menu.add(new AbstractAction("Reset") {

			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {

					@Override
					public void execution() {
						int answer = JOptionPane.showConfirmDialog(null,
								"Are you sure to erase all data and reset device?", "Clean all",
								JOptionPane.YES_NO_OPTION);
						try {
							switch (answer) {
								case 0:
									LoopManager.startingTime = System.currentTimeMillis();
									SingleSensorBoard.getInstance().ResetUI();
									_SingleBoardCommands.Reset();
									break;
								case 1:
									break;
							}
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		menu.add(new AbstractAction("Actual settings") {

			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						JOptionPane.showMessageDialog(null, "IVCharacteristic: "
								+ Boolean.toString(SingleSensorBoard.getIVCharacteristic().getFlagON()) + "\n"
								+ "ITCharacteristic: "
								+ Boolean.toString(SingleSensorBoard.getITCharacteristic().getFlagON()) + "\n"
								+ "Temperature Feedback: "
								+ Boolean.toString(SingleSensorBoard.getHeater().getFeedbakON()) + "\n"
								+ "Setted temperature: "
								+ Double.toString(SingleSensorBoard.getHeater().getFeedBackController().getTarget())
								+ " °C" + "\n" + "Setted voltage Fall: "
								+ Double.toString(_VoltAmpMeterCommands.GetVoltageFall()) + " [V]" + "\n"
								+ "Setted voltage heater: " + Double.toString(_HeaterCommands.GetVoltageHeater())
								+ " [a.u]" + "\n" + "Ext. Feedback: "
								+ Boolean.toString(_SingleBoardCommands.getFeedbackExternal()) + "\n"
								+ "Bias with ext.: "
								+ Boolean.toString(_SingleBoardCommands.getSumInputWithExternalSignal()) + "\n"
								+ "Amp. meter autorange: "
								+ Boolean.toString(_VoltAmpMeterCommands.getAutorangeAmpMeter()) + "\n"
								+ "Amp. meter range: " + Integer.toString(_VoltAmpMeterCommands.getAmpMeterRange())
								+ "\n" + "Amp. meter ref. Resistor: "
								+ Double.toString(_VoltAmpMeterCommands.getAmpMeterResistor() / 1000.) + "[kOhm]",
								"Settings", JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}
		});

		return menu;
	}

	protected JMenu BuildAdvancedMenu() {
		JMenu menu = new JMenu("Advanced");
		menu.add(BuildAmpMeterMenu());
		menu.add(BuildADCMenu());

		final JCheckBoxMenuItem checkboxExtFeedback = new JCheckBoxMenuItem();
		final AbstractAction actionExtFeedback = new AbstractAction("Ext. Feedback") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						_SingleBoardCommands.setFeedbackExternal(checkboxExtFeedback.getState());
					}
				});
			}
		};

		checkboxExtFeedback.setAction(actionExtFeedback);
		menu.add(checkboxExtFeedback);

		final JCheckBoxMenuItem checkboxSumExternalSignal = new JCheckBoxMenuItem();
		final AbstractAction ActionSumExternalSignal = new AbstractAction("Sum Ext. Signal") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						_SingleBoardCommands.setSumInputWithExternalSignal(checkboxSumExternalSignal.getState());
					}
				});
			}
		};

		checkboxSumExternalSignal.setAction(ActionSumExternalSignal);
		menu.add(checkboxSumExternalSignal);

		// Add a MenuListener that check the status off checkboxs when menu is selected
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				if (checkboxExtFeedback.getState() != _SingleBoardCommands.getFeedbackExternal())
					checkboxExtFeedback.setState(_SingleBoardCommands.getFeedbackExternal());

				if (checkboxSumExternalSignal.getState() != _SingleBoardCommands.getSumInputWithExternalSignal())
					checkboxSumExternalSignal.setState(_SingleBoardCommands.getSumInputWithExternalSignal());
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		return menu;
	}

	protected JMenu BuildAmpMeterMenu() {

		JMenu menu = new JMenu("Amp Meter");

		final JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
		AbstractAction action = new AbstractAction("Autorange") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						_VoltAmpMeterCommands.setAutorangeAmpMeter(checkBox.getState());
					}
				});
			}
		};

		checkBox.setAction(action);
		menu.add(checkBox);
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						checkBox.setState(_VoltAmpMeterCommands.getAutorangeAmpMeter());
					}
				});
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});

		menu.add(new AbstractAction("Set Ref. resistor") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog(null, "Enter index of ref. resistor [0-7]");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							_VoltAmpMeterCommands.setAmpMeterRange(Integer.valueOf(answer));
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		return menu;
	}

	protected JMenu BuildADCMenu() {
		JMenu menu = new JMenu("ADC");
		menu.add(new AbstractAction("Set Averange time ADC") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog(null, "Set Averange time ADC (ms)");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							_VoltAmpMeterCommands.setAverangeTimeADC(Integer.valueOf(answer));
						} catch (Exception _e) {
							if (verbose) {
								JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
								_e.printStackTrace();
							}
						}
					}
				});
			}
		});

		return menu;
	}

	protected JMenu BuildCalibrationHeaterMenu(final ModeHeater heater) {
		JMenu menu = new JMenu("Calibration");

		menu.add(BuildCalibrationHeaterMenuItem(heater));
		menu.add(new AbstractAction("Display") {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Vector<Point2D> data = new Vector<Point2D>();
					for (int i = 0; i < heater.getLUT().get_XArray().size(); i++)
						data.add(new Point2D.Double(heater.getLUT().get_XArray().get(i),
								heater.getLUT().get_YArray().get(i)));

					_chart.clearData();
					_chart.addSeries(data, "Calibration");
					_chart.GetChartPanel().getChart().getXYPlot().getDomainAxis().setLabel("Resistance [Ohm]");
					_chart.GetChartPanel().getChart().getXYPlot().getRangeAxis().setLabel("Temperature [°C]");
				} catch (Exception _e) {
					if (verbose) {
						JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
						_e.printStackTrace();
					}
				}
			}
		});

		return menu;
	}

	protected JMenuItem BuildCalibrationHeaterMenuItem(final ModeHeater heater) {
		JMenuItem item = new JMenuItem(new AbstractAction("Calibrate heater") {
			@Override
			public void actionPerformed(ActionEvent e) {
				String answer = JOptionPane.showInputDialog(null, "Enter calibration parameters: <alpha> <beta>");
				try {
					String values[] = answer.split(" ");
					CalibrateHeater c = new CalibrateHeater(Double.valueOf(values[0]), Double.valueOf(values[1]), _TM,
							_HeaterCommands, heater, SingleSensorBoard.getChamberTemperature());
					JOptionPane.showMessageDialog(null, "Calibrating...", "Calibration",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception _e) {
					if (verbose) {
						JOptionPane.showMessageDialog(null, _e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
						_e.printStackTrace();
					}
				}
			}
		});

		return item;
	}
}
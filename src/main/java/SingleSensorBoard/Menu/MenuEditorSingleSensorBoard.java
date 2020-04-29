package SingleSensorBoard.Menu;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import SingleSensorBoard.*;
import SingleSensorBoard.Commands.ICommands;
import core.ATask;
import core.DataManager;
import core.LoopManager;
import core.TaskManager;

public class MenuEditorSingleSensorBoard {

	private TaskManager _TM;

	public MenuEditorSingleSensorBoard(TaskManager TM) {
		_TM = TM;
	}

	public JMenuBar constructMenuBar() {
		JMenuBar menu = new JMenuBar();
		menu.add(BuildFileMenu());
		menu.add(BuildDisplayMenu());
		menu.add(BuildSetMenu());
		menu.add(BuildAdvancedMenu(SingleSensorBoard.getCommands()));
		return menu;
	}

	private JMenu BuildDisplayMenuMeasurment() {
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

	private JMenu BuildDisplayIVCharacteristic() {
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

	private JMenu BuildDisplayITCharacteristic() {
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

	private JMenu BuildDisplayMenuHeater() {
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

	private JMenu BuildDisplayChamberMenu() {
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

	private JMenu BuildDisplayMenu() {
		JMenu menu = new JMenu("Display");

		menu.add(BuildDisplayMenuMeasurment());
		menu.add(BuildDisplayMenuHeater());
		menu.add(BuildDisplayChamberMenu());
		menu.add(BuildDisplayIVCharacteristic());
		menu.add(BuildDisplayITCharacteristic());

		return menu;
	}

	private JMenu BuildSetMenu() {
		JMenu menu = new JMenu("Set");

		menu.add(BuildSetMenuMeasurment());
		menu.add(BuildSetMenuHeater(SingleSensorBoard.getHeater()));
		menu.add(BuildSetMenuIVCharacteristic());
		menu.add(BuildSetMenuITCharacteristic());

		return menu;
	}

	private JMenu BuildSetMenuMeasurment() {
		JMenu menu = new JMenu("Measurement");

		menu.add(new AbstractAction("Voltage fall") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String answer = JOptionPane.showInputDialog("Set Voltage fall:");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							SingleSensorBoard.getCommands().SetVoltageFall(Double.valueOf(answer));
						} catch (Exception e2) {
							e2.printStackTrace();
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
				} catch (Exception e2) {
					e2.printStackTrace();
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
							heater.getCommands().SetVoltageHeater(Double.valueOf(answer));
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
			}
		});

		menu.add(BuildFeedbackMenu(heater));

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

		menu.add(new ActionSetFeedbackParameters("Parameters", 5, 5, 1, 10, heater));
		return menu;
	}

	private JMenu BuildSetMenuIVCharacteristic() {
		final IVCharacteristic _ivCharacteristic = SingleSensorBoard.getIVCharacteristic();
		final ITCharacteristic _itCharacteristic = SingleSensorBoard.getITCharacteristic();

		JMenu menu = new JMenu("IV-Characteristic");

		// Add feedbackOnOff
		final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
		final AbstractAction action = new AbstractAction("ON") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_ivCharacteristic.setFlagON(checkbox.getState());
				if (checkbox.getState() == true)
					_itCharacteristic.setFlagON(false);
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
				String answer = JOptionPane.showInputDialog("Set Voltage Range: <Min> <Max> <Step>");
				final String[] values = answer.split(" ");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							_ivCharacteristic.define_VPATH(Double.valueOf(values[0]), Double.valueOf(values[1]),
									Double.valueOf(values[2]));
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
			}
		});

		return menu;
	}

	private JMenu BuildSetMenuITCharacteristic() {
		final IVCharacteristic _ivCharacteristic = SingleSensorBoard.getIVCharacteristic();
		final ITCharacteristic _itCharacteristic = SingleSensorBoard.getITCharacteristic();
		JMenu menu = new JMenu("IT-Characteristic");

		// Add feedbackOnOff
		final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
		final AbstractAction action = new AbstractAction("ON") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_itCharacteristic.setFlagON(checkbox.getState());
				if (checkbox.getState() == true)
					_ivCharacteristic.setFlagON(false);
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
				String answer = JOptionPane.showInputDialog("Set Temperature Range: <Min> <Max> <Step>");
				final String[] values = answer.split(" ");
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						try {
							_itCharacteristic.define_TPATH(Double.valueOf(values[0]), Double.valueOf(values[1]),
									Double.valueOf(values[2]));
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
			}
		});

		return menu;
	}

	protected JMenuItem BuildExportTXT(final JFreeChart chart) {
		JMenuItem menuItem = new JMenuItem(new AbstractAction("Export .txt") {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(null, "txt"));
				fileChooser.setDialogTitle("Save CSV");

				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					final File fileToSave = fileChooser.getSelectedFile();

					_TM.addTask(new ATask() {
						@Override
						public void execution() {
							XYDataset dataset = chart.getXYPlot().getDataset();
							DataManager DManager = new DataManager();

							String XLabel = chart.getXYPlot().getDomainAxis().getLabel();
							String YLabel = chart.getXYPlot().getRangeAxis().getLabel();

							for (int i = 0; i < dataset.getSeriesCount(); i++) {
								DManager.addColoumn(
										((String) dataset.getSeriesKey(i) + "_" + XLabel).replace(" ", "_"));
								DManager.addColoumn(
										((String) dataset.getSeriesKey(i) + "_" + YLabel).replace(" ", "_"));
								for (int j = 0; j < dataset.getItemCount(i); j += 2) {
									DManager.add(i, (double) dataset.getX(i, j));
									DManager.add(i + 1, (double) dataset.getY(i, j));
								}
							}

							DManager.save(fileToSave.getAbsolutePath());
						}
					});
				}
			}
		});

		return menuItem;
	}

	private JMenu BuildFileMenu() {
		JMenu menu = new JMenu("File");

		menu.add(BuildChartPropertyMenu(SingleSensorBoard.getInstance().GetChartPanel().getChart()));

		menu.add(BuildExportTXT(SingleSensorBoard.getInstance().GetChartPanel().getChart()));

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
									SingleSensorBoard.getHeater().EraseData();
									SingleSensorBoard.getVoltAmpMeter().EraseData();
									SingleSensorBoard.getChamberHumidity().EraseData();
									SingleSensorBoard.getChamberTemperature().EraseData();
									SingleSensorBoard.getIVCharacteristic().EraseData();
									SingleSensorBoard.getITCharacteristic().EraseData();
									SingleSensorBoard.getLRIVCharacteristic().EraseData();
									SingleSensorBoard.getInstance().ResetUI();
									SingleSensorBoard.getCommands().ResetDevice();
									break;
								case 1:
									break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		menu.add(new AbstractAction("Actual settings") {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "IVCharacteristic: "
						+ Boolean.toString(SingleSensorBoard.getIVCharacteristic().getFlagON()) + "\n"
						+ "ITCharacteristic: " + Boolean.toString(SingleSensorBoard.getITCharacteristic().getFlagON())
						+ "\n" + "Temperature Feedback: "
						+ Boolean.toString(SingleSensorBoard.getHeater().getFeedbakON()) + "\n" + "Setted temperature: "
						+ Double.toString(SingleSensorBoard.getHeater().getFeedBackController().getTarget()) + " °C"
						+ "\n" + "Setted voltage Fall: "
						+ Double.toString(SingleSensorBoard.getCommands().GetVoltageFall()) + " [V]" + "\n"
						+ "Setted voltage heater: "
						+ Double.toString(SingleSensorBoard.getCommands().GetVoltageHeater()) + " [a.u]" + "\n"
						+ "Ext. Feedback: " + Boolean.toString(SingleSensorBoard.getCommands().getFeedbackExternal())
						+ "\n" + "Bias with ext.: "
						+ Boolean.toString(SingleSensorBoard.getCommands().getSumInputWithExternalSignal()) + "\n"
						+ "Amp. meter autorange: "
						+ Boolean.toString(SingleSensorBoard.getCommands().getAutorangeAmpMeter()) + "\n"
						+ "Amp. meter range: " + Integer.toString(SingleSensorBoard.getCommands().getAmpMeterRange()),
						"Settings", JOptionPane.INFORMATION_MESSAGE);
			}

		});

		return menu;
	}

	protected JMenu BuildChartPropertyMenu(final JFreeChart chart) {
		JMenu menu = new JMenu("Property Chart");

		final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
		final AbstractAction action = new AbstractAction("FIFO scroll") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkbox.getState()) {
					try {
						String answer = JOptionPane.showInputDialog("Set dimension of domain window:");
						chart.getXYPlot().getDomainAxis().setFixedAutoRange(Double.valueOf(answer));
					} catch (Exception _e) {
						_e.printStackTrace();
					}
				} else
					chart.getXYPlot().getDomainAxis().setFixedAutoRange(0);
			}
		};

		checkbox.setAction(action);
		menu.add(checkbox);
		return menu;
	}

	protected JMenu BuildAdvancedMenu(final ICommands Commands) {
		JMenu menu = new JMenu("Advanced");
		menu.add(BuildAmpMeterMenu());

		final JCheckBoxMenuItem checkboxExtFeedback = new JCheckBoxMenuItem();
		final AbstractAction actionExtFeedback = new AbstractAction("Ext. Feedback") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						Commands.setFeedbackExternal(checkboxExtFeedback.getState());
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
						Commands.setSumInputWithExternalSignal(checkboxSumExternalSignal.getState());
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
				if (checkboxExtFeedback.getState() != Commands.getFeedbackExternal())
					checkboxExtFeedback.setState(Commands.getFeedbackExternal());

				if (checkboxSumExternalSignal.getState() != Commands.getSumInputWithExternalSignal())
					checkboxSumExternalSignal.setState(Commands.getSumInputWithExternalSignal());
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

		return menu;
	}
}
package SingleSensorBoard.Menu;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;

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
import core.ChartFrame;
import core.DataManager;
import core.LoopManager;
import core.ModChartPanel;
import core.TaskManager;
import core.themal.LookUpTable;

import java.awt.geom.*;

public class MenuEditorSingleSensorBoard {

	protected TaskManager _TM;
	protected ICommands _commands;
	protected ChartFrame _chart;
	public boolean verbose = true;

	public MenuEditorSingleSensorBoard(TaskManager TM, ICommands commands, ChartFrame chart) {
		_TM = TM;
		_commands = commands;
		_chart = chart;
	}

	public JMenuBar constructMenuBar() {
		JMenuBar menu = new JMenuBar();
		menu.add(BuildFileMenu());
		menu.add(BuildDisplayMenu());
		menu.add(BuildSetMenu());
		menu.add(BuildAdvancedMenu());
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
							_commands.SetVoltageFall(Double.valueOf(answer));
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
							_commands.SetVoltageHeater(Double.valueOf(answer));
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

		menu.add(new ActionSetFeedbackParameters("Parameters", 5, 5, 1, 10, heater));
		menu.add(BuildCalibrationHeaterMenu(heater));

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

	protected JMenuItem BuildExportTXT() {
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
							XYDataset dataset = _chart.GetChartPanel().getChart().getXYPlot().getDataset();
							DataManager DManager = new DataManager();

							String XLabel = _chart.GetChartPanel().getChart().getXYPlot().getDomainAxis().getLabel();
							String YLabel = _chart.GetChartPanel().getChart().getXYPlot().getRangeAxis().getLabel();

							for (int i = 0; i < dataset.getSeriesCount(); i++) {

								String labelColumnX = (dataset.getSeriesKey(i) + "_" + XLabel).replace(" ", "_");
								String labelColumnY = (dataset.getSeriesKey(i) + "_" + YLabel).replace(" ", "_");

								DManager.addColoumn(labelColumnX);
								DManager.addColoumn(labelColumnY);

								for (int j = 0; j < dataset.getItemCount(i); j++) {
									DManager.add(labelColumnX, (double) dataset.getX(i, j));
									DManager.add(labelColumnY, (double) dataset.getY(i, j));
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

		menu.add(BuildChartPropertyMenu());
		menu.add(BuildExportTXT());
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
									_commands.ResetDevice();
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
				JOptionPane.showMessageDialog(null, "IVCharacteristic: "
						+ Boolean.toString(SingleSensorBoard.getIVCharacteristic().getFlagON()) + "\n"
						+ "ITCharacteristic: " + Boolean.toString(SingleSensorBoard.getITCharacteristic().getFlagON())
						+ "\n" + "Temperature Feedback: "
						+ Boolean.toString(SingleSensorBoard.getHeater().getFeedbakON()) + "\n" + "Setted temperature: "
						+ Double.toString(SingleSensorBoard.getHeater().getFeedBackController().getTarget()) + " °C"
						+ "\n" + "Setted voltage Fall: " + Double.toString(_commands.GetVoltageFall()) + " [V]" + "\n"
						+ "Setted voltage heater: " + Double.toString(_commands.GetVoltageHeater()) + " [a.u]" + "\n"
						+ "Ext. Feedback: " + Boolean.toString(_commands.getFeedbackExternal()) + "\n"
						+ "Bias with ext.: " + Boolean.toString(_commands.getSumInputWithExternalSignal()) + "\n"
						+ "Amp. meter autorange: " + Boolean.toString(_commands.getAutorangeAmpMeter()) + "\n"
						+ "Amp. meter range: " + Integer.toString(_commands.getAmpMeterRange()), "Settings",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		return menu;
	}

	protected JMenu BuildChartPropertyMenu() {
		JMenu menu = new JMenu("Property Chart");

		menu.add(new AbstractAction("FIFO scroll") {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String answer = JOptionPane.showInputDialog(
							"Set dimension of domain window \n (to disable FIFO scroll enter a negative value):");
					_chart.GetChartPanel().getChart().getXYPlot().getDomainAxis()
							.setFixedAutoRange(Double.valueOf(answer));
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

	protected JMenu BuildAdvancedMenu() {
		JMenu menu = new JMenu("Advanced");
		menu.add(BuildAmpMeterMenu());

		final JCheckBoxMenuItem checkboxExtFeedback = new JCheckBoxMenuItem();
		final AbstractAction actionExtFeedback = new AbstractAction("Ext. Feedback") {
			@Override
			public void actionPerformed(ActionEvent e) {
				_TM.addTask(new ATask() {
					@Override
					public void execution() {
						_commands.setFeedbackExternal(checkboxExtFeedback.getState());
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
						_commands.setSumInputWithExternalSignal(checkboxSumExternalSignal.getState());
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
				if (checkboxExtFeedback.getState() != _commands.getFeedbackExternal())
					checkboxExtFeedback.setState(_commands.getFeedbackExternal());

				if (checkboxSumExternalSignal.getState() != _commands.getSumInputWithExternalSignal())
					checkboxSumExternalSignal.setState(_commands.getSumInputWithExternalSignal());
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
				String answer = JOptionPane.showInputDialog(null, "Enter calibration parameters: <T0> <alpha> <beta>");
				try {
					String values[] = answer.split(" ");
					CalibrateHeater c = new CalibrateHeater(Double.valueOf(values[0]), Double.valueOf(values[1]),
							Double.valueOf(values[2]), _TM, _commands, heater);
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
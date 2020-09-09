package SingleSensorBoard.Commands;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

	private boolean SerialPortInit = false;

	public SingleBoardCommands() {
		super();

		SerialPort port = null;

		final JFrame frame = new JFrame();
		frame.setTitle("Ports");
		frame.setResizable(false);

		String list[] = new String[SerialPort.getCommPorts().length + 1];
		for (int i = 0; i < SerialPort.getCommPorts().length; i++)
			list[i] = SerialPort.getCommPorts()[i].getSystemPortName();

		list[SerialPort.getCommPorts().length] = "Run as Simulation";
		JComboBox<String> PortsMenu = new JComboBox<String>(list);
		PortsMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		PortsMenu.setPreferredSize(new Dimension(500, 50));

		JButton OkButton = new JButton(new AbstractAction("Ok") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SerialPortInit = true;
			}
		});

		OkButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel Label = new JLabel("Select the Serial Port");

		Label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		frame.add(Label, BorderLayout.NORTH);
		frame.add(PortsMenu, BorderLayout.CENTER);
		frame.add(OkButton, BorderLayout.SOUTH);
		frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		while (!SerialPortInit) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
			}
		}

		if (PortsMenu.getSelectedIndex() < SerialPort.getCommPorts().length) {
			port = SerialPort.getCommPorts()[PortsMenu.getSelectedIndex()];
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
		} else {
			_serial = null;
			_HeaterCommands = new HeaterCommands(_serial);
			_VoltAmpMeterCommands = new VoltAmpMeterCommands(_serial);
			_TempHumidityCommands = new TempHumidityCommands(_serial);
		}

		frame.dispose();
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

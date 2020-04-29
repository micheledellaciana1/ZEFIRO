package SingleSensorBoard;

import java.util.ArrayList;
import java.util.Vector;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import SingleSensorBoard.Commands.ICommands;

public class IVCharacteristic implements PropertyChangeListener {

	private boolean _flagON = false;
	private ArrayList<SingleCharacteristic> _oldCharacteristics;
	private SingleCharacteristic _actualCharaceristic;

	private ArrayList<Double> _VPATH;
	private int _MarkPlaceVPATH = 0;
	private boolean _flagChangeVoltage = true;
	private ICommands _Commands;
	private ModeVoltAmpMeter _voltAmpMeter;
	private int _sizeVoltAmpMeterAtChangeVoltage;

	public PropertyChangeSupport ChangeSupport = new PropertyChangeSupport(this);

	public IVCharacteristic(ICommands Commands, ModeVoltAmpMeter voltAmpMeter) {
		_oldCharacteristics = new ArrayList<SingleCharacteristic>();
		_actualCharaceristic = new SingleCharacteristic();
		_Commands = Commands;
		_voltAmpMeter = voltAmpMeter;

		define_VPATH(-5, 5, 1);
	}

	public void define_VPATH(double VMin, double VMax, double dV) {
		if (VMax > VMin && dV > 0) {
			_VPATH = new ArrayList<Double>();
			_MarkPlaceVPATH = 0;
			_actualCharaceristic.clear();
			_flagChangeVoltage = true;

			for (double V = VMin; V <= VMax; V += dV)
				_VPATH.add(V);

			for (double V = VMax; V >= VMin; V -= dV)
				_VPATH.add(V);
		}
	}

	public void setFlagON(boolean ON) {
		ChangeSupport.firePropertyChange("flagON", _flagON, ON);
		_flagON = ON;
	}

	public boolean getFlagON() {
		return _flagON;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (_flagON && evt.getPropertyName().equals("FinishedLoop"))
			try {
				if (_flagChangeVoltage) {
					_Commands.SetVoltageFall(_VPATH.get(_MarkPlaceVPATH));
					_sizeVoltAmpMeterAtChangeVoltage = _voltAmpMeter.getSize();
					_flagChangeVoltage = false;
					return;
				}

				if (readyToAcquire(50)) {
					double VoltageFall = _voltAmpMeter.getVoltage().lastElement().getY();
					double Current = _voltAmpMeter.getCurrent().lastElement().getY();
					_actualCharaceristic.add(new Point2D.Double(Current, VoltageFall));
					_flagChangeVoltage = true;
					_MarkPlaceVPATH++;

					if (_MarkPlaceVPATH > _VPATH.size() - 1) {
						_MarkPlaceVPATH = 0;
						_oldCharacteristics.add(new SingleCharacteristic(_actualCharaceristic));

						ChangeSupport.firePropertyChange("FinishedIVCharacteristic", null, null);
						_actualCharaceristic.clear();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private boolean readyToAcquire(int NPoints) {
		if (_voltAmpMeter.getSize() > NPoints + _sizeVoltAmpMeterAtChangeVoltage)
			if (ValueIsStable(_voltAmpMeter.getVoltage(), NPoints))
				if (ValueIsStable(_voltAmpMeter.getCurrent(), NPoints))
					return true;
		return false;
	}

	private boolean ValueIsStable(Vector<Point2D> values, int NPoints) {
		SimpleRegression RO = new SimpleRegression(true);
		for (int i = values.size() - NPoints; i < values.size(); i++)
			RO.addData(values.get(i).getX(), values.get(i).getY());

		RegressionResults results = RO.regress();

		double slope = results.getParameterEstimate(1);
		double errSlope = results.getStdErrorOfEstimate(1);

		if ((slope - errSlope) * (slope + errSlope) <= 0)
			return true;
		return false;
	}

	public SingleCharacteristic getActualCharacteristic() {
		return _actualCharaceristic;
	}

	public ArrayList<SingleCharacteristic> getoldCharacteristics() {
		return _oldCharacteristics;
	}

	public void EraseData() {
		for (SingleCharacteristic C : _oldCharacteristics)
			C.clear();
		_actualCharaceristic.clear();
	}

	public class SingleCharacteristic extends Vector<Point2D> {
		public SingleCharacteristic() {
			super();
		}

		public SingleCharacteristic(SingleCharacteristic toCopy) {
			super();
			for (Point2D point2d : toCopy) {
				this.add(point2d);
			}
		}
	}
}
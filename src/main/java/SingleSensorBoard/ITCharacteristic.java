package SingleSensorBoard;

import java.util.ArrayList;
import java.util.Vector;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ITCharacteristic implements PropertyChangeListener {

	private boolean _flagON = false;
	private ArrayList<SingleCharacteristic> _oldCharacteristics;
	private SingleCharacteristic _actualCharaceristic;

	private ArrayList<Double> _TPATH;
	private int _MarkPlaceTPATH = 0;
	private boolean _flagChangeTemperature = true;

	private boolean _VoltAmpIsStable;
	private boolean _heaterIsStable;

	private ModeVoltAmpMeter _voltAmpMeter;
	private ModeHeater _heater;
	private long _timeChangedTemperature;

	public PropertyChangeSupport ChangeSupport = new PropertyChangeSupport(this);

	public ITCharacteristic(ModeVoltAmpMeter VoltAmpMeter, ModeHeater Heater) {
		_oldCharacteristics = new ArrayList<SingleCharacteristic>();
		_actualCharaceristic = new SingleCharacteristic();

		_voltAmpMeter = VoltAmpMeter;
		_heater = Heater;

		define_TPATH(20, 500, 10);
	}

	public void define_TPATH(double TMin, double TMax, double dT) {
		if (TMax > TMin && dT > 0) {
			_TPATH = new ArrayList<Double>();
			_MarkPlaceTPATH = 0;
			_flagChangeTemperature = true;
			_actualCharaceristic.clear();

			for (double T = TMin; T <= TMax; T += dT)
				_TPATH.add(T);

			for (double T = TMax; T >= TMin; T -= dT)
				_TPATH.add(T);
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
		if (evt.getPropertyName().equals("HeaterChangedStability"))
			_VoltAmpIsStable = (boolean) evt.getNewValue();

		if (evt.getPropertyName().equals("HeaterChangedStability"))
			_heaterIsStable = (boolean) evt.getNewValue();

		if (_flagON && _heater.getFeedbakON() && _heater.getFeedbakON()) {
			try {
				if (_flagChangeTemperature) {
					_heater.setTargetTemperature(_TPATH.get(_MarkPlaceTPATH));
					_timeChangedTemperature = System.currentTimeMillis();
					_flagChangeTemperature = false;
					return;
				}

				if (System.currentTimeMillis() - _timeChangedTemperature < 1000)
					return;

				if (_VoltAmpIsStable && _heaterIsStable) {
					double Temperature = _heater.getTemperature().lastElement().getY();
					double Current = _voltAmpMeter.getCurrent().lastElement().getY();
					_actualCharaceristic.add(new Point2D.Double(Temperature, Current));
					_flagChangeTemperature = true;
					_MarkPlaceTPATH++;

					if (_MarkPlaceTPATH > _TPATH.size() - 1) {
						_MarkPlaceTPATH = 0;
						_oldCharacteristics.add(new SingleCharacteristic(_actualCharaceristic));

						ChangeSupport.firePropertyChange("FinishedITCharacteristic", null, null);
						_actualCharaceristic.clear();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		_flagChangeTemperature = true;
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

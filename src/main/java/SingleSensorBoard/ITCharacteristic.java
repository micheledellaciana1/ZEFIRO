package SingleSensorBoard;

import java.util.ArrayList;
import java.util.Vector;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ITCharacteristic implements PropertyChangeListener {

	private boolean _flagON = false;
	private boolean _continousRamping = false;

	private ArrayList<SingleCharacteristic> _oldCharacteristics;
	private ArrayList<SingleCharacteristic> _oldArreniusPlot;
	private SingleCharacteristic _actualCharaceristic;
	private SingleCharacteristic _actualArreniusPlot;

	private ArrayList<Double> _TPATH;
	private int _MarkPlaceTPATH = 0;
	private boolean _flagChangeTemperature = true;

	private ModeVoltAmpMeter _voltAmpMeter;
	private ModeHeater _heater;
	private long _timeChangedTemperature;
	public int millisDelay = 1000;

	public PropertyChangeSupport ChangeSupport = new PropertyChangeSupport(this);

	public ITCharacteristic(ModeVoltAmpMeter VoltAmpMeter, ModeHeater Heater) {
		_oldCharacteristics = new ArrayList<SingleCharacteristic>();
		_actualCharaceristic = new SingleCharacteristic();

		_oldArreniusPlot = new ArrayList<SingleCharacteristic>();
		_actualArreniusPlot = new SingleCharacteristic();

		_voltAmpMeter = VoltAmpMeter;
		_heater = Heater;

		define_TPATH(20, 500, 10);
	}

	public void define_TPATH(double TStart, double TFinish, double dT) {
		if ((TFinish - TStart) * dT > 0) {
			_TPATH = new ArrayList<Double>();
			_MarkPlaceTPATH = 0;
			_actualCharaceristic.clear();
			_flagChangeTemperature = true;

			if (dT > 0) {
				for (double T = TStart; T <= TFinish; T += dT)
					_TPATH.add(T);

				for (double T = TFinish; T >= TStart; T -= dT)
					_TPATH.add(T);
			}
			if (dT < 0) {
				for (double T = TStart; T >= TFinish; T += dT)
					_TPATH.add(T);

				for (double T = TFinish; T <= TStart; T -= dT)
					_TPATH.add(T);
			}
		}
	}

	public void setFlagON(boolean ON) {
		ChangeSupport.firePropertyChange("flagON", _flagON, ON);
		_flagON = ON;

		if (ON == true) {
			_actualCharaceristic.clear();
			_MarkPlaceTPATH = 0;
			_flagChangeTemperature = true;
		}
	}

	public boolean getFlagON() {
		return _flagON;
	}

	public void setCountinousRamping(boolean ON) {
		_continousRamping = ON;
	}

	public boolean getCountinousRamping() {
		return _continousRamping;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (_flagON && _heater.getFeedbakON() && evt.getPropertyName().equals("FinishedLoop")) {
			try {
				if (_flagChangeTemperature) {
					_heater.setTargetTemperature(_TPATH.get(_MarkPlaceTPATH));
					_timeChangedTemperature = System.currentTimeMillis();
					_flagChangeTemperature = false;
					return;
				}

				if (System.currentTimeMillis() - _timeChangedTemperature < millisDelay)
					return;

				double Temperature = _heater.getTemperature().lastElement().getY();
				double Current = _voltAmpMeter.getCurrent().lastElement().getY();
				_actualCharaceristic.add(new Point2D.Double(Temperature, Current));
				try {
					_actualArreniusPlot.add(new Point2D.Double(1. / Temperature, Current));
				} catch (Exception e) {
				}

				_flagChangeTemperature = true;
				_MarkPlaceTPATH++;

				if (_MarkPlaceTPATH > _TPATH.size() - 1) {
					_MarkPlaceTPATH = 0;
					_oldCharacteristics.add(new SingleCharacteristic(_actualCharaceristic));
					_oldArreniusPlot.add(new SingleCharacteristic(_actualArreniusPlot));

					ChangeSupport.firePropertyChange("FinishedITCharacteristic", null, null);
					if (_continousRamping) {
						_actualCharaceristic.clear();
						_actualArreniusPlot.clear();
					} else
						_flagON = false;
				}
			} catch (

			Exception e) {
				e.printStackTrace();
			}
		}
	}

	public SingleCharacteristic getActualCharacteristic() {
		return _actualCharaceristic;
	}

	public SingleCharacteristic getActualArreniusPlot() {
		return _actualArreniusPlot;
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

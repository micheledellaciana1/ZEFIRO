package SingleSensorBoard;

import java.util.ArrayList;
import java.util.Vector;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

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

	private ModeVoltAmpMeter _voltAmpMeter;
	private ModeHeater _heater;
	private int _sizeHeaterAtChangeTemperature;

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
		if (_flagON && evt.getPropertyName().equals("FinishedLoop") && _heater.getFeedbakON())
			try {
				if (_flagChangeTemperature) {
					_heater.setTargetTemperature(_TPATH.get(_MarkPlaceTPATH));
					_sizeHeaterAtChangeTemperature = _heater.getSize();
					_flagChangeTemperature = false;
					return;
				}

				if (readyToAcquire(50)) {
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

	private boolean readyToAcquire(int NPoints) {
		if (_heater.getSize() > NPoints + _sizeHeaterAtChangeTemperature)
			if (ValueIsStable(_voltAmpMeter.getVoltage(), NPoints))
				if (ValueIsStable(_voltAmpMeter.getCurrent(), NPoints))
					if (TemperatureIsStable(_heater.getTemperature(), NPoints))
						return true;
		return false;
	}

	private boolean ValueIsStable(Vector<Point2D> values, int NPoints) {
		if (values.size() < NPoints)
			return false;

		SimpleRegression RO = new SimpleRegression(true);
		for (int i = values.size() - NPoints; i < values.size(); i++)
			RO.addData(values.get(i).getX(), values.get(i).getY());

		RegressionResults results = RO.regress();

		double slope = results.getParameterEstimate(1);
		double errSlope = results.getStdErrorOfEstimate(1);

		if ((slope - errSlope) * (slope + errSlope) < 0)
			return true;
		return false;
	}

	private boolean TemperatureIsStable(Vector<Point2D> temperatures, int NPoints) {
		if (temperatures.size() < NPoints)
			return false;

		double centralTime = temperatures.get(temperatures.size() - NPoints / 2).getX();
		SimpleRegression RO = new SimpleRegression(true);
		for (int i = temperatures.size() - NPoints; i < temperatures.size(); i++)
			RO.addData(temperatures.get(i).getX() - centralTime, temperatures.get(i).getY());

		RegressionResults results = RO.regress();

		double slope = results.getParameterEstimate(1);
		double errSlope = results.getStdErrorOfEstimate(1);
		double intercept = results.getParameterEstimate(0);
		double errIntercept = results.getStdErrorOfEstimate(0);

		if ((slope - errSlope) * (slope + errSlope) <= 0)
			if ((intercept - _TPATH.get(_MarkPlaceTPATH) - errIntercept)
					* (intercept - _TPATH.get(_MarkPlaceTPATH) + errIntercept) <= 0)
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

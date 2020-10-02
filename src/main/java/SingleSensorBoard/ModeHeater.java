package SingleSensorBoard;

import java.util.ArrayList;
import java.util.Vector;

import SingleSensorBoard.Commands.HeaterCommands;

import java.awt.geom.Point2D;

import core.*;
import core.themal.*;

public class ModeHeater extends AMultipleDataStream {
    protected FeedBackController_type1 _FBC;
    protected LookUpTable _LUT;
    protected boolean _feedbackON = false;
    protected HeaterCommands _Commands;
    protected long _timeLastCallFeedback;
    public long checkTimeFeedback = 100;

    public ModeHeater(String name, long period, HeaterCommands Commands) {
        super(name, period, 5);
        _Commands = Commands;

        _FBC = new FeedBackController_type1(0.02, 0.01, 0.00, 1);
        _FBC.OnlyPositiveValue = true;
        _FBC.MaxResponce = 3.3;
        _FBC.set_target_value(0);
    }

    public void setFeedBackController(FeedBackController_type1 FBC) {
        _FBC = FBC;
    }

    public FeedBackController_type1 getFeedBackController() {
        return _FBC;
    }

    public void setTargetTemperature(double TargetTemperature) {
        _FBC.set_target_value(TargetTemperature);
    }

    public void setLUT(LookUpTable LUT) {
        _LUT = LUT;
    }

    public LookUpTable getLUT() {
        return _LUT;
    }

    public void setFeedbakON(boolean ON) {
        ChangeSupport.firePropertyChange("ChangefeedbackON", _feedbackON, ON);
        if (_feedbackON != ON) {
            _feedbackON = ON;
            _FBC.reset();
        }
    }

    public boolean getFeedbakON() {
        return _feedbackON;
    }

    @Override
    public ArrayList<Double> acquireData() {
        ArrayList<Double> data = new ArrayList<Double>();

        double resistance = _Commands.measureResistanceHeater();
        double Temperature;

        try {
            Temperature = _LUT.getY(resistance);
        } catch (Exception e) {
            Temperature = resistance;
        }

        double power = _Commands.measurePowerHeater();

        data.add(Temperature);
        data.add(resistance);
        data.add(power);
        data.add(_Commands.GetVoltageHeater());
        data.add(_FBC.getTarget());

        if (_feedbackON) {
            if (System.currentTimeMillis() - _timeLastCallFeedback > checkTimeFeedback) {
                _timeLastCallFeedback = System.currentTimeMillis();
                try {
                    double responce = _FBC.responce(Temperature);
                    _Commands.SetVoltageHeater(responce);
                } catch (Exception e) {
                }
            }
        }

        return data;
    }

    public Vector<Point2D> getTemperature() {
        return _datas.get(0);
    }

    public Vector<Point2D> getResistance() {
        return _datas.get(1);
    }

    public Vector<Point2D> getPower() {
        return _datas.get(2);
    }

    public Vector<Point2D> getVoltageHeater() {
        return _datas.get(3);
    }

    public Vector<Point2D> getTargetTemperature() {
        return _datas.get(4);
    }
}
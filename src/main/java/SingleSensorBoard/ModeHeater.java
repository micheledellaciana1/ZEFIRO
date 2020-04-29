package SingleSensorBoard;

import java.util.ArrayList;
import java.util.Vector;

import SingleSensorBoard.Commands.ICommands;

import java.awt.geom.Point2D;

import core.*;
import core.themal.*;

public class ModeHeater extends AMultipleDataStream {
    private FeedBackController_type1 _FBC;
    private LookUpTable _LUT;
    private boolean _feedbackON = false;
    private ICommands _Commands;

    public ModeHeater(String name, long period, ICommands Commands) {
        super(name, period, 4);
        _Commands = Commands;

        _FBC = new FeedBackController_type1(0.5, 0.5, 0, 2);
        _FBC.OnlyPositiveValue = true;
        _FBC.set_target_value(0);
    }

    public ICommands getCommands() {
        return _Commands;
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

        if (_feedbackON) {
            try {
                double responce = _FBC.responce(Temperature);
                _Commands.SetVoltageHeater(responce);
            } catch (Exception e) {
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
}
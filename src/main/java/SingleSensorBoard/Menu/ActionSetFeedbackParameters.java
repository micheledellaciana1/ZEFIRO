package SingleSensorBoard.Menu;

import java.awt.event.ActionEvent;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import SingleSensorBoard.ModeHeater;

public class ActionSetFeedbackParameters extends AbstractAction {

    private JFrame _frame = new JFrame("FeedbackParameters");
    private JSlider _propSlider;
    private JSlider _integralSlider;
    private JSlider _derSlider;
    private JSlider _sensitivitySlider;
    private int division = 1000;

    private ModeHeater _heater;

    public ActionSetFeedbackParameters(String name, double maxProp, double maxIntegral, double maxDer,
            double maxSensitivity, ModeHeater heater) {
        super(name);

        _frame = new JFrame("FeedbackParameter");
        _propSlider = new JSlider(0, (int) (maxProp * division));
        _integralSlider = new JSlider(0, (int) maxIntegral * division);
        _derSlider = new JSlider(0, (int) maxDer * division);
        _sensitivitySlider = new JSlider(0, (int) maxSensitivity * division);
        _heater = heater;

        _frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                _frame.setVisible(false);
            }
        });

        JLabel _propSliderLabel = new JLabel("  Proportional");
        _propSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        _propSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        _propSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                _heater.getFeedBackController().setParameters(0, _propSlider.getValue() / (double) division);
            }
        });

        JLabel _IntegralSliderLabel = new JLabel("  Integral");
        _IntegralSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        _integralSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        _integralSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                _heater.getFeedBackController().setParameters(1, _integralSlider.getValue() / (double) division);
            }
        });

        JLabel _derSliderLabel = new JLabel("  Derivative");
        _derSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        _derSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        _derSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                _heater.getFeedBackController().setParameters(2, _derSlider.getValue() / (double) division);
            }
        });

        JLabel _sensitivitySliderLabel = new JLabel("  Sensitivity");
        _sensitivitySliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        _sensitivitySlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        _sensitivitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                _heater.getFeedBackController().setParameters(3, _sensitivitySlider.getValue() / (double) division);
            }
        });

        _frame.setLayout(new BoxLayout(_frame.getContentPane(), BoxLayout.PAGE_AXIS));

        _frame.add(_propSliderLabel);
        _frame.add(_propSlider);
        _frame.add(_IntegralSliderLabel);
        _frame.add(_integralSlider);
        _frame.add(_derSliderLabel);
        _frame.add(_derSlider);
        _frame.add(_sensitivitySliderLabel);
        _frame.add(_sensitivitySlider);

        _frame.setSize(400, 180);
        _frame.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double InitialValueProportional = _heater.getFeedBackController().getParameters(0);
        double InitialValueIntegral = _heater.getFeedBackController().getParameters(1);
        double InitialValueDerivative = _heater.getFeedBackController().getParameters(2);
        double InitialValueSensitivity = _heater.getFeedBackController().getParameters(3);

        _propSlider.setValue((int) (InitialValueProportional * division));
        _integralSlider.setValue((int) (InitialValueIntegral * division));
        _derSlider.setValue((int) (InitialValueDerivative * division));
        _sensitivitySlider.setValue((int) (InitialValueSensitivity * division));

        _frame.setVisible(true);
    }
}
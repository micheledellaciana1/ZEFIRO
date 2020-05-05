package SingleSensorBoard.main;

import SingleSensorBoard.*;
import core.LoopManager;

public class App {
    App() {
        super();
    }

    public static void main(String[] args) throws Exception {

        LoopManager Manager = new LoopManager();

        Manager.add_mode(SingleSensorBoard.getTaskManager());
        Manager.add_mode(SingleSensorBoard.getVoltAmpMeter());
        Manager.add_mode(SingleSensorBoard.getHeater());
        Manager.add_mode(SingleSensorBoard.getChamberHumidity());
        Manager.add_mode(SingleSensorBoard.getChamberTemperature());

        SingleSensorBoard app = SingleSensorBoard.getInstance();
        app.buildMenuBar();
        app.displayVoltageVsTime();
        app.setVisible(true);

        SingleSensorBoard.getInstance().startUpdaterThread();

        Thread t1 = new Thread(Manager);
        t1.start();
    }
}
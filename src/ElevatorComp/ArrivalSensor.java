package ElevatorComp;

import Config.ConfigInfo;
import ControlComp.Logging;

public class ArrivalSensor implements Runnable {

    private ElevatorCar elevator;
    private boolean floorReached;

    public ArrivalSensor(ElevatorCar elevator){
        this.floorReached = false;
        this.elevator = elevator;
    }

    public void alertSensor(){
        floorReached = true;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(ConfigInfo.FLOOR_TRAVERSAL_TIME + 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!floorReached){
            //System.out.println("Elevator " + elevator.getElevatorID() + "'s arrival sensor has timed out!!!");
            Logging.info("ArrivalSensor", ""+elevator.getElevatorID()," arrival sensor has timed out!!!" );
            elevator.hardFault();
        }

    }
}

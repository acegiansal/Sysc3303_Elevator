package ElevatorComp;

import ControlComp.ElevatorBox;

public class ElevatorCommunications implements Runnable {

    ElevatorCar elevator;

    public ElevatorCommunications(ElevatorCar car){
        this.elevator = car;
    }

    @Override
    public void run() {

    }
}

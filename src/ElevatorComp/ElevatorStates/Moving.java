package ElevatorComp.ElevatorStates;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
import ElevatorComp.ElevatorCar;

public class Moving extends ElevatorState {
    public Moving(ElevatorCar elevator){
        super(elevator);
    }

    @Override
    public void performAction() {

        //Change currentFloor number
        try {
            System.out.println(Thread.currentThread().getName() + " Is moving 1 floor for " + ConfigInfo.FLOOR_TRAVERSAL_TIME + "ms");
            Thread.sleep(ConfigInfo.FLOOR_TRAVERSAL_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateStatus() {
        // Update currentFloor
        if(elevator.getFloorQueue().get(0) > elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
        } else if(elevator.getFloorQueue().get(0) < elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
        } else {
            System.out.println(Thread.currentThread().getName() + "An error occured! Status is idle but elevator moved!");
        }

        elevator.changeState(new DecideOpen(elevator));

    }
}

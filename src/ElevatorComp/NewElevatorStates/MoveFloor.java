package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import ElevatorComp.ElevatorCar;

public class MoveFloor extends ShaftState{
    public MoveFloor(ElevatorCar elevator) {
        super(elevator);
    }

    @Override
    public void entry(){
        // TODO ALERT ARRIVAL SENSOR
        System.out.println("Elevator moving 1 floor!");
        this.setTimer(ConfigInfo.FLOOR_TRAVERSAL_TIME);
    }

    @Override
    public void timeout(){

        // CHECK SCENARIO, IF IT SHOULD BREAK

        // Update currentFloor
        if(elevator.getFloorQueue().get(0) > elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
        } else if(elevator.getFloorQueue().get(0) < elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
        } else {
            System.out.println(Thread.currentThread().getName() + "An error occured! Status is idle but elevator moved!");
        }
        System.out.println("Now on floor: " + elevator.getCurrentFloor());

        this.updateStatus();
        this.shouldOpen();
    }
}

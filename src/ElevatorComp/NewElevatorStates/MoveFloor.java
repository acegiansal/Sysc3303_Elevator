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
        System.out.println("Elevator " + elevator.getElevatorID() + " moving 1 floor!");

        // Update currentFloor
        if(elevator.getFloorQueue().get(0) > elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
        } else if(elevator.getFloorQueue().get(0) < elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
        }

        //Update the floor now to tell the scheduler that it shouldn't tell it to use the same floor
        this.updateStatus();
        this.setTimer(ConfigInfo.FLOOR_TRAVERSAL_TIME);

    }

    @Override
    public void timeout(){

        // CHECK SCENARIO, IF IT SHOULD BREAK
        System.out.println("Elevator " + elevator.getElevatorID() + " Now on floor: " + elevator.getCurrentFloor());
        this.shouldOpen();
    }

    @Override
    public String toString(){
        return "MoveFloor";
    }
}

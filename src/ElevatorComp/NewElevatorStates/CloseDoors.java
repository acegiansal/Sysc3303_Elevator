package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import ElevatorComp.ElevatorCar;

public class CloseDoors extends DoorState{
    public CloseDoors(ElevatorCar elevator) {
        super(elevator);
    }

    @Override
    public void entry(){
        super.entry();
        System.out.println("Elevator " + elevator.getElevatorID() + " Doors closing for " + (ConfigInfo.DOOR_OPEN_TIME/1000) + " seconds on floor " + elevator.getCurrentFloor());
        this.setTimer(ConfigInfo.DOOR_OPEN_TIME);
    }

    @Override
    public void timeout(){

        // Remove floor
        elevator.getFloorQueue().remove(0);
        if(elevator.queueIsEmpty()){
            // Change state to idle
            elevator.changeState(new Idle(elevator));
        } else {
            ElevatorState nextState = elevator.getFloorQueue().get(0) == elevator.getCurrentFloor() ? new OpenDoors(elevator) : new MoveFloor(elevator);
            elevator.changeState(nextState);
        }
    }

    @Override
    public String toString(){
        return "CloseDoors";
    }
}

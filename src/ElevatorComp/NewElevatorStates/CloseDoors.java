package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
import ElevatorComp.ElevatorCar;
import ControlComp.Logging;
import Testing.TestingElevator;

public class CloseDoors extends DoorState{
    public CloseDoors(ElevatorCar elevator) {
        super(elevator);
    }

    @Override
    public void entry(){
        super.entry();
        //System.out.println("Elevator " + elevator.getElevatorID() + " Doors closing for " + (ConfigInfo.DOOR_OPEN_TIME/1000) + " seconds on floor " + elevator.getCurrentFloor());
        Logging.info("CloseDoors", ""+elevator.getElevatorID(), " Doors closing for " + (ConfigInfo.DOOR_OPEN_TIME/1000) + " seconds on floor " + elevator.getCurrentFloor());
        elevator.setDoorStatus(ElevatorStatus.CLOSING);
        updateStatus();
        TestingElevator.closeFlag(true);
        this.setTimer(ConfigInfo.DOOR_OPEN_TIME);
    }

    @Override
    public void timeout(){
        System.out.println("Closed doors timed out!");

        // State that doors are closed
        elevator.setDoorStatus(ElevatorStatus.CLOSED);
        updateStatus();

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

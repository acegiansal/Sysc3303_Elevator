package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
import ElevatorComp.ElevatorCar;
import ControlComp.Logging;

public class BrokenDoors extends DoorState{

    private int attemptCounter;

    public BrokenDoors(ElevatorCar elevator, int attemptCounter) {
        super(elevator);
        this.attemptCounter = attemptCounter;
    }

    @Override
    public void entry(){
        //System.out.println(elevator.getElevatorID() + " doors are stuck open! Trying to close...");
        Logging.info("BrokenDoors", ""+elevator.getElevatorID(), " doors are stuck open! Trying to close...");
        elevator.setDoorStatus(ElevatorStatus.DOOR_STUCK);
        updateStatus();
        this.setTimer(ConfigInfo.DOOR_CHECK_TIME);
    }

    @Override
    public void timeout() {
        if(attemptCounter == 0){
            //System.out.println(elevator.getElevatorID() + " doors fixed! Doors now closing");
            Logging.info("BrokenDoors", ""+elevator.getElevatorID(), " doors fixed! Doors now closing");
            elevator.setTransFaulted(false);
            elevator.changeState(new CloseDoors(elevator));
        } else {
            elevator.changeState(new BrokenDoors(elevator, attemptCounter-1));
        }
    }
}

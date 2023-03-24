package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import ElevatorComp.ElevatorCar;

public class BrokenDoors extends DoorState{

    private int attemptCounter;

    public BrokenDoors(ElevatorCar elevator, int attemptCounter) {
        super(elevator);
        this.attemptCounter = attemptCounter;
    }

    @Override
    public void entry(){
        System.out.println(elevator.getElevatorID() + " doors are stuck open! Trying to close...");
        this.setTimer(ConfigInfo.DOOR_CHECK_TIME);
    }

    @Override
    public void timeout() {
        if(attemptCounter == 0){
            System.out.println(elevator.getElevatorID() + " doors fixed! Doors now closing");
            elevator.setTransFaulted(false);
            elevator.changeState(new CloseDoors(elevator));
        } else {
            elevator.changeState(new BrokenDoors(elevator, attemptCounter-1));
        }
    }
}

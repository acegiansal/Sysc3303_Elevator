package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import ControlComp.Logging;
import ElevatorComp.ElevatorCar;
import Testing.TestingElevator;

public class OpenDoors extends DoorState{
    public OpenDoors(ElevatorCar elevator) {
        super(elevator);
    }

    @Override
    public void entry(){
        super.entry();
        //System.out.println("Elevator " + elevator.getElevatorID() + " Doors opening for " + (ConfigInfo.DOOR_OPEN_TIME/1000) + " seconds on floor " + elevator.getCurrentFloor());
        Logging.info("OpenDoors", ""+ elevator.getElevatorID()," Doors opening for " + (ConfigInfo.DOOR_OPEN_TIME/1000) + " seconds on floor " + elevator.getCurrentFloor() );
        this.setTimer(ConfigInfo.DOOR_OPEN_TIME);
        TestingElevator.openFlag(true);
    }
    @Override
    public void timeout(){
        ElevatorState nextState = elevator.isTransFaulted() ? new BrokenDoors(elevator, 2) : new CloseDoors(elevator);
        elevator.changeState(nextState);
    }

    @Override
    public String toString(){
        return "OpenDoors";
    }
}

package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import ElevatorComp.ElevatorCar;

public class OpenDoors extends DoorState{
    public OpenDoors(ElevatorCar elevator) {
        super(elevator);
    }

    @Override
    public void entry(){
        super.entry();
        System.out.println(Thread.currentThread().getName() + " Doors opening for " + (ConfigInfo.DOOR_OPEN_TIME/1000) + " seconds on floor " + elevator.getCurrentFloor());
        this.setTimer(ConfigInfo.DOOR_OPEN_TIME);
    }
    @Override
    public void timeout(){
        elevator.changeState(new CloseDoors(elevator));
    }
}

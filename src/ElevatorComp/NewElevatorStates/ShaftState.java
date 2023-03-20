package ElevatorComp.NewElevatorStates;

import DataComp.RequestPacket;
import ElevatorComp.ElevatorCar;

import java.util.Arrays;

public class ShaftState extends ElevatorState {
    public ShaftState(ElevatorCar elevator) {
        super(elevator);
    }

    @Override
    public void entry() {
    }

    @Override
    public void exit() {
    }

    @Override
    public void timeout() {
    }

    public void shouldOpen(){
//        System.out.println(elevator.getFloorQueue().toString());
        ElevatorState nextState = elevator.getFloorQueue().get(0) == elevator.getCurrentFloor() ? new OpenDoors(elevator) : new MoveFloor(elevator);
        elevator.changeState(nextState);
    }

}

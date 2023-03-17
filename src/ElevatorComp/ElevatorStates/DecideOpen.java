package ElevatorComp.ElevatorStates;

import ElevatorComp.ElevatorCar;

import java.util.Arrays;

public class DecideOpen extends ElevatorState {

    private boolean shouldOpen;

    public DecideOpen(ElevatorCar elevator){
        super(elevator);
        this.shouldOpen = false;
    }

    @Override
    public void performAction() {
        if(elevator.getFloorQueue().contains(elevator.getCurrentFloor())){
            System.out.println("Door needs to open");
            shouldOpen = true;
        } else {
            System.out.println("Floor Queue: " + elevator.getFloorQueue().toString());
            System.out.println("Current floor: " + elevator.getCurrentFloor());
            shouldOpen = false;
        }
    }

    @Override
    public void updateStatus() {
        ElevatorState nextState = shouldOpen ? new OpenDoors(elevator) : new CheckFinished(elevator);
        elevator.changeState(nextState);
    }
}

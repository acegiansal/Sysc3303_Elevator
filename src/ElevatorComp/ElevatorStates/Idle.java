package ElevatorComp.ElevatorStates;

import DataComp.ElevatorStatus;
import ElevatorComp.ElevatorCar;

import java.util.ArrayList;

public class Idle extends ElevatorState {

    public Idle(ElevatorCar elevator){
        super(elevator);
    }

    public void performAction(){
        ArrayList<Integer> workQueue = elevator.getFloorQueue();
        System.out.println("Floor queue after IDLE: {" + workQueue.toString() + "}");

    }

    @Override
    public void updateStatus() {
        System.out.println("Elevator starting a task");

        ElevatorStatus status = new ElevatorStatus(elevator.getCurrentFloor(), elevator.getStatus().getDirection(), elevator.getStatus().getId());
        elevator.setStatus(status);
        elevator.changeState(new DecideOpen(elevator));
    }
}

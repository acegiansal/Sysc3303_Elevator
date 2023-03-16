package ElevatorComp.ElevatorStates;

import DataComp.ElevatorStatus;
import ElevatorComp.ElevatorCar;

import java.util.ArrayList;

public class Idle extends ElevatorState {

    public Idle(ElevatorCar elevator){
        this.elevator = elevator;
    }

    public void performAction(){
        ArrayList<Integer> workQueue = elevator.getFloorQueue();

//        elevator.changeState();


    }
}

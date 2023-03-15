package ElevatorComp.ElevatorStates;

import DataComp.ElevatorStatus;
import ElevatorComp.ElevatorCar;

public class Idle extends ElevatorState {

    public Idle(ElevatorCar elevator){
        this.elevator = elevator;
    }

    public void performAction(){

    }
}

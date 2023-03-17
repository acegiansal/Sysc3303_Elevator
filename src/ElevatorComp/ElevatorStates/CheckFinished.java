package ElevatorComp.ElevatorStates;

import DataComp.ElevatorStatus;
import ElevatorComp.ElevatorCar;

public class CheckFinished extends ElevatorState {

    private boolean finished;

    public CheckFinished(ElevatorCar elevator){
        super(elevator);
        this.finished = false;
    }

    @Override
    public void performAction() {
        if(elevator.queueIsEmpty()){
            System.out.println(Thread.currentThread().getName() + " Queue is now empty!");
            this.finished = true;
        } else {
            this.finished = false;
        }

    }
    @Override
    public void updateStatus() {
        ElevatorState nextState;
        if(finished){
            nextState = new Idle(elevator);
            ElevatorStatus status = new ElevatorStatus(elevator.getCurrentFloor(), ElevatorStatus.IDLE, elevator.getElevatorID());
            elevator.setStatus(status);
        } else {
            nextState = new Moving(elevator);
        }

        elevator.changeState(nextState);

    }
}

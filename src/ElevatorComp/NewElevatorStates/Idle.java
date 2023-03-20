package ElevatorComp.NewElevatorStates;

import DataComp.ElevatorStatus;
import DataComp.RequestPacket;
import ElevatorComp.ElevatorCar;

public class Idle extends ShaftState {
    public Idle(ElevatorCar elevator) {
        super(elevator);
    }

    @Override
    public void entry(){
        System.out.println("Elevator " + elevator.getElevatorID() + " is now idle");
        elevator.setDirection(ElevatorStatus.IDLE);
        // update status
        this.updateStatus();
    }

    @Override
    public void exit() {
        System.out.println("Elevator " + elevator.getElevatorID() + " is now active!");
    }

    @Override
    public void timeout() {
        System.out.println("Elevator received timeout event when it should not have!");
    }

    @Override
    public void requestReceived(RequestPacket request){
        super.requestReceived(request);
        this.updateStatus();
        this.shouldOpen();
    }

    @Override
    public String toString(){
        return "Idle";
    }
}

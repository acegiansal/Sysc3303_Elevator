package ElevatorComp.NewElevatorStates;

import ControlComp.Logging;
import DataComp.ElevatorStatus;
import DataComp.RequestPacket;
import ElevatorComp.ElevatorCar;

import java.util.ArrayList;

public abstract class ElevatorState {

    protected ElevatorCar elevator;
    protected Timer timer;
    /**
     * Thread that handles the timer runnable
     */
    private Thread timerThread;

    public ElevatorState(ElevatorCar elevator) {
        this.elevator = elevator;
        this.timer = new Timer(elevator);
    }

    /**
     * Starts a timer
     *
     * @param time The time (in ms) to pause for
     */
    public void setTimer(int time) {
        timer.setTime(time);
        timerThread = new Thread(timer, Thread.currentThread().getName() + " Elevator Timer");
        timerThread.start();
    }

    /**
     * Interrupts the timer
     */
    public void killTimer() {
        timerThread.interrupt();
    }

    public abstract void entry();

    public abstract void exit();

    public abstract void timeout();

    public void requestReceived(RequestPacket request) {
        this.elevator.setDirection(request.getDirection());
        this.elevator.addFloor(request.getStartFloor());
        this.elevator.addFloor(request.getEndFloor());
        this.elevator.setScenario(request.getScenario());
    }

    protected void updateStatus(){
        ElevatorStatus status;
        if(elevator.queueIsEmpty()){
            status = new ElevatorStatus(elevator.getCurrentFloor(), elevator.getDirection(), elevator.getDoorStatus(), elevator.getStatus().getId());
        } else {
            ArrayList<Integer> floorCopy = new ArrayList<>(elevator.getFloorQueue());
            status = new ElevatorStatus(elevator.getCurrentFloor(), elevator.getDirection(), elevator.getDoorStatus(), elevator.getStatus().getId(), floorCopy);
            //System.out.println("Elevator " + elevator.getElevatorID() + " Setting status to " + status);
        }
        Logging.info("ElevatorState","" + elevator.getElevatorID()," Setting status to " + status);
        elevator.setStatus(status);
    }

    protected void updateStatus(String direction){
        ElevatorStatus status = new ElevatorStatus(elevator.getCurrentFloor(), direction, elevator.getDoorStatus(), elevator.getStatus().getId());
        //System.out.println("Elevator " + elevator.getElevatorID() + " Setting status to " + status);
        Logging.info("ElevatorState","" + elevator.getElevatorID()," Setting status to " + status);
        elevator.setStatus(status);
    }

}

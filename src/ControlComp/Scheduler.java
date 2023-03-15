package ControlComp;

public class Scheduler implements Runnable{

    private ElevatorBox databox;
    private int numOfElevators;

    public Scheduler(ElevatorBox databox, int elevatorNum){
        this.numOfElevators = elevatorNum;
        this.databox = databox;
    }

    @Override
    public void run() {
        
    }
}

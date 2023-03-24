package ElevatorComp;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
import DataComp.RequestPacket;
import ElevatorComp.NewElevatorStates.*;

import java.util.ArrayList;
import java.util.Collections;

public class ElevatorCar {

    private ElevatorStatus status;
    private ArrayList<Integer> floorQueue;
    private int elevatorID;
    private ElevatorState currentState;
    private int scenario;
    private int currentFloor;
    private String currentDirection;
    private boolean statusUpdated;

    private boolean transFaulted;
    private boolean hardFaulted;

    public ElevatorCar(int elevatorID){
        this.elevatorID = elevatorID;
        floorQueue = new ArrayList<>();
        status = new ElevatorStatus(elevatorID);
        this.currentState = new Idle(this);
        this.scenario = 0;
        this.currentFloor = 1;
        this.currentDirection = ElevatorStatus.IDLE;
        this.statusUpdated = false;

        transFaulted = false;
        hardFaulted = false;

        // Start comms
        ElevatorCommunications comms = new ElevatorCommunications(this);
        new Thread(comms, "El" + elevatorID).start();

    }

    public synchronized ElevatorStatus getStatus(){
        return status;
    }

    public synchronized void setStatus(ElevatorStatus newStatus){
        status = newStatus;
        this.statusUpdated = true;
    }

    public synchronized ArrayList<Integer> getFloorQueue(){
        while(queueIsEmpty()){
            try {
                System.out.println("Elevator " + elevatorID + " is waiting for command in state " + currentState);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Integer> queue = floorQueue;
        notifyAll();
        return queue;
    }

    public boolean queueIsEmpty(){
        return floorQueue.isEmpty();
    }

    public void setDirection(String direction){
        this.currentDirection = direction;
    }
    public String getDirection(){
        return currentDirection;
    }

    public synchronized void addFloor(int toAdd){
        if(!floorQueue.contains(toAdd)) {
            floorQueue.add(toAdd);
            System.out.println("Elevator " + elevatorID + " floor: " + toAdd + " has button pushed and lamp turned on");
        }
        String direction = this.getDirection();
        //Sort based on direction (elevator will attempt to go to each one in order
        if(direction.equals("u")) {
            Collections.sort(floorQueue);
        } else if(direction.equals("d")){
            floorQueue.sort(Collections.reverseOrder());
        } else {
            System.out.println("Got an idle request when it should not be!");
        }
        notifyAll();
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
        if(scenario == 1){
            transFaulted = true;
        } else if (scenario == 2){
            hardFaulted = true;
        }
    }

    public boolean isTransFaulted() {
        return transFaulted;
    }

    public void setTransFaulted(boolean transFaulted) {
        this.transFaulted = transFaulted;
    }

    public boolean isHardFaulted() {
        return hardFaulted;
    }

    public void setHardFaulted(boolean hardFaulted) {
        this.hardFaulted = hardFaulted;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public void timeout(){
        this.currentState.timeout();
    }

    public void requestReceived(RequestPacket packet){
        this.currentState.requestReceived(packet);
    }

    public synchronized boolean isStatusUpdated() {
        return statusUpdated;
    }

    public synchronized void setStatusUpdated(boolean statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    public void changeState(ElevatorState state){
        this.currentState.exit();
        this.currentState = state;
        this.currentState.entry();
    }

    public void hardFault(){
        // Hard fault elevator
        System.out.println("Elevator " + getElevatorID() + " has experienced a hard fault!");
        ElevatorStatus status = new ElevatorStatus(getCurrentFloor(), ElevatorStatus.STUCK,getElevatorID());
        setStatus(status);
    }

    public static void main(String[] args){
        for(int i = 0; i< ConfigInfo.NUM_ELEVATORS; i++){
            ElevatorCar el = new ElevatorCar(i);
        }
    }
}

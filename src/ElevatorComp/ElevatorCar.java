package ElevatorComp;

import Config.ConfigInfo;
import ControlComp.Logging;
import DataComp.ElevatorStatus;
import DataComp.RequestPacket;
import ElevatorComp.NewElevatorStates.*;
import Testing.TestingElevator;

import java.util.ArrayList;
import java.util.Collections;

public class ElevatorCar implements Runnable{

    private ElevatorStatus status;
    private ArrayList<Integer> floorQueue;
    private int elevatorID;
    private ElevatorState currentState;
    private int scenario;
    private int currentFloor;
    private int currentDoorStatus;
    private String currentDirection;
    private boolean statusUpdated;

    private boolean transFaulted;
    private boolean hardFaulted;

    private ElevatorCommunications comms;

    private boolean stopped = false;

    private static final int HARD_FAULT = 2;
    private static final int TRANS_FAULT = 1;

    public ElevatorCar(int elevatorID){
        this.elevatorID = elevatorID;
        floorQueue = new ArrayList<>();
        status = new ElevatorStatus(elevatorID);
        this.currentState = new Idle(this);
        this.scenario = 0;
        this.currentFloor = 1;
        this.currentDirection = ElevatorStatus.IDLE;
        this.currentDoorStatus = 0;
        this.statusUpdated = false;

        transFaulted = false;
        hardFaulted = false;

        // Start comms
        comms = new ElevatorCommunications(this);
        new Thread(comms, "El" + elevatorID).start();
    }

    public ElevatorCar(){
        //needed a default constructor that doesn't do anything for testing
    }

    public synchronized ElevatorStatus getStatus(){
        return status;
    }

    public synchronized void setStatus(ElevatorStatus newStatus){
        status = newStatus;
        this.statusUpdated = true;
    }

    public synchronized int getDoorStatus(){
        return currentDoorStatus;
    }

    public synchronized void setDoorStatus(int newStatus){
        currentDoorStatus = newStatus;
        this.statusUpdated = true;
    }


    public synchronized ArrayList<Integer> getFloorQueue(){
        while(queueIsEmpty()){
            try {
                //System.out.println("Elevator " + elevatorID + " is waiting for command in state " + currentState);
                Logging.info("ElevatorCar", ""+ elevatorID, "is waiting for command in " + currentState);

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
            //System.out.println("Elevator " + elevatorID + " floor: " + toAdd + " has button pushed and lamp turned on");
            Logging.info("ElevatorCar", ""+ elevatorID, " floor: " + toAdd + " has button pushed and lamp turned on");
        }
        String direction = this.getDirection();
        //Sort based on direction (elevator will attempt to go to each one in order
        if(direction.equals("u")) {
            Collections.sort(floorQueue);
        } else if(direction.equals("d")){
            floorQueue.sort(Collections.reverseOrder());
        } else {
            //System.out.println("Got an idle request when it should not be!");
            Logging.warning("ElevatorCar", "Got an idle request when it should not be!");
        }
        notifyAll();
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
        if(scenario == TRANS_FAULT){
            transFaulted = true;
        } else if (scenario == HARD_FAULT){
            hardFaulted = true;
        }
        TestingElevator.setFaulted(this);
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
        //System.out.println("Elevator " + getElevatorID() + " has experienced a hard fault!");
        Logging.info("ElevatorCar", "" + getElevatorID()," has experienced a hard fault!"  );
        ElevatorStatus status = new ElevatorStatus(getCurrentFloor(), ElevatorStatus.STUCK, ElevatorStatus.CLOSED, getElevatorID());
        setStatus(status);
    }

    public void stop(){
        stopped = true;
    }

    public void run(){
        if (!stopped) {
            for (int i = 0; i < ConfigInfo.NUM_ELEVATORS; i++) {
                ElevatorCar el = new ElevatorCar(i);
            }
        }
    }

    public static void main(String[] args){
        for(int i = 0; i< ConfigInfo.NUM_ELEVATORS; i++){
            ElevatorCar el = new ElevatorCar(i);
        }
    }
}

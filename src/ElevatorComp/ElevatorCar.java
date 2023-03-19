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

    public ElevatorCar(int elevatorID){
        this.elevatorID = elevatorID;
        floorQueue = new ArrayList<>();
        status = new ElevatorStatus(elevatorID);
        this.currentState = new Idle(this);
        this.scenario = 0;
        this.currentFloor = 1;
        this.currentDirection = ElevatorStatus.IDLE;


        // Start comms
        ElevatorCommunications comms = new ElevatorCommunications(this);
        new Thread(comms, "El" + elevatorID).start();

    }

    public synchronized ElevatorStatus getStatus(){
        return status;
    }

    public synchronized void setStatus(ElevatorStatus newStatus){
        status = newStatus;
    }

    public synchronized ArrayList<Integer> getFloorQueue(){
        while(queueIsEmpty()){
            try {
                System.out.println(Thread.currentThread().getName() + " is waiting for command");
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

    public void changeState(ElevatorState state){
        this.currentState.exit();
        this.currentState = state;
        this.currentState.entry();
    }

    public static void main(String[] args){
        for(int i = 0; i< ConfigInfo.NUM_ELEVATORS; i++){
            ElevatorCar el = new ElevatorCar(i);
        }
    }
}

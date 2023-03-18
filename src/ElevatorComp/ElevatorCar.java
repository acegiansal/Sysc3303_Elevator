package ElevatorComp;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
import DataComp.RequestPacket;
import ElevatorComp.ElevatorStates.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ElevatorCar implements Runnable {

    private ElevatorStatus status;
    private ArrayList<Integer> floorQueue;
    private int elevatorID;
    private ElevatorState currentState;
    private int scenario;
    private int currentFloor;

    public ElevatorCar(int elevatorID){
        this.elevatorID = elevatorID;
        floorQueue = new ArrayList<>();
        status = new ElevatorStatus(elevatorID);
        this.currentState = new Idle(this);
        this.scenario = 0;
        this.currentFloor = 1;

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
        getStatus().setDirection(direction);
    }

    public synchronized void addFloor(int toAdd){
        if(!floorQueue.contains(toAdd)) {
            floorQueue.add(toAdd);
        }
        String direction = this.getStatus().getDirection();
        //Sort based on direction (elevator will attempt to go to each one in order
        if(direction.equals("u")) {
            Collections.sort(floorQueue);
        } else if(direction.equals("d")){
            floorQueue.sort(Collections.reverseOrder());
        }
        notifyAll();
    }

    public void handleRequest(RequestPacket request){
        setDirection(request.getDirection());
        addFloor(request.getStartFloor());
        addFloor(request.getEndFloor());
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

    public void changeState(ElevatorState state){
        this.currentState = state;
    }

    @Override
    public void run() {
        while(true){
            this.currentState.performAction();
            this.currentState.updateStatus();
        }
    }

    public static void main(String[] args){
        for(int i = 0; i< ConfigInfo.NUM_ELEVATORS; i++){
            ElevatorCar el = new ElevatorCar(i);
            Thread elThread = new Thread(el, "Elevator Car " + i);
            elThread.start();
        }

//        ElevatorCar el = new ElevatorCar(0);
//        Thread elThread = new Thread(el, "Elevator Car " + 0);
//        elThread.start();
    }
}

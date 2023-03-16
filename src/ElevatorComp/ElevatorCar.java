package ElevatorComp;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
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

    public ElevatorCar(int elevatorID){
        this.elevatorID = elevatorID;
        floorQueue = new ArrayList<>();
        status = new ElevatorStatus(elevatorID);
        this.currentState = new Idle(this);

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
        while(this.floorQueue.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Integer> queue = floorQueue;
        notifyAll();
        return queue;
    }

    public void setDirection(String direction){
        getStatus().setDirection(direction);
    }

    public synchronized void addFloor(int toAdd){
        floorQueue.add(toAdd);
        String direction = this.getStatus().getDirection();
        //Sort based on direction (elevator will attempt to go to each one in order
        if(direction.equals("u")) {
            Collections.sort(floorQueue);
        } else if(direction.equals("d")){
            Collections.sort(floorQueue, Collections.reverseOrder());
        }
        notifyAll();
    }

    public void changeState(ElevatorState state){
        this.currentState = state;
    }


    @Override
    public void run() {
        while(true){
            this.currentState.performAction();
        }
    }

    public static void main(String[] args){
        for(int i = 0; i< ConfigInfo.NUM_ELEVATORS; i++){
            ElevatorCar el = new ElevatorCar(i);
            Thread elThread = new Thread(el, "Elevator Car " + i);
            elThread.start();
        }
    }
}

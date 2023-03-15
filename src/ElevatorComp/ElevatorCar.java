package ElevatorComp;

import DataComp.ElevatorStatus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ElevatorCar {

    private ElevatorStatus status;
    private ArrayList<Integer> floorQueue;
    private int elevatorID;

    public ElevatorCar(int elevatorID){
        this.elevatorID = elevatorID;
        floorQueue = new ArrayList<>();
        status = new ElevatorStatus();

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

    public synchronized void addFloor(int toAdd){
        floorQueue.add(toAdd);
        if(this.getStatus().getDirection().equals("u")) {
            Collections.sort(floorQueue);
        } else if(this.getStatus){

        }
    }
}

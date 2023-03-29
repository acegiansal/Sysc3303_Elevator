package ControlComp;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
import DataComp.RequestPacket;
import Testing.TestingElevator;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Scheduler implements Runnable{

    private ElevatorBox databox;
    private int numOfElevators;
    private DatagramSocket sendReceiveSocket;

    private boolean stopped = false;
    private int maxFloor;


    public Scheduler(ElevatorBox databox, int elevatorNum, int maxFloor){
        this.numOfElevators = elevatorNum;
        this.databox = databox;
        this.maxFloor = maxFloor;

        try {
            sendReceiveSocket = new DatagramSocket(ConfigInfo.SCHEDULER_PORT);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    private byte[] receiveData(){
        byte[] received = new byte[ConfigInfo.PACKET_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(received, received.length);

        // Block until a datagram packet is received from receiveSocket.
        try {
            //System.out.println(Thread.currentThread().getName() + " Waiting..."); // so we know we're waiting
            //System.out.println("Scheduler is waiting for something");
            Logging.info2("Scheduler","Scheduler is waiting for something" );
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            if (!stopped) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return received;
    }

    private int algorithm(RequestPacket req){

        boolean lookingForEl = false;
        //System.out.println("============");
        ArrayList<Integer> toUse = new ArrayList<>();
        do {
            // 1. Get all elevators that are either idle or going in the same direction
            toUse = getSameDirection(req);
            //System.out.println("All in same direction: " + toUse.toString());
            Logging.info2("Scheduler","All in same direction: " + toUse.toString() );

            // 2. Get all elevators that are not ahead
            toUse = getCarNotPast(req, toUse);
            //System.out.println("Elevators not ahead: " + toUse.toString());
            Logging.info2("Scheduler","Elevators not ahead: " + toUse.toString());

            // 3. Get elevators that are closest
            toUse = getClosestElevators(req, toUse);
            //System.out.println("Closest elevators: " + toUse.toString());
            Logging.info2("Scheduler","Closest elevators: " + toUse.toString() );

            // 4. Check if there are any elevators in queue. If yes, continue, if false, wait 1 second then do it again
            lookingForEl = !(toUse.size() > 0);
            if(lookingForEl){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } while(lookingForEl);

        // 5. Grab the first elevator
        return toUse.get(0);
    }

    private ArrayList<Integer> getSameDirection(RequestPacket request){
        ArrayList<Integer> toUse = new ArrayList<>();

        for(int i=0; i<numOfElevators; i++){
            ElevatorStatus status = databox.getStatus(i);
            if(status.getDirection().equals(request.getDirection()) || status.getDirection().equals(ElevatorStatus.IDLE)){
                toUse.add(status.getId());
            }
        }
        return toUse;
    }

    private ArrayList<Integer> getCarNotPast(RequestPacket request, ArrayList<Integer> sameDirection){
        ArrayList<Integer> toUse = new ArrayList<>();

        for (Integer integer : sameDirection) {
            ElevatorStatus status = databox.getStatus(integer);
//            System.out.println("El " + status.getId() + " is at " + status.getCurrentFloor() + ", start floor is: " + request.getStartFloor());
            if (request.getDirection().equals(ElevatorStatus.UP)) {
                if (status.getCurrentFloor() <= request.getStartFloor() || status.getDirection().equals(ElevatorStatus.IDLE)) {
                    toUse.add(status.getId());
                }
            } else {
                if (status.getCurrentFloor() >= request.getStartFloor() || status.getDirection().equals(ElevatorStatus.IDLE)) {
                    toUse.add(status.getId());
                }
            }
        }
        return toUse;
    }

    private ArrayList<Integer> getClosestElevators(RequestPacket request, ArrayList<Integer> choices){
        ArrayList<Integer> toUse = new ArrayList<>();
        int min = ConfigInfo.NUM_FLOORS;

        // Get smallest distance
        for (Integer integer : choices) {
            ElevatorStatus status = databox.getStatus(integer);
            int distance = getDistance(status.getCurrentFloor(), request.getStartFloor());
            if(distance < min){
                min = distance;
            }
        }

        // Get all elevators with that distance
        for (Integer integer : choices) {
            ElevatorStatus status = databox.getStatus(integer);
            int distance = getDistance(status.getCurrentFloor(), request.getStartFloor());
            if(distance == min){
                toUse.add(integer);
            }
        }
        return toUse;
    }

    private int getDistance(int elFloor, int reqFloor){
        return Math.abs(elFloor - reqFloor);
    }

    @Override
    public void run() {
        while(!stopped){
            // Receive floor request
            byte[] request = receiveData();

            if(RequestPacket.isValidRequest(request, maxFloor)){
                RequestPacket req = RequestPacket.translateRequestBytes(request);
                TestingElevator.setReqPak(req);
                int chosenEl = algorithm(req);
                TestingElevator.addElevator(chosenEl);
                // Put into box and update the status of the chosen elevator
                //System.out.println("Putting {" + Arrays.toString(request) + "} into elevator: [" + chosenEl + "]");
                Logging.info("Scheduler",""+ chosenEl, "Putting " + Arrays.toString(request) + " into elevator");
                ElevatorStatus prevStatus = databox.getStatus(chosenEl);
                ElevatorStatus newStatus = new ElevatorStatus(prevStatus.getCurrentFloor(), req.getDirection(), prevStatus.getId());
                databox.setStatus(chosenEl, newStatus);
                databox.setRequest(chosenEl, request);
            } else {
                Logging.info2("Scheduler","Received invalid request! Dropping request" );
            }
        }
        
    }

    public void closing(){
        stopped = true;
        sendReceiveSocket.close();
    }

//    public static void main(String[] args){
//        int elNum = 2;
//        Scheduler test = new Scheduler(new ElevatorBox(elNum), elNum);
//        RequestPacket testPacket = new RequestPacket(1, 3,"u", 0, "10:00");
//
//        //System.out.println(test.algorithm(testPacket));
//        Logging.info2("Scheduler","" + test.algorithm(testPacket));
//    }

}

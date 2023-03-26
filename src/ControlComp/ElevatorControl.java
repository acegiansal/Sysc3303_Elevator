package ControlComp;

import Config.ConfigInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.exit;

public class ElevatorControl implements Runnable{

    private ArrayList<ElevatorIntermediate> mediators;
    private ElevatorBox databox;
    private Scheduler scheduler;
    private DatagramSocket receiveSocket;

    private boolean stopped = false;

    Logging logger = new Logging();

    public ElevatorControl(int elevatorNum){
        mediators = new ArrayList<>();
        databox = new ElevatorBox(elevatorNum);
        scheduler = new Scheduler(databox, elevatorNum);

        for(int i = 0; i<elevatorNum; i++){
            mediators.add(i, new ElevatorIntermediate(databox, i));
        }

        try {
            receiveSocket = new DatagramSocket(ConfigInfo.CONTROL_PORT);
        } catch (SocketException se) {
            se.printStackTrace();
            exit(1);
        }

        // Start scheduler
        new Thread(scheduler, "Scheduler").start();
    }

    private void handleReceiveStatus(){

        byte[] data = new byte[ConfigInfo.PACKET_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        // Block until a datagram packet is received from receiveSocket.
        try {
            Logging.info2("ElevatorControl", "Elevator Control is Waiting for something from the elevators...");
            //System.out.println("Elevator Control is Waiting for something from the elevators..."); // so we know we're waiting
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            if (!stopped) {
                e.printStackTrace();
                exit(1);
            }
        }
        //TODO check if packet is valid status upadte
        //Get ID of the elevator then delegates to respective mediator
        int mediatorTarget = data[0];
        ElevatorIntermediate mediator = mediators.get(mediatorTarget);

        String medString = "Mediator " + mediatorTarget;
        mediator.setRunConfig(receivePacket.getPort(), data);
        Thread delegation = new Thread(mediator, medString);
        delegation.start();
    }

    public void controlElevator(){
        while(!stopped){
            handleReceiveStatus();
        }
    }

    public void stop(){
        stopped = true;
        receiveSocket.close();
        for (int i = 0; i < mediators.size(); i++){
            mediators.get(i).closing();
        }
        scheduler.closing();
    }

    public void run(){
        if (!stopped) {
            this.controlElevator();
        }
    }

    public static void main(String[] args){
        ElevatorControl controller = new ElevatorControl(ConfigInfo.NUM_ELEVATORS);
        controller.controlElevator();
    }

}

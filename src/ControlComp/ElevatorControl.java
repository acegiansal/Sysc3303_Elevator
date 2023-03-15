package ControlComp;

import Config.ConfigInfo;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ElevatorControl {

    private ArrayList<ElevatorIntermediate> mediators;
    private ElevatorBox databox;
    private Scheduler scheduler;
    private DatagramSocket receiveSocket;

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
            System.exit(1);
        }

        // Start scheduler
        new Thread(scheduler, "Scheduler").start();
    }

    private void handleReceiveStatus(){

        byte[] data = new byte[ConfigInfo.PACKET_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        // Block until a datagram packet is received from receiveSocket.
        try {
            System.out.println("Elevator Control is Waiting for something from the elevators..."); // so we know we're waiting
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //Get ID of the elevator then delegates to respective mediator
        int mediatorTarget = data[0];
        ElevatorIntermediate mediator = mediators.get(mediatorTarget);
        String medString = "Mediator " + mediatorTarget;
        mediator.setRunConfig(receivePacket.getPort(), data);
        Thread delegation = new Thread(mediator, medString);
        delegation.start();
    }

    public void controlElevator(){
        while(true){
            handleReceiveStatus();
        }
    }

    public static void main(String[] args){
        ElevatorControl controller = new ElevatorControl(2);
        controller.controlElevator();
    }

}

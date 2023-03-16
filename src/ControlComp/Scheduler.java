package ControlComp;

import Config.ConfigInfo;

import java.io.IOException;
import java.net.*;

public class Scheduler implements Runnable{

    private ElevatorBox databox;
    private int numOfElevators;
    private DatagramSocket sendReceiveSocket;

    public Scheduler(ElevatorBox databox, int elevatorNum){
        this.numOfElevators = elevatorNum;
        this.databox = databox;

        try {
            sendReceiveSocket = new DatagramSocket(ConfigInfo.SCHEDULER_PORT);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

//    public void sendData(byte[] data) {
//
//        DatagramPacket sendPacket = null;
//        // Create a packet that sends to the same computer at the previously specified
//        // port
//        try {
//            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), ConfigInfo.FLOOR_PORT);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        // Send the datagram packet to the server via the send/receive socket.
//        try {
//            sendReceiveSocket.send(sendPacket);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }

    private byte[] receiveData(){
        byte[] received = new byte[ConfigInfo.PACKET_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(received, received.length);

        // Block until a datagram packet is received from receiveSocket.
        try {
            //System.out.println(Thread.currentThread().getName() + " Waiting..."); // so we know we're waiting
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return received;
    }

    private int chooseElevator(byte[] request){
        // TODO implement actual algorithm
        return 0;
    }

    @Override
    public void run() {

        while(true){
            // Receive floor request
            byte[] request = receiveData();
            //TODO check if request is valid
            // Schedule
            int chosenEl = chooseElevator(request);
            // Put into box
            databox.setRequest(chosenEl, request);
        }
        
    }
}

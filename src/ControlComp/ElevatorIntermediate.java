package ControlComp;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;

import java.io.IOException;
import java.net.*;

public class ElevatorIntermediate implements Runnable{

    private ElevatorBox databox;
    private DatagramSocket sendSocket;
    private int elevatorPort;
    private byte[] status;
    private int elevatorID;

    public ElevatorIntermediate(ElevatorBox databox, int elevatorID){
        this.elevatorID = elevatorID;
        this.databox = databox;
        this.status = new byte[ConfigInfo.PACKET_SIZE];
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public void setRunConfig(int hostPort, byte[] status){
        this.elevatorPort = hostPort;
        this.status = status;
    }

    public ElevatorStatus translateStatusBytes(byte[] data){
        int currentFloor = data[1];
        String direction = new String(data, 2, 1);
        return new ElevatorStatus(currentFloor, direction);
    }

    private void sendData(byte[] data){
        DatagramPacket sendPacket = null;
        // Create a packet that sends to the same computer at the previously specified
        // port
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), this.elevatorPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Send the datagram packet to the server via the send/receive socket.
        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {

        // Translate data
        ElevatorStatus newStatus = translateStatusBytes(this.status);
        // Set status
        databox.setStatus(elevatorID, newStatus);
        // Get request
        byte[] scheduleRequest = databox.getRequest(elevatorID);
        // Send request
        sendData(scheduleRequest);
    }

//    public static void main(String[] args){
//        byte[] test = {3, 5, 117};
//        ElevatorIntermediate testInt = new ElevatorIntermediate(new ElevatorBox(1), 0);
//        testInt.translateStatusBytes(test);
//    }
}

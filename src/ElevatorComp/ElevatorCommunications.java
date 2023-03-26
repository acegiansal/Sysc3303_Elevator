package ElevatorComp;

import Config.ConfigInfo;
import DataComp.RequestPacket;
import DataComp.ElevatorStatus;
import Testing.TestingElevator;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ElevatorCommunications implements Runnable {

    private ElevatorCar elevator;
    private DatagramSocket sendReceiveSocket;

    public ElevatorCommunications(ElevatorCar car){
        this.elevator = car;
        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Sends data to the given port
     * @param data The data to be sent
     */
    public void sendData(byte[] data) {

        DatagramPacket sendPacket = null;
        // Create a packet that sends to the same computer at the previously specified
        // port
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), ConfigInfo.CONTROL_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // Send the datagram packet to the server via the send/receive socket.
        try {
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

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

    public void closing(){
        sendReceiveSocket.close();
    }

    @Override
    public void run() {

        while(true){
            // send packet
            ElevatorStatus status = elevator.getStatus();
            byte[] transformed = ElevatorStatus.translateToBytes(status);
            if(!elevator.isStatusUpdated()){
                transformed[3] = 1;
            }
            sendData(transformed);
            elevator.setStatusUpdated(false);

            //receive request
            byte[] request = receiveData();
            //Translate packet
            if(!RequestPacket.isEmptyRequest(request)){
                RequestPacket req = RequestPacket.translateRequestBytes(request);

                // put floor in or go back to send
                elevator.requestReceived(req);
            }

            // Pause before checking again
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}

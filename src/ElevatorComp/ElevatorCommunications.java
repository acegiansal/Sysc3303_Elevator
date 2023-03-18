package ElevatorComp;

import Config.ConfigInfo;
import DataComp.RequestPacket;
import DataComp.ElevatorStatus;

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

    @Override
    public void run() {

        while(true){
            // send packet
            ElevatorStatus status = elevator.getStatus();
            byte[] transformed = ElevatorStatus.translateToBytes(status);
            sendData(transformed);

            //receive request
            byte[] request = receiveData();
            //Translate packet
            if(!RequestPacket.isEmptyRequest(request)){
                int startFloor = request[0];
                int endFloor = request[1];
                int scenario = request[2];
                String direction = new String(request, 3, 1);

                RequestPacket req = new RequestPacket(startFloor, endFloor, direction, scenario);

                // put floor in or go back to send
                elevator.handleRequest(req);
            } else {
//                System.out.println("Request is empty!: " + Arrays.toString(request));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}

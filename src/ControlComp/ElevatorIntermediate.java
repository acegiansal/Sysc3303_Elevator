package ControlComp;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ElevatorIntermediate implements Runnable {

    private ElevatorBox databox;
    private DatagramSocket sendSocket;
    private int elevatorPort;
    private byte[] status;
    private int elevatorID;

    private boolean stopped = false;

    public ElevatorIntermediate(ElevatorBox databox, int elevatorID) {
        this.elevatorID = elevatorID;
        this.databox = databox;
        this.status = new byte[ConfigInfo.PACKET_SIZE];
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            if (!stopped) {
                se.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void setRunConfig(int hostPort, byte[] status) {
        this.elevatorPort = hostPort;
        this.status = status;
    }

    private void sendData(byte[] data) {
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
            if (!stopped) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void closing(){
        sendSocket.close();
        stopped = true;
    }

    @Override
    public void run() {

        // Translate data
        ElevatorStatus newStatus = ElevatorStatus.translateStatusBytes(this.status);
        // Set status IF the elevator has just been updated
        if (status[3] != 1) {
            databox.setStatus(elevatorID, newStatus);
            Logging.info2("ElevatorIntermediate", "" + newStatus);
            //System.out.println(newStatus);
        }
        // Get request
        byte[] scheduleRequest = databox.getRequest(elevatorID);
//        System.out.println("Sending to elevator: " + Arrays.toString(scheduleRequest));
        // Send request
        sendData(scheduleRequest);
    }
}

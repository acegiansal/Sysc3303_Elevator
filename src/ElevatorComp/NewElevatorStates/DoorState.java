package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import DataComp.RequestPacket;
import ElevatorComp.ElevatorCar;

import java.io.IOException;
import java.net.*;

public abstract class DoorState extends ElevatorState{
    private DatagramSocket sendSocket;

    public DoorState(ElevatorCar elevator) {
        super(elevator);
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void entry() {
        byte[] toFloor = new byte[ConfigInfo.PACKET_SIZE];
        toFloor[0] = (byte)elevator.getElevatorID();
        toFloor[1] = (byte)elevator.getCurrentFloor();
        toFloor = RequestPacket.combineByteArr(2, toFloor, elevator.getDirection().getBytes());
        sendData(toFloor);
    }

    @Override
    public void exit() {
        sendSocket.close();
        this.killTimer();
    }

    @Override
    public abstract void timeout();

    /**
     * Sends data to the given port
     * @param data The data to be sent
     */
    private void sendData(byte[] data) {

        DatagramPacket sendPacket = null;
        // Create a packet that sends to the same computer at the previously specified
        // port
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), ConfigInfo.FLOOR_PORT);
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
}

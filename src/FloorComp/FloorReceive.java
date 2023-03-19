package FloorComp;

import Config.ConfigInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class FloorReceive implements Runnable{
    /** The floor receive port */
    private static int FLOOR_PORT;
    /** A socket that sends and receives data */
    private DatagramSocket receiveSocket;
    ArrayList<Floor> floors;


    public FloorReceive(ArrayList<Floor> floors) {
        this.floors = floors;
        try {
            receiveSocket = new DatagramSocket(ConfigInfo.FLOOR_PORT);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    private void getInfo() {
        while (true) {
            //Receive reply
            byte[] reply = new byte[ConfigInfo.PACKET_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(reply, reply.length);
            try {
                // Block until a datagram is received via sendReceiveSocket.
//            logging.info2("Floor", "Floor Waiting for reply");
                //System.out.println("Floor Waiting for reply");
                receiveSocket.receive(receivePacket);
                decodePacket(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void decodePacket(DatagramPacket receivePacket) {
        byte[] reply = receivePacket.getData();
        int elevatorID = reply[0];
        int floor = reply[1];
        String direction;
        if (reply[2] == 0){
            direction = "Down";
        } else if (reply[2] == 1){
            direction = "Up";
        } else {
            direction = "Leaving";
        }
        this.floors.get(floor).arrive(direction, elevatorID);
    }

    @Override
    public void run() {
        this.getInfo();
    }
}

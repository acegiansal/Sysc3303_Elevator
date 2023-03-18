package FloorComp;

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
            receiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    private void getInfo() {
        while (true) {
            //Receive reply
            byte[] reply = new byte[50];
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
        // take specified packet and decode here, then send data to floor.
    }

    @Override
    public void run() {
        this.getInfo();
    }
}

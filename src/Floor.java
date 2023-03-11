import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class that represents a floor in an elevator system
 */
public class Floor implements Runnable{

    /** The scheduler responsible for the floor */
    private int schedulerPort;
    private String[] testString;
    /** A socket that sends and receives data */
    private DatagramSocket sendReceiveSocket;

    public Floor(int schedulerPort) {
        this.schedulerPort = schedulerPort;
        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public Floor(int schedulerPort, String[] test){
        this.schedulerPort = schedulerPort;
        testString = test;
        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Read elevator input from a file
     * @param file The file containing the elevator instructions
     */
    private void readFromFile(File file){
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //Split information (divided by spaces)
                String[] splitData = data.split(" ");
                //Data must be 4 items long
                if (splitData.length != 4){
                    logging.warning( "Floor", "INPUT DATA INVALID!!");
                    System.out.println("INPUT DATA INVALID!!");
                    break;
                }else {
                    //Direction is true if 'Up' is selected
                    String direction = (splitData[2].equals("Up")) ? "u" : "d";
                    System.out.println(splitData[0]);
                    prepareSend(splitData[0], Integer.parseInt(splitData[1]), direction, Integer.parseInt(splitData[3]));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            logging.warning( "Floor", "An error occurred while reading the input file" + e.getMessage());
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    private byte[] sendRpcRequest(byte[] data){

        System.out.println("Floor sending: " + Arrays.toString(data));
        sendData(data, schedulerPort);

        //Receive reply
        byte[] reply = new byte[50];
        DatagramPacket receivePacket = new DatagramPacket(reply, reply.length);

        try {
            // Block until a datagram is received via sendReceiveSocket.
            System.out.println("Floor Waiting for reply");
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return reply;

    }

    /**
     * Sends data to the given port
     * @param data The data to be sent
     */
    public void sendData(byte[] data, int port) {
        DatagramPacket sendPacket = null;
        // Create a packet that sends to the same computer at the previously specified
        // port
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
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

    /**
     * Sends the data to scheduler
     * @param time The time that the input was retrieved
     * @param floorNumber The original floor number
     * @param direction The direction that the elevator should be going to
     * @param carButton The target floor
     */
    private void prepareSend(String time, int floorNumber, String direction, int carButton){
        byte[] request = PacketProcessor.createRequestPacket(time, floorNumber, direction, carButton);
        logging.info( "Floor", "Floor Sending" + Arrays.toString(request));
        sendRpcRequest(request);
    }

    void testPrepareSend(String time, int floorNumber, String direction, int carButton){
        byte[] request = PacketProcessor.createRequestPacket(time, floorNumber, direction, carButton);
        sendRpcRequest(request);
    }

    public void run(){
        for (int i = 0; i < testString.length; i++) {
            String[] splitData = testString[i].split(" ");
            String direction = (splitData[2].equals("Up")) ? "u" : "d";
            System.out.println(splitData[0]);
            System.out.println("The direction of the damn elevator is " + direction);
            prepareSend(splitData[0], Integer.parseInt(splitData[1]), direction, Integer.parseInt(splitData[3]));
        }
    }

    public static void main(String[] args){
        Floor floorControl = new Floor(Config.SCHEDULER_PORT);

        //Read information from selected file
        File file = new File("src/elevatorFile");
        floorControl.readFromFile(file);

        while(true){
            byte[] getRequest = PacketProcessor.createGetRequest();
            byte[] reply = floorControl.sendRpcRequest(getRequest);
            System.out.println("Floor got reply: " + Arrays.toString(reply));
        }

    }
}
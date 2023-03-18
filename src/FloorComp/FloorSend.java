package FloorComp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import Config.*;

public class FloorSend implements Runnable{

    ArrayList<Floor> floors;
    private static int MAX_FLOORS;
    /** The scheduler responsible for the floor */
    private static int SCHEDULER_PORT;
    /** A socket that sends and receives data */
    private DatagramSocket sendSocket;
    private int timestamp;

    public enum floorType {
        TOP,
        BOT,
        MID
    }

    public FloorSend(int numFloors, int schedulerPort){
        MAX_FLOORS = numFloors;
        SCHEDULER_PORT = schedulerPort;
        timestamp = 0;
        floors = new ArrayList<>();
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        for(int i = 0; i<numFloors; i++){
            floorType type;

            if(i == 0){
                type = floorType.BOT;
            } else if(i == MAX_FLOORS){
                type = floorType.TOP;
            } else {
                type = floorType.MID;
            }
            floors.add(i, new Floor((i+1), type));
        }
        FloorReceive floorReceive = new FloorReceive(this.floors);
        Thread floorReceiveThread = new Thread(floorReceive);
        floorReceiveThread.start();
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
                    System.out.println( "Floor" + "INPUT DATA INVALID!!");
                    //System.out.println("INPUT DATA INVALID!!");
                    break;
                }else {
                    //Direction is true if 'Up' is selected
                    String direction = (splitData[2].equals("Up")) ? "u" : "d";
                    timestamp = (timestamp == 0) ? timeConversion(splitData[0]) : timestamp;
                    if (timeConversion(splitData[0]) == timestamp){
                        System.out.println("First: " + timestamp);
                        prepareSend(splitData[0], Integer.parseInt(splitData[1]), direction, Integer.parseInt(splitData[3]));
                    } else if (timeConversion(splitData[0]) > timestamp){
                        Thread.sleep((timeConversion(splitData[0]) - timestamp));
                        System.out.println("Slept: " + (timeConversion(splitData[0]) - timestamp));
                        prepareSend(splitData[0], Integer.parseInt(splitData[1]), direction, Integer.parseInt(splitData[3]));
                    }
                }
            }
            myReader.close();
//            System.exit(0);
        } catch (FileNotFoundException | InterruptedException e) {
//            logging.warning( "Floor", "An error occurred while reading the input file" + e.getMessage());
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Converts time from date format to seconds
     * @param time The current time in date format
     * @return Current time in seconds
     */
    public static int timeConversion(String time) {
        long timeSecs = 0;
        try {
            timeSecs = new SimpleDateFormat("hh:mm:ss.SSS").parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) (timeSecs / 1000);
    }

    /**
     * Sends the data to scheduler
     * @param time The time that the input was retrieved
     * @param floorNumber The original floor number
     * @param direction The direction that the elevator should be going to
     * @param carButton The target floor
     */
    private void prepareSend(String time, int floorNumber, String direction, int carButton){
//        needs to make a new packet to send the request
        byte[] request = new byte[1];
//        byte[] request = PacketProcessor.createRequestPacket(time, floorNumber, direction, carButton);
//        logging.info2( "Floor", "Floor Sending" + Arrays.toString(request));
        Floor selectedFloor = floors.get(floorNumber);
//        push floor button
        selectedFloor.send(direction);
        sendRpcRequest(request);
    }

    /**
     * Sends the given byte array data as an RPC request to the Scheduler subsystem and waits for a reply.
     * @param data the byte array to send as an RPC request
     */
    private void sendRpcRequest(byte[] data){

        //System.out.println("Floor sending: " + Arrays.toString(data));
//        logging.info2("Floor","Floor sending: " + Arrays.toString(data));
        sendData(data, SCHEDULER_PORT);

        //Receive reply
        byte[] reply = new byte[50];
        DatagramPacket receivePacket = new DatagramPacket(reply, reply.length);

//        try {
//            // Block until a datagram is received via sendReceiveSocket.
////            logging.info2("Floor", "Floor Waiting for reply");
//            //System.out.println("Floor Waiting for reply");
//            ReceiveSocket.receive(receivePacket);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        return reply;

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
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args){
//        FloorSend floorSend = new FloorSend(22, ConfigInfo.SCHEDULER_PORT);
    	FloorSend floorSend = new FloorSend(22, 5020);
        Thread floorSendThread = new Thread(floorSend);
        floorSendThread.start();
    }

    public void run(){
        //Read information from selected file
        File file = new File("src/elevatorFile");
        readFromFile(file);

//        while(true){
//            byte[] getRequest = PacketProcessor.createGetRequest();
//            byte[] reply = floorControl.sendRpcRequest(getRequest);
//            logging.info2("Floor", "Floor got reply: " + Arrays.toString(reply) );
//            //System.out.println("Floor got reply: " + Arrays.toString(reply));
//        }
    }
}

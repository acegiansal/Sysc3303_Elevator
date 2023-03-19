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
import DataComp.RequestPacket;

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

    public FloorSend(int numFloors, int schedulerPort, int numElevators){
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
            floors.add(i, new Floor((i+1), type, numElevators));
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
                    RequestPacket request = new RequestPacket(Integer.parseInt(splitData[1]), Integer.parseInt(splitData[3]), direction, Integer.parseInt(splitData[4]), splitData[0]);
                    timestamp = (timestamp == 0) ? timeConversion(splitData[0]) : timestamp;
                    if (timeConversion(splitData[0]) == timestamp){
                        System.out.println("First: " + timestamp);
                        prepareSend(request);
                    } else if (timeConversion(splitData[0]) > timestamp){
                        Thread.sleep((timeConversion(splitData[0]) - timestamp));
                        System.out.println("Slept: " + (timeConversion(splitData[0]) - timestamp));
                        prepareSend(request);
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException | InterruptedException e) {
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

    private void prepareSend(RequestPacket request){

        byte[] toSend = RequestPacket.translateToBytes(request);
        Floor selectedFloor = floors.get(request.getStartFloor());
        // Push floor button
        selectedFloor.send(request.getDirection());
        sendData(toSend, SCHEDULER_PORT);
    }

    /**
     * Sends the given byte array data as an RPC request to the Scheduler subsystem and waits for a reply.
     * @param data the byte array to send as an RPC request
     */
    private void sendRpcRequest(byte[] data){
        sendData(data, SCHEDULER_PORT);
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

    public void run(){
        //Read information from selected file
        File file = new File("src/elevatorFile");
        readFromFile(file);

    }

    public static void main(String[] args){
    	FloorSend floorSend = new FloorSend(ConfigInfo.NUM_FLOORS, ConfigInfo.SCHEDULER_PORT, ConfigInfo.NUM_ELEVATORS);
        Thread floorSendThread = new Thread(floorSend);
        floorSendThread.start();
    }
}

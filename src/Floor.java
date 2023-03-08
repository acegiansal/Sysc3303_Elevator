import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class that represents a floor in an elevator system
 */
public class Floor {

    /** The scheduler responsible for the floor */
    private int schedulerPort;
    private String testString;
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


    private void sendRpcRequest(byte[] data){
        DatagramPacket sendPacket = null;
        // Create a packet that sends to the same computer at the previously specified
        // port
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), schedulerPort);
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

        //Receive reply

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

    /**
     * Sends the data using a preexisting ElevatorInfo object
     * @param info Existing ElevatorInfo object to send
     */
//    private void send(ElevatorInfo info){
//        logging.info( "Floor", "Floor Sending" + info);
//        //System.out.println("Floor Sending " + info);
//        scheduler.addFloorMessage(info);
//    }

//    //Created for only testing purposes
//    void testReceiveFromSched(){
//        ElevatorInfo info = scheduler.getElevatorMessages();
//        System.out.println("Floor Receiving " + info);
//        testString = info.toString();
//    }
//
//    //Created for only testing purposes
//    void testSend(String time, int floorNumber, boolean direction, int carButton){
//        ElevatorInfo info = new ElevatorInfo(direction, floorNumber, time, carButton);
//        System.out.println("Floor Sending " + info);
//        scheduler.addFloorMessage(info);
//    }

    //Created for only testing purposes
    String getTestString(){
        return testString;
    }

    public static void main(String[] args){
        Floor floorControl = new Floor(Config.SCHEDULER_PORT);

        //Read information from selected file
        File file = new File("src/elevatorFile");
        floorControl.readFromFile(file);

    }
}

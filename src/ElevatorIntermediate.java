import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ElevatorIntermediate implements Runnable {

    private int elevatorID;
    /** A socket that sends and receives data */
    private DatagramSocket sendReceiveSocket;
    private ElevatorBox databox;

    public ElevatorIntermediate(ElevatorBox box, int elevatorID, int intermediatePort){
        this.elevatorID = elevatorID;
        this.databox = box;
        try {
            sendReceiveSocket = new DatagramSocket(intermediatePort);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Handles an RPC request sent by the Floor
     */
    public void handleRpcRequest(){
        // Read out information
        byte[] data = new byte[50];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);

        // Block until a datagram packet is received from receiveSocket.
        try {
            logging.info2("ElevatorIntermediate",Thread.currentThread().getName() + " mediator Waiting...");
            //System.out.println("\n [" + Thread.currentThread().getName() + "] mediator Waiting..."); // so we know we're waiting
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Translate Status
        extractStatus(data);
        //Get request
        if(PacketProcessor.isGetRequest(data)) {
            logging.info2("ElevatorIntermediate", Thread.currentThread().getName() + " mediator Has GET request..." );
            //System.out.println("\n [" + Thread.currentThread().getName() + "] mediator Has GET request...");
            byte[] requestData = databox.getRequestData(elevatorID);
            sendData(requestData, receivePacket.getPort());
        } else { //Put request
            logging.info2("ElevatorIntermediate", Thread.currentThread().getName() + " mediator Has PUT request...");
            //System.out.println("\n [" + Thread.currentThread().getName() + "] mediator Has PUT request...");
            //Puts into box
            databox.putResponseData(data, elevatorID);

            //Sends reply to floor
            byte[] reply = PacketProcessor.createOkReply();
            sendData(reply, receivePacket.getPort());
        }
    }

    private void extractStatus(byte[] data){
        int delimiter = PacketProcessor.findDelimiter(data, 1);
        int stateDelim = PacketProcessor.findDelimiter(data, delimiter+1);
        int floorDelim = PacketProcessor.findDelimiter(data, stateDelim+1);

        logging.info2("ElevatorIntermediate","" + Arrays.toString(data));
        //System.out.println("-------------" + Arrays.toString(data));


        String enumVal = new String(data, delimiter+1, stateDelim-delimiter-1);
        ElevatorState state = ElevatorState.valueOf(enumVal);
        int floorNumber = data[floorDelim - 1];

        int stateValue = (state == ElevatorState.IDLE) ? 0 : 1;
        String elevatorNum = (elevatorID == 0) ? "el1" : "el2";

        logging.info2("ElevatorIntermediate", "Setting state to " + stateValue);
        //System.out.println("Setting state to " + stateValue);

        databox.setElevatorData(elevatorNum + "State", stateValue);
        databox.setElevatorData(elevatorNum + "Floor", floorNumber);

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

    @Override
    public void run() {
        while(true){
            handleRpcRequest();
        }
    }
}

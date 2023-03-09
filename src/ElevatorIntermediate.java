import java.io.IOException;
import java.net.*;

public class ElevatorIntermediate implements Runnable {

    private int elevatorID;
    private int intermediatePort;
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
            System.out.println("\n [" + Thread.currentThread().getName() + "] mediator Waiting..."); // so we know we're waiting
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Get request
        if(PacketProcessor.isGetRequest(data)) {
            System.out.println("\n [" + Thread.currentThread().getName() + "] mediator Has GET request...");
            byte[] requestData = databox.getRequestData(elevatorID);
            sendData(requestData, receivePacket.getPort());
        } else { //Put request
            System.out.println("\n [" + Thread.currentThread().getName() + "] mediator Has PUT request...");
            //Puts into box
            databox.putResponseData(data, elevatorID);

            //Sends reply to floor
            byte[] reply = PacketProcessor.createOkReply();
            sendData(reply, receivePacket.getPort());
        }
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

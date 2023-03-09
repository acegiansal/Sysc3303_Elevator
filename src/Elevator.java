import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Class meant to represent an elevator
 */
public class Elevator implements Runnable{

    private ElevatorState state;
    /** The scheduler for the elevator */
    private int intermediatePort;
    /** Represents request currently being processed */
    private ElevatorInfo currentRequest;
    /** The floor that the elevator started before moving */
    private int startingFloor;
    /** The loading time for the elevator (doors open and close) */
    private static final int LOAD_TIME = 1530;
    /** The moving time for one floor of the elevator */
    private static final int MOVE_TIME = 7060;
    /** A socket that sends and receives data */
    private DatagramSocket sendReceiveSocket;
    private int elevatorID;
    /** arrival sensor for the elevator approaching a floor */
    private boolean arrivalSensor;
    /** Direction lamp indicating the direction an elevator will go */
    private boolean directionLamp;
    /** Testing array */
    private boolean testing[] = {false, false, false, false};

    /**
     * Constructor for Elevator
     * @param intermediatePort port for the Elevator class
     * @param id the id of the elevator
     */
    public Elevator(int intermediatePort, int id) {
        this.intermediatePort = intermediatePort;
        this.startingFloor = 1;
        this.elevatorID = id;
        this.currentRequest = new ElevatorInfo("d", startingFloor, "", 1, id);
        this.arrivalSensor = false;

        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function to send an RPC request
     * @param data the data to be sent in a byte array
     * @return a byte array, the data received
     */
    public byte[] sendRpcRequest(byte[] data){
        sendData(data, intermediatePort);

        byte[] received = new byte[50];
        DatagramPacket receivePacket = new DatagramPacket(received, received.length);

        // Block until a datagram packet is received from receiveSocket.
        try {
            System.out.println(Thread.currentThread().getName() + " Waiting..."); // so we know we're waiting
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // If data is get request
        if(PacketProcessor.isGetRequest(data)) {
            System.out.println("GET REQUEST RECEIVED (" + Arrays.toString(data) + ")");
            //Translate request
            translateRequest(received);
            handleEvent(ElevatorEvent.CALL);
        } else {
            System.out.println("PUT REQUEST SENT (" + Arrays.toString(data) + ")");
        }
        return received;
    }

    /**
     * Translates the request from the Floor
     * @param data the data received
     */
    private void translateRequest(byte[] data){
        ElevatorInfo translated = PacketProcessor.translateRequest(data);
        this.currentRequest.setDirection(translated.getDirection());
        this.currentRequest.setTime(translated.getTime());
        this.currentRequest.setCarButton(translated.getCarButton());
        this.currentRequest.setFloorNumber(translated.getFloorNumber());
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
     * handles event
     * @param event The incoming event for the elevator
     */
    private void handleEvent(ElevatorEvent event){

        switch (event) {
            case CALL -> {
                currentRequest.setState(ElevatorState.CHECK_FLOOR);
                if (!testing[0]) {
                    checkFloor();
                }
            }
            case DOORS_OPEN -> {
                if (this.state == ElevatorState.IDLE) {
                    currentRequest.setState(ElevatorState.DOOR_OPENED);
                } else if (this.state == ElevatorState.MOVING) {
                    currentRequest.setState(ElevatorState.DOOR_OPENED);
                }
                doorOpen();
            }
            case DOORS_CLOSE -> {
                currentRequest.setState(ElevatorState.DOOR_CLOSED);
                if (!testing[3]) {
                    doorClosed();
                }
            }
            case PROCESS_REQUEST -> {
                currentRequest.setState(ElevatorState.MOVING);
                if (!testing[1]) {
                    moveElevator();
                }
            }
            case FINISH_REQUEST -> {
                currentRequest.setState(ElevatorState.IDLE);
                byte[] response = {0, 1, (byte) this.startingFloor};
                System.out.println(Thread.currentThread().getName() + " is sending PUT request!");
                byte[] reply = sendRpcRequest(response);
                System.out.println(Thread.currentThread().getName() + " GOT REPLY: " + Arrays.toString(reply));
            }
        }

    }

    /**
     * Checks if the elevator is already at the starting floor
     */
    private void checkFloor(){
        System.out.println();
        if (this.startingFloor == currentRequest.getFloorNumber()){
            logging.info("Elevator", "Elevator does not need to move to process request");
            handleEvent(ElevatorEvent.DOORS_OPEN);
        } else {
            logging.info( "Elevator", "Elevator needs to move to process request");
            handleEvent(ElevatorEvent.PROCESS_REQUEST);
        }
    }

    /**
     * Simulates opening elevator doors
     */
    private void doorOpen(){
        logging.info( "Elevator", "Elevator doors are open! (open for " + LOAD_TIME + " milliseconds) on floor " + startingFloor);

        try {
            Thread.sleep(LOAD_TIME);
        } catch (Exception e) {
            logging.warning("Elevator", "Broke");
            e.printStackTrace();
        }
        if (!testing[2]) {
            handleEvent(ElevatorEvent.DOORS_CLOSE);
        }
    }

    /**
     * Simulates closing doors
     */
    private void doorClosed(){
        logging.info("Elevator","Door closing on floor " + startingFloor );
        this.arrivalSensor = false;
        //If car is at the destination floor
        if (this.startingFloor == currentRequest.getCarButton()){
            handleEvent(ElevatorEvent.FINISH_REQUEST);
        } else {    // If not at destination floor, it must not have finished the request yet
            handleEvent(ElevatorEvent.PROCESS_REQUEST);
        }
    }

    /**
     * Simulates moving to the elevator
     */
    private void moveElevator(){

        int destination;
        if(this.startingFloor == currentRequest.getFloorNumber()){
            // If elevator is moving to destination
            destination = currentRequest.getCarButton();
        } else {
            // If elevator is moving to the starting floor
            destination = currentRequest.getFloorNumber();
        }

        String direction = this.startingFloor < destination ? "Up" : "Down";
        logging.info( "Elevator", "Elevator is moving " + direction);
        try {
            if(currentRequest.getFloorNumber()==startingFloor){
                this.arrivalSensor=true;
                this.directionLamp = direction.equals("Up");
            }
            else {
                Thread.sleep((long) MOVE_TIME * Math.abs(currentRequest.getFloorNumber() - startingFloor) - (MOVE_TIME / 2));
                this.arrivalSensor = true;
                this.directionLamp = direction.equals("Up");
            }
        } catch (Exception e) {
            logging.warning("Elevator", "Broke");
            e.printStackTrace();
        }
        this.startingFloor = destination;

        //TODO: WAIT TO SIMULATE ELEVATOR MOVING

        handleEvent(ElevatorEvent.DOORS_OPEN);
    }

    @Override
    public void run() {
        //Get request
        // Send get request
        // Receive request
        // Process request
        byte[] getRequest = PacketProcessor.createGetRequest();
        while(true) {
            System.out.println(Thread.currentThread().getName() + " is sending GET request!");
            byte[] reply = sendRpcRequest(getRequest);
            System.out.println(Thread.currentThread().getName() +" got request: " + Arrays.toString(reply));
        }
    }


}

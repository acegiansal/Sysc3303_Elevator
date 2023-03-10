import java.io.IOException;
import java.net.DatagramSocket;
import java.net.*;

public class Scheduler {
    /** Current state of the scheduler */
    private SchedulerState state;
    /** data box used for putting and getting elevator data */
    private ElevatorBox databox;
    /** A socket that sends and receives data */
    private DatagramSocket sendReceiveSocket;


    /**
     * Enum meant to represent scheduler states
     */
    enum SchedulerState {
        IDLE,
        RECEIVING
    }

    /**
     * Enum meant to represent scheduler events
     */
    private enum SchedulerEvent {
        REQUEST,
        RECEIVING_RESPONSE
    }

    /**
     * Creates a scheduler object
     */
    public Scheduler(int schedulerPort) {

        this.state = SchedulerState.IDLE;

        this.databox = new ElevatorBox(2);
        // Create elevator intermediates
        ElevatorIntermediate mediate1 = new ElevatorIntermediate(databox, 0, Config.ELEVATOR_HOST_1);
        ElevatorIntermediate mediate2 = new ElevatorIntermediate(databox, 1, Config.ELEVATOR_HOST_2);

        try {
            sendReceiveSocket = new DatagramSocket(schedulerPort);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }

        // Start elevator intermediates
        Thread med1 = new Thread(mediate1, "med1");
        Thread med2 = new Thread(mediate2, "med2");
        med1.start();
        med2.start();
    }

    /**
     * Handles incoming event for the scheduler
     * @param event Incoming event for the scheduler
     */
    private void handleEvent(SchedulerEvent event) {

        switch (event) {
            case REQUEST -> {
                logging.info("Scheduler", "Scheduler receiving request!");
                this.state = SchedulerState.RECEIVING;
            }
            case RECEIVING_RESPONSE -> {
                logging.info("Scheduler", "Scheduler sending reply");
                this.state = SchedulerState.IDLE;
            }
        }
    }

    /**
     * Handles incoming RPC requests from clients and dispatches them to the appropriate handler.
     * Blocks until a datagram packet is received from the receiveSocket.
     */
    public void handleRpcRequest() {
        // Read out information
        byte[] data = new byte[50];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);

        // Block until a datagram packet is received from receiveSocket.
        try {
            System.out.println("\n SCHEDULER Waiting..."); // so we know we're waiting
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Get request
        if(PacketProcessor.isGetRequest(data)) {
            //Figure out which elevator to get reply from
            //Send reply
            logging.info("Scheduler", "Scheduler got GET request, sending reply");
            byte[] response = databox.getSomeResponseData();
            sendData(response, receivePacket.getPort());

        } else { //Put request
            this.handleEvent(SchedulerEvent.REQUEST);
            //Does algorithm
             int chosenElevator = algorithm(data);
             if (chosenElevator==-1) {
                System.out.println("The algorithm could not choose an elevator!!");
                System.exit(2);
             } else if(chosenElevator == 0){
                 databox.setElevatorData("el1State", 1);
             } else{
                databox.setElevatorData("el2State", 1);
             }

//            int chosenElevator = 0;
            Thread helper = new Thread(new BoxHelper(data, chosenElevator));
            helper.start();
            //Sends reply to floor
            logging.info("Scheduler", "Scheduler got PUT request, adding to queue and sending reply");
            byte[] reply = PacketProcessor.createOkReply();
            sendData(reply, receivePacket.getPort());
        }

    }

    /**
     * The algorithm to determine which elevator should service a request
     * @param data the request data
     * @return the id of the elevator that should service the request
     */
    public int algorithm(byte[] data){

        boolean bothBusy = true;
        ElevatorInfo req = PacketProcessor.translateRequest(data);

        do {
            if (databox.getElevatorData().get("el1State") == 0 && databox.getElevatorData().get("el2State") == 0) {
                int el1Distance = Math.abs(databox.getElevatorData().get("el1Floor") - req.getFloorNumber());
                int el2Distance = Math.abs(databox.getElevatorData().get("el2Floor") - req.getFloorNumber());
                if (el1Distance < el2Distance) {
                    return 0;
                } else if (el1Distance > el2Distance) {
                    return 1;
                } else {
                    if (req.getDirection().equals("Up") && databox.getElevatorData().get("el1Floor") < req.getFloorNumber()) {
                        return 0;
                    } else if (req.getDirection().equals("Up") && databox.getElevatorData().get("el2Floor") < req.getFloorNumber()) {
                        return 1;
                    } else if (req.getDirection().equals("Down") && databox.getElevatorData().get("el1Floor") > req.getFloorNumber()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            } else if (databox.getElevatorData().get("el1State") != 0 && databox.getElevatorData().get("el2State") == 0) {
                return 1;
            } else if (databox.getElevatorData().get("el1State") == 0 && databox.getElevatorData().get("el2State") != 0) {
                return 0;
            } else {
                bothBusy = true;
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        } while(bothBusy);

        return -1;
    }

    /**
     * Sends the specified data to the specified port using the send/receive socket.
     * @param data the data to be sent
     * @param port the port to send the data to
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

     A private helper class that implements the Runnable interface to perform
     some processing in a separate thread.
     */
    private class BoxHelper implements Runnable {
        byte[] data;
        int chosenElevator;

        /**
         Constructs a new BoxHelper object with the specified data and chosen elevator.
         @param data the data to be processed
         @param chosenElevator the elevator to which the processed data will be sent
         */
        public BoxHelper(byte[] data, int chosenElevator) {
            this.data = data;
            this.chosenElevator = chosenElevator;
        }

        /**
         Performs the actual processing of the data in a separate thread.
         This method sends the specified data to the chosen elevator by putting it into
         a databox.
         */
        @Override
        public void run() {
            System.out.println("SCHEDULER HELPER SENDING PUT REQUEST TO ELEVATOR " + chosenElevator);
            databox.putRequestData(data, chosenElevator);
        }
    }

    /**
     * Getter for the state
     * @return The current state of the scheduler
     */
    public SchedulerState getState(){
        return state;
    }

    public static void main(String[] args) {
        Scheduler mainSched = new Scheduler(Config.SCHEDULER_PORT);

        while(true) {
            mainSched.handleRpcRequest();
        }
    }
}

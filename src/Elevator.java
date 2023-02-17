/**
 * Class meant to represent an elevator
 */
public class Elevator implements Runnable{



    private ElevatorState state;
    /** The scheduler for the elevator */
    private Scheduler scheduler;
    /** Represents request currently being processed */
    private ElevatorInfo currentRequest;
    /** The floor that the elevator started before moving */
    private int startingFloor;
    /** The loading time for the elevator (doors open and close) */
    private static final int LOAD_TIME = 1530;
    /** Testing array */
    private boolean testing[] = {false, false, false, false};

    /**
     * Creates the elevator object
     * @param scheduler Scheduler object responsible for the elevator
     */
    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
        this.startingFloor = 1;
        this.state = ElevatorState.IDLE;
    }

    /**
     * Getter for state of elevator
     * @return Current state of elevator
     */
    public ElevatorState getState(){
        return this.state;
    }

    /**
     * handles event
     * @param event The incoming event for the elevator
     */
    private void handleEvent(ElevatorEvent event){

        switch (event) {
            case CALL -> {
                this.state = ElevatorState.CHECK_FLOOR;
                if (!testing[0]) {
                    checkFloor();
                }
            }
            case DOORS_OPEN -> {
                if (this.state == ElevatorState.IDLE) {
                    this.state = ElevatorState.DOOR_OPENED;
                } else if (this.state == ElevatorState.MOVING) {
                    this.state = ElevatorState.DOOR_OPENED;
                }
                doorOpen();
            }
            case DOORS_CLOSE -> {
                this.state = ElevatorState.DOOR_CLOSED;
                if (!testing[3]) {
                    doorClosed();
                }
            }
            case PROCESS_REQUEST -> {
                this.state = ElevatorState.MOVING;
                if (!testing[1]) {
                    moveElevator();
                }
            }
            case FINISH_REQUEST -> {
                this.state = ElevatorState.IDLE;
                send(this.currentRequest);
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
        this.startingFloor = destination;
        handleEvent(ElevatorEvent.DOORS_OPEN);
    }

    /**
     * Receives information sent from the floor through the scheduler
     * ElevatorInfo containing floor information
     */
    private void receiveFromSched(){
        currentRequest = scheduler.getFloorMessages();
        logging.info( "Elevator", "Floor Receiving" + currentRequest);
        handleEvent(ElevatorEvent.CALL);
    }

    /**
     * Sends Information to the scheduler
     * @param info ElevatorInfo containing information about the elevator
     */
    private void send(ElevatorInfo info){
        logging.info( "Elevator", "Floor Sending" + info);
        scheduler.addElevatorMessage(info);
    }

    //Created for only testing purposes
    void testReceiveFromSched(){
        currentRequest = scheduler.getFloorMessages();
        System.out.println("Elevator Receiving " + currentRequest);
        handleEvent(ElevatorEvent.CALL);
    }

    //Created for only testing purposes
    void testSend(ElevatorInfo info){
        System.out.println("Elevator Sending " + info);
        scheduler.addElevatorMessage(info);
    }

    //Created for testing purposes
    void setTesting(boolean[] t){
        testing = t;
    }

    @Override
    public void run() {
        while(true) {
            this.receiveFromSched();
        }

    }


}

import java.util.logging.Logger;

/**
 * Class meant to represent an elevator
 */
public class Elevator implements Runnable{

    /**
     * Logger for elevator class
     */
    private static final Logger LOGGER = Logger.getLogger(Elevator.class.getName());

    private ElevatorState state;
    /** The scheduler for the elevator */
    private Scheduler scheduler;
    private ElevatorInfo currentRequest;
    private int startingFloor;
    private static final int LOAD_TIME = 1530;

    /**
     * Creates the elevator object
     * @param scheduler Scheduler object responsible for the elevator
     */
    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
        this.startingFloor = 1;
        this.state = ElevatorState.IDLE;
    }

    public ElevatorState getState(){
        return this.state;
    }

    private void handleEvent(ElevatorEvent event){

        switch (event) {
            case CALL -> {
                this.state = ElevatorState.CHECK_FLOOR;
                checkFloor();
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
                doorClosed();
                this.state = ElevatorState.DOOR_CLOSED;
            }
            case PROCESS_REQUEST -> {
                this.state = ElevatorState.MOVING;
                moveElevator();
            }
            case FINISH_REQUEST -> {
                this.state = ElevatorState.IDLE;
                send(this.currentRequest);
                receiveFromSched();
            }
        }

    }

    private void checkFloor(){
        if (this.startingFloor == currentRequest.getFloorNumber()){
            System.out.println("Elevator does not need to move to process request");
            handleEvent(ElevatorEvent.DOORS_OPEN);
        } else {
            System.out.println("Elevator needs to move to process request");
            handleEvent(ElevatorEvent.PROCESS_REQUEST);
        }
    }

    private void doorOpen(){
        System.out.println("Elevator doors are open! (open for " + LOAD_TIME + " milliseconds) on floor " + startingFloor);

        try {
            Thread.sleep(LOAD_TIME);
        } catch (Exception e) {
            System.out.print("Broke");
            e.printStackTrace();
        }

        handleEvent(ElevatorEvent.DOORS_CLOSE);
    }

    private void doorClosed(){
        System.out.println("Door closing on floor " + startingFloor);
        //If car is at the destination floor
        if (this.startingFloor == currentRequest.getCarButton()){
            handleEvent(ElevatorEvent.FINISH_REQUEST);
        } else {    // If not at destination floor, it must not have finished the request yet
            handleEvent(ElevatorEvent.PROCESS_REQUEST);
        }
    }

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
        System.out.println("Elevator is moving " + direction);
        this.startingFloor = destination;
        handleEvent(ElevatorEvent.DOORS_OPEN);
    }

    /**
     * Receives information sent from the floor through the scheduler
     * ElevatorInfo containing floor information
     */
    private void receiveFromSched(){
        currentRequest = scheduler.getFloorMessages();
        LOGGER.info("Floor Receiving" + currentRequest);
        handleEvent(ElevatorEvent.CALL);
    }

    /**
     * Sends Information to the scheduler
     * @param info ElevatorInfo containing information about the elevator
     */
    private void send(ElevatorInfo info){
        LOGGER.info("Floor Sending" + info);
        scheduler.addElevatorMessage(info);
    }

    //Created for only testing purposes
    void testReceiveFromSched(){
        ElevatorInfo info = scheduler.getFloorMessages();
        System.out.println("Elevator Receiving " + info);
        send(info);
    }

    //Created for only testing purposes
    void testSend(ElevatorInfo info){
        System.out.println("Elevator Sending " + info);
        scheduler.addElevatorMessage(info);
    }

    @Override
    public void run() {
        this.receiveFromSched();
        send(this.currentRequest);

    }


}

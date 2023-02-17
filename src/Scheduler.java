import java.util.ArrayList;

public class Scheduler implements Runnable{

    /** The ElevatorInfo messages sent by the floor */
    private ArrayList<ElevatorInfo> floorMessages;
    /** The ElevatorInfo messages sent by the elevator */
    private ArrayList<ElevatorInfo> elevatorMessages;
    private SchedulerState state;

    enum SchedulerState {
        IDLE,
        RECEIVING
    }

    private enum SchedulerEvent {
        REQUEST,
        RECEIVING_RESPONSE
    }

    /**
     * Creates a scheduler object
     * @param floorMessages An arraylist to store messages sent by the floor
     * @param elevatorMessages An arraylist to store messages sent by the elevator
     */
    public Scheduler(ArrayList<ElevatorInfo> floorMessages, ArrayList<ElevatorInfo> elevatorMessages) {
        this.floorMessages = floorMessages;
        this.elevatorMessages = elevatorMessages;
        this.state = SchedulerState.IDLE;
    }

    private void handleEvent(SchedulerEvent event) {

        switch (event) {
            case REQUEST -> {
                System.out.println("Scheduler receiving request!");
                this.state = SchedulerState.RECEIVING;
            }
            case RECEIVING_RESPONSE -> {
                System.out.println("Scheduler received response");
                this.state = SchedulerState.IDLE;
            }
        }
    }

    /**
     * Returns a message sent by the floor (FIFO)
     * @return ElevatorInfo sent from the floor
     */
    public synchronized ElevatorInfo getFloorMessages() {
        while(floorMessages.size() <= 0){
            try {
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        ElevatorInfo temp = floorMessages.remove(0);
        notifyAll();
        return temp;
    }

    /**
     * Adds a floor message
     * @param floorMessage ElevatorInfo message sent by the floor
     */
    public synchronized void addFloorMessage(ElevatorInfo floorMessage) {
        handleEvent(SchedulerEvent.REQUEST);
        this.floorMessages.add(floorMessage);
        notifyAll();
    }

    /**
     * Get messages sent by the elevator (FIFO)
     * @return ElevatorInfo message sent by the elevator
     */
    public synchronized ElevatorInfo getElevatorMessages() {
        while(elevatorMessages.size() <= 0){
            try {
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        ElevatorInfo temp = elevatorMessages.remove(0);
        notifyAll();
        return temp;
    }

    /**
     * Adds a message sent by the elevator
     * @param elevatorMessage ElevatorInfo message sent by the elevator
     */
    public synchronized void addElevatorMessage(ElevatorInfo elevatorMessage) {
        handleEvent(SchedulerEvent.RECEIVING_RESPONSE);
        this.elevatorMessages.add(elevatorMessage);
        notifyAll();
    }

    public SchedulerState getState(){
        return state;
    }

    @Override
    public void run() {
        // TODO should continuously check then send requests to elevator(s)
        //No functionality for the scheduler thread as of this iteration

    }
}

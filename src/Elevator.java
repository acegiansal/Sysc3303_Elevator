/**
 * Class meant to represent an elevator
 */
public class Elevator implements Runnable{

    /** The scheduler for the elevator */
    private Scheduler scheduler;

    /**
     * Creates the elevator object
     * @param scheduler Scheduler object responsible for the elevator
     */
    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Receives information sent from the floor through the scheduler
     * @return ElevatorInfo containing floor information
     */
    private ElevatorInfo receiveFromSched(){
        ElevatorInfo info = scheduler.getFloorMessages();
        System.out.println("Elevator Receiving " + info);
        return info;
    }

    /**
     * Sends Information to the scheduler
     * @param info ElevatorInfo containing information about the elevator
     */
    private void send(ElevatorInfo info){
        System.out.println("Elevator Sending " + info);
        scheduler.addElevatorMessage(info);
    }

    @Override
    public void run() {
        //Continuously run until manual input terminates the code (due to the real time expectations of an elevator)
        while (true) {
            send(this.receiveFromSched());

            //Pause to show message
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

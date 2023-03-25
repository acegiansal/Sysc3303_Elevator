package ElevatorComp.NewElevatorStates;

import ElevatorComp.ElevatorCar;

/**
 * Class that represents a timer object
 * @author Giancarlo Salvador #101139903
 *
 */
public class Timer implements Runnable {

    /** The state machine that the timer runs for */
    private ElevatorCar context;
    /** The time to pause */
    private int time;

    /**
     * Creates the timer with a specific state machine
     * @param context The state machine to time for
     */
    public Timer(ElevatorCar context) {
        this.context = context;
    }

    /**
     * Sets the time
     * @param time Time in ms to pause for
     */
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void run() {
        if (Thread.currentThread().isInterrupted()) {
            return;
        } else {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Creates a timeout event for the state machine
            context.timeout();
        }
    }

}

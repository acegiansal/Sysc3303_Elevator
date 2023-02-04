import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger ;
import java.util.logging.SimpleFormatter;

/**
 * Class meant to represent info about an elevator system
 */
public class ElevatorInfo {

    /**
     * Logger for elevator class
     */
    private static final Logger LOGGER = Logger.getLogger(ElevatorInfo.class.getName());

    /** The direction of the elevator */
    private boolean direction;
    /** The original floor number of the elevator */
    private int floorNumber;
    /** The time that the input was received*/
    private String time;
    /** The destination floor */
    private int carButton;

    /**
     * Creates an ElevatorInfo object
     * @param direction The direction of the elevator
     * @param floorNumber The original floor number of the elevator
     * @param time The time that the input was received
     * @param carButton The destination floor
     */
    public ElevatorInfo(boolean direction, int floorNumber, String time, int carButton) {


        LOGGER.info("Creating an ElevatorInfo object with parameters: direction=" + direction+ ", floorNumber="+ floorNumber + ", time=" +time + ", carButton=" + carButton);
        this.direction = direction;
        this.floorNumber = floorNumber;
        this.time = time;
        this.carButton = carButton;
    }

    @Override
    public String toString() {
        return "ElevatorInfo{" +
                "direction=" + direction +
                ", floorNumber=" + floorNumber +
                ", time='" + time + '\'' +
                ", carButton=" + carButton +
                '}';
    }

    /**
     * Getter for direction
     * @return True if going up, false otherwise
     */
    public boolean getDirection() {
        return direction;
    }

    /**
     * Setter for direction
     * @param direction The direction of the elevator
     */
    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    /**
     * Getter for the floorNumber
     * @return The original floor of the elevator
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Setter for floorNumber
     * @param floorNumber The original floor number of the elevator
     */
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    /**
     * Getter for the time
     * @return The time that the input was received
     */
    public String getTime() {
        return time;
    }

    /**
     * Setter for the time
     * @param time The time that the input was received
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Getter for the carButton
     * @return The destination floor number
     */
    public int getCarButton() {
        return carButton;
    }

    /**
     * Setter for the carButton
     * @param carButton The destination floor number
     */
    public void setCarButton(int carButton) {
        this.carButton = carButton;
    }

}

/**
 * Class meant to represent info about an elevator system
 */
public class ElevatorInfo {


    /** The direction of the elevator */
    private String direction;
    /** The original floor number of the elevator */
    private int floorNumber;
    /** The time that the input was received*/
    private String time;
    /** The destination floor */
    private int carButton;
    /** current state of the elevator */
    private ElevatorState currentState;
    /** elevator id used to identify the elevator */
    private int elevatorID;

    /**
     * Creates an ElevatorInfo object
     * @param direction The direction of the elevator
     * @param floorNumber The original floor number of the elevator
     * @param time The time that the input was received
     * @param carButton The destination floor
     */
    public ElevatorInfo(String direction, int floorNumber, String time, int carButton, int elevatorID) {


        logging.info( "ElevatorInfo", "Creating an ElevatorInfo object with parameters: direction=" + direction + ", floorNumber="+ floorNumber + ", time=" +time + ", carButton=" + carButton);
        this.direction = direction;
        this.floorNumber = floorNumber;
        this.time = time;
        this.carButton = carButton;
        this.currentState = ElevatorState.IDLE;
        this.elevatorID = elevatorID;
    }
    /**
     Constructs a new ElevatorInfo object with the specified direction, floor number,
     time, and car button.
     @param direction the direction of the elevator (up/down)
     @param floorNumber the current floor number of the elevator
     @param time the current time of the elevator
     @param carButton the current status of the car button of the elevator
     */
    public ElevatorInfo(String direction,  int floorNumber, String time, int carButton){
        this.direction = direction;
        this.floorNumber = floorNumber;
        this.time = time;
        this.carButton = carButton;
    }

    /**
     Returns the ID of the elevator.
     @return the ID of the elevator
     */
    public int getElevatorID() {
        return elevatorID;
    }

    /**
     Returns a string representation of the elevator information.
     @return a string representation of the elevator information
     */
    @Override
    public String toString() {
        return "ElevatorInfo{" +
                "Direction is " + direction +
                ", Floor Number is " + floorNumber +
                ", Time ='" + time + '\'' +
                ", Car Button = " + carButton +
                '}';
    }

    /**
     * Getter for state
     * @return state
     */
    public ElevatorState getState() {
        return currentState;
    }

    /**
     *
     * @param state
     */
    public void setState(ElevatorState state) {
        this.currentState = state;
    }

    /**
     * Getter for direction
     * @return True if going up, false otherwise
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Setter for direction
     * @param direction The direction of the elevator
     */
    public void setDirection(String direction) {
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

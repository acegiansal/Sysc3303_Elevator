import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Class that represents a floor in an elevator system
 */
public class Floor implements Runnable{

    /**
     * Logger for Floor class
     */
    private static final Logger LOGGER = Logger.getLogger(Floor.class.getName());

    /** The scheduler responsible for the floor */
    private Scheduler scheduler;
    private String testString;

    /**
     * Creates a floor object
     * @param scheduler The scheduler object responsible for the floor
     */
    public Floor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Read elevator input from a file
     * @param file The file containing the elevator instructions
     */
    private void readFromFile(File file){
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //Split information (divided by spaces)
                String[] splitData = data.split(" ");
                //Data must be 4 items long
                if (splitData.length != 4){
                    LOGGER.warning("INPUT DATA INVALID!!");
                    System.out.println("INPUT DATA INVALID!!");
                    break;
                }else {
                    //Direction is true if 'Up' is selected
                    boolean direction = splitData[2].equals("Up");
                    //Send the data to the scheduler
                    send(splitData[0], Integer.parseInt(splitData[1]), direction, Integer.parseInt(splitData[3]));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            LOGGER.warning("An error occurred while reading the input file" + e.getMessage());
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Receives information from the elevator through the scheduler
     * @return ElevatorInfo with information about the elevator
     */
    private ElevatorInfo receiveFromSched(){
       ElevatorInfo info = scheduler.getElevatorMessages();
       LOGGER.info("Floor Receiving" + info);
       //System.out.println("Floor Receiving " + info);
       return info;
    }

    /**
     * Sends the data to scheduler
     * @param time The time that the input was retrieved
     * @param floorNumber The original floor number
     * @param direction The direction that the elevator should be going to
     * @param carButton The target floor
     */
    private void send(String time, int floorNumber, boolean direction, int carButton){
        ElevatorInfo info = new ElevatorInfo(direction, floorNumber, time, carButton);
        LOGGER.info("Floor Sending" + info);
        //System.out.println("Floor Sending " + info);
        scheduler.addFloorMessage(info);
    }

    /**
     * Sends the data using a preexisting ElevatorInfo object
     * @param info Existing ElevatorInfo object to send
     */
    private void send(ElevatorInfo info){
        LOGGER.info("Floor Sending" + info);
        //System.out.println("Floor Sending " + info);
        scheduler.addFloorMessage(info);
    }

    //Created for only testing purposes
    void testReceiveFromSched(){
        ElevatorInfo info = scheduler.getElevatorMessages();
        System.out.println("Floor Receiving " + info);
        testString = info.toString();
    }

    //Created for only testing purposes
    void testSend(String time, int floorNumber, boolean direction, int carButton){
        ElevatorInfo info = new ElevatorInfo(direction, floorNumber, time, carButton);
        System.out.println("Floor Sending " + info);
        scheduler.addFloorMessage(info);
    }

    //Created for only testing purposes
    String getTestString(){
        return testString;
    }

    @Override
    public void run() {
        //Read information from selected file
        File file = new File("src/elevatorFile");
        readFromFile(file);

        this.receiveFromSched();

    }
}

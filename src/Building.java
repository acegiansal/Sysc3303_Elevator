import java.util.ArrayList;


/**
 * Building class that creates the Elevator system
 */
public class Building {



    /**
     * Creates and runs elevator system
     * @param args Arguments passed upon program launch
     */
    public static void main(String[] args){

        ArrayList<ElevatorInfo> floorMessages;
        ArrayList<ElevatorInfo> elevatorMessages;

        floorMessages = new ArrayList<>();
        elevatorMessages = new ArrayList<>();

//        Scheduler schedy = new Scheduler(floorMessages, elevatorMessages);
//        Floor floor1 = new Floor(schedy);
//        Elevator elevator1 = new Elevator(schedy);

        Elevator el1 = new Elevator(Config.ELEVATOR_HOST_1, 0);
        Elevator el2 = new Elevator(Config.ELEVATOR_HOST_2, 1);

//        Thread floor = new Thread(floor1);
        Thread elevator1 = new Thread(el1, "el1");
        Thread elevator2 = new Thread(el2, "el2");
//        Thread scheduler = new Thread(schedy);

        //Start threads
//        floor.start();
        elevator1.start();
        elevator2.start();
//        scheduler.start();
    }
}
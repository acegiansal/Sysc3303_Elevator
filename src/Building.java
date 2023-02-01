import java.util.ArrayList;

public class Building {


    public static void main(String[] args){
        ArrayList<ElevatorInfo> floorMessages;
        ArrayList<ElevatorInfo> elevatorMessages;

        floorMessages = new ArrayList<>();
        elevatorMessages = new ArrayList<>();

        Scheduler schedy = new Scheduler(floorMessages, elevatorMessages);
        Floor floor1 = new Floor(schedy);
        Elevator elevator1 = new Elevator(schedy);

        Thread floor = new Thread(floor1);
        Thread elevator = new Thread(elevator1);
        Thread scheduler = new Thread(schedy);

        floor.start();
        elevator.start();
        scheduler.start();
    }
}

import java.util.ArrayList;
import java.util.Queue;

public class Scheduler implements Runnable{

    private ArrayList<ElevatorInfo> floorMessages;
    private ArrayList<ElevatorInfo> elevatorMessages;

    public Scheduler(ArrayList<ElevatorInfo> floorMessages, ArrayList<ElevatorInfo> elevatorMessages) {
        this.floorMessages = floorMessages;
        this.elevatorMessages = elevatorMessages;
    }

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

    public synchronized void addFloorMessage(ElevatorInfo floorMessage) {
        this.floorMessages.add(floorMessage);
        notifyAll();
    }

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

    public synchronized void addElevatorMessage(ElevatorInfo elevatorMessage) {
        this.elevatorMessages.add(elevatorMessage);
        notifyAll();
    }



    @Override
    public void run() {

    }
}

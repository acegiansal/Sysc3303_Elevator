import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class Floor implements Runnable{

    private Scheduler scheduler;

    public Floor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private void readFromFile(File file){
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitData = data.split(" ");
                if (splitData.length != 4){
                    System.out.println("INPUT DATA INVALID!!");
                    break;
                }else {
                    boolean direction = splitData[2].equals("Up");
                    send(splitData[0], Integer.parseInt(splitData[1]), direction, Integer.parseInt(splitData[3]));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private ElevatorInfo receiveFromSched(){
       ElevatorInfo info = scheduler.getElevatorMessages();
       System.out.println("Floor Receiving " + info);
       return info;
    }

    private void send(String time, int floorNumber, boolean direction, int carButton){
        ElevatorInfo info = new ElevatorInfo(direction, floorNumber, time, carButton);
        System.out.println("Floor Sending " + info);
        scheduler.addFloorMessage(info);
    }

    private void send(ElevatorInfo info){
        System.out.println("Floor Sending " + info);
        scheduler.addFloorMessage(info);
    }

    @Override
    public void run() {
        File file = new File("src/elevatorFile");
        readFromFile(file);
        while (true){
            this.send(this.receiveFromSched());
        }
    }
}

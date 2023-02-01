import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class Floor implements Runnable{

    private Scheduler scheduler;
    URL url = getClass().getResource("elevatorFile");
    File file = new File(url.getPath());

    public Floor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private void readFromFile(String file){
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitData = data.split(" ");
                if (splitData.length != 4){
                    System.out.println("INPUT DATA INVALID!!");
                    break;
                }else {
                    boolean direction;
                    if (splitData[2].equals("Down")){
                        direction = false;
                    }else {
                        direction = true;
                    }
                    send(splitData[0], Integer.parseInt(splitData[1]), direction, Integer.parseInt(splitData[3]));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void recieveFromSched(){
       ElevatorInfo info = scheduler.getElevatorMessages();
       System.out.println("Floor Receiving " + info);
    }

    private void send(String time, int floorNumber, boolean direction, int carButton){
        ElevatorInfo info = new ElevatorInfo(direction, floorNumber, time, carButton);
        System.out.println("Floor Sending " + info);
        scheduler.addFloorMessage(info);
    }

    @Override
    public void run() {
        readFromFile(file.toString());
        while (true){
            this.recieveFromSched();
        }
    }
}

package ElevatorComp.ElevatorStates;

import Config.ConfigInfo;
import ElevatorComp.ElevatorCar;

import java.util.ArrayList;

public class CloseDoors extends ElevatorState {

    public CloseDoors(ElevatorCar elevator){
        super(elevator);
    }

    @Override
    public void performAction() {
        try {
            System.out.println(Thread.currentThread().getName() + " Door is closing");
            Thread.sleep(ConfigInfo.DOOR_OPEN_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus() {

        // Remove the floor
//        ArrayList<Integer> workQueue = elevator.getFloorQueue();
        elevator.getFloorQueue().remove(0);

        //Notify the floor
        byte[] toFloor = new byte[ConfigInfo.PACKET_SIZE];
        toFloor[0] = (byte)elevator.getElevatorID();
        toFloor[1] = (byte)elevator.getCurrentFloor();
        //send data

        elevator.changeState(new CheckFinished(elevator));

    }
}

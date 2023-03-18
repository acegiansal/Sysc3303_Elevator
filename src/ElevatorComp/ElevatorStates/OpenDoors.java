package ElevatorComp.ElevatorStates;

import Config.ConfigInfo;
import ElevatorComp.ElevatorCar;

public class OpenDoors extends ElevatorState {

    public OpenDoors(ElevatorCar elevator){
        super(elevator);
    }

    @Override
    public void performAction() {
        try {
            System.out.println(Thread.currentThread().getName() + " Door is opening for " + ConfigInfo.DOOR_OPEN_TIME + " on floor " + elevator.getCurrentFloor());
            Thread.sleep(ConfigInfo.DOOR_OPEN_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus() {
        //Notify the floor
        byte[] toFloor = new byte[ConfigInfo.PACKET_SIZE];
        toFloor[0] = (byte)elevator.getElevatorID();
        toFloor[1] = (byte)elevator.getCurrentFloor();
        // Send data

        elevator.changeState(new CloseDoors(elevator));
    }
}

public class ElevatorLamp {

    private int ElevatorButton;

    public void ElevatorButton(int floorNumber) {
        logging.info("ElevatorLamp", "" + Thread.currentThread().getName(),"Elevator Lamp for floor " + floorNumber + " is On");
    }

}

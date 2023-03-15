package DataComp;

public class ElevatorStatus {

    private int currentFloor;
    private String direction;


    public ElevatorStatus(int currentFloor, String direction) {
        this.currentFloor = currentFloor;
        this.direction = direction;
    }

    public ElevatorStatus(){
        currentFloor = 1;
        direction = "i";
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public String getDirection() {
        return direction;
    }
}

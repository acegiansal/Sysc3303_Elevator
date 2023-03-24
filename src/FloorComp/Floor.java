package FloorComp;

public class Floor {

    private int floorNum;
    private FloorButton floorButton;
    private DirectionLamp upLamp;
    private DirectionLamp downLamp;
    FloorSend.floorType floorType;
    private int[] elevatorCars;

    public Floor(int floorNum, FloorSend.floorType type, int numElevators){

        this.floorNum = floorNum;
        this.floorType = type;
        this.floorButton = new FloorButton("Up");
        this.upLamp = new DirectionLamp();
        this.downLamp = new DirectionLamp();
        elevatorCars = new int[numElevators];

    }

    public boolean send(String direction){
        if (this.floorType == FloorSend.floorType.BOT && direction.equals("Down")){
            return false;
        } else if (this.floorType == FloorSend.floorType.TOP && direction.equals("Up")){
            return false;
        } else {
            this.floorButton.pressed(direction);
            return true;
        }
    }

    public void arrive(String direction, int elevatorID){

        if (direction.equals("Up")){
            upLamp.toggleLamp();
        } else if (direction.equals("Down")) {
            downLamp.toggleLamp();
        } else {
            upLamp.toggleLamp();
            downLamp.toggleLamp();
        }
        elevatorCars[elevatorID] ^= 1;
        if(elevatorCars[elevatorID] == 0){
            System.out.println("Elevator " + elevatorID + " has LEFT the floor " + floorNum);
        } else {
            System.out.println("Elevator " + elevatorID + " has ENTERED the floor " + floorNum);
        }

        this.floorButton.resetLamp();
        // LOGGER PRINT THE ELEVATOR ARRIVING AT THIS FLOOR
    }
}

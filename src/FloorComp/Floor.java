package FloorComp;

import ElevatorComp.ElevatorCar;

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

    public void send(String direction){
        this.floorButton.pressed(direction);
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

    }
}

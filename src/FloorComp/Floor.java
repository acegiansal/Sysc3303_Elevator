package FloorComp;

import ElevatorComp.ElevatorCar;

public class Floor {

    int floorNum;
    FloorButton floorButton;
    DirectionLamp upLamp;
    DirectionLamp downLamp;
    FloorSend.floorType floorType;



    public Floor(int floorNum, FloorSend.floorType type){

        this.floorNum = floorNum;
        this.floorType = type;
        this.floorButton = new FloorButton("Up");
        this.upLamp = new DirectionLamp();
        this.downLamp = new DirectionLamp();
    }

    public void send(String direction){
        this.floorButton.pressed(direction);
//        this.directionLamp.toggleLamp(true);
    }

    public void arrive(String direction, int elevatorID){
        if (direction.equals("Up")){
            upLamp.toggleLamp(true);
        } else if (direction.equals("Down")) {
            downLamp.toggleLamp(true);
        } else {
            upLamp.toggleLamp(false);
            downLamp.toggleLamp(false);
        }
        // LOGGER PRINT THE ELEVATOR ARRIVING AT THIS FLOOR
    }
}

package FloorComp;

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

    public boolean send(String direction){
        if (this.floorType == FloorSend.floorType.BOT && direction.equals("Down")){
            return false;
        } else if (this.floorType == FloorSend.floorType.TOP && direction.equals("Up")){
            return false;
        } else {
            this.floorButton.pressed(direction);
            return true;
        }
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
        this.floorButton.resetLamp();
        // LOGGER PRINT THE ELEVATOR ARRIVING AT THIS FLOOR
    }
}

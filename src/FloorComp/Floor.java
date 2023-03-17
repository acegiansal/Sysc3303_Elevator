package FloorComp;

public class Floor {

    int floorNum;
    FloorButton floorButton;
    DirectionLamp directionLamp;
    FloorSend.floorType floorType;



    public Floor(int floorNum, FloorSend.floorType type){

        this.floorNum = floorNum;
        this.floorType = type;
        this.floorButton = new FloorButton("Up");
        this.directionLamp = new DirectionLamp();
    }

    public void send(String direction){
        this.floorButton.pressed(direction);
        this.directionLamp.toggleLamp(true);
    }



}

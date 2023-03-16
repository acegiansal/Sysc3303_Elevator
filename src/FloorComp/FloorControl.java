package FloorComp;

import java.util.ArrayList;

public class FloorControl {

    ArrayList<Floor> floors;
    private static int MAX_FLOORS;
    public enum floorType {
        TOP,
        BOT,
        MID
    }

    public FloorControl(int numFloors){
        MAX_FLOORS = numFloors;
        for(int i = 0; i<numFloors; i++){
            floorType type;

            if(i == 0){
                type = floorType.BOT;
            } else if(i == MAX_FLOORS){
                type = floorType.TOP;
            } else {
                type = floorType.MID;
            }
            floors.add(i, new Floor(i, type));
        }
    }

    public void readInputFile(String filename){

    }

    public static void main(String[] args){
        //Read files

    }
}

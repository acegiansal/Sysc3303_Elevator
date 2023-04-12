package DataComp;

import Config.ConfigInfo;
import ControlComp.Logging;

import java.util.ArrayList;


public class ElevatorStatus {

    private int currentFloor;
    private String direction;
    private int doorStatus;
    private int id;
    private ArrayList<Integer> floors;

    /* Constants */
    public static final String UP = "u";
    public static final String DOWN = "d";
    public static final String IDLE = "i";
    public static final String STUCK = "s";
    public static final int OPENING = 1;
    public static final int CLOSING = 2;
    public static final int CLOSED = 0;
    public static final int DOOR_STUCK = 3;

    public ElevatorStatus(int currentFloor, String direction, int doorStatus, int id) {
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.id = id;
        this.doorStatus = doorStatus;
        floors = new ArrayList<>();
    }

    public ElevatorStatus(int currentFloor, String direction, int doorStatus, int id, ArrayList<Integer> floors) {
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.id = id;
        this.doorStatus = doorStatus;
        this.floors = new ArrayList<>(floors);
    }

    public ElevatorStatus(int id){
        currentFloor = 1;
        direction = IDLE;
        this.id = id;
        floors = new ArrayList<>();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public String getDirection() {
        return direction;
    }

    public int getId() {
        return id;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ArrayList<Integer> getFloors() {
        return floors;
    }

    public int getDoorStatus() {
        return doorStatus;
    }

    public static byte[] translateToBytes(ElevatorStatus status, byte statusUpdated){
        byte[] translated = new byte[ConfigInfo.PACKET_SIZE];
        translated[0] = (byte)status.getId();
        translated[1] = (byte)status.getCurrentFloor();
        translated = RequestPacket.combineByteArr(2, translated, status.getDirection().getBytes());

        translated[3] = statusUpdated;

        translated[4] = (byte)status.getDoorStatus();

        ArrayList<Integer> floorCopy = status.getFloors();

        for(int i =0; i<floorCopy.size(); i++){
            translated[i + 5] = floorCopy.get(i).byteValue();
        }

        translated[floorCopy.size() + 6] = 0; // End of floor sections

        return translated;
    }



    public static ElevatorStatus translateStatusBytes(byte[] data){
        int currentFloor = data[1];
        String direction = new String(data, 2, 1);
        int doorStatus = data[4];
        ArrayList<Integer> floors = new ArrayList<>();
        for(int i=5; i<data.length; i++){
            if(data[i] == 0){
                break;
            } else {
                floors.add((int) data[i]);
            }
        }

        return new ElevatorStatus(currentFloor, direction, doorStatus, data[0], floors);
    }

    public static boolean isValidStatus(byte[] data){
        if(data[1] == 0){
            return false;
        }

        String direction = new String(data, 2, 1);
        return direction.equals(UP) || direction.equals(DOWN) || direction.equals(IDLE) || direction.equals(STUCK);
    }

    @Override
    public String toString() {
        return "ElevatorStatus{" +
            "id=" + id +
            ", currentFloor=" + currentFloor +
            ", direction='" + direction + '\'' +
            ", doorStatus=" + doorStatus +
            ", floors=" + floors +
            '}';
    }

    public static void main(String[] args){
        byte[] data = {0, 5, 117, 0, 1, 3, 5, 0};
        System.out.println(ElevatorStatus.translateStatusBytes(data));
    }
}
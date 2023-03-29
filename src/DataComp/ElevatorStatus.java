package DataComp;

import Config.ConfigInfo;
import ControlComp.Logging;


public class ElevatorStatus {

    private int currentFloor;
    private String direction;
    private int id;

    /* Constants */
    public static final String UP = "u";
    public static final String DOWN = "d";
    public static final String IDLE = "i";
    public static final String STUCK = "s";

    public ElevatorStatus(int currentFloor, String direction, int id) {
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.id = id;
    }

    public ElevatorStatus(int id){
        currentFloor = 1;
        direction = IDLE;
        this.id = id;
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

    public static byte[] translateToBytes(ElevatorStatus status){
        byte[] translated = new byte[ConfigInfo.PACKET_SIZE];
        translated[0] = (byte)status.getId();
        translated[1] = (byte)status.getCurrentFloor();
        translated = RequestPacket.combineByteArr(2, translated, status.getDirection().getBytes());

        return translated;
    }

    public static ElevatorStatus translateStatusBytes(byte[] data){
        int currentFloor = data[1];
        String direction = new String(data, 2, 1);
        return new ElevatorStatus(currentFloor, direction, data[0]);
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
                "currentFloor=" + currentFloor +
                ", direction='" + direction + '\'' +
                ", id=" + id +
                '}';
    }
}
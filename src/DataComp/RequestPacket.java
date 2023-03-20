package DataComp;

import Config.ConfigInfo;

public class RequestPacket {

    private int startFloor;
    private int endFloor;
    private String direction;
    private int scenario;
    private String time;


    public RequestPacket(int startFloor, int endFloor, String direction, int scenario, String time) {
        this.startFloor = startFloor;
        this.endFloor = endFloor;
        this.direction = direction;
        this.scenario = scenario;
        this.time = time;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    public String getDirection() {
        return direction;
    }

    public int getScenario() {
        return scenario;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static boolean isEmptyRequest(byte[] data){
        for (int i=0; i<data.length; i++){
            if(data[i] != 0){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param request
     * @return
     */
    public static byte[] translateToBytes(RequestPacket request){
        byte[] translated = new byte[ConfigInfo.PACKET_SIZE];
        translated[0] = (byte)request.getStartFloor();
        translated[1] = (byte)request.getEndFloor();
        translated[2] = (byte) request.getScenario();
        translated = RequestPacket.combineByteArr(3, translated, request.getDirection().getBytes());
        translated = combineByteArr(4, translated, request.getTime().getBytes());

        return translated;
    }

    public static RequestPacket translateRequestBytes(byte[] data){
        int startFloor = data[0];
        int endFloor = data[1];
        int scenario = data[2];
        String direction = new String(data, 3, 1);
        int endOfTime = 0;
        for(int i=4; i<data.length; i++){
            if(data[i] == 0){
                break;
            }
            endOfTime++;
        }
        String time = new String(data, 4, endOfTime);
        return new RequestPacket(startFloor, endFloor, direction, scenario, time);
    }

    /**
     * Function to combine 2 byte arrays
     * @param start the start index
     * @param subject the byte array to add to
     * @param toAdd the byte array to be added
     * @return the combined byte array
     */
    public static byte[] combineByteArr(int start, byte[] subject, byte[] toAdd) {
        byte[] combinedByteArr = new byte[subject.length];

        // Re-make array
        for (int i = 0; i < subject.length; i++) {
            if (i < start) {
                combinedByteArr[i] = subject[i];
            } else if (i < (start + toAdd.length)) {
                combinedByteArr[i] = toAdd[i - start];
            }
        }

        return combinedByteArr;
    }

    @Override
    public String toString() {
        return "RequestPacket{" +
                "startFloor=" + startFloor +
                ", endFloor=" + endFloor +
                ", direction='" + direction + '\'' +
                ", scenario=" + scenario +
                ", time='" + time + '\'' +
                '}';
    }
}

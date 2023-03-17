package DataComp;

public class RequestPacket {

    private int startFloor;
    private int endFloor;
    private String direction;
    private int scenario;


    public RequestPacket(int startFloor, int endFloor, String direction, int scenario) {
        this.startFloor = startFloor;
        this.endFloor = endFloor;
        this.direction = direction;
        this.scenario = scenario;
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

    public static boolean isEmptyRequest(byte[] data){
        for (int i=0; i<data.length; i++){
            if(data[i] != 0){
                return false;
            }
        }
        return true;
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

}

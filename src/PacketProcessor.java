import java.nio.charset.StandardCharsets;

/**
 * A Packet Processor class
 * Contains logic to process the various packets used
 */
public class PacketProcessor {

    public static final byte GET_BYTE = 1;
    public static final byte REPLY = 2;

    /**
     * Creates a request packet
     * @param time the time of the request
     * @param floorNumber the origin floor number
     * @param direction the direction to move
     * @param carButton the destination floor number
     * @return the formatted request in byte array form
     */
    public static byte[] createRequestPacket(String time, int floorNumber, String direction, int carButton){
        byte[] request = new byte[50];

        request[0] = 0; //Put request
        request[1] = (byte)floorNumber;
        request[2] = 0;
        request = combineByteArr(3, request, direction.getBytes());
        request[direction.getBytes().length + 3] = 0;
        request[direction.getBytes().length + 4] = (byte) carButton;
        request[direction.getBytes().length + 5] = 0;
        request = combineByteArr(direction.getBytes().length + 6, request, time.getBytes(StandardCharsets.UTF_8));

        return request;
    }

    /**
     * Translates a request from byte array form to ElevatorInfo form
     * @param data the request in byte array form
     * @return an Elevator info object containing the request info
     */
    public static ElevatorInfo translateRequest(byte[] data){

        int floorNumber = data[1];
        String direction = new String(data, 3, 4);
        int carButton = data[5];
        int lengthOfTime = 7;
        for(int i=7; i<data.length; i++){
            if(data[i] == 0){
                break;
            }
            lengthOfTime++;
        }
        String time = new String(data, 7, lengthOfTime);

        return new ElevatorInfo(direction, floorNumber, time, carButton);
    }

    /**
     * Creates a get byte
     * @return a get byte
     */
    public static byte[] createGetRequest(){
        return new byte[]{GET_BYTE};
    }

    /**
     * Returns true if the data is a get request
     * @param data the data in byte form
     * @return true if the data is a get request
     */
    public static boolean isGetRequest(byte[] data){
        return data[0] == GET_BYTE;
    }

    /**
     * Creates an OK reply
     * @return a byte array containing an OK reply
     */
    public static byte[] createOkReply(){
        return new byte[]{REPLY};
    }

    /**
     * Returns true if the data is an OK reply
     * @param data the data in byte form
     * @return true if the data is an OK reply
     */
    public static boolean isOkReply(byte[] data){
        return data[0] == REPLY;
    }

    /**
     * Function to combine 2 byte arrays
     * @param start the start index
     * @param subject the byte array to add to
     * @param toAdd the byte array to be added
     * @return the combined byte array
     */
    private static byte[] combineByteArr(int start, byte[] subject, byte[] toAdd) {
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

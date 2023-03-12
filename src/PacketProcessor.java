import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * A Packet Processor class
 * Contains logic to process the various packets used
 */
public class PacketProcessor {

    public static final byte GET_BYTE = 1;
    public static final byte REPLY = 2;
    public static final byte DELIMITER = 0;

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
        request = combineByteArr(2, request, direction.getBytes());
        request[direction.getBytes().length + 2] = (byte) carButton;
        request = combineByteArr(direction.getBytes().length + 3, request, time.getBytes());
        request[direction.getBytes().length + time.getBytes().length + 4] = DELIMITER;

        return request;
    }

    /**
     * Translates a request from byte array form to ElevatorInfo form
     * @param data the request in byte array form
     * @return an Elevator info object containing the request info
     */
    public static ElevatorInfo translateRequest(byte[] data){

        int floorNumber = data[1];
        String direction = new String(data, 2, 1);
        int carButton = data[3];
        int lengthOfTime = 0;
        for(int i=4; i<data.length; i++){
            if(data[i] == 0){
                break;
            }
            lengthOfTime++;
        }
        String time = new String(data, 4, lengthOfTime);

        return new ElevatorInfo(direction, floorNumber, time, carButton);
    }

    /**
     * Adds the elevator status information to the given byte array data.
     * @param info the ElevatorInfo object containing the elevator state information
     * @param data the byte array to add the elevator status information to
     * @return the byte array with the elevator status information added
     */
    public static byte[] addElevatorStatus(ElevatorInfo info, byte[] data){
        int dIndex = findDelimiter(data, 1);
        if(dIndex == -1){
            logging.info2("PacketProcessor", "CAN'T FIND THE DELIMETER IN DATA: " + Arrays.toString(data));
            //System.out.println("CAN'T FIND THE DELIMETER IN DATA: " + Arrays.toString(data));
            System.exit(1);
        }

        byte[] stateBytes = info.getState().toString().getBytes();

        byte[] destDelim = {0, (byte)info.getCarButton(), 0};

        byte[] withStatus =  combineByteArr(dIndex+1, data, stateBytes);

        return combineByteArr(dIndex + stateBytes.length + 1, withStatus, destDelim);

    }

    /**
     * Finds the delimiter in the given byte array data starting from the specified index start.
     * @param data the byte array to search for the delimiter
     * @param start the index to start searching from in the byte array
     * @return the index of the delimiter in the byte array, or -1 if the delimiter is not found
     */
    public static int findDelimiter(byte[] data, int start){
        int dIndex = -1;
        for (int i=start; i<50; i++){
            if(data[i] == DELIMITER){
                dIndex = i;
                break;
            }
        }
        return dIndex;
    }

    /**
     * Creates a GET request byte array with the format [GET_BYTE, 0].
     * @return the GET request byte array
     */
    public static byte[] createGetRequest() {
        byte[] getRequest = new byte[50];
        getRequest[0] = GET_BYTE;
        getRequest[1] = 0;
        return getRequest;
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

    public static void main(String[] args){
        byte[] tester = PacketProcessor.createRequestPacket("10:15", 3, "u", 6);
        logging.info2("PacketProcessor",""+Arrays.toString(tester) );
        //System.out.println(Arrays.toString(tester));

        ElevatorInfo testInfo = new ElevatorInfo("u", 5, "12:00", 5, 1);

        byte[] result = addElevatorStatus(testInfo, tester);
        logging.info2("PacketProcessor",""+Arrays.toString(result) );
        //System.out.println(Arrays.toString(result));
//        PacketProcessor.extractStatus(result);
//        System.out.println(translateRequest(result));
    }
}

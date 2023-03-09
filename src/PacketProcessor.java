import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PacketProcessor {

    public static final byte GET_BYTE = 1;
    public static final byte REPLY = 2;
    public static final byte DELIMITER = 0;

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

    public static byte[] addElevatorStatus(ElevatorInfo info, byte[] data){
        int dIndex = findDelimiter(data, 1);
        if(dIndex == -1){
            System.out.println("CAN'T FIND THE DELIMETER IN DATA: " + Arrays.toString(data));
            System.exit(1);
        }

        byte[] stateBytes = info.getState().toString().getBytes();

        byte[] destDelim = {0, (byte)info.getCarButton(), 0};

        byte[] withStatus =  combineByteArr(dIndex+1, data, stateBytes);

        return combineByteArr(dIndex + stateBytes.length + 1, withStatus, destDelim);

    }

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

    public static byte[] createGetRequest(){
        return new byte[]{GET_BYTE, 0};
    }

    public static boolean isGetRequest(byte[] data){
        return data[0] == GET_BYTE;
    }

    public static byte[] createOkReply(){
        return new byte[]{REPLY};
    }

    public static boolean isOkReply(byte[] data){
        return data[0] == REPLY;
    }

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
        System.out.println(Arrays.toString(tester));

        ElevatorInfo testInfo = new ElevatorInfo("u", 5, "12:00", 5, 1);

        byte[] result = addElevatorStatus(testInfo, tester);
        System.out.println(Arrays.toString(result));
        System.out.println(translateRequest(result));
    }
}

import java.nio.charset.StandardCharsets;

public class PacketProcessor {

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

    public static ElevatorInfo translateRequest(byte[] data){

        int floorNumber = (int)data[1];
        String direction = new String(data, 3, 4);
        int carButton = (int)data[5];
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

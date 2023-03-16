package DataComp;

import Config.ConfigInfo;

public class ElevatorPacket {

    public static byte[] createOk(){
        // TODO:
        return new byte[3];
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

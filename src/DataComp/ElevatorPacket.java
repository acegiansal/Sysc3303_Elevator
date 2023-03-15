package DataComp;

public class ElevatorPacket {

    public static byte[] createOk(){
        // TODO:
        return new byte[3];
    }

    public static byte[] createStatus(){
        return new byte[3];
    }

    public static ElevatorStatus translateStatus(){
        return  new ElevatorStatus();
    }

}

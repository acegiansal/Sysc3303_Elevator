package Config;

public class ConfigInfo {
    /* UDP config */
    public static final int PACKET_SIZE = 15;
    public static final int CONTROL_PORT = 5000;
    public static final int FLOOR_PORT = 5500;
    public static final int SCHEDULER_PORT = 5900;


    /* Basic Config */
    public static final int NUM_FLOORS  = 10;
    public static final int NUM_ELEVATORS = 1;

    /* Timing Config */
    public static final int DOOR_OPEN_TIME = 500;
    public static final int FLOOR_TRAVERSAL_TIME = 750;
}

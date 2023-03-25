//import org.junit.Test;
//import java.util.Arrays;
//
//import static org.junit.Assert.*;
//
///*
// One clarification for this testing suite is that it must be run individually
// */
//public class TestingElevator {
//
//    private static byte[] dataPackF, dataPackE, reply;
//
//    private static boolean idleToCheck = false, idleToOpen = false, checkToMoving = false,
//            movingToOpen = false, openToClose = false, closeToIdle = false, closeToMoving = false,
//            lamp = false;
//
//    private static int elevator1 = 1, elevator2 = 0, e1Floor, e2Floor;
//
//    private static String direction;
//
//    @org.junit.Before
//    public void setUp() throws Exception{
//    }
//
//    public static void setDataFloor(byte[] test){
//        dataPackF = test;
//    }
//
//    public static void setDataElevator(byte[] test){
//        dataPackE = test;
//    }
//
//    public static void setDataReply(byte[] test){
//        reply = test;
//    }
//
//    public static void idleOpenFlag(boolean test) {
//        idleToOpen = test;
//    }
//
//    public static void idleCheckFlag(boolean test) {
//        idleToCheck = test;
//    }
//
//    public static void checkMovingFlag(boolean test) {
//        checkToMoving = test;
//    }
//
//    public static void movingOpenFlag(boolean test) {
//        movingToOpen = test;
//    }
//
//    public static void openCloseFlag(boolean test) {
//        openToClose = test;
//    }
//
//    public static void closeIdleFlag(boolean test) {
//        closeToIdle = test;
//    }
//
//    public static void closeMovingFlag(boolean test) {
//        closeToMoving = test;
//    }
//
//    public static void setE1(boolean test){
//        if (test) {
//            elevator1 = 0;
//        }
//    }
//
//    public static void setE2(boolean test){
//        if (test) {
//            elevator2 = 1;
//        }
//    }
//
//    public static void e1Floor(int floor){
//        e1Floor = floor;
//    }
//
//    public static void e2Floor(int floor){
//        e2Floor = floor;
//    }
//
//    public static void lampPower(boolean test){
//        lamp = test;
//    }
//
//    public static void setDirection(String d){
//        direction = d;
//    }
//
//    @Test
//    public void testFloorSend() throws InterruptedException {
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String test[] = {"14:05:15.0 3 Up 6"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(5000);
//        byte[] testData = PacketProcessor.createRequestPacket("14:05:15.0", 3, "u",6);
//        assertEquals(Arrays.toString(testData), Arrays.toString(dataPackF));
//        sc.stop();
//        fl.stop();
//    }
//
//    @Test
//    public void testElevatorReceive() throws InterruptedException{
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String test[] = {"14:05:15.0 3 Up 6"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(20000);
//        byte[] testData = PacketProcessor.createRequestPacket("14:05:15.0", 3, "u",6);
//        assertEquals(Arrays.toString(testData), Arrays.toString(dataPackE));
//    }
//
//    @Test
//    public void testReply() throws InterruptedException{
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String[] test = {"14:05:15.0 3 Up 6"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(20000);
//        byte[] testData = PacketProcessor.createOkReply();
//        assertEquals(Arrays.toString(testData), Arrays.toString(reply));
//    }
//
//    @Test
//    public void testStateTransition() throws InterruptedException{
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String[] test = {"14:05:15.0 3 Up 6"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(20000);
//        //assuming that the elevator is done correctly, this is the path it should take if going to different floor
//        assertEquals(true, idleToCheck);
//        assertEquals(true,checkToMoving);
//        assertEquals(true, movingToOpen);
//        assertEquals(true, openToClose);
//        assertEquals(true, closeToIdle);
//    }
//
//    @Test // for if a floor is chosen that the elevator is already on
//    public void testStateTransition2() throws InterruptedException{
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String[] test = {"14:05:15.0 1 Up 1"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(20000);
//        assertEquals(true, idleToCheck);
//        assertEquals(true, idleToOpen);
//        assertEquals(true, openToClose);
//        assertEquals(true, closeToIdle);
//    }
//
//    @Test
//    public void userInterfaceUp() throws InterruptedException{
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String[] test = {"14:05:15.0 3 Up 6"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(20000);
//        assertEquals(true,lamp);
//        assertEquals("Up", direction);
//    }
//
//    @Test
//    public void userInterfaceDown() throws InterruptedException{
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String[] test = {"14:06:15.0 2 Down 1"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(20000);
//        assertEquals(true,lamp);
//        assertEquals("Down", direction);
//    }
//
//    @Test
//    public void secondElevator() throws InterruptedException {
//        Scheduler s = new Scheduler(23);
//        Thread sc = new Thread(s);
//        String[] test = {"14:05:15.0 3 Up 6", "14:06:15.0 2 Down 1"};
//        Floor f = new Floor(23, test);
//        Thread fl = new Thread(f);
//        Building.main(null);
//        sc.start();
//        fl.start();
//        Thread.sleep(20000);
//        if (elevator1 == 0){
//            assertEquals(1,elevator2);
//            assertEquals(1,e1Floor);
//            assertEquals(6,e2Floor);
//        }
//        else{
//            assertEquals(1,elevator1);
//            assertEquals(0,elevator2);
//            assertEquals(6,e1Floor);
//            assertEquals(1,e2Floor);
//        }
//    }
//}

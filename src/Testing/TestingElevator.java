package Testing;

import Config.ConfigInfo;
import ControlComp.ElevatorControl;
import DataComp.RequestPacket;
import ElevatorComp.ElevatorCar;
import FloorComp.FloorSend;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TestingElevator {

    private static boolean idle = false, moving = false, checkFloor = false,
            open = false, close = false, lamp = false;

    private static int elevator1 = 1, elevator2 = 0, e1Floor, e2Floor;

    private static String direction, fileInput;

    private static String[] inputParse;

    private static byte[] eleData;

    private ElevatorControl eleCon;
    private ElevatorCar ecar;
    private FloorSend fs;

    private static RequestPacket rp;

    public static void checkFloorFlag(boolean test) {
        checkFloor = test;
    }

    public static void idleFlag(boolean test) {
        idle = test;
    }

    public static void movingFlag(boolean test) {
        moving = test;
    }

    public static void openFlag(boolean test) {
        open = test;
    }

    public static void closeFlag(boolean test) {
        close = test;
    }

    public static void setE1(boolean test) {
        if (test) {
            elevator1 = 0;
        }
    }

    public static void setE2(boolean test) {
        if (test) {
            elevator2 = 1;
        }
    }

    public static void e1Floor(int floor) {
        e1Floor = floor;
    }

    public static void e2Floor(int floor) {
        e2Floor = floor;
    }

    public static void lampPower(boolean test) {
        lamp = test;
    }

    public static void setFileInput(String in){
        fileInput = in;
    }


    public static void setInputParse(String[] parsed){
        inputParse = parsed;
    }

    public static void setReqPak(RequestPacket reqPak){
        rp = reqPak;
    }

    public static void seteleData(byte[] data){
        eleData = data;
    }

    @BeforeEach
    public void setUp() throws Exception {
        eleCon = new ElevatorControl(ConfigInfo.NUM_ELEVATORS);
        ecar = new ElevatorCar();
        fs = new FloorSend(ConfigInfo.NUM_FLOORS, ConfigInfo.SCHEDULER_PORT, ConfigInfo.NUM_ELEVATORS);
        Thread eleConT = new Thread(eleCon);
        Thread eCarT = new Thread(ecar);
        Thread fsT = new Thread(fs);
        eleConT.start();
        eCarT.start();
        fsT.start();
    }

    @AfterEach
    public void close() throws Exception{
        eleCon.stop();
        ecar.stop();
        fs.stop();
    }

    @Test
    public void testFloorReadFile() throws InterruptedException {
        String tester = "14:30:15.0 5 Down 2 0";
        String[] tester2 = {"14:30:15.0", "5", "Down", "2", "0"};
        Thread.sleep(2000);
        assertEquals(tester, fileInput);
        assertEquals(tester2, inputParse);
    }

    @Test
    public void testFloorSend() throws InterruptedException{
        RequestPacket requestTest = new RequestPacket(Integer.parseInt("5"), Integer.parseInt("2"), "d", Integer.parseInt("0"), "14:30:15.0");
        Thread.sleep(5000);
        assertEquals(requestTest.getDirection(), rp.getDirection());
        assertEquals(requestTest.getStartFloor(), rp.getStartFloor());
        assertEquals(requestTest.getEndFloor(), rp.getEndFloor());
        assertEquals(requestTest.getTime(), rp.getTime());
        assertEquals(requestTest.getScenario(), rp.getScenario());
    }

    @Test
    public void testElevatorReceive() throws InterruptedException{
        Thread.sleep(5000);
        RequestPacket test = new RequestPacket(Integer.parseInt("5"), Integer.parseInt("2"), "d", Integer.parseInt("0"), "14:30:15.0");
        assertEquals(test.getDirection(), RequestPacket.translateRequestBytes(eleData).getDirection());
        assertEquals(test.getStartFloor(), RequestPacket.translateRequestBytes(eleData).getStartFloor());
        assertEquals(test.getEndFloor(), RequestPacket.translateRequestBytes(eleData).getEndFloor());
        assertEquals(test.getTime(), RequestPacket.translateRequestBytes(eleData).getTime());
        assertEquals(test.getScenario(), RequestPacket.translateRequestBytes(eleData).getScenario());

    }

    @Test
    public void checkStates() throws InterruptedException{
        Thread.sleep(20000);
        assertEquals(true, idle);
        assertEquals(true, moving);
        assertEquals(true, checkFloor);
        assertEquals(true, open);
        assertEquals(true, close);
        assertEquals(true, lamp);
    }


}
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

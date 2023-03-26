package Testing;

import Config.ConfigInfo;
import ControlComp.ElevatorControl;
import DataComp.RequestPacket;
import ElevatorComp.ElevatorCar;
import FloorComp.FloorSend;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestingElevator {

    private static boolean idle = false, moving = false, checkFloor = false,
            open = false, close = false, lamp = false, invalidInput = false;

    private static String fileInput;

    private static String[] inputParse;

    private static ElevatorCar faulted;

    private static byte[] eleData;

    private ElevatorControl eleCon;
    private ElevatorCar ecar;
    private FloorSend fs;

    private static RequestPacket rp;

    private static ArrayList<Integer> elevatorList = new ArrayList();

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

    public static void lampPower(boolean test) {
        lamp = test;
    }

    public static void setFileInput(String in) {
        fileInput = in;
    }


    public static void setInputParse(String[] parsed) {
        inputParse = parsed;
    }

    public static void setReqPak(RequestPacket reqPak) {
        rp = reqPak;
    }

    public static void seteleData(byte[] data) {
        eleData = data;
    }

    public static void addElevator(int ec) {
        elevatorList.add(ec);
    }

    public static void setFaulted(ElevatorCar ec){
        faulted = ec;
    }

    public static void setInvalidInput(){
        invalidInput = true;
    }

    /**
     * Creates the necessary objects to run the program, then starts them as threads
     *
     * @throws Exception
     */
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

    /**
     * Stops the threads by using a conditional flag and closes all sockets
     *
     * @throws Exception
     */
    @AfterEach
    public void close() throws Exception {
        eleCon.stop();
        ecar.stop();
        fs.stop();
    }

    /**
     * Testing for a successful read from the provided file
     * Values used for testing: {14:25:15.0 1 Up 3 1}, {14:30:15.0 5 Down 2 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void testFloorReadFile() throws InterruptedException {
        String tester = "14:30:15.0 5 Down 2 0";
        String[] tester2 = {"14:30:15.0", "5", "Down", "2", "0"};
        Thread.sleep(2000);
        assertEquals(tester, fileInput);
        assertEquals(tester2, inputParse);
    }

    /**
     * Testing for a successful send request from the floor
     * Values used for testing: {14:25:15.0 1 Up 3 1}, {14:30:15.0 5 Down 2 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void testFloorSend() throws InterruptedException {
        RequestPacket requestTest = new RequestPacket(Integer.parseInt("5"), Integer.parseInt("2"), "d", Integer.parseInt("0"), "14:30:15.0");
        Thread.sleep(5000);
        assertEquals(requestTest.getDirection(), rp.getDirection());
        assertEquals(requestTest.getStartFloor(), rp.getStartFloor());
        assertEquals(requestTest.getEndFloor(), rp.getEndFloor());
        assertEquals(requestTest.getTime(), rp.getTime());
        assertEquals(requestTest.getScenario(), rp.getScenario());
    }

    /**
     * Testing for a successful receive request from the elevator
     * Values used for testing: {14:25:15.0 1 Up 3 1}, {14:30:15.0 5 Down 2 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void testElevatorReceive() throws InterruptedException {
        Thread.sleep(5000);
        RequestPacket test = new RequestPacket(Integer.parseInt("5"), Integer.parseInt("2"), "d", Integer.parseInt("0"), "14:30:15.0");
        assertEquals(test.getDirection(), RequestPacket.translateRequestBytes(eleData).getDirection());
        assertEquals(test.getStartFloor(), RequestPacket.translateRequestBytes(eleData).getStartFloor());
        assertEquals(test.getEndFloor(), RequestPacket.translateRequestBytes(eleData).getEndFloor());
        assertEquals(test.getTime(), RequestPacket.translateRequestBytes(eleData).getTime());
        assertEquals(test.getScenario(), RequestPacket.translateRequestBytes(eleData).getScenario());

    }

    /**
     * Testing for if the hierarchical state machine completes all states after sucessfully sending an elevator
     * Values used for testing: {14:25:15.0 1 Up 3 1}, {14:30:15.0 5 Down 2 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void checkStates() throws InterruptedException {
        Thread.sleep(20000);
        assertEquals(true, idle);
        assertEquals(true, moving);
        assertEquals(true, checkFloor);
        assertEquals(true, open);
        assertEquals(true, close);
        assertEquals(true, lamp);
    }

    /**
     * Testing for if more than 1 elevator is used concurrently
     * Values used for testing: {14:25:15.0 1 Up 3 1}, {14:30:15.0 5 Down 2 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void multipleElevator() throws InterruptedException {
        Thread.sleep(20000);
        assertNotEquals(elevatorList.get(0), elevatorList.get(1));
    }


    //STARTING FROM HERE, EACH TEST CASE SHOULD BE RUN MANUALLY, ONE BY ONE WITH PROVIDED TEST VALUES

    /**
     * Testing for if the same elevator is used since it is closer
     * Values used for testing: {14:05:15.0 3 Up 6 2}, {14:15:15.0 2 Down 1 0}, {14:25:15.0 1 Up 3 1},
     * {14:30:15.0 5 Down 2 0}, {14:30:15.0 3 Down 2 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void sameElevator() throws InterruptedException {
        Thread.sleep(25000);
        for (int i = 0; i < elevatorList.size(); i++) {
            System.out.println("The elevator IDs are: " + elevatorList.get(i));
        }
        assertEquals(elevatorList.get(2), elevatorList.get(0));
    }

    /**
     * Test where an elevator experiences a hard fault
     * Values used for testing: {14:05:15.0 3 Up 6 2}
     *
     * @throws InterruptedException
     */
    @Test
    public void elevatorHardFault() throws InterruptedException {
        Thread.sleep(5000);
        assertNotEquals(true, idle);
        assertEquals(2,faulted.getCurrentFloor());
        assertEquals(true, faulted.isHardFaulted());
        assertEquals(false, faulted.isTransFaulted());
    }

    /**
     * Test where an elevator experiences a transient fault
     * Values used for testing: {14:05:15.0 3 Up 6 1}
     *
     * @throws InterruptedException
     */
    @Test
    public void elevatorTransient() throws InterruptedException{
        Thread.sleep(20000);
        assertEquals(true, idle);
        assertEquals(6, faulted.getCurrentFloor());
        assertNotEquals(true, faulted.isHardFaulted());
    }

    /**
     * Test where an elevator is fed less arguments than required
     * Values used for testing: {14:05:15.0 3 Up 6}
     *
     * @throws InterruptedException
     */
    @Test
    public void badInputNumber() throws InterruptedException{
        Thread.sleep(2000);
        assertEquals(true, invalidInput);
    }

    /**
     * Test where bad input is given after a valid input and before another valid input
     * Values used for testing: {14:05:15.0 3 Up 6 0}, {14:05:15.0}, {14:15:15.0 2 Down 1 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void badInputMiddle() throws InterruptedException{
        Thread.sleep(20000);
        assertEquals(true, invalidInput);
        //should still have working elevators
        assertEquals(true, idle);
        assertEquals(true, moving);
        assertEquals(true, checkFloor);
        assertEquals(true, open);
        assertEquals(true, close);
        assertEquals(true, lamp);
        assertEquals(1, elevatorList.size());
    }

    /**
     * Test where the input is asking for the elevator to move to the floor it is currently on
     * Values used for testing: {14:05:15.0 1 Up 1 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void sameFloor() throws InterruptedException{
        Thread.sleep(15000);
        assertEquals(false, moving);
    }

    /**
     * Test where the input mismatches the actual items
     * Values used for testing: {a x Up 1 0}
     *
     * @throws InterruptedException
     */
    @Test
    public void badInputMismatch() throws InterruptedException{
        Thread.sleep(15000);
        assertEquals(false, checkFloor);
        assertEquals(false, idle);
        assertEquals(false, moving);
        assertEquals(false, checkFloor);
        assertEquals(false, open);
        assertEquals(false, close);
        assertEquals(false, lamp);
    }
}

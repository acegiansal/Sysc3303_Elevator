//import org.junit.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.*;
//public class TestingElevator {
//    @org.junit.Before
//    public void setUp() throws Exception{
//
//    }
//
//    @Test
//    public void testFloorSend(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        Floor floor = new Floor(s);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        floor.testSend("14:05:15.0", 3, true, 6);
//        ElevatorInfo test = s.getFloorMessages(); //getting the floor message deletes it, so we have to store it
//        assertEquals(test.getFloorNumber(), eInfo.getFloorNumber());
//        assertEquals(test.getCarButton(), eInfo.getCarButton());
//        assertEquals(test.getTime(), eInfo.getTime());
//    }
//
//    @Test
//    public void testElevatorSend(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        Elevator elevator = new Elevator(s);
//        elevator.testSend(eInfo);
//        ElevatorInfo test = s.getElevatorMessages();
//        assertEquals(test.getFloorNumber(), eInfo.getFloorNumber());
//        assertEquals(test.getCarButton(), eInfo.getCarButton());
//        assertEquals(test.getTime(), eInfo.getTime());
//    }
//
//    @Test
//    public void testElevatorReceive(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        s.addFloorMessage(eInfo);
//        Elevator elevator = new Elevator(s);
//        elevator.testReceiveFromSched();
//        ElevatorInfo test = s.getElevatorMessages();
//        assertEquals(test.getFloorNumber(), eInfo.getFloorNumber());
//        assertEquals(test.getCarButton(), eInfo.getCarButton());
//        assertEquals(test.getTime(), eInfo.getTime());
//    }
//
//    @Test
//    public void testFloorReceive(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        Floor floor = new Floor(s);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        s.addElevatorMessage(eInfo);
//        floor.testReceiveFromSched();
//        assertEquals(eInfo.toString(), floor.getTestString());
//    }
//
//    @Test
//    public void testIdleScheduler(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        Scheduler.SchedulerState x = Scheduler.SchedulerState.IDLE;
//        assertEquals(x, s.getState());
//    }
//
//    @Test
//    public void testReceivingScheduler(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        s.addFloorMessage(eInfo);
//        Scheduler.SchedulerState x = Scheduler.SchedulerState.RECEIVING;
//        assertEquals(x, s.getState());
//    }
//
//    @Test
//    public void testIdleElevator(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        Elevator elevator = new Elevator(s);
//        ElevatorState x = ElevatorState.IDLE;
//        assertEquals(x, elevator.getState());
//    }
//
//    @Test
//    public void testCheck_FloorElevator(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        Elevator elevator = new Elevator(s);
//        s.addFloorMessage(eInfo);
//        ElevatorState x = ElevatorState.CHECK_FLOOR;
//        elevator.setTesting(new boolean[] {true, false, false, false});
//        elevator.testReceiveFromSched();
//        assertEquals(x, elevator.getState());
//    }
//
//    @Test
//    public void testMovingElevator(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        Elevator elevator = new Elevator(s);
//        s.addFloorMessage(eInfo);
//        elevator.setTesting(new boolean[] {false, true, false, false});
//        ElevatorState x = ElevatorState.MOVING;
//        elevator.testReceiveFromSched();
//        assertEquals(x, elevator.getState());
//    }
//
//    @Test
//    public void testOpenDoorsElevator(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        Elevator elevator = new Elevator(s);
//        s.addFloorMessage(eInfo);
//        elevator.setTesting(new boolean[] {false, false, true, false});
//        ElevatorState x = ElevatorState.DOOR_OPENED;
//        elevator.testReceiveFromSched();
//        assertEquals(x, elevator.getState());
//    }
//
//    @Test
//    public void testClosedDoorsElevator(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        Elevator elevator = new Elevator(s);
//        s.addFloorMessage(eInfo);
//        elevator.setTesting(new boolean[] {false, false, false, true});
//        ElevatorState x = ElevatorState.DOOR_CLOSED;
//        elevator.testReceiveFromSched();
//        assertEquals(x, elevator.getState());
//    }
//
//    @Test
//    public void successfulIdleElevatorLoop(){
//        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
//        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
//        Scheduler s = new Scheduler(eMSG, fMSG);
//        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
//        Elevator elevator = new Elevator(s);
//        s.addFloorMessage(eInfo);
//        elevator.setTesting(new boolean[] {false, false, false, false});
//        ElevatorState x = ElevatorState.IDLE;
//        elevator.testReceiveFromSched();
//        assertEquals(x, elevator.getState());
//    }
//}

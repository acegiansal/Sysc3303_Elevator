import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class TestingElevator {
    @org.junit.Before
    public void setUp() throws Exception{
    }

    @Test
    public void testFloorSend(){
        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
        Scheduler s = new Scheduler(eMSG, fMSG);
        Floor floor = new Floor(s);
        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
        floor.testSend("14:05:15.0", 3, true, 6);
        ElevatorInfo test = s.getFloorMessages(); //getting the floor message deletes it, so we have to store it
        assertEquals(test.getFloorNumber(), eInfo.getFloorNumber());
        assertEquals(test.getCarButton(), eInfo.getCarButton());
        assertEquals(test.getTime(), eInfo.getTime());
    }

    @Test
    public void testElevatorSend(){
        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
        Scheduler s = new Scheduler(eMSG, fMSG);
        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
        Elevator elevator = new Elevator(s);
        elevator.testSend(eInfo);
        ElevatorInfo test = s.getElevatorMessages();
        assertEquals(test.getFloorNumber(), eInfo.getFloorNumber());
        assertEquals(test.getCarButton(), eInfo.getCarButton());
        assertEquals(test.getTime(), eInfo.getTime());
    }

    @Test
    public void testElevatorReceive(){
        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
        Scheduler s = new Scheduler(eMSG, fMSG);
        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
        s.addFloorMessage(eInfo);
        Elevator elevator = new Elevator(s);
        elevator.testReceiveFromSched();
        ElevatorInfo test = s.getElevatorMessages();
        assertEquals(test.getFloorNumber(), eInfo.getFloorNumber());
        assertEquals(test.getCarButton(), eInfo.getCarButton());
        assertEquals(test.getTime(), eInfo.getTime());
    }

    @Test
    public void testFloorReceive(){
        ArrayList<ElevatorInfo> eMSG = new ArrayList<>();
        ArrayList<ElevatorInfo> fMSG = new ArrayList<>();
        Scheduler s = new Scheduler(eMSG, fMSG);
        Floor floor = new Floor(s);
        ElevatorInfo eInfo = new ElevatorInfo(true, 3, "14:05:15.0", 6);
        s.addElevatorMessage(eInfo);
        floor.testReceiveFromSched();
        assertEquals(eInfo.toString(), floor.getTestString());
    }

}

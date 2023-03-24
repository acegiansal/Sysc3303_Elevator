package ElevatorComp.NewElevatorStates;

import Config.ConfigInfo;
import DataComp.ElevatorStatus;
import ElevatorComp.ArrivalSensor;
import ElevatorComp.ElevatorCar;

public class MoveFloor extends ShaftState{

    private ArrivalSensor sensor;

    public MoveFloor(ElevatorCar elevator) {
        super(elevator);
        sensor = new ArrivalSensor(elevator);
    }

    @Override
    public void entry(){
        System.out.println("Elevator " + elevator.getElevatorID() + " motor starting!");

        // Update currentFloor
        if(elevator.getFloorQueue().get(0) > elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
        } else if(elevator.getFloorQueue().get(0) < elevator.getCurrentFloor()){
            elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
        }

        //Update the floor now to tell the scheduler that it shouldn't tell it to use the same floor
        this.updateStatus();

        Thread sensorThread = new Thread(sensor, "Elevator " + elevator.getElevatorID() + " arrival sensor");
        sensorThread.start();
        this.setTimer(ConfigInfo.FLOOR_TRAVERSAL_TIME);

    }

    @Override
    public void timeout(){
        if(!elevator.isHardFaulted()){
            sensor.alertSensor();
            System.out.println("Elevator " + elevator.getElevatorID() + " Now on floor: " + elevator.getCurrentFloor());
            this.shouldOpen();
        }
    }

    @Override
    public String toString(){
        return "MoveFloor";
    }
}

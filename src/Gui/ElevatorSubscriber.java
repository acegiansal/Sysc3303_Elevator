package Gui;

import DataComp.ElevatorStatus;

public interface ElevatorSubscriber {

    void handleUpdate(ElevatorStatus status);

}

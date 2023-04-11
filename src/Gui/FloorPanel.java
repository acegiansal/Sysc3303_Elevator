package Gui;

import DataComp.ElevatorStatus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FloorPanel extends JPanel implements ElevatorSubscriber {

    private ArrayList<MiniFloor> floors;
    private int maxFloors;


    public FloorPanel(int maxFloors){
        int numRows = maxFloors / 5;
        if(maxFloors % 5 > 0){
            numRows++;
        }
        this.setLayout(new GridLayout(numRows, 5));
        this.maxFloors = maxFloors;
        floors = new ArrayList<>();

        for(int i = 0; i<maxFloors; i++){
            floors.add(new MiniFloor(i));
            this.add(floors.get(i));
        }

        this.setBackground(AppFrame.PRIMARY);
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    @Override
    public void handleUpdate(ElevatorStatus status) {
        floors.get(status.getCurrentFloor()-1).handleUpdate(status);
    }
}

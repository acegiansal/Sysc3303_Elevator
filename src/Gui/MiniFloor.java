package Gui;

import DataComp.ElevatorStatus;

import javax.swing.*;
import java.awt.*;

public class MiniFloor extends JPanel implements ElevatorSubscriber{

    private int floor;
    private DirectionPanel arrows;
    private JButton lamp;
    private static final Color OFF = new Color(30, 30, 44);
    private static final Color ON = new Color(242, 159, 103);

    public MiniFloor(int floor){
        this.floor = floor + 1;
        arrows = new DirectionPanel();
        arrows.setDirection(ElevatorStatus.IDLE);
        lamp = new JButton(Integer.toString(this.floor));
        lamp.setEnabled(false);
        lamp.setBackground(OFF);

        this.add(arrows);
        this.add(lamp);
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setBackground(AppFrame.PRIMARY);
        this.setPreferredSize(new Dimension(100,50));
    }

    @Override
    public void handleUpdate(ElevatorStatus status) {
        if(status.getDoorStatus() == ElevatorStatus.CLOSING){
            lamp.setBackground(OFF);
            arrows.setDirection(ElevatorStatus.IDLE);
        } else if(status.getDoorStatus() != ElevatorStatus.CLOSED){
            lamp.setBackground(ON);
            arrows.setDirection(status.getDirection());
        }
    }
}

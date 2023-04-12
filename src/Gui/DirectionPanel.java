package Gui;

import DataComp.ElevatorStatus;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;

public class DirectionPanel extends JPanel {

    private BasicArrowButton up;
    private BasicArrowButton down;

    public DirectionPanel(){
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        up = new BasicArrowButton(BasicArrowButton.NORTH);
        this.add(up);
        up.setEnabled(false);

        down = new BasicArrowButton(BasicArrowButton.SOUTH);
        this.add(down);
        down.setEnabled(false);
    }

    public void setDirection(String direction){
        if(direction.equals(ElevatorStatus.UP)) {
            up.setBackground(Color.GREEN);
            down.setBackground(Color.GRAY);
        } else if(direction.equals(ElevatorStatus.DOWN)){
            up.setBackground(Color.GRAY);
            down.setBackground(Color.GREEN);
        } else {
            up.setBackground(Color.GRAY);
            down.setBackground(Color.GRAY);
        }
    }
}

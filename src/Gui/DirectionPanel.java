package Gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;

public class DirectionPanel extends JPanel {

    public DirectionPanel(){
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
        this.add(up);
        up.setBackground(Color.GREEN);
        up.setEnabled(false);

        BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
        this.add(down);

        this.setBackground(Color.PINK);
    }
}

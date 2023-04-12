package Gui;

import Gui.NumberPanel.MultiNumberPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiTest implements ActionListener {

    private MultiNumberPanel testPanel;
    private int counter;

    public GuiTest(){
        counter = 0;
        JFrame frame = new JFrame("Test frame");

        frame.setLayout(new BorderLayout());

        this.testPanel = new MultiNumberPanel(2);
        frame.add(testPanel, BorderLayout.CENTER);

        JButton counter = new JButton("Increment");
        counter.addActionListener(this);
        frame.add(counter, BorderLayout.SOUTH);

        DirectionPanel direction = new DirectionPanel();
        frame.add(direction, BorderLayout.NORTH);


        frame.setBackground(Color.pink);
        frame.setSize(500,400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        new GuiTest();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        counter++;
        testPanel.displayNum(counter%100);
    }
}

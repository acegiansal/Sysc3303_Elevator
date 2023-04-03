package Gui;

import ControlComp.ElevatorControl;
import DataComp.ElevatorStatus;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame implements ElevatorSubscriber {

    private JPanel contentPane;
    private TimerPanel timerPanel;

    public AppFrame(){
        super("Elevator Dashboard");

        contentPane = new JPanel();
        contentPane.setBackground(Color.pink);
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);

        // Create 4 elevator panels
        JPanel elevatorDiv = new JPanel();
        contentPane.add(elevatorDiv, BorderLayout.CENTER);

        ElevatorPanel testPanel = new ElevatorPanel(0, 20);
        elevatorDiv.add(testPanel);

        // DEBUG
        byte[] data = {0, 18, 117, 0, 1, 3, 5, 0};
        testPanel.handleUpdate(ElevatorStatus.translateStatusBytes(data));
        // DEBUG ENDS

        // Create bottom panel
        JPanel bottomPanel = new JPanel();
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        //Create timer panel
        timerPanel = new TimerPanel();
        timerPanel.setBackground(Color.pink);
        bottomPanel.add(timerPanel);
        timerPanel.startTimer();

        this.setSize(500,400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void handleUpdate(ElevatorStatus status) {
        System.out.println("View Got: " + status);
    }

    public void startTimer(){
        timerPanel.startTimer();
    }

    public void stopTimer(){
        timerPanel.stopTimer();
    }

    public static void main(String[] args){
        new AppFrame();
    }
}

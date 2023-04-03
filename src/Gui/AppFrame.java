package Gui;

import Config.ConfigInfo;
import ControlComp.ElevatorControl;
import DataComp.ElevatorStatus;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class AppFrame extends JFrame implements ElevatorSubscriber {

    private JPanel contentPane;
    private TimerPanel timerPanel;
    private ArrayList<ElevatorPanel> elevatorPanels;

    public static final Color PRIMARY = new Color(37, 71, 106);
    public static final Color BACKGROUND = new Color(225, 231, 240);



    public AppFrame(){
        super("Elevator Dashboard");

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);

        // Create 4 elevator panels
        JPanel elevatorDiv = new JPanel();
        elevatorDiv.setBackground(PRIMARY);
        contentPane.add(elevatorDiv, BorderLayout.CENTER);

        elevatorPanels = new ArrayList<>();
        Border raisedBevel = BorderFactory.createRaisedBevelBorder();
        for(int i = 0; i< ConfigInfo.NUM_ELEVATORS; i++){
            ElevatorPanel elPanel = new ElevatorPanel(i, ConfigInfo.NUM_FLOORS);
            elPanel.setBackground(BACKGROUND);
            elPanel.setBorder(raisedBevel);
            elevatorDiv.add(elPanel);
            elevatorPanels.add(elPanel);
        }

        // Create bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        //Create timer panel
        timerPanel = new TimerPanel();
        timerPanel.setBackground(Color.pink);
        bottomPanel.add(timerPanel);

        this.setSize(500,425);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void handleUpdate(ElevatorStatus status) {
        System.out.println("View Got: " + status);
        for(ElevatorPanel pane: elevatorPanels){
            pane.handleUpdate(status);
        }
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

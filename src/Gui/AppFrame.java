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
    private FloorPanel floorPanel;
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

        //Create floor panel
        floorPanel = new FloorPanel(ConfigInfo.NUM_FLOORS);
        floorPanel.setBackground(PRIMARY);
        bottomPanel.add(floorPanel);

        this.setSize(540,700);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void handleUpdate(ElevatorStatus status) {
        System.out.println("View Got: " + status);
        for(ElevatorPanel pane: elevatorPanels){
            pane.handleUpdate(status);
        }
        floorPanel.handleUpdate(status);
    }

    public static void main(String[] args){
        new AppFrame();
    }
}

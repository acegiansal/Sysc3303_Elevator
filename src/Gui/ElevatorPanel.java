package Gui;

import DataComp.ElevatorStatus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import Gui.NumberPanel.MultiNumberPanel;

public class ElevatorPanel extends JPanel implements ElevatorSubscriber{

    private int id;
    private JTextField statusField;
    private JTextArea floorField;
    private DirectionPanel directionPanel;
    private MultiNumberPanel floorNumber;

    public ElevatorPanel(int id, int maxFloors){
        this.id = id;

        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.setBackground(Color.pink);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JPanel statusPane = new JPanel();
        statusPane.setLayout(new BoxLayout(statusPane, BoxLayout.PAGE_AXIS));
        JLabel statusLabel = new JLabel("Status:");
        statusField = new JTextField("Idle");
        statusField.setEditable(false);
        statusPane.add(statusLabel);
        statusPane.add(statusField);
        this.add(statusPane, c);

        c.gridy = 1;
        JPanel floorPanel = new JPanel();
        floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.PAGE_AXIS));
        JLabel floorLabel = new JLabel("Floor queue");
        floorField = new JTextArea(5,10);
        floorField.setEditable(false);
        floorPanel.add(floorLabel);
        floorPanel.add(floorField);
        this.add(floorPanel, c);

        c.gridy = 0;
        c.gridx = 2;
        c.gridwidth = 1;
        directionPanel = new DirectionPanel();
        this.add(directionPanel, c);

        c.gridy = 1;
        c.insets = new Insets(10,10,0,0);
        int numDigits = String.valueOf(maxFloors).length();
        floorNumber = new MultiNumberPanel(numDigits);
        floorNumber.setPreferredSize(new Dimension(80, 40));
        this.add(floorNumber, c);

    }

    private void setFloorQueue(ArrayList<Integer> floors){
        StringBuilder floorSet = new StringBuilder();
        for(int i = 0; i<floors.size() - 1; i++){
            floorSet.append(floors.get(i)).append(", ");
        }
        floorSet.append(floors.get(floors.size()-1));
        floorField.setText(floorSet.toString());
    }

    private void setStatus(int doorStatus, String direction){
        directionPanel.setDirection(direction);
        String toPut = "Idle";
        if(doorStatus == ElevatorStatus.CLOSED){
            if(direction.equals(ElevatorStatus.STUCK)){
                toPut = "Hard Faulted!";
            } else if(direction.equals(ElevatorStatus.IDLE)){
                toPut = "Idle";
            } else {
                toPut = "Moving!";
            }
        } else if(doorStatus == ElevatorStatus.OPENING){
            toPut = "Opening Doors!";
        } else if(doorStatus == ElevatorStatus.CLOSING){
            toPut = "Closing Doors!";
        } else if(doorStatus == ElevatorStatus.DOOR_STUCK){
            toPut = "Doors Stuck!";
        }
        statusField.setText(toPut);
    }

    private void setFloor(int floor){
        floorNumber.displayNum(floor);
    }

    @Override
    public void handleUpdate(ElevatorStatus status) {
        if(status.getId() == id){
            // Handle update
            setFloorQueue(status.getFloors());
            setStatus(status.getDoorStatus(), status.getDirection());
            setFloor(status.getCurrentFloor());
        }
    }
}

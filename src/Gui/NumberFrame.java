package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static Gui.NumberFrame.segments.*;

public class NumberFrame extends JPanel {

    private static final int HEIGHT = 5;
    private static final int WIDTH = 3;

    private static final Color OFF = Color.lightGray;
    private static final Color ON = Color.ORANGE;
    private static final Color NOTUSED = Color.BLACK;

    private static final segments[] ZERO = {T, TL, TR, BL, BR, B};
    private static final segments[] ONE = {TR, BR};
    private static final segments[] TWO = {T, TR, M, BL, B};
    private static final segments[] THREE = {T, TR, M, BR, B};
    private static final segments[] FOUR = {TL, TR, M, BR};
    private static final segments[] FIVE = {T, TL, M, BR, B};
    private static final segments[] SIX = {T, TL, M, BL, BR, B};
    private static final segments[] SEVEN = {T, TR, BR};
    private static final segments[] EIGHT = {T, TL, TR, M, BL, BR, B};
    private static final segments[] NINE = {T, TL, TR, M, BR, B};

    private HashMap<Integer, segments[]> segmentMap;

    public enum segments {
        T, TL, TR, M, BL, BR, B
    }

    private JButton[][] panels;

    private int count;

    public NumberFrame(){

        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        segmentMap = new HashMap<>();
        // Add all segments
        segmentMap.put(0, ZERO);
        segmentMap.put(1, ONE);
        segmentMap.put(2, TWO);
        segmentMap.put(3, THREE);
        segmentMap.put(4, FOUR);
        segmentMap.put(5, FIVE);
        segmentMap.put(6, SIX);
        segmentMap.put(7, SEVEN);
        segmentMap.put(8, EIGHT);
        segmentMap.put(9, NINE);

        this.count = 0;
        panels = new JButton[HEIGHT][WIDTH];

        this.setLayout(new GridLayout(HEIGHT,WIDTH));

        for(int i = 0; i<HEIGHT; i++){
            for(int j = 0; j<WIDTH; j++){
                panels[i][j] = new JButton();

                panels[i][j].setEnabled(false);
                panels[i][j].setBackground(OFF);

                this.add(panels[i][j]);
            }
        }

        displayNum(this.count);
    }

    public void displayNum(int toShow){
        segments[] sequence = segmentMap.get(toShow);

        resetNumbers();

        for(segments seg: sequence){
            switch (seg) {
                case T -> {
                    panels[0][0].setBackground(ON);
                    panels[0][1].setBackground(ON);
                    panels[0][2].setBackground(ON);
                }
                case TL -> {
                    panels[2][0].setBackground(ON);
                    panels[1][0].setBackground(ON);
                    panels[0][0].setBackground(ON);
                }
                case TR -> {
                    panels[2][2].setBackground(ON);
                    panels[1][2].setBackground(ON);
                    panels[0][2].setBackground(ON);
                }
                case M -> {
                    panels[2][0].setBackground(ON);
                    panels[2][1].setBackground(ON);
                    panels[2][2].setBackground(ON);
                }
                case BL -> {
                    panels[3][0].setBackground(ON);
                    panels[4][0].setBackground(ON);
                }
                case BR -> {
                    panels[3][2].setBackground(ON);
                    panels[4][2].setBackground(ON);
                }
                case B -> {
                    panels[4][0].setBackground(ON);
                    panels[4][1].setBackground(ON);
                    panels[4][2].setBackground(ON);
                }
            }

        }
    }

    public void resetNumbers(){
        for(int i = 0; i<HEIGHT; i++){
            for(int j = 0; j<WIDTH; j++){
                panels[i][j].setBackground(OFF);
            }
        }
    }

}

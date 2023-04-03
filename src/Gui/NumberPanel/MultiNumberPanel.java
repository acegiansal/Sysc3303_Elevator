package Gui.NumberPanel;

import Gui.AppFrame;

import javax.swing.*;
import java.awt.*;

public class MultiNumberPanel extends JPanel {

    private NumberFrame[] numbers;
    private int maxDigits;

    public MultiNumberPanel(int maxDigits){
        this.maxDigits = maxDigits;

        this.setLayout(new GridLayout(1, maxDigits));

        numbers = new NumberFrame[maxDigits];

        for(int i = 0; i<maxDigits; i++){
            numbers[i] = new NumberFrame();
            numbers[i].setBackground(AppFrame.PRIMARY);
            this.add(numbers[i]);
        }
    }

    public void displayNum(int toShow){
        int numDigits = String.valueOf(toShow).length();
        int[] digits = new int[maxDigits];

        for (int i = maxDigits-1; i >= maxDigits-numDigits; i--) {
            digits[i] = toShow % 10;
            toShow /= 10;
        }

        for (int i = 0; i < digits.length; i++) {
            numbers[i].displayNum(digits[i]);
        }
    }

}

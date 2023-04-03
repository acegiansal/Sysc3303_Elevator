package Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerPanel extends JPanel {
    private Timer timer;
    private long counter;
    private JLabel timeLabel;
    private static int delay = 98;

    public TimerPanel(){
        timer = new Timer(delay, updateTimer);
        counter = 0;
        timeLabel = new JLabel("00:00.0");
        this.add(timeLabel);
    }

    public void startTimer(){
        timer.start();
    }

    public void stopTimer(){
        timer.stop();
    }

    public String getNewTime(long count){
        long tenMilis = count % 10;
        long secs = (count / 10) % 60;
        long mins = (count / 600);

        return String.format("%02d:%02d.%d", mins, secs, tenMilis);
    }

    ActionListener updateTimer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            counter++;
            timeLabel.setText(getNewTime(counter));
        }
    };
}

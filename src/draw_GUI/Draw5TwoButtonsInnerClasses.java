package draw_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Draw5TwoButtonsInnerClasses {
    JFrame frame;
    JLabel label;

    /*** main ***/
    public static void main (String[] args) {
        Draw5TwoButtonsInnerClasses gui = new Draw5TwoButtonsInnerClasses();
        gui.go();
    }

    /*** business method ***/
    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // button and assigning his own listener
        JButton labelButton = new JButton("Change Label");
        labelButton.addActionListener(new LabelListener());
        // button and assigning his own listener
        JButton circleButton = new JButton("Change Circle");
        circleButton.addActionListener(new ColorListener());
        label = new JLabel("I’m a label");
        MyDrawPanel drawPanel = new MyDrawPanel();
        // add widgets
        frame.getContentPane().add(BorderLayout.SOUTH, circleButton);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.getContentPane().add(BorderLayout.EAST, labelButton);
        frame.getContentPane().add(BorderLayout.WEST, label);
        frame.setSize(300,300);
        frame.setVisible(true);
    }

    /*** inner class - listener for labelButton ***/
    class LabelListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent event) {
            label.setText("Ouch!");
        }
    } // close inner class

    /*** inner class - listener for circleButton ***/
    class ColorListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent event) {
            frame.repaint();
        }
    } // close inner class
}

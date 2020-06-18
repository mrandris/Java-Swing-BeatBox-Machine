package draw_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Draw3SimpleGUIWithActionListener implements ActionListener {
    JFrame frame;

    public static void main(String[] args) {
        // implement action listener
        Draw3SimpleGUIWithActionListener gui = new Draw3SimpleGUIWithActionListener();
        gui.go();
    }
    public void go() {
        // setup frame and widgets
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyDrawPanel drawPanel = new MyDrawPanel();
        JButton button = new JButton("Change colors");

        // set button to do something (listen to action)
        button.addActionListener(this);

        // add widgets to frame
        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(300,350);
        frame.setVisible(true);
    }

    // setup action that is performed upon event
    // implementation of interface method
    @Override
    public void actionPerformed(ActionEvent event) {
        frame.repaint();
    }
}

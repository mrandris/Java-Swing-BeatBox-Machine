package draw_GUI;

import javax.swing.*;

public class Draw1SimpleGUI {
    public static void main(String[] args) {
        Draw1SimpleGUI gui = new Draw1SimpleGUI();
        gui.go();
    }

    public void go() {
        // setup frame and widgets
        JFrame frame = new JFrame();
        JButton button = new JButton("Click Me!");

        // set close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // add widget to frame
        frame.getContentPane().add(button);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}

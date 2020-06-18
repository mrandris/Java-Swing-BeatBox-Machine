package draw_GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Draw2SimpleGUIWithActionListener implements ActionListener {
    JButton button;

    public static void main(String[] args) {
        // implement action listener
        Draw2SimpleGUIWithActionListener gui = new Draw2SimpleGUIWithActionListener();
        gui.go();
    }

    public void go() {
        // setup frame and widgets
        JFrame frame = new JFrame();
        button = new JButton("Click Me!");

        // set button to do something (listen to action)
        button.addActionListener(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(button);
        frame.getContentPane().add(new MyDrawPanel());
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    // setup action that is performed upon event
    // implementation of interface method
    @Override
    public void actionPerformed(ActionEvent event) {
        button.setText("Clicked");
    }
}

package draw_GUI;

import javax.swing.*;
import java.awt.*;

public class Draw4SimpleAnimation {
    int x = 70;
    int y = 70;

    /*** main ***/
    public static void main (String[] args) {
        Draw4SimpleAnimation gui = new Draw4SimpleAnimation();
        gui.go();
    }

    /*** business method ***/
    public void go() {
        // setup frame and widgets
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyDrawPanel drawPanel = new MyDrawPanel();

        frame.getContentPane().add(drawPanel);
        frame.setSize(300,300);
        frame.setVisible(true);

        // animation business: change the position of the circle + repaint drawPanel
        for (int i = 0; i < 130; i++) {
            x++;
            y++;
            drawPanel.repaint();
            try {
                Thread.sleep(50);
            } catch(Exception ex) { }
        }
    }// close go() method

    /*** draw panel ***/
    class MyDrawPanel extends JPanel {
        public void paintComponent(Graphics g) {
            // "reset" draw panel
            g.setColor(Color.white);
            g.fillRect(0,0,this.getWidth(), this.getHeight());
            // draw circle
            g.setColor(Color.green);
            g.fillOval(x,y,40,40);
        }
    } // close inner class
} // close outer class

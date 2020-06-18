package music_MIDI;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class Music6TrackWithAnimation {
    static JFrame frame = new JFrame("My First Music Video");
    static MyDrawPanel drawPanel;

    /*** main ***/
    public static void main(String[] args) {
        Music6TrackWithAnimation mini = new Music6TrackWithAnimation();
        mini.go();
    } // close method

    /*** this is where it all happens, the make business method ***/
    public void go() {
        setUpGui();

        try {
            // initiate sequencer & track
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            // listener: "drawPanel", events controller: all events of type "127"
            sequencer.addControllerEventListener(drawPanel, new int[] {127});
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            // create random notes (noteOn, controllerEvent, noteOff)
            int r = 0;
            for (int i = 0; i < 60; i+= 4) {
                r = (int) ((Math.random() * 50) + 1);
                track.add(makeEvent(144,1, r ,100, i));
                track.add(makeEvent(176,1,127,0, i));
                track.add(makeEvent(128,1, r ,100,i + 2));
            } // end loop

            // play sequence
            sequencer.setSequence(seq);
            sequencer.start();
            sequencer.setTempoInBPM(120);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // close method

    /*** setting up graphical interface ***/
    public void setUpGui() {
        drawPanel = new MyDrawPanel();
        frame.setContentPane(drawPanel);
        frame.setBounds(30,30, 300,300);
        frame.setVisible(true);
    } // close method

    /*** generate event ***/
    public MidiEvent makeEvent(int command, int channel, int note, int velocity, int tick) {
        MidiEvent event = null;

        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(command, channel, note, velocity);
            event = new MidiEvent(a, tick);
        }catch(Exception e) { }
        return event;
    } // close method

    /*** inner class draw panel ***/
    class MyDrawPanel extends JPanel implements ControllerEventListener {
        boolean msg = false;

        // if event happens, repaint GUI
        // implementation of interface method
        @Override
        public void controlChange(ShortMessage event) {
            msg = true;
            repaint();
        }

        // repaint GUI with pseudo-random rectangle
        public void paintComponent(Graphics g) {
            if (msg) {
                Graphics2D g2 = (Graphics2D) g;
                int r = (int) (Math.random() * 250);
                int gr = (int) (Math.random() * 250);
                int b = (int) (Math.random() * 250);
                g.setColor(new Color(r,gr,b));
                int ht = (int) ((Math.random() * 120) + 10);
                int width = (int) ((Math.random() * 120) + 10);
                int x = (int) ((Math.random() * 40) + 10);
                int y = (int) ((Math.random() * 40) + 10);
                g.fillRect(x,y,ht, width);
                msg = false;
            } // close if
        } // close method
    } // close inner class
} // close class
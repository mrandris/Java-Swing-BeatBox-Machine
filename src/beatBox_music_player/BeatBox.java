package beatBox_music_player;

import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class BeatBox {
    JPanel mainPanel;
    // ArrayList for all checkboxes
    ArrayList<JCheckBox> checkboxList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;
    // array of instrument names - only for GUI labels
    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap", "High Tom",
            "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
    // array of instruments - as notes for the MIDI ShortMessage
    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
    // set track length (number of beats / ticks)
    int trackLength = 20;

    /*** main ***/
    public static void main (String[] args) {
        new BeatBox().buildGUI();
    }

    /*** business method ***/
    public void buildGUI() {
        // setup frame and panel
        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        // set border for GUI
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        /*** box for buttons ***/
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        // buttons and listeners + add them on box "buttonBox"
        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton resetPattern = new JButton("Reset");
        resetPattern.addActionListener(new MyResetListener());
        buttonBox.add(resetPattern);

        JButton randomPattern = new JButton("Random");
        randomPattern.addActionListener(new MyRandomListener());
        buttonBox.add(randomPattern);

        JButton savePattern = new JButton("Save Pattern");
        savePattern.addActionListener(new MySaveListener());
        buttonBox.add(savePattern);

        JButton loadPattern = new JButton("Load Pattern");
        loadPattern.addActionListener(new MyLoadListener());
        buttonBox.add(loadPattern);
        /*** end button box ***/

        /*** box for instrument names ***/
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        // add instrument names to name box
        for (int i = 0; i < instruments.length; i++) {
            nameBox.add(new Label(instrumentNames[i]));
        }

        // add buttons nd instruments to JPanel "background"
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);
        // add JPanel "background" to the frame
        theFrame.getContentPane().add(background);

        /*** checkbox panel ***/
        checkboxList = new ArrayList<>();
        // setup layout for the center panel
        GridLayout grid = new GridLayout(instruments.length, trackLength);
        grid.setVgap(1);
        grid.setHgap(2);
        // create center panel & add to JPanel "background"
        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        // create checkboxes (set initial value "false") + add them to ArrayList + add them to center panel (GUI)
        for (int i = 0; i < trackLength*instruments.length; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        } // end loop

        setUpMidi();

        // finish frame setup
        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);
    } // close method

    /*** MIDI setup ***/
    public void setUpMidi() {
        try {
            // initiate sequencer & track
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ,4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch(Exception e) {
            e.printStackTrace();
        }
    } // close method

    /*** inner class - start button ***/
    public class MyStartListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            // call business method for music part
            buildTrackAndStart();
        }
    } // close inner class

    public void buildTrackAndStart() {
        // array for the beats/instruments:
        // element takes the value of instrument's note
        // if element = 0 instrument is mute at that beat
        int[] trackList = null;

        // reset track: delete old, create new
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < instruments.length; i++) { // for each row (for each instrument: Congo, High Hat, etc...)
            trackList = new int[trackLength];

            //key = note to be played = instrument
            int key = instruments[i];

            for (int j = 0; j < trackLength; j++ ) {
                JCheckBox jc = (JCheckBox) checkboxList.get(j + (trackLength*i));
                // if checkbox is selected play instrument, else remain mute
                if ( jc.isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            } // close inner loop

            makeTracks(trackList); // make note on & note off events for all beats and add them to track
            track.add(makeEvent(176,1,127,0,trackLength)); // controller event
        } // close outer

        // make sure there is an event at the last beat (0 -> trackLength-1)
        // otherwise might not go all the way before starts over
        track.add(makeEvent(192,9,1,0,trackLength-1));
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY); // continuous looping
            sequencer.start(); // start playing
            sequencer.setTempoInBPM(120);
        } catch(Exception e) {
            e.printStackTrace();
        }
    } // close buildTrackAndStart method

    // make note on & note off events for all beats and add them to track
    public void makeTracks(int[] list) {
        for (int i = 0; i < trackLength; i++) {
            int key = list[i];
            if (key != 0) {
                track.add(makeEvent(144,9,key, 100, i));
                track.add(makeEvent(128,9,key, 100, i+1));
            }
        }
    }

    // make events for the track - note on & note off
    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    /*** inner class - stop button ***/
    public class MyStopListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            // stop player
            sequencer.stop();
        }
    } // close inner class

    /*** inner class - tempo up button ***/
    public class MyUpTempoListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * 1.03));
        }
    } // close inner class

    /*** inner class - tempo down button ***/
    public class MyDownTempoListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * .97));
        }
    } // close inner class

    /*** inner class to clear pattern ***/
    public class MyResetListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            // create checkboxes (set initial value "false") + add them to ArrayList + add them to center panel (GUI)
            for (JCheckBox checkbox : checkboxList) {
                checkbox.setSelected(false);
            } // end loop
            buildTrackAndStart();
        }
    }

    /*** inner class to clear pattern ***/
    public class MyRandomListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            // create checkboxes (set initial value "false") + add them to ArrayList + add them to center panel (GUI)
            for (JCheckBox checkbox : checkboxList) {
                checkbox.setSelected(false);
                // every 5th checkbox is true
                int rand = (int) (Math.random()*100);
                if(rand%5 == 0) {
                    checkbox.setSelected(true);
                }
            } // end loop
            buildTrackAndStart();
        }
    }

    /*** inner class to save pattern - when "Save Pattern" button is clicked ***/
    public class MySaveListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            // make boolean array to store the state of each checkbox
            int checkboxNumber = trackLength*instruments.length;
            boolean[] checkboxState = new boolean[checkboxNumber];
            // iterate over checkboxes and save state (if clicked: state = true, else /default/ state = false)
            for (int i = 0; i < checkboxNumber; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if (check.isSelected()) {
                    checkboxState[i] = true;
                }
            }
            // serialize to to file
            // choose file from file dialog box
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(theFrame);
            File file = fileSave.getSelectedFile();
            try {
                FileOutputStream fileStream = new FileOutputStream(file);
                ObjectOutputStream os = new ObjectOutputStream(fileStream);
                os.writeObject(checkboxState);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        } // close method
    } // close inner class

    /*** inner class to load pattern - when "Load Pattern" button is clicked ***/
    public class MyLoadListener implements ActionListener {
        // implementation of interface method
        @Override
        public void actionPerformed(ActionEvent a) {
            int checkboxNumber = trackLength*instruments.length;
            boolean[] checkboxState = null;
            // read the object from file and cast it back to boolean array
            // choose file from file dialog box
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(theFrame);
            File file = fileOpen.getSelectedFile();
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream is = new ObjectInputStream(fileIn);
                checkboxState = (boolean[]) is.readObject();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            // iterate over checkboxes and restore state (if clicked: state = true, else /default/ state = false)
            for (int i = 0; i < checkboxNumber; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if (checkboxState[i]) {
                    check.setSelected(true);
                } else {
                    check.setSelected(false);
                }
            }
            // stop sequence if currently playing
            sequencer.stop();
            // call business method for music part
            buildTrackAndStart();
        } // close method
    } // close inner class
} // close class

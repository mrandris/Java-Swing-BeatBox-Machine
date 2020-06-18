package music_MIDI;

import javax.sound.midi.*;
public class Music5FirstTrackWithControllerEvent implements ControllerEventListener {
    public static void main(String[] args) {
        Music5FirstTrackWithControllerEvent mini = new Music5FirstTrackWithControllerEvent();
        mini.go();
    }

    public void go() {
        try {
            // initiate sequencer & track
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            // events we are interested in are kept in this array
            // 127 = the highest note (typically never used, that is why we want this particular event)
            int[] eventsIWant = {127};

            // add event listener listening to events type 127
            sequencer.addControllerEventListener(this, eventsIWant);

            // continue initiation of sequencer & track
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            // create notes (noteOn, controllerEvent, noteOff)
            for (int i = 5; i < 60; i+= 4) {
                track.add(makeEvent(144,1,i,100,i));
                track.add(makeEvent(176,1,127,0,i));
                track.add(makeEvent(128,1,i,100,i + 2));
            } // end loop

            // play sequence
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(220);
            sequencer.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // close

    // prints "la" whenever a note is played (= a "127" type event happened, see int[] eventsIWant)
    // implementation of interface method
    @Override
    public void controlChange(ShortMessage event) {
        System.out.println("la");
    }

    public MidiEvent makeEvent(int command, int channel, int note, int velocity, int tick) {
        // command 144 - note on
        // command 128 - note off
        // command 176 - controller event
        // command 192 - change instrument - in this case "note"=instrument type & "velocity"=0

        // channel = musician
        // note - high, low...
        // velocity - velocity (how fast and hard you pressed the play button)

        // tick - beat number

        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(command, channel, note, velocity);
            event = new MidiEvent(a, tick);
        }catch(Exception e) { }
        return event;
    }
} // close class

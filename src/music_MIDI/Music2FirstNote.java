package music_MIDI;

import javax.sound.midi.*;

public class Music2FirstNote {
    public static void main(String[] args) {
        Music2FirstNote mini = new Music2FirstNote();
        mini.play();
    }

    public void play() {
        try {
            // initiate sequencer & track
            Sequencer player = MidiSystem.getSequencer();
            player.open();

            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            // send notes to track
            // note on
            ShortMessage a = new ShortMessage();
            a.setMessage(144, 1, 20, 100);
            MidiEvent noteOn = new MidiEvent(a, 1);
            track.add(noteOn);
            //note off
            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, 20, 100);
            MidiEvent noteOff = new MidiEvent(b, 20);
            track.add(noteOff);

            // play sequence
            player.setSequence(seq);
            player.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

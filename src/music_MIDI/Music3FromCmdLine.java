package music_MIDI;

import javax.sound.midi.*;
public class Music3FromCmdLine { // this is the first one
    public static void main(String[] args) {
        Music3FromCmdLine mini = new Music3FromCmdLine();
//        if (args.length < 2) {
//            System.out.println("Don’t forget the instrument and note args");
//        } else {
            int instrument = Integer.parseInt("40");
            int note = Integer.parseInt("70");
            mini.play(instrument, note);
//        }
    } // close main

    public void play(int instrument, int note) {
        try {
            // sequencer & track initialization
            Sequencer player = MidiSystem.getSequencer();
            player.open();
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();
            MidiEvent event = null;

            // send notes to track
            // set instrument
            ShortMessage first = new ShortMessage();
            // command 192 -> set instrument
            first.setMessage(192, 1, instrument, 0);
            MidiEvent changeInstrument = new MidiEvent(first, 1);
            track.add(changeInstrument);

            // note on
            ShortMessage a = new ShortMessage();
            // command 144 -> play note
            a.setMessage(144, 1, note, 100);
            MidiEvent noteOn = new MidiEvent(a, 1);
            track.add(noteOn);

            //note off
            ShortMessage b = new ShortMessage();
            // command 128 -> end note
            b.setMessage(128, 1, note, 100);
            MidiEvent noteOff = new MidiEvent(b, 16);
            track.add(noteOff);

            // play sequence
            player.setSequence(seq);
            player.start();

        } catch (Exception ex) {ex.printStackTrace();}
    } // close play
} // close class
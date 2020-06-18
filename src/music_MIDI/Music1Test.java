package music_MIDI;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class Music1Test {
    public static void main(String[] args) {
            Music1Test mt = new Music1Test();
            mt.play();
    }

    public void play() {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            System.out.println("We have  sequencer");
        } catch (MidiUnavailableException ex) {
            System.out.println("Exception!");
        }
    }
}

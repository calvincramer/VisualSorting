package visualsorting;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;


public class SoundThread
    implements Runnable {
    
    public SoundThread(String name) {
        this.threadName = name;
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        
        running = true;

        channels[1].noteOn(note, 100);

        while (running) {}
        
        channels[1].noteOff(note);
        
    }
    
    public void kill() {
        running = false;
    }
    
    public void start () {
       //System.out.println("Starting " +  threadName );
       if (t == null) {
          t = new Thread (this, threadName);
          t.start();
       }
    }
    
    public void setNote(int note) {
        this.note = note;
        
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private boolean running;
    
    private int note;
    private int duration;
    private Synthesizer synth;
    private MidiChannel[] channels;
    
    protected Thread t;
    private String threadName;
}
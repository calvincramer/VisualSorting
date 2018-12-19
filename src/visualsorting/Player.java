package visualsorting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player {

    //base folder for all the soundpacks
    private static final String base = "soundpacks";
    private final String soundPack;
    private int NUM_SOUND_FILES;
    private List<Clip> clips;
    private List<Integer> currentlyPlayingStack = new ArrayList<>();
    private int allowedSimul;
    
    public Player(String soundPack, int numContinuousSoundsPlayingAllowed) {
        this.allowedSimul = numContinuousSoundsPlayingAllowed;
        if (this.allowedSimul <= 0)
            this.allowedSimul = 1;
        
        this.soundPack = soundPack;
        
        //TODO... get file from jar
        File soundPackFolder;
        URL url = Player.class.getResource("/soundpacks/" + soundPack);
        String ext = url.toExternalForm();
        soundPackFolder = new File(ext);
        System.out.println(soundPackFolder);
        
        System.out.println("sound pack folder: " + soundPackFolder.getAbsolutePath());
        this.NUM_SOUND_FILES = soundPackFolder.listFiles().length;
        init();

        
        
        /*
        URL soundPackURL = VisualSorting.class.getResource("/soundpacks/" + soundPack).;
        if (soundPackURL != null) {
            File soundPackFolder = new File(soundPackURL.getFile());
            this.NUM_SOUND_FILES = soundPackFolder.listFiles().length;
            init();
            
        } else {
            System.out.println("COULD NOT FIND THE SOUNDPACKS FROM THE JAR");
            System.out.println("or more specifically, /soundpacks/" + soundPack);
            this.NUM_SOUND_FILES = 0;
        }
        */
    }
    
    
    private void init() {
        //initialize all sounds
        clips = new ArrayList<>(this.NUM_SOUND_FILES);
        try {
            for (int i = 0; i < this.NUM_SOUND_FILES; i++) {
                String noteFileName = (i+1) + ".wav";
                InputStream in = Util.class.getResourceAsStream("/" + base + "/" + soundPack + "/"+ noteFileName); 
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(in);
                AudioFormat audioFormat = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
                Clip audioClip = (Clip) AudioSystem.getLine(info);
                audioClip.open(audioStream);
                this.clips.add(audioClip);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("LINE UNAVAILABLE");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Plays a sound from url
     * Credit to: greenLizard and m13r on stack overflow:
     * {@link https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java}
     * and also:
     * {@link https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples}
     * @param soundPack -- the name of the sound pack to be used
     * @param resourceURL -- the name of the file to be played
    */
    private void playSound(int noteNumber){
        //stop the last playing clip from playing
        if (this.currentlyPlayingStack.size() >= this.allowedSimul && this.currentlyPlayingStack.size() > 0) {
            int stopPlayingIndex = this.currentlyPlayingStack.remove(0);
            this.clips.get(stopPlayingIndex).stop();
            this.clips.get(stopPlayingIndex).setMicrosecondPosition(0);
        }
        
        this.clips.get(noteNumber - 1).setMicrosecondPosition(0);
        this.clips.get(noteNumber - 1).start();
        this.currentlyPlayingStack.add(noteNumber - 1);
    }
    
    
    /**
     * Calculates the note to be played based upon a value and range
     * @param low - the lowest value that num can be
     * @param high - the highest value that num can be
     * @param num - the number that represents the note to be played
     * @return the note number, inclusively between low and high
     */
    private int getNoteNumber(double low, double high, double num) {
        double n = num / (high - low);
        double note = n * NUM_SOUND_FILES;
        int noteNumber = (int) Math.round(note);
        if (noteNumber < 1) noteNumber = 1;
        if (noteNumber > NUM_SOUND_FILES) noteNumber = NUM_SOUND_FILES;
        
        return noteNumber;
    }
    
    
    /**
     * Plays a note from the sound pack
     * @param min the minimum value that num can be
     * @param max the maximum value that num can be
     * @param num the actual number that represents the sound played
     */
    protected void playSound(double min, double max, double num) {
        this.playSound(getNoteNumber(min, max, num));
    }
}

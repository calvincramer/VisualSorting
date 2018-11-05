package visualsorting;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * Plays a sound from url
 * Credit to: greenLizard and m13r on stack overflow:
 * {@link https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java}
 * and also:
 * {@link https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples}
 */
public class MakeSound {
    
    private static final String base = "soundpacks";
    private static final String chosenSoundPack = "pure";
    
    public static void playSound(String resourceURL){

        final int BUFFER_SIZE = 128000;
        InputStream in = null;
        AudioInputStream audioStream = null;
        AudioFormat audioFormat = null;
    
        try {
            in = MakeSound.class.getResourceAsStream("/" + base + "/" + chosenSoundPack + "/"+ resourceURL); 
            audioStream = AudioSystem.getAudioInputStream(in);
            audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            //audioClip.addLineListener(this);
            audioClip.open(audioStream);
            audioClip.start();
        } catch (NullPointerException e) {
            System.out.println("Playing sounds too fast, could not get the audio input stream!");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
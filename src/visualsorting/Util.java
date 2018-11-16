package visualsorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Util {
    
    /**
     * Reads a file and returns each line as a String in an array
     * @param url
     * @return 
     */
    protected static String[] readFile(File file) {
        Scanner reader;
        List<String> lines = new ArrayList<>();

        try {
            reader = new Scanner(file);
            while (reader.hasNextLine())
                lines.add(reader.nextLine());
        } 
        catch (Exception e) { e.printStackTrace(); }
        
        return lines.toArray(new String[lines.size()]);
    }
    
    /**
     * Writes a file
     * @param data
     * @param url
     * @return 
     */
    protected static boolean writeFile(String data, String url) {
        
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(url), StandardCharsets.UTF_8);
            writer.write(data);
            writer.close();
            
        } catch (FileNotFoundException ex) { 
            ex.printStackTrace(); 
            return false;
        } catch (IOException ex) {
            ex.printStackTrace(); 
            return false;
        }
        return true;
    }
    
    /**
     * integer[] |-> string in the form: "n1 n2 n3 n4 ..."
     * @param a
     * @return 
     */
    protected static String toStringArr(int[] a) {
        String s = "";
        for (int n : a) {
            s += n + " ";
        }
        return s;
    }
    
    /**
     * Places commas in a string according to where they would be in an integer number
     * @param numStr
     * @return 
     */
    protected static String commifyString(String numStr) {
        int i = numStr.length() - 3;
        while (i >= 1) {
            numStr = numStr.substring(0, i) + "," + numStr.substring(i);
            i -= 3;
        }
        return numStr;
    }
    
    /**
     * Determines if the array contains the number i
     * @param i
     * @param arr
     * @return 
     */
    protected static boolean contains(int i, int[] arr) {
        if (arr == null)
            return false;
        for (int n : arr) {
            if (i == n)
                return true;
        }
        return false;
    }
    
    //base folder for all the soundpacks
    private static final String base = "soundpacks";
    
    /**
     * Plays a sound from url
     * Credit to: greenLizard and m13r on stack overflow:
     * {@link https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java}
     * and also:
     * {@link https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples}
     * @param soundPack -- the name of the sound pack to be used
     * @param resourceURL -- the name of the file to be played
    */
    protected static void playSound(String soundPack, String resourceURL){

        final int BUFFER_SIZE = 128000;
        InputStream in = null;
        AudioInputStream audioStream = null;
        AudioFormat audioFormat = null;
    
        try {
            in = Util.class.getResourceAsStream("/" + base + "/" + soundPack + "/"+ resourceURL); 
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

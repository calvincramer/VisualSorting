package visualsorting;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
//import sun.audio.AudioPlayer; //can't use this via jar
//import sun.audio.AudioStream;

public class VisualSorting {
    
    public VisualSorting() {
        int[] array = new int[NUM_ELEMENTS];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
        
        array = VisualSorting.shuffleArray(array);
        
        this.sorter = new SelectionSort(array);
        //this.sorter = new BubbleSort(array);
        
        window = new MainFrame(sorter);
        window.setSorter(sorter);
        window.setVisible(true);
        
        timerTask = new RepeatingTimer(this);
        timer = new Timer(true);
        
        //wait a little right after the window pops up
        try {
            Thread.sleep( START_DELAY );
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        timer.scheduleAtFixedRate(timerTask, 0, CLOCK_SPEED); //17ms about 60fps
    }
    
    public void tick() {
        
        if (sorter.isFinished() && !doingEndCheck) {
            //timer.cancel();
            this.doingEndCheck = true;
            sorter.setSelectedIndex(0);
            sorter.setLastPairSwappedIncedies(null);
            //System.out.println("Done!");
        }
        if (doingEndCheck) {
            window.repaint();
            sorter.setSelectedIndex(sorter.getSelectedIndex() + 1);
            //System.out.println(sorter.getSelectedIndex());
            
            if (sorter.getSelectedIndex() >= sorter.getArray().length) {
                sorter.setSelectedIndex(-1);
                timer.cancel();
                return;
            }
            
            //play sound
            //this.playSound(1, NUM_ELEMENTS + 1, sorter.getArray()[sorter.getSelectedIndex()] );
            return;
        }
        
        sorter.step();
        
        //shouldn't have to set the array every tick!
        //window.setArray(bs.getArray());
        //only need to repaint it
        window.repaint();
        
        //play sound
        //this.playSound(1, NUM_ELEMENTS + 1, sorter.getArray()[sorter.getSelectedIndex()] );
    }
    
    /*
    private void playSound(int low, int high, int num) {
        //between 0 and 60 (A1 to A7)
        double n = num * 1.0 / (high - low);
        double note = n * 60;
        int noteNumber = (int) Math.round(note);
        if (noteNumber < 0) noteNumber = 0;
        if (noteNumber > 60) noteNumber = 60;
        
         try {
            InputStream inputStream = new FileInputStream(System.getProperty("user.home") + "\\Desktop\\noteTest\\note" + noteNumber + ".wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
    */
    
    public static int[] shuffleArray(int[] array) {
        Random r = new Random();
        for (int i = array.length - 1; i > 0; i--) {
          int index = r.nextInt(i + 1);
          
          // swap
          int a = array[index];
          array[index] = array[i];
          array[i] = a;
        }
        return array;
    }
    
    public static void main(String[] args) {
        new VisualSorting();
    }
    
    private static int getNoteNumber(int low, int high, int num) {
        //between 0 and 60 (A1 to A7)
        double n = num * 1.0 / (high - low);
        double note = n * 60;
        int noteNumber = (int) Math.round(note);
        if (noteNumber < 0) noteNumber = 0;
        if (noteNumber > 60) noteNumber = 60;
        
        return noteNumber;
    }
    
    private static final int NUM_ELEMENTS = 40;
    
    private static final int CLOCK_SPEED = 30;
    private static final int START_DELAY = 1500;
    
    private boolean doingEndCheck;
    
    private Timer timer;
    private TimerTask timerTask;
    
    private MainFrame window;
    private SteppableSorter sorter;
    
}

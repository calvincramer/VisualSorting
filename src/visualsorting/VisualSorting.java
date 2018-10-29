package visualsorting;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class to show sorting algorithms visualized
 * @author Calvin Cramer
 */
public class VisualSorting {
    
    //number of elements in array
    private static final int NUM_ELEMENTS = 300;
    
    //clock speed tick in ms
    private static final int CLOCK_SPEED = 10;
    //delay from when window opens and when sorting starts, in ms
    private static final int START_DELAY = 1500;
    
    private boolean doingEndCheck;
    
    private Timer timer;
    private TimerTask timerTask;
    
    private MainFrame window;
    private SteppableSorter sorter;
    
    /**
     * Creates the window to show the visual sorting algorithm
     */
    public VisualSorting() {
        //array of numbers to work on
        int[] array = new int[NUM_ELEMENTS];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
        
        //sort array
        array = VisualSorting.shuffleArray(array);
        
        //sorter to be used
        //TODO: better way to specify this?, read at runtime?
        this.sorter = new BubbleSort(array);
        
        //make window
        window = new MainFrame(sorter);
        window.setSorter(sorter);
        window.setVisible(true);
        
        //window update clock
        //timerTask = new RepeatingTimer(this);
        timerTask = new TimerTask() {
            @Override public void run() {
                tick();
            }
        };
        timer = new Timer(true);
        
        //wait a little right after the window pops up
        try {
            Thread.sleep( START_DELAY );
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        //start timer
        timer.scheduleAtFixedRate(timerTask, 0, CLOCK_SPEED); //17ms about 60fps
    }
    
    /**
     * Clock tick to update window and step the algorithm
     */
    public void tick() {
        
        if (sorter.isFinished() && !doingEndCheck) {
            //timer.cancel();
            this.doingEndCheck = true;
            sorter.setSelectedIndicies(new int[]{0});
            sorter.setLastPairSwappedIncedies(null);
            //System.out.println("Done!");
        }
        if (doingEndCheck) {    //sweep from left to right
            window.repaint();
            sorter.setSelectedIndicies(new int[]{sorter.getSelectedIndicies()[0] + 1});
            
            if (sorter.getSelectedIndicies()[0] >= sorter.getArray().length) {
                sorter.setSelectedIndicies(new int[]{-1});
                timer.cancel();
                return;
            }
        }
        else {  //still doing sorting
            sorter.step();
            //only need to repaint it
            window.repaint();
        }
        
        //play sound
        //only playing the sound of the first selected index
        if (sorter.getSelectedIndicies()[0] >= 0 && sorter.getSelectedIndicies()[0] < sorter.getArray().length)
            this.playSound(1, NUM_ELEMENTS + 1, sorter.getArray()[sorter.getSelectedIndicies()[0]] );
    }
    
    /**
     * Plays a piano note
     * @param low
     * @param high
     * @param num 
     */
    private void playSound(int low, int high, int num) {
        MakeSound.playSound(getNoteNumber(low, high, num) + ".wav");
    }
    
    /**
     * Shuffles an array
     * @param array
     * @return 
     */
    public static int[] shuffleArray(int[] array) {
        Random r = new Random(System.currentTimeMillis());
        for (int i = array.length - 1; i > 0; i--) {
          int index = r.nextInt(i + 1);
          
          // swap
          int a = array[index];
          array[index] = array[i];
          array[i] = a;
        }
        return array;
    }
    
    /**
     * Start
     * @param args 
     */
    public static void main(String[] args) {
        new VisualSorting();
    }
    
    /**
     * Calculates the note to be played based upon a value and range
     * @param low - the lowest value that num can be
     * @param high - the highest value that num can be
     * @param num - the number that represents the note to be played
     * @return the note number, inclusively between low and high
     */
    private static int getNoteNumber(int low, int high, int num) {
        //between 0 and 60 (A1 to A7)
        double n = num * 1.0 / (high - low);
        double note = n * 60;
        int noteNumber = (int) Math.round(note);
        if (noteNumber < 0) noteNumber = 0;
        if (noteNumber > 60) noteNumber = 60;
        
        return noteNumber;
    }
}

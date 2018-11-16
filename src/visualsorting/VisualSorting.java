package visualsorting;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class to show sorting algorithms visualized
 * @author Calvin Cramer
 */
public class VisualSorting {
    
    //number of elements in array
    private static final int NUM_ELEMENTS = 32;
    //number of sound files in the specifies sound pack
    private static int NUM_SOUND_FILES = 0;
    
    //clock speed tick in ms
    //0 = as fast as possible
    protected static final int CLOCK_SPEED = 10;
    
    //delay from when window opens and when sorting starts, in ms
    private static final int START_DELAY = 1000;
    
    //sorting algorithm to use
    protected final Class<?> sorterClass = MergeSort.class;
    
    private boolean doingEndCheck;
    
    private Timer timer;
    private TimerTask timerTask;
    
    private MainFrame window;
    private SteppableSorter sorter = null;
    
    private int[] copyArr;
    
    private String soundPack = "piano";
    
    protected static long startTime = -1;
    //protected static long currentTime = -1;
    
    /**
     * Creates the window to show the visual sorting algorithm
     */
    public VisualSorting() {        
        this.init();
        
        //array of numbers to work on
        int[] array = new int[NUM_ELEMENTS];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
        
        //sort array
        array = VisualSorting.shuffleArray(array);
                
        //copy original array
        this.copyArr = new int[array.length];
        for (int i = 0; i < array.length; i++)
            this.copyArr[i] = array[i];
        
        //sorter to be used
        try {
            this.sorter = (SteppableSorter) sorterClass.newInstance();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        if (sorter == null) {
            System.out.println("SORTER WAS NOT CONSTRUCTED");
            System.exit(1);
        }
        sorter.setArray(array);
        
        //make window
        window = new MainFrame(sorter);
        window.setSorter(sorter);
        window.setVisible(true);
        
        //window update clock
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
        VisualSorting.startTime = System.currentTimeMillis();
        //VisualSorting.currentTime = System.currentTimeMillis();
        if (CLOCK_SPEED == 0) {
            while (tick()) {}
        } else {
            timer.scheduleAtFixedRate(timerTask, 0, CLOCK_SPEED); //17ms about 60fps
        }
    }
    
    /**
     * Read all options from file
     */
    private void init() {
        //find number of sound files in the selected soundPack
        //File f = new File("/" + this.soundPack + "/");
        File f = new File(VisualSorting.class.getResource("/soundpacks/" + this.soundPack).getFile());
        this.NUM_SOUND_FILES = f.listFiles().length;
        
        //other init stuff

        
        //checks
        if (CLOCK_SPEED < 0) {
            System.out.println("NEGTIVE CLOCK SPEEDS ARE NOT ALLOWED DUMMY, I CAN'T TIME TRAVEL.\nAlthough it would make sense to undo all of the steps from a sorting algorithm in reverse");
            System.exit(1);
        }
    }
    
    /**
     * Clock tick to update window and step the algorithm
     * @return returns whether the process is complete (false) or is still running (true)
     */
    public boolean tick() {
        
        if (sorter.isFinished() && !doingEndCheck) {
            this.doingEndCheck = true;
            sorter.clearColoredIndices();
            sorter.addColoredIndex(0, sorter.SELECTED_COLOR);
            //sorter.setSelectedIndicies(new int[]{0});
            //sorter.setLastPairSwappedIncedies(null);
            window.repaint();
            return true;
        }

        if (doingEndCheck) {    //sweep from left to right
            int nextIndex = sorter.getColoredIndices().get(0).getKey() + 1;
            sorter.clearColoredIndices();
            if (nextIndex >= sorter.getArray().length) {
                window.repaint();   //or else the last selected index on the right will still be selected
                endProcedure();
                return false;
            }
            sorter.addColoredIndex(nextIndex, sorter.SELECTED_COLOR);
            window.repaint();
        }
        else {  //still doing sorting
            //VisualSorting.currentTime = System.currentTimeMillis();
            sorter.step();
            //only need to repaint it
            window.repaint();
        }
        
        //play sound
        if (sorter.indexToPlaySound() >= 0 && sorter.indexToPlaySound() < sorter.getArray().length) {
            int numberMappedToSoundScale = 
                    (int) (sorter.getArray()[sorter.indexToPlaySound()] * 1.0 * VisualSorting.NUM_SOUND_FILES / sorter.getMax());
            this.playSound(1, NUM_SOUND_FILES, numberMappedToSoundScale);
        }
        return true;
    }
    
    /**
     * When the sorter ends
     */
    public void endProcedure() {
        //clear colors
        
        //sorter.setSelectedIndicies(new int[]{-1});
        timer.cancel();
        
        //check if the array was sorted properly
        System.out.println("Original array: " + toStringArr(this.copyArr));
        System.out.println("Sorted array: " + toStringArr(sorter.array));
        
        Arrays.sort(this.copyArr);
        
        System.out.println("Array.sort() original: " + toStringArr(this.copyArr));

        int numErrors = 0;
        if (this.copyArr.length != sorter.array.length) {
            System.out.println("ERROR: lengths of sorted and original arrays not the same");
            numErrors++;
        }
        for (int i = 0; i < copyArr.length; i++) {
            if (copyArr[i] != sorter.array[i]) {
                System.out.println("ERROR: not sorted properly at index " + i);
                numErrors++;
            }
        }
        
        if (numErrors == 0)
            System.out.println("SORTING SUCCESSFUL");
        else
            System.out.println("NUM ERRORS: " + numErrors);
        
    }
    
    /**
     * Plays a piano note
     * @param low
     * @param high
     * @param num 
     */
    private void playSound(int low, int high, int num) {
        MakeSound.playSound(this.soundPack, getNoteNumber(low, high, num) + ".wav");
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
     * TODO: REPLACE MAGIC NUMBER 60 WITH ACTUAL NUMBER OF NOTE FILES IN THE SELECTED SOUND FOLDER
     */
    private static int getNoteNumber(int low, int high, int num) {
        //between 0 and 60 (A1 to A7)
        double n = num * 1.0 / (high - low);
        double note = n * NUM_SOUND_FILES;
        int noteNumber = (int) Math.round(note);
        if (noteNumber < 1) noteNumber = 1;
        if (noteNumber > NUM_SOUND_FILES) noteNumber = NUM_SOUND_FILES;
        
        return noteNumber;
    }
    
    private static String toStringArr(int[] a) {
        String s = "";
        for (int n : a) {
            s += n + " ";
        }
        return s;
    }
}

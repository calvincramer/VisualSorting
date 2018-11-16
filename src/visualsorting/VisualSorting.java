package visualsorting;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import visualsorting.sorters.*;

/**
 * Main class to show sorting algorithms visualized
 * @author Calvin Cramer
 */
public class VisualSorting {
    
    
    //sorting algorithm to use
    private Class<?> SORTER_CLASS = DEFAULT_SORTER_CLASS;
    
    //number of elements in array
    private int NUM_ELEMENTS = DEFAULT_NUM_ELEMENTS;
    
    //the sound pack to be used (specifies the folder name)
    private String SOUND_PACK = DEFAULT_SOUND_PACK;
    //number of sound files in the specifies sound pack
    private int NUM_SOUND_FILES = 0;
    
    //clock speed tick in ms
    //0 = as fast as possible
    protected int CLOCK_SPEED = DEFAULT_CLOCK_SPEED;
    
    //delay from when window opens and when sorting starts, in ms
    private int START_DELAY = DEFAULT_START_DELAY; 
    
    
    //default options
    private static final Class<?> DEFAULT_SORTER_CLASS = InsertionSort.class;
    private static final int      DEFAULT_NUM_ELEMENTS = 64;
    private static final String   DEFAULT_SOUND_PACK   = "pure";
    private static final int      DEFAULT_CLOCK_SPEED  = 50;
    private static final int      DEFAULT_START_DELAY  = 2000;
    
    //other members
    private boolean doingEndCheck;
    private Timer timer;
    private TimerTask timerTask;
    private MainFrame window;
    private SteppableSorter sorter = null;
    private int[] copyArr;
    protected long startTime = -1;
    
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
            this.sorter = (SteppableSorter) SORTER_CLASS.newInstance();
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
        window = new MainFrame(sorter, this);
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
        startTime = System.currentTimeMillis();
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
        //parse options file
        URL optionsFileURL = VisualSorting.class.getResource("/options.txt");
        if (optionsFileURL != null) {
            File optionsFile = new File(optionsFileURL.getFile());
            String[] options = Util.readFile(optionsFile);
            for (String optionLine : options) {
                //System.out.println(s);
                String[] optionNameValue = optionLine.split("=");
                if (optionNameValue.length != 2)
                    continue;
                String option = optionNameValue[0];
                String optionValue = optionNameValue[1];
                try {
                    switch (option) {
                        case "sorter": {
                            this.SORTER_CLASS = Class.forName("visualsorting.sorters." + optionValue);
                            break;
                        }
                        case "numElements": {
                            this.NUM_ELEMENTS = Integer.parseInt(optionValue);
                            break;
                        }
                        case "soundPack": {
                            this.SOUND_PACK = optionValue;
                            break;
                        }
                        case "clockSpeed": {
                            this.CLOCK_SPEED = Integer.parseInt(optionValue);
                            break;
                        }
                        case "startDelay": {
                            this.START_DELAY = Integer.parseInt(optionValue);
                            break;
                        }
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("COULD NOT FIND THE SORTER WITH NAME: " + optionValue);
                    System.out.println("FOR THE OPTION LINE: " + optionLine);
                    e.printStackTrace();
                    System.exit(1);
                } catch (NumberFormatException e) {
                    System.out.println("THE OPTION VALUE: " + optionValue);
                    System.out.println("IN: " + optionLine + " SHOULD BE AN INTEGER");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } else {
            System.out.println("Options file could not be found, using default options.");
        }
        
        //find number of sound files in the selected soundPack
        URL soundPackURL = VisualSorting.class.getResource("/soundpacks/" + this.SOUND_PACK);
        if (soundPackURL != null) {
            File soundPackFolder = new File(soundPackURL.getFile());
            this.NUM_SOUND_FILES = soundPackFolder.listFiles().length;
        } else {
            System.out.println("COULD NOT FIND THE SOUNDPACKS FROM THE JAR");
            System.out.println("or more specifically, /soundpacks/" + this.SOUND_PACK);
        }
        
        //other init stuff ...

        
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
    /**
     * Clock tick to update window and step the algorithm
     * @return returns whether the process is complete (false) or is still running (true)
     */
    public boolean tick() {
        
        if (sorter.isFinished() && !doingEndCheck) {
            this.doingEndCheck = true;
            sorter.clearColoredIndices();
            sorter.addColoredIndex(0, sorter.SELECTED_COLOR, true);
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
            sorter.addColoredIndex(nextIndex, sorter.SELECTED_COLOR, true);
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
                    (int) (sorter.getArray()[sorter.indexToPlaySound()] * 1.0 * NUM_SOUND_FILES / sorter.getMax());
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
        System.out.println("Original array: " + Util.toStringArr(this.copyArr));
        System.out.println("Sorted array: " + Util.toStringArr(sorter.array));
        
        Arrays.sort(this.copyArr);
        
        System.out.println("Array.sort() original: " + Util.toStringArr(this.copyArr));

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
        Util.playSound(this.SOUND_PACK, getNoteNumber(low, high, num) + ".wav");
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
    private int getNoteNumber(int low, int high, int num) {
        //between 0 and 60 (A1 to A7)
        double n = num * 1.0 / (high - low);
        double note = n * NUM_SOUND_FILES;
        int noteNumber = (int) Math.round(note);
        if (noteNumber < 1) noteNumber = 1;
        if (noteNumber > NUM_SOUND_FILES) noteNumber = NUM_SOUND_FILES;
        
        return noteNumber;
    }
    

    
    
}

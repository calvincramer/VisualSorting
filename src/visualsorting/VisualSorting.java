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
    //private Class<?> SORTER_CLASS = DEFAULT_SORTER_CLASS;
    
    //number of elements in array
    //private int NUM_ELEMENTS = DEFAULT_NUM_ELEMENTS;
    
    //the sound pack to be used (specifies the folder name)
    //private String SOUND_PACK = DEFAULT_SOUND_PACK;
    //number of sound files in the specifies sound pack
    //private int NUM_SOUND_FILES = 0;
    
    //clock speed tick in ms
    //0 = as fast as possible
    //protected int CLOCK_SPEED = DEFAULT_CLOCK_SPEED;
    
    //delay from when window opens and when sorting starts, in ms
    //private int START_DELAY = DEFAULT_START_DELAY; 
    
    //the number of simultaneuous sounds that can be played
    //private int NUM_SIMUL_SOUNDS = DEFAULT_NUM_SIMUL_SOUNDS;
    
    
    //default options
    //private static final Class<?> DEFAULT_SORTER_CLASS = InsertionSort.class;
    //private static final int      DEFAULT_NUM_ELEMENTS = 64;
    //private static final String   DEFAULT_SOUND_PACK   = "pure";
    //private static final int      DEFAULT_CLOCK_SPEED  = 50;
    //private static final int      DEFAULT_START_DELAY  = 2000;
    //private static final int      DEFAULT_NUM_SIMUL_SOUNDS = 1;
    
    //other members
    private boolean doingEndCheck;
    private Timer timer;
    private TimerTask updateSortingStepperTask;
    private TimerTask windowRepaintTask;
    private MainFrame window;
    private Options options;
    private Player player;
    private SteppableSorter sorter = null;
    private int[] copyArr;
    protected long startTime = -1;
    
    /**
     * Creates the window to show the visual sorting algorithm
     */
    public VisualSorting() {        
        this.init();
        
        //start array
        int[] array = StartArrayFactory.generate(options.NUM_ELEMENTS, options.START_ARRAY_STRUCTURE, options.START_ARRAY_NUMBERS_TYPE);
                
        //copy original array
        this.copyArr = new int[array.length];
        for (int i = 0; i < array.length; i++)
            this.copyArr[i] = array[i];
        
        //sorter to be used
        try {
            this.sorter = (SteppableSorter) options.SORTER_CLASS.newInstance();
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
        sorter.setColors(options);
        
        //make window
        this.window = new MainFrame(sorter, this, options);
        this.window.setSorter(sorter);
        this.window.setVisible(true);
        
        //timer tasks
        this.updateSortingStepperTask = new TimerTask() {
            @Override public void run() {
                sortTick();
            }
        };
        this.windowRepaintTask = new TimerTask() {
            @Override
            public void run() {
                windowRepaintTick();
            }
        };
        timer = new Timer(true);
        
        //wait a little right after the window pops up
        try {
            Thread.sleep( options.START_DELAY );
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        //start timer
        startTime = System.currentTimeMillis();
        //VisualSorting.currentTime = System.currentTimeMillis();
        if (options.CLOCK_SPEED == 0) {
            while (sortTick()) {}
        } else {
            timer.scheduleAtFixedRate(windowRepaintTask, 0, 17); //17ms about 60fps
            timer.scheduleAtFixedRate(updateSortingStepperTask, 0, options.CLOCK_SPEED); 
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
            String[] optionLines = Util.readFile(optionsFile);
            this.options = new Options(optionLines);
        } else {
            System.out.println("Options file could not be found, using default options.");
            this.options = new Options();
        }
        
        //find number of sound files in the selected soundPack
        this.player = new Player(options.SOUND_PACK, options.NUM_SIMUL_SOUNDS);
        
        //other init stuff ...

        
        //checks
        if (options.CLOCK_SPEED < 0) {
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
    public boolean sortTick() {
        
        if (sorter.isFinished() && !doingEndCheck) {
            this.doingEndCheck = true;
            sorter.clearColoredIndices();
            sorter.addColoredIndex(0, sorter.SELECTED_COLOR, true);
            //sorter.setSelectedIndicies(new int[]{0});
            //sorter.setLastPairSwappedIncedies(null);
            //window.repaint();
            return true;
        }

        if (doingEndCheck) {    //sweep from left to right
            int nextIndex = sorter.getColoredIndices().get(0).getKey() + 1;
            sorter.clearColoredIndices();
            if (nextIndex >= sorter.getArray().length) {
                //window.repaint();   //or else the last selected index on the right will still be selected
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
            //window.repaint();
        }
        
        //play sound
        if (sorter.indexToPlaySound() >= 0 && sorter.indexToPlaySound() < sorter.getArray().length) {
            player.playSound(1, sorter.getMax(), sorter.getArray()[sorter.indexToPlaySound()]);
        }
        return true;
    }
    
    /**
     * Repaints the window
     */
    public void windowRepaintTick() {
        this.window.repaint();
    }
    
    /**
     * When the sorter ends
     */
    public void endProcedure() {
        timer.cancel();
        this.window.repaint();  //just in case we haven't repainted at the end of the sorting
        
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
}

package visualsorting;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class to show sorting algorithms visualized
 * @author Calvin Cramer
 */
public class VisualSorting {

    //other members
    private boolean doingEndCheck;
    private Timer timer;
    private TimerTask updateSortingStepperTask;
    private TimerTask windowRepaintTask;
    private MainFrame window;
    private Options options;
    private Player player;
    private SteppableSorter<Integer> sorter = null;
    private List<Integer> copyArr;
    protected long startTime;
    protected long currentTime;
    private int numSortTicks = 0;
    private boolean currentlySorting = false;
    
    /**
     * Creates the window to show the visual sorting algorithm
     */
    public VisualSorting() {        
        this.init();
        
        //start array
        Integer[] arrayTemp = StartArrayFactory.generate(options.NUM_ELEMENTS, options.START_ARRAY_STRUCTURE, options.START_ARRAY_NUMBERS_TYPE);
        List<Integer> array = Util.arrayToList(arrayTemp);
        
        //copy original array
        this.copyArr = new ArrayList<>(array);
        //this.copyArr = new Integer[array.length];
        //for (int i = 0; i < array.length; i++)
        //    this.copyArr[i] = array[i];
        
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
        //why is the graph not showing on the laptop? -- because of the Thread.sleep
        
        //create timer tasks
        this.updateSortingStepperTask = new TimerTask() {
            @Override public void run() {
                sortTick();
        }};
        this.windowRepaintTask = new TimerTask() {
            @Override public void run() {
                windowRepaintTick();
        }};
        timer = new Timer(true);
        
        //start timers
        if (options.CLOCK_SPEED == 0) {
            while (sortTick()) {}
        } else {
            timer.scheduleAtFixedRate(windowRepaintTask, 0, 17); //17ms about 60fps
            timer.scheduleAtFixedRate(updateSortingStepperTask, options.START_DELAY, options.CLOCK_SPEED); 
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
        if (this.numSortTicks == 0) {
            this.currentTime = 0;
            this.startTime = System.currentTimeMillis();
            this.currentlySorting = true;
        }
        this.numSortTicks++;
        
        if (sorter.isFinished() && !doingEndCheck) {
            this.doingEndCheck = true;
            this.currentlySorting = false;
            sorter.clearColoredIndices();
            sorter.clearSwapArrows();
            sorter.addColoredIndex(0, sorter.SELECTED_COLOR, true);
            return true;
        }

        if (doingEndCheck) {    //sweep from left to right
            int nextIndex = sorter.getColoredIndices().get(0).getKey()+ 1;
            sorter.clearColoredIndices();
            if (nextIndex >= sorter.getArray().size()) {
                endProcedure();
                return false;
            }
            sorter.addColoredIndex(nextIndex, sorter.SELECTED_COLOR, true);
        }
        else {  //still doing sorting
            sorter.step();
        }
        
        //play sound
        if (sorter.indexToPlaySound() >= 0 && sorter.indexToPlaySound() < sorter.getArray().size()) {
            player.playSound(1, sorter.getMax(), sorter.getArray().get(sorter.indexToPlaySound()));
        }
        return true;
    }
    
    /**
     * Repaints the window
     */
    public void windowRepaintTick() {
        if (this.currentlySorting == true)
            this.currentTime = System.currentTimeMillis() - this.startTime;
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
        System.out.println("Sorted array: " + Util.toStringArr(sorter.getArray()));
        
        //Arrays.sort(this.copyArr);
        Collections.sort(copyArr);
        
        System.out.println("Array.sort() original: " + Util.toStringArr(this.copyArr));

        int numErrors = 0;
        if (copyArr.size() != sorter.getArray().size()) {
            System.out.println("ERROR: lengths of sorted and original arrays not the same");
            numErrors++;
        }
        for (int i = 0; i < copyArr.size(); i++) {
            if (copyArr.get(i).compareTo(sorter.getArray().get(i)) != 0) {
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
     * Start
     * @param args 
     */
    public static void main(String[] args) {
        new VisualSorting();
    }
}

package visualsorting;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import visualsorting.StartArrayFactory.*;

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
        
        //gather options
        Integer num_elements = (Integer) options.getOption("NUM_ELEMENTS").getData();
        NumberType start_array_numbers_type = (NumberType) options.getOption("START_ARRAY_NUMBERS_TYPE").getData();
        ArrayStructure start_array_structure = (ArrayStructure) options.getOption("START_ARRAY_STRUCTURE").getData();
        Class<?> sorter_class = (Class<?>) options.getOption("SORTER_CLASS").getData();
        Integer start_delay = (Integer) options.getOption("START_DELAY").getData();
        Integer clock_speed = (Integer) options.getOption("CLOCK_SPEED").getData();
        
        //start array
        Integer[] arrayTemp = StartArrayFactory.generate(num_elements, start_array_structure, start_array_numbers_type);
        List<Integer> array = Util.arrayToList(arrayTemp);
        
        //copy original array
        this.copyArr = new ArrayList<>(array);
        //this.copyArr = new Integer[array.length];
        //for (int i = 0; i < array.length; i++)
        //    this.copyArr[i] = array[i];
        
        //sorter to be used
        try {
            this.sorter = (SteppableSorter) sorter_class.newInstance();
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
        if (clock_speed == 0) {
            while (sortTick()) {}
        } else {
            timer.scheduleAtFixedRate(windowRepaintTask, 0, 17); //17ms about 60fps
            timer.scheduleAtFixedRate(updateSortingStepperTask, start_delay, clock_speed); 
        }
    }
    
    
    /**
     * Read all options from file
     */
    private void init() {
        //parse options file
        long startReadingOptionsTime = System.currentTimeMillis();
        
        String[] optionLines = Util.readFileFromJar("options.txt");
        if (optionLines == null) {
            System.out.println("Options file could not be found, using default options.");
            this.options = new Options();
        } 
        else {
            this.options = new Options(optionLines);
        }
        
        /*
        URL optionsFileURL = VisualSorting.class.getResource("/options.txt");
        System.out.println("OptionsURL: " + optionsFileURL);
        if (optionsFileURL != null) {
            File optionsFile = new File(optionsFileURL.getFile());
            System.out.println("Option file: " + optionsFile.getAbsolutePath());
            String[] optionLines = Util.readFile(optionsFile);
            this.options = new Options(optionLines);
        } else {
            System.out.println("Options file could not be found, using default options.");
            this.options = new Options();
        }
        */
        System.out.println("Time to init options: " + (System.currentTimeMillis() - startReadingOptionsTime) + "ms");
        
        
        //find number of sound files in the selected soundPack
        long startSoundTime = System.currentTimeMillis();
        String sound_pack = (String) options.getOption("SOUND_PACK").getData();
        Integer num_simul_sounds = (Integer) options.getOption("NUM_SIMUL_SOUNDS").getData();
        this.player = new Player(sound_pack, num_simul_sounds);
        System.out.println("Time to init sounds: " + (System.currentTimeMillis() - startSoundTime) + "ms");
        
        //other init stuff ...

        //checks
        Integer clock_speed = (Integer) options.getOption("CLOCK_SPEED").getData();
        Integer start_delay = (Integer) options.getOption("START_DELAY").getData();
        if (clock_speed < 0 || start_delay < 0) {
            System.err.println("NEGTIVE CLOCK SPEEDS OR DELAYS ARE NOT ALLOWED DUMMY, I CAN'T TIME TRAVEL.\nAlthough it would make sense to undo all of the steps from a sorting algorithm in reverse");
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
            //check for an array
            if (sorter.getArray() == null || sorter.getArray().isEmpty()) {
                endProcedure();
                return false;
            }
            
            int nextIndex = sorter.getColoredIndices().get(0).getKey() + 1;
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
                System.err.println("ERROR: not sorted properly at index " + i);
                numErrors++;
            }
        }
        
        if (numErrors == 0)
            System.out.println("SORTING SUCCESSFUL");
        else
            System.err.println("NUM ERRORS: " + numErrors);
        
    }
       
    
    /**
     * Start
     * @param args 
     */
    public static void main(String[] args) {
        new VisualSorting();
    }
}

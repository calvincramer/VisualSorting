package visualsorting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;

/**
 * Abstract class that represents a sorting algorithm that can be called to incrementally step through the algorithm
 * Note that this class only supports sorting the array once, if you want to redo it then you will need to reinitialize.
 * @author Calvin Cramer
 */
public abstract class SteppableSorter {
    
    public int[] array;
    public int[] sortedFinalArray;

    //public int[] lastSwappedIndicies;
    //public int[] selectedIndicies;
    private List<Pair<Integer, Color>> coloredIndicies;
    private int indexToPlaySound;
    
    public boolean done;
    public long numComparisons;
    public long numSwaps;
    public long numArrayAccesses;
    
    public final Color SELECTED_COLOR   = new Color(0  , 255, 180);
    public final Color SWAP_COLOR_1     = new Color(255, 0  , 255);
    public final Color SWAP_COLOR_2     = new Color(183, 74 , 247);
    public final Color DEFAULT_COLOR    = new Color(0  , 120, 255);
    
    public SteppableSorter(int[] array) {
        this.array = array;
        //this.lastSwappedIndicies = null;
        //this.selectedIndicies = null;
        this.coloredIndicies = new ArrayList<>();
        this.indexToPlaySound = -1;
        this.done = false;
        this.numComparisons = 0;
        this.numSwaps = 0;
        this.numArrayAccesses = 0;
        
        //end goal
        this.sortedFinalArray = new int[array.length];
        for (int i = 0; i < array.length; i++)
            this.sortedFinalArray[i] = array[i];
        Arrays.sort(sortedFinalArray);
    }
    
    /**
     * Step once in the algorithm
     */
    abstract void step();
    
    /**
     * Returns the name of the sorter
     * @return 
     */
    abstract String getSorterName();
    
    /**
     * Gets the array
     * @return 
     */
    public int[] getArray() {
        return array;
    }
    
    /**
     * Adds a color to a specific index
     * If a color is already present at that index, it is replaced with the average of the two colors
     * @param i
     * @param c 
     * @param playSoundHere 
     */
    public void addColoredIndex(int i, Color c, boolean playSoundHere) {
        if (i < 0 || i >= this.array.length) {
            System.err.println("CANT COLOR THE INDEX, IT IS OUT OF RANGE");
            return;
        }
        //search if already present in list
        for (int k = 0; k < this.coloredIndicies.size(); k++) {
            if (this.coloredIndicies.get(k).getKey().equals(i)) {
                Color oldColor = this.coloredIndicies.get(k).getValue();
                Color newColor = new Color(
                        (oldColor.getRed() + c.getRed()) / 2, 
                        (oldColor.getGreen() + c.getGreen()) / 2, 
                        (oldColor.getBlue() + c.getBlue()) / 2);
                this.coloredIndicies.set(k, new Pair<Integer, Color>(this.coloredIndicies.get(k).getKey(), newColor));
                return;      //since we only allow adding of coloredIndicies thru this method, there will be at most 1 match
            }
        }
        
        this.coloredIndicies.add(new Pair<Integer, Color>(i, c));
        
        if (playSoundHere)
            this.indexToPlaySound = i;
    }
    
    /**
     * Adds a color to a specific index
     * If a color is already present at that index, it is replaced with the average of the two colors
     * @param i
     * @param c 
     */
    public void addColoredIndex(int i, Color c) {
        addColoredIndex(i, c, false);
    }
    
    /**
     * Removes all colored indices
     */
    public void clearColoredIndices() {
        this.coloredIndicies.clear();;
    }
    
    /**
     * Removes all colored indices with a specific color
     * @param c 
     */
    public void removeAllColoredIndiciesOf(Color c) {
        for (int i = 0; i < this.coloredIndicies.size(); i++) {
            if (this.coloredIndicies.get(i).getValue().equals(c)) {
                this.coloredIndicies.remove(i);
                i--;
            }
        }
    }
    
    /**
     * Returns the desired color at the index i
     * @param i 
     */
    public Color getColorAt(int i) {
        for (Pair<Integer, Color> p : this.coloredIndicies)
            if (p.getKey() == i)
                return p.getValue();
        return this.DEFAULT_COLOR;
    }

    /**
     * Returns the index whose sound is going to be played
     * @return 
     */
    public int indexToPlaySound() {
        return this.indexToPlaySound;
    }
    
    /**
     * Returns all colored indices
     * @return 
     */
    public List<Pair<Integer, Color>> getColoredIndices() {
        return this.coloredIndicies;
    }
    
    /**
     * Determines if the algorithm is finished.
     * It is finished if the array is sorted exactly as Arrays.sort would sort the original array
     * Optionally (considering a very bad sorting algorithm), a subclass may define its own finishing criteria, before the array is sorted completely
     * @return 
     */
    public boolean isFinished() {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != this.sortedFinalArray[i])
                return false;
        }
        return true;
    }
    
    /**
     * Determines if the algorithm is finished.
     * It is finished if the array is sorted.
     * @return 
     */
    public static boolean isSorted(int[] array) {
        for(int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i+1])
                return false;
        }
        return true;
    }
    
    /**
     * Convenience method to swap two elements in the array  
     * Takes 4 array accesses (getting and setting take 1 each)
     * Adds number of swaps and array accesses to running total
     * @param i
     * @param j 
     */
    protected void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        
        this.numArrayAccesses += 4;
        this.numSwaps++;
    }
    
    /**
     * Convenience method to swap two elements in the array  
     * @param i
     * @param j 
     */
    public static void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /**
     * Convenience method to print an array
     * @param array 
     */
    public static void printArray(int[] array) { 
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ", ");
        }
        System.out.println("\n");
    }

    
    /**
     * Returns the maximum number in the array
     * @return 
     */
    public int getMax() {
        return this.sortedFinalArray[this.sortedFinalArray.length - 1];
    }
}

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
    private List<Pair<Integer, List<Color>>> coloredIndicies;
    private int indexToPlaySound;
    
    public boolean done;
    public long numComparisons;
    public long numSwaps;
    public long numArrayAccesses;
    
    public Color DEFAULT_COLOR;
    public Color SELECTED_COLOR;
    public Color SWAP_COLOR_1;
    public Color SWAP_COLOR_2;

    /**
     * Step once in the algorithm
     */
    protected abstract void step();
    
    /**
     * Returns the name of the sorter
     * @return 
     */
    protected abstract String getSorterName();
    
    /**
     * Gets the array
     * @return 
     */
    public int[] getArray() {
        return array;
    }
    
    /**
     * Sets the array
     * @param arr 
     */
    public void setArray(int[] arr) {
        this.array = arr;
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
     * Sets the default colors according to the options
     * @param options 
     */
    public void setColors(Options options) {
        this.DEFAULT_COLOR = options.DEFAULT_COLOR;
        this.SWAP_COLOR_1 = options.SWAP_COLOR_1;
        this.SWAP_COLOR_2 = options.SWAP_COLOR_2;
        this.SELECTED_COLOR = options.SELECTED_COLOR;
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
            //System.err.println("CANT COLOR THE INDEX, IT IS OUT OF RANGE");
            return;
        }
        //search if already present in list
        for (int k = 0; k < this.coloredIndicies.size(); k++) {
            if (this.coloredIndicies.get(k).getKey().equals(i)) {
                //Color oldColor = this.getColorAt(i);
                //Color newColor = new Color(
                //        (oldColor.getRed() + c.getRed()) / 2, 
                //        (oldColor.getGreen() + c.getGreen()) / 2, 
                //        (oldColor.getBlue() + c.getBlue()) / 2);
                //this.coloredIndicies.set(k, new Pair<Integer, Color>(this.coloredIndicies.get(k).getKey(), newColor));
                this.coloredIndicies.get(k).getValue().add(c);
                return;      //since we only allow adding of coloredIndicies thru this method, there will be at most 1 match
            }
        }
        //start new list for this index
        List<Color> temp = new ArrayList<Color>();
        temp.add(c);
        this.coloredIndicies.add(new Pair<Integer, List<Color>>(i, temp));
        
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
            if (this.coloredIndicies.get(i).getValue().contains(c)) {
                this.coloredIndicies.get(i).getValue().remove(c);
                //remove list if it's empty
                if (this.coloredIndicies.get(i).getValue().size() == 0)
                    this.coloredIndicies.remove(i);
                i--;
            }
        }
    }
    
    /**
     * Returns the list of desired colors at the index i
     * If no desired color is set for the index then the default color is returned
     * @param i
     * @return 
     */
    public List<Color> getColorsAt(int i) {
        for (Pair<Integer, List<Color>> p : this.coloredIndicies)
            if (p.getKey() == i)
                return p.getValue();
        List<Color> temp = new ArrayList<>();
        temp.add(this.DEFAULT_COLOR);
        return temp;
    }
    
    /**
     * Returns the desired color at the index i
     * If the list of colors for this index is larger than 1 then the average color is returned
     * If no desired color is set for the index then the default color is returned
     * @param i 
     */
    public Color getColorAt(int i) {
        for (Pair<Integer, List<Color>> p : this.coloredIndicies)
            if (p != null && p.getKey() == i)
                return getAverageColor(p.getValue());
        return this.DEFAULT_COLOR;
    }
    
    /**
     * Returns the average color from the list
     * If the list is null or empty, null is returned
     * @param colors
     * @return 
     */
    public Color getAverageColor(List<Color> colors) {
        if (colors == null || colors.size() == 0)
            return null;
        else if (colors.size() == 1)
            return colors.get(0);
        
        int totalR = 0;
        int totalG = 0;
        int totalB = 0;
        
        for (Color c : colors) {
            totalR += c.getRed();
            totalG += c.getGreen();
            totalB += c.getBlue();
        }
        return new Color(totalR / colors.size(), totalG / colors.size(), totalB / colors.size());
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
    public List<Pair<Integer, List<Color>>> getColoredIndices() {
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

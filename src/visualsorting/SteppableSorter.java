package visualsorting;

/**
 * Abstract class that represents a sorting algorithm that can be called to incrementally step through the algorithm
 * Note that this class only supports sorting the array once, if you want to redo it then you will need to reinitialize.
 * @author Calvin Cramer
 */
public abstract class SteppableSorter {
    
    public int[] array;
    public int[] lastSwappedIndicies;
    public int[] selectedIndicies;
    public boolean done;
    public long numComparisons;
    public long numSwaps;
    
    public SteppableSorter(int[] array) {
        this.array = array;
        this.lastSwappedIndicies = null;
        this.selectedIndicies = null;
        this.done = false;
        this.numComparisons = 0;
        this.numSwaps = 0;
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
     * Returns the last swapped indices
     * @return 
     */
    public int[] getLastPairSwappedIncedies() {
        return this.lastSwappedIndicies;
    }
    
    /**
     * Determines if the algorithm is finished.
     * It is finished if the array is sorted.
     * Optionally (considering a very bad sorting algorithm), a subclass may define its own finishing criteria, before the array is sorted completely
     * @return 
     */
    public boolean isFinished() {
        for(int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i+1])
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
     * @param i
     * @param j 
     */
    public void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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
    

    int[] getSelectedIndicies() {
        return this.selectedIndicies;
    }
    
    void setSelectedIndicies(int[] indicies) {
        this.selectedIndicies = indicies;
    }
    
    void setLastPairSwappedIncedies(int[] indecies) {
        this.lastSwappedIndicies = indecies;
    }

    public static void printArray(int[] array) { 
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ", ");
        }
        System.out.println("\n");
    }

}

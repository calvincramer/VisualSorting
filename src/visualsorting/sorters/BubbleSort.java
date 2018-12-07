package visualsorting.sorters;

import visualsorting.SteppableSorter;
import visualsorting.Util;

/**
 * A steppable bubble sort algorithm
 * @author Calvin Cramer
 */
public class BubbleSort<T extends Number & Comparable<T>> 
    extends SteppableSorter<T> {
    
    //sorting variables
    int m;
    int i;
    
    @Override
    public void setArray(T[] array) {
        super.setArray(array);
        
        this.m = array.length;
        this.i = 0;
    }
  
    /**
     * Sorts the array in one go
     * For debug purposes
     */
    public void sort() {
        for (int m = array.length; m >= 0; m--)
            for (int i = 0; i < array.length - 1; i++)
                if (array[i].compareTo(array[i+1]) > 0)
                    swap(i, i+1);
    }

    @Override
    protected void step() {
        if (done) return;
        
        this.numComparisons++;
        if (i < array.length - 1) {
            this.numComparisons++;
            this.numArrayAccesses += 2;
            if (array[i].compareTo(array[i + 1]) > 0) {
                swap(i, i + 1);
                this.clearColoredIndiciesOf(this.SWAP_COLOR_1);
                this.addColoredIndex(i, this.SWAP_COLOR_1, false);
                this.addColoredIndex(i+1, this.SWAP_COLOR_1, true);
                this.clearSwapArrowsOf(this.SWAP_COLOR_1);
                this.addSwapArrow(i, i+1, this.SWAP_COLOR_1);
                //this.lastSwappedIndicies = new int[] {i, i+1};
            }
        }
        
        i++;
        this.numComparisons++;
        if (i >= m) {
            i = 0;
            m--;
            this.numComparisons++;
            if (m < 0) {
                done = true;
            }
        }
        this.clearColoredIndiciesOf(this.SELECTED_COLOR);
        this.addColoredIndex(i, this.SELECTED_COLOR, false);
        //this.selectedIndicies = new int[]{i};
    }
    
    @Override
    protected String getSorterName() {
        return "Bubble Sort";
    }
    
    public static void main(String[] args) {
        Integer[] input = new Integer[7];
        for (int i = 1; i <= input.length; i++) {
            input[i - 1] = Math.abs(i - input.length) + 1;
        }
        BubbleSort bs = new BubbleSort();
        bs.setArray(input);
        
        while (!bs.isFinished()) {
            bs.step();
            Util.printArray(bs.array);
            System.out.println();
        }
  
    }
}

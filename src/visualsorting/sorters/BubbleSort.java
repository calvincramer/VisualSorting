package visualsorting.sorters;

import visualsorting.SteppableSorter;

/**
 * A steppable bubble sort algorithm
 * @author Calvin Cramer
 */
public class BubbleSort 
    extends SteppableSorter {
    
    @Override
    public void setArray(int[] array) {
        super.setArray(array);
        
        this.m = array.length;
        this.i = 0;
    }
  
    /**
     * Sorts the array in one go
     * For debug purposes
     */
    public void sort() {

        for (int m = array.length; m >= 0; m--) {
            for (int i = 0; i < array.length - 1; i++) {
                
                if (array[i] > array[i+1]) {
                    swap(i, i+1);
                }
                
            }
        }
    }

    @Override
    protected void step() {
        if (done) return;
        
        this.numComparisons++;
        if (i < array.length - 1) {
            this.numComparisons++;
            this.numArrayAccesses += 2;
            if (array[i] > array[i + 1]) {
                swap(i, i + 1);
                this.removeAllColoredIndiciesOf(this.SWAP_COLOR_1);
                this.addColoredIndex(i, this.SWAP_COLOR_1);
                this.addColoredIndex(i+1, this.SWAP_COLOR_1);
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
        this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
        this.addColoredIndex(i, this.SELECTED_COLOR, true);
        //this.selectedIndicies = new int[]{i};
    }
    
    @Override
    protected String getSorterName() {
        return "Bubble Sort";
    }
    
    public static void main(String[] args) {
        int[] input = new int[7];
        for (int i = 1; i <= input.length; i++) {
            input[i - 1] = Math.abs(i - input.length) + 1;
        }
        BubbleSort bs = new BubbleSort();
        bs.setArray(input);
        
        while (!bs.isFinished()) {
            bs.step();
            SteppableSorter.printArray(bs.array);
            System.out.println();
        }
  
    }
     
    //sorting variables
    int m;
    int i;
}

package visualsorting.sorters;

import visualsorting.SteppableSorter;
import visualsorting.Util;

public class SelectionSort<T extends Number & Comparable<T>>
    extends SteppableSorter<T> {
    
    private int i;
    private int j;
    private int index;

    public SelectionSort() {
        this.i = 0;
        this.j = i + 1;
        this.index = i;
    }
 
    public static int[] doSelectionSort(int[] arr) {
         for (int i = 0; i < arr.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < arr.length; j++)
                if (arr[j] < arr[index])
                    index = j;
      
            int smallerNumber = arr[index]; 
            arr[index] = arr[i];
            arr[i] = smallerNumber;
        }
        return arr;
    }
     
    
    @Override
    protected void step() {
        if (done) return;
        
        this.numComparisons++;
        this.numArrayAccesses += 2;
        if (array[j].compareTo(array[index]) < 0)
            index = j;
        
        j++;
        
        this.numComparisons++;
        if (j >= array.length) {
            //swap
            T smallerNumber = array[index];
            array[index] = array[i];
            array[i] = smallerNumber;
            this.numSwaps++;
            this.numArrayAccesses += 4;
            //this.lastSwappedIndicies = new int[] {index, i};
            this.clearColoredIndiciesOf(this.SWAP_COLOR_1);
            this.addColoredIndex(index, this.SWAP_COLOR_1);
            this.addColoredIndex(i, SWAP_COLOR_1);
            this.clearSwapArrowsOf(this.SWAP_COLOR_1);
            this.addSwapArrow(i, index, this.SWAP_COLOR_1);
            //increment
            i++;
            index = i;
            j = i + 1;
            this.numComparisons++;
            if (i >= array.length - 1) {
                done = true;
            }
        }
        //this.selectedIndicies = new int[]{j};
        this.clearColoredIndiciesOf(this.SELECTED_COLOR);
        this.addColoredIndex(j, this.SELECTED_COLOR, true);
        
    }
    
    @Override
    protected String getSorterName() {
        return "SelectionSort";
    }
    
    public static void main(String args[]) {
         
        Integer[] array = {7,6,5,4,3,2,1};
        array = Util.shuffleArray(array);
        
        SelectionSort ss = new SelectionSort();
        ss.setArray(array);
        
        System.out.println("Initial:");
        Util.printArray(ss.array);
        System.out.println();
        
        while(!ss.done) {
            ss.step();
            Util.printArray(ss.array);
        }
    }
}

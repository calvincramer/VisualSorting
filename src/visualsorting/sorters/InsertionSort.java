package visualsorting.sorters;

import visualsorting.SteppableSorter;
import visualsorting.Util;

public class InsertionSort<T extends Number & Comparable<T>>  
    extends SteppableSorter<T> {

    private int stage = 0;
    private int i;
    private int j;
    
    public InsertionSort() {
        i = 1;
        j = i;
    }
    
    @Override
    protected void step() {
        if (done) return;
        
        switch (stage) {
            case 0:
                j = i;
                stage = 1;
                //break;    //fall through to stage one
            case 1: 
                this.numComparisons++;
                if (j > 0) {
                    this.numComparisons++;
                    this.numArrayAccesses += 2;
                    if (array.get(j-1).compareTo(array.get(j)) > 0) {
                        swap(j, j-1);
                        this.clearColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(j, this.SWAP_COLOR_1, true);
                        this.addColoredIndex(j-1, this.SWAP_COLOR_1, false);
                        this.clearSwapArrowsOf(this.SWAP_COLOR_1);
                        this.addSwapArrow(j, j-1, this.SWAP_COLOR_1);
                        //this.lastSwappedIndicies = new int[] {j, j-1};
                        j--;
                        this.clearColoredIndiciesOf(this.SELECTED_COLOR);
                        this.addColoredIndex(j+1, this.SELECTED_COLOR, true);
                        //this.selectedIndicies = new int[]{j+1};
                    }
                    else {
                        i++;
                        this.clearColoredIndiciesOf(this.SELECTED_COLOR);
                        this.addColoredIndex(i, this.SELECTED_COLOR, true);
                        //this.selectedIndicies = new int[]{i};
                        stage = 0;
                    }
                }
                else {
                    i++;
                    this.clearColoredIndiciesOf(this.SELECTED_COLOR);
                    this.addColoredIndex(i, this.SELECTED_COLOR, true);
                    //this.selectedIndicies = new int[]{i};
                    stage = 0;
                }
                break;
        }
    }

    @Override
    protected String getSorterName() {
        return "InsertionSort";
    }
    
    
    public static Integer[] doInsertionSort(Integer[] arr) {
         int i = 1;
         while (i < arr.length) {
             int j = i;
             while (j > 0 && arr[j-1] > arr[j]) {
                 Util.swap(j, j-1, arr);
                 j--;
             }
             i++;
         }
         return arr;
    }
    
    
    public static void main(String[] args) {
        Integer[] array = {7,6,5,4,3,2,1};
        array = Util.shuffleArray(array);
        array = InsertionSort.doInsertionSort(array);
        Util.printArray(array);
    }

}

package visualsorting.sorters;

import visualsorting.SteppableSorter;
import visualsorting.VisualSorting;

public class InsertionSort 
    extends SteppableSorter {

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
                    if (array[j-1] > array[j]) {
                        swap(j, j-1);
                        this.removeAllColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(j, this.SWAP_COLOR_1, true);
                        this.addColoredIndex(j-1, this.SWAP_COLOR_1, false);
                        //this.lastSwappedIndicies = new int[] {j, j-1};
                        j--;
                        this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
                        this.addColoredIndex(j+1, this.SELECTED_COLOR, true);
                        //this.selectedIndicies = new int[]{j+1};
                    }
                    else {
                        i++;
                        this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
                        this.addColoredIndex(i, this.SELECTED_COLOR, true);
                        //this.selectedIndicies = new int[]{i};
                        stage = 0;
                    }
                }
                else {
                    i++;
                    this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
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
    
    
    public static int[] doInsertionSort(int[] arr) {
         int i = 1;
         while (i < arr.length) {
             int j = i;
             while (j > 0 && arr[j-1] > arr[j]) {
                 SteppableSorter.swap(j, j-1, arr);
                 j--;
             }
             i++;
         }
         return arr;
    }
    
    
    public static void main(String[] args) {
        int[] array = {7,6,5,4,3,2,1};
        array = VisualSorting.shuffleArray(array);
        array = InsertionSort.doInsertionSort(array);
        SteppableSorter.printArray(array);
    }

}

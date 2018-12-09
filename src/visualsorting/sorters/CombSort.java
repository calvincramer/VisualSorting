package visualsorting.sorters;

import java.util.List;
import visualsorting.SteppableSorter;
import visualsorting.Util;

public class CombSort<T extends Number & Comparable<T>> 
    extends SteppableSorter<T> {

    private int gap;
    private double shrink;
    private int i;
    private int stage;
    
    
    public CombSort() {
        //this.gap = arr.length;
        this.shrink = 1.3;
        this.i = 0;
        this.stage = 0;
    }
    
    
    @Override 
    public void setArray(List<T> arr) {
        super.setArray(arr);
        this.gap = arr.size();
    }
    
    
    @Override
    protected void step() {
        if (done)   return;

        switch (stage) {
            case 0:
                gap = (int) Math.floor(gap / shrink);
                if (gap > 1)
                    this.numComparisons++;
                else
                    gap = 1;
                i = 0;
                this.clearColoredIndiciesOf(this.SELECTED_COLOR);
                this.addColoredIndex(i, this.SELECTED_COLOR, true);
                //this.selectedIndicies = new int[]{i};
                stage = 1;
                break;
            case 1:
                this.numComparisons++;
                if (i + gap < array.size()) {
                    this.numComparisons++;
                    this.numArrayAccesses += 2;
                    if (array.get(i).compareTo(array.get(i + gap)) > 0) {
                        swap(i, i + gap);
                        
                        this.clearColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(i, this.SWAP_COLOR_1);
                        this.addColoredIndex(i + gap, this.SWAP_COLOR_1);
                        this.clearSwapArrowsOf(this.SWAP_COLOR_1);
                        this.addSwapArrow(i, i + gap, this.SWAP_COLOR_1);
                        //this.lastSwappedIndicies = new int[] {i, i + gap};
                    }
                    i++;
                    this.clearColoredIndiciesOf(this.SELECTED_COLOR);
                    this.addColoredIndex(i, this.SELECTED_COLOR, true);     //only play the left one
                    this.addColoredIndex(i + gap, this.SELECTED_COLOR, false);
                    //this.selectedIndicies = new int[]{i, i + gap};
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
        return "CombSort";
    }
    
    public static Integer[] doCombSort(Integer[] arr) {
        int gap = arr.length;
        double shrink = 1.3;
        boolean sorted = false;
        
        while (!sorted) {
            gap = (int) Math.floor(gap / shrink);
            if (gap > 1) {
                sorted = false; //not sorted yet
            } 
            else {
                gap = 1;
                sorted = true;
            }
            
            int i = 0;
            while (i + gap < arr.length) {
                if (arr[i] > arr[i + gap]) {
                    Util.swap(i, i + gap, arr);
                    //sorted is false
                    sorted = false;
                }
                i++;
            }
        }
        return arr;
    }
    
    
    public static void main(String[] args) {
        Integer[] array = {7,6,5,4,3,2,1};
        array = Util.shuffleArray(array);
        array = CombSort.doCombSort(array);
        Util.printArray(array);
    }
}

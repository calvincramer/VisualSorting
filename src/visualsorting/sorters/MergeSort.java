package visualsorting.sorters;

import java.util.List;
import java.util.ArrayList;
import visualsorting.SteppableSorter;
import visualsorting.Util;

/**
 * Credit https://en.wikipedia.org/wiki/Merge_sort#Top-down_implementation
 * @author Calvin
 */
public class MergeSort<T extends Number & Comparable<T>>
    extends SteppableSorter<T> {

    private List<T> copyArr;
    private List<Pair> intervals;
    private int state;
    private Pair currentInterval;
    private int currentMiddle;
    private int i;
    private int j;
    private int k;
    
    
    @Override
    public void setArray(List<T> arr) {
        super.setArray(arr);
        
        this.state = 0;
        this.copyArr = new ArrayList<>(arr);
        
        //make all split calls
        intervals = new ArrayList<>();
        addIntervals(intervals, 0, arr.size());
        //for (Pair p : intervals)
        //    System.out.println(p);
        
        this.state = 0;
    }
    
    
    private void addIntervals(List<Pair> collect, int left, int right) {
        if (right - left < 2)
            return;
        
        int middle = (left + right) / 2;
        addIntervals(collect, left, middle);
        addIntervals(collect, middle, right);
        
        collect.add(new Pair(left, right));
    }
    
    
    @Override
    protected void step() {
        switch (state) {
            case 0:
                if (!this.intervals.isEmpty() || this.intervals == null) {
                    this.currentInterval = this.intervals.remove(0);
                    this.currentMiddle = (currentInterval.left + currentInterval.right) / 2; 
                    this.i = currentInterval.left;
                    this.j = currentMiddle;
                    this.k = currentInterval.left;
                    this.state = 1;
                    //copy array into copy so we can merge successfully
                    this.copyArr = new ArrayList<>(array);
                    this.numArrayAccesses += array.size() * 2;  //copy whole array requires 2x accesses (one get one set)
                }
                else {
                    return; //should be done with algorithm
                }
                //return; fall thru
            case 1:
                if (k < currentInterval.right) {
                    if (i < currentMiddle && (j >= currentInterval.right || lteCheck(arrayAccessCheck(copyArr, i), arrayAccessCheck(copyArr, j))) ) {
                        array.set(k, copyArr.get(i));
                        this.numArrayAccesses++;
                        this.clearColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(k, this.SWAP_COLOR_1, true);
                        this.addColoredIndex(i, this.SWAP_COLOR_1);
                        this.clearSwapArrowsOf(this.SWAP_COLOR_1);
                        this.addSwapArrow(k, i, this.SWAP_COLOR_1);
                        //this.lastSwappedIndicies = new int[]{k,i};
                        i++;
                    }
                    else {
                        array.set(k, copyArr.get(j));
                        this.numArrayAccesses++;
                        this.clearColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(k, this.SWAP_COLOR_1, true);
                        this.addColoredIndex(j, this.SWAP_COLOR_1);
                        this.clearSwapArrowsOf(this.SWAP_COLOR_1);
                        this.addSwapArrow(k, j, this.SWAP_COLOR_1);
                        //this.lastSwappedIndicies = new int[]{k,j};
                        j++;
                    }
                    this.clearColoredIndiciesOf(this.SELECTED_COLOR);
                    this.addColoredIndex(k, this.SELECTED_COLOR, true);
                    //this.selectedIndicies = new int[]{k};
                    k++;
                }
                else {
                    state = 0;
                }
                return;
        }   
    }
    

    private boolean lteCheck(T a, T b) {
        this.numComparisons++;
        return a.compareTo(b) <= 0;
    }
    

    private T arrayAccessCheck(List<T> array, int index) {
        this.numArrayAccesses++;
        return array.get(index);
    }

    
    @Override
    protected String getSorterName() {
        return "MergeSort";
    }
    
    
    private class Pair {
        int left;
        int right;
        
        public Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }
        
        @Override
        public String toString() {
            return "(" + left + ", " + right + ")";
        }
    }
    

    private static void printArr(int[] a) {
        for (int n : a) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
    

    public static void main(String[] args) {
        List<Integer> arr = Util.oneLineInitList(new Integer[]{8,7,6,5,4,3,2,1});
        MergeSort ms = new MergeSort();
        ms.setArray(arr);
        
        while (!ms.isFinished()) {
            Util.printArray(ms.array);
            Util.printArray(ms.copyArr);
            ms.step();
            
            System.out.println();
        }
        Util.printArray(ms.array);
            Util.printArray(ms.copyArr);
    }
}

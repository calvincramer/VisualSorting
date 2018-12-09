package visualsorting.sorters;

import java.util.List;
import visualsorting.SteppableSorter;
import visualsorting.Util;

/**
 * Credit to Wikipedia for the algorithm:
 * https://en.wikipedia.org/wiki/Heapsort
 * And also 
 * https://johnderinger.wordpress.com/2012/12/28/heapify/
 * for the siftDown method since Wikipedia doesn't does something weird 
 * @author Calvin
 */
public class HeapSort<T extends Number & Comparable<T>>  
    extends SteppableSorter<T> {

    private int stage;
    private int start;
    private int end;
    private int siftStart;
    private int siftEnd;
    private int returnToStage;
    private int root;
    
    
    public HeapSort() {
        this.stage = 0;
    }
    

    public int iParent(int i) {
        return (int) ((i-1) / 2);
    }
    
    
    public int iLeftChild(int i) {
        return 2*i + 1;
    }
    
    
    public int iRightChild(int i) {
        return 2*i + 2;
    }
    

    public void heapSort(T[] arr) {
        heapify(arr);
        int end = arr.length - 1;
        while (end > 0) {
            //swap
            T temp = arr[end];
            arr[end] = arr[0];
            arr[0] = temp;
            
            end--;
            
            siftDown(arr, 0, end);
        }
    }
    

    public void heapify(T[] arr) {
        int start = iParent(arr.length - 1);
        while (start >= 0) {
            siftDown(arr, start, arr.length - 1);
            start--;
        }
    }
    

    public void siftDown(T[] arr, int start, int end) {
        int root = start;
        while (iLeftChild(root) <= end) {
            int child = iLeftChild(root);
            
            if (child + 1 <= end && arr[child].compareTo(arr[child + 1]) < 0) 
                child = child + 1;

            if (arr[root].compareTo(arr[child]) < 0) {
                T temp = arr[root];
                arr[root] = arr[child];
                arr[child] = temp;

                root = child;
            } else
                return;
        }
    }

    
    @Override
    protected void step() {
        if (stage == 0) {   //heapify init
            start = iParent(array.size() - 1);
            stage = 1;
        }
        if (stage == 1) {   //heapify loop
            this.numComparisons++;
            if (start >= 0) {
                siftStart = start;
                siftEnd = array.size() - 1;
                returnToStage = 1;
                stage = 4;
                start--;
            }
            else {
                stage = 2;
                start = -1;
            }
            
        }
        if (stage == 2) {   //heapsort while loop init
            end = array.size() - 1;
            stage = 3;
        }
        if (stage == 3) {   //heapsort while loop
            this.numComparisons++;
            if (end > 0) {
                this.swap(0, end);

                this.clearColoredIndiciesOf(this.SWAP_COLOR_1);
                this.addColoredIndex(end, this.SWAP_COLOR_1, true);
                this.addColoredIndex(0, this.SWAP_COLOR_1);
                this.clearSwapArrowsOf(this.SWAP_COLOR_1);
                this.addSwapArrow(end, 0, this.SWAP_COLOR_1);

                end--;
                siftStart = 0;
                siftEnd = end;
                returnToStage = 3;
                stage = 4;
                return; //let another tick go by
            }
            else {
                //done
            }
        }
        if (stage == 4) {   //sift down init
            root = siftStart;
            stage = 5;
        }
        if (stage == 5) {
            this.numComparisons++;
            if (iLeftChild(root) <= siftEnd) {
                int child = iLeftChild(root);
                
                this.numComparisons++;
                if (child + 1 <= siftEnd) {
                    this.numComparisons++;
                    this.numArrayAccesses += 2;
                    if (array.get(child).compareTo(array.get(child + 1)) < 0) 
                       child = child + 1;
                }
                
                this.numComparisons++;
                this.numArrayAccesses += 2;
                if (array.get(root).compareTo(array.get(child)) < 0) {
                    this.swap(root, child);
                    
                    this.clearColoredIndiciesOf(this.SWAP_COLOR_2);
                    this.addColoredIndex(root, this.SWAP_COLOR_2, true);
                    this.addColoredIndex(child, this.SWAP_COLOR_2);
                    this.clearSwapArrowsOf(this.SWAP_COLOR_2);
                    this.addSwapArrow(root, child, this.SWAP_COLOR_2);

                    root = child;
                } else { //done with sift down
                    stage = returnToStage;
                }
            } else {
                stage = returnToStage;
            }
            
        }
    }

    
    @Override
    protected String getSorterName() {
        return "HeapSort";
    }
    
    
    public static void main(String[] args) {
        Integer[] arr = {1,5,3,7,5,9,56,3,9,7,4,2,9,6,3,89,6};
        //List<Integer> arr = Util.oneLineInitList(new Integer[]{1,5,3,7,5,9,56,3,9,7,4,2,9,6,3,89,6});
        Util.printArray(arr);
        
        HeapSort hs = new HeapSort();
        hs.heapSort(arr);

        Util.printArray(arr);
    }
}

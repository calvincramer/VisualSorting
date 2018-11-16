package visualsorting;

/**
 * Credit to Wikipedia for the algorithm:
 * https://en.wikipedia.org/wiki/Heapsort
 * And also 
 * https://johnderinger.wordpress.com/2012/12/28/heapify/
 * for the siftDown method since Wikipedia doesn't does something weird 
 * @author Calvin
 */
public class HeapSort 
    extends SteppableSorter {

    private int stage;
    
    public int iParent(int i) {
        return (int) ((i-1) / 2);
    }
    public int iLeftChild(int i) {
        return 2*i + 1;
    }
    public int iRightChild(int i) {
        return 2*i + 2;
    }
    
    public void heapSort(int[] arr) {
        heapify(arr);
        int end = arr.length - 1;
        while (end > 0) {
            //swap
            int temp = arr[end];
            arr[end] = arr[0];
            arr[0] = temp;
            
            end--;
            
            siftDown(arr, 0, end);
        }
    }
    
    public void heapify(int[] arr) {
        int start = iParent(arr.length - 1);
        while (start >= 0) {
            siftDown(arr, start, arr.length - 1);
            start--;
        }
    }
    
    public void siftDown(int[] arr, int start, int end) {
        int root = start;
        while (iLeftChild(root) <= end) {
            int child = iLeftChild(root);
            
            if (child + 1 <= end && arr[child] < arr[child + 1]) 
                child = child + 1;

            if (arr[root] < arr[child]) {
                int temp = arr[root];
                arr[root] = arr[child];
                arr[child] = temp;

                root = child;
            } else
                return;
        }
    }

    @Override
    void step() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getSorterName() {
        return "HeapSort";
    }
    
    
    public static void main(String[] args) {
        int[] arr = {1,5,3,7,5,9,56,3,9,7,4,2,9,6,3,89,6};
        SteppableSorter.printArray(arr);
        
        HeapSort hs = new HeapSort();
        hs.setArray(arr);
        hs.heapSort(arr);

        SteppableSorter.printArray(arr);
    }
}

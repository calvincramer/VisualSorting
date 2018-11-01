package visualsorting;

public class SelectionSort 
    extends SteppableSorter {

    public SelectionSort(int[] array) {
        super(array);
        
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
    void step() {
        if (done) return;
        
        if (array[j] < array[index])
            index = j;
        
        j++;
        
        if (j >= array.length) {
            //swap
            int smallerNumber = array[index];
            array[index] = array[i];
            array[i] = smallerNumber;
            this.lastSwappedIndicies = new int[] {index, i};
            //increment
            i++;
            index = i;
            j = i + 1;
            if (i >= array.length - 1) {
                done = true;
            }
        }
        this.selectedIndicies = new int[]{j};
        
    }
    
    @Override
    String getSorterName() {
        return "SelectionSort";
    }
    
    public static void main(String args[]) {
         
        int[] array = {7,6,5,4,3,2,1};
        array = VisualSorting.shuffleArray(array);
        
        SelectionSort ss = new SelectionSort(array);
        
        System.out.println("Initial:");
        SteppableSorter.printArray(ss.array);
        System.out.println();
        
        while(!ss.done) {
            ss.step();
            SteppableSorter.printArray(ss.array);
        }
        
    }
    
    private int i;
    private int j;
    private int index;
}

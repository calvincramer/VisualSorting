package visualsorting;

public class BubbleSort 
    extends SteppableSorter {
    
    public BubbleSort(int[] array) {
        super(array);
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
                    swapNumbers(i, i+1);
                }
                
            }
        }
    }

    @Override
    public void step() {
        if (done) return;
        
        if (i < array.length - 1 && array[i] > array[i + 1]) {
            swapNumbers(i, i + 1);
            this.lastSwappedIndecies = new int[] {i, i+1};
        }
        
        i++;
        if (i >= m) {
            i = 0;
            m--;
            if (m < 0) {
                done = true;
            }
        }
        this.selectedIndex = i;
    }
    
    public static void main(String[] args) {
        int[] input = new int[7];
        for (int i = 1; i <= input.length; i++) {
            input[i - 1] = Math.abs(i - input.length) + 1;
        }
        BubbleSort bs = new BubbleSort(input);
        
        while (!bs.isFinished()) {
            bs.step();
            bs.printNumbers();
            System.out.println();
        }
  
    }
     
    //sorting variables
    int m;
    int i;

}

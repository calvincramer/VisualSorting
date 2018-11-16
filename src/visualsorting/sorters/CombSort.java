package visualsorting;

public class CombSort 
    extends SteppableSorter {

    private int gap;
    private double shrink;
    private boolean sorted;
    private int i;
    
    private int stage;
    
    public CombSort() {
        //this.gap = arr.length;
        this.shrink = 1.3;
        this.sorted = false;
        this.i = 0;
        this.stage = 0;
    }
    
    @Override 
    public void setArray(int[] arr) {
        super.setArray(arr);
        this.gap = arr.length;
    }
    
    @Override
    void step() {
        if (done)   return;

        switch (stage) {
            case 0:
                gap = (int) Math.floor(gap / shrink);
                if (gap > 1) {
                    sorted = false; //not sorted yet
                    this.numComparisons++;
                } 
                else {
                    gap = 1;
                    sorted = true;
                }
                i = 0;
                this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
                this.addColoredIndex(i, this.SELECTED_COLOR);
                //this.selectedIndicies = new int[]{i};
                stage = 1;
                break;
            case 1:
                this.numComparisons++;
                if (i + gap < array.length) {
                    this.numComparisons++;
                    this.numArrayAccesses += 2;
                    if (array[i] > array[i + gap]) {
                        swap(i, i + gap);
                        
                        this.removeAllColoredIndiciesOf(this.SWAP_COLOR_1);
                        this.addColoredIndex(i, this.SWAP_COLOR_1);
                        this.addColoredIndex(i + gap, this.SWAP_COLOR_1);
                        //this.lastSwappedIndicies = new int[] {i, i + gap};
                        //sorted is false
                        sorted = false;
                    }
                    i++;
                    this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
                    this.addColoredIndex(i, this.SELECTED_COLOR);
                    this.addColoredIndex(i + gap, this.SELECTED_COLOR);
                    //this.selectedIndicies = new int[]{i, i + gap};
                }
                else {
                    i++;
                    this.removeAllColoredIndiciesOf(this.SELECTED_COLOR);
                    this.addColoredIndex(i, this.SELECTED_COLOR);
                    //this.selectedIndicies = new int[]{i};
                    stage = 0;
                }
                break;
                
        }
 
    }

    @Override
    String getSorterName() {
        return "CombSort";
    }
    
    
    public static int[] doCombSort(int[] arr) {
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
                    SteppableSorter.swap(i, i + gap, arr);
                    //sorted is false
                    sorted = false;
                }
                i++;
            }
        }
        return arr;
    }
    
    public static void main(String[] args) {
        int[] array = {7,6,5,4,3,2,1};
        array = VisualSorting.shuffleArray(array);
        array = CombSort.doCombSort(array);
        SteppableSorter.printArray(array);
    }

}
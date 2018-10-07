package visualsorting;

public abstract class SteppableSorter {
    
    public int[] array;
    public int[] lastSwappedIndecies;
    public int selectedIndex;
    public boolean done;
    
    public SteppableSorter(int[] array) {
        this.array = array;
        this.lastSwappedIndecies = null;
        this.selectedIndex = -1;
        this.done = false;
    }
    
    abstract void step();
    
    public int[] getArray() {
        return array;
    }
    
    public int[] getLastPairSwappedIncedies() {
        return this.lastSwappedIndecies;
    }
    
    public boolean isFinished() {
        for(int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i+1])
                return false;
        }
        return true;
    }
    
    public void swapNumbers(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    int getSelectedIndex() {
        return this.selectedIndex;
    }
    
    void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }
    
    void setLastPairSwappedIncedies(int[] indecies) {
        this.lastSwappedIndecies = indecies;
    }

    public void printNumbers() { 
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ", ");
        }
        System.out.println("\n");
    }

}

package visualsorting;

import java.util.TimerTask;

public class RepeatingTimer extends TimerTask{
 
    public RepeatingTimer(VisualSorting manager) {
        this.manager = manager;
    }
    
    @Override
    public void run() {
        manager.tick();
    }
    
    private VisualSorting manager;
    
}
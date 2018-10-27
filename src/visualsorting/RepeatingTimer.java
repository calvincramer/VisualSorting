package visualsorting;

import java.util.TimerTask;

/**
 * Repeating timer for the window tick
 * Not used anymore. Better to use an annonymous class.
 * @author Calvin
 */
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
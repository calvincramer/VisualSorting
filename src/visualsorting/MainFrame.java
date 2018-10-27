package visualsorting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;

/**
 * Visual of a steppable sorter
 * @author Calvin Cramer
 */
public class MainFrame extends JFrame{
    
    public MainFrame(SteppableSorter sorter) {
        
        this.init(sorter.getSorterName());
        
        this.setSorter(sorter);
        
    }
    
    /**
     * Initializes the frames components
     * @param sorterMethod 
     */
    private void init(String sorterMethod) {
        
        this.setTitle("Visual Sorting!" + " - " + sorterMethod);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        
        this.setBounds(50, 50, width - 100, height - 100);
        
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentMoved( ComponentEvent e ) {
                        //System.out.println("I moved!");
            }
            @Override
            public void componentShown( ComponentEvent e ) {}
            @Override
            public void componentHidden( ComponentEvent e ) {}
            @Override
            public void componentResized( ComponentEvent e ) {
                //System.out.println("I got resized!");
                frameResized();
            }
        });
        
    }
    
    @Override
    public void update(Graphics g) {
        
    }
    
    private void frameResized() {
        //makes sure the off screen buffer is updated to the new size of the frame
        if (offScreenImage == null) return;
        
        if (offScreenImage.getWidth(null) < this.getWidth() || offScreenImage.getHeight(null) < this.getHeight()) {
            //System.out.println("New offscreen");
            offScreenImage = this.createImage(this.getWidth(), this.getHeight());
            offScreen = offScreenImage.getGraphics();
        }
    }

    @Override
    public void paint(Graphics g) {
        
        if (sorter == null) {
            System.out.println("null sorter in call to paint window");
            return;
        }
        
        if (this.offScreenImage == null || this.offScreen == null) {
            offScreenImage = this.createImage(this.getWidth(), this.getHeight());
            offScreen = offScreenImage.getGraphics();
        }

        //clearing the off screen buffer
        offScreen.setColor(Color.BLACK);
        offScreen.fillRect(0, 0, offScreenImage.getWidth(null), offScreenImage.getHeight(null));
        
        //getting the array from the sorter to draw
        int[] array = sorter.getArray();
        
        //set up math
        int graphWidth = this.getWidth() - this.getInsets().left - this.getInsets().right - GRAPH_INSETS.left - GRAPH_INSETS.right;
        int graphHeight = this.getHeight() - this.getInsets().top - this.getInsets().bottom - GRAPH_INSETS.top - GRAPH_INSETS.bottom;
        int columnWidth = (graphWidth / array.length ) - NUMBER_PADDING;
        int actualGraphWidth = (array.length * columnWidth) + (array.length - 1) * NUMBER_PADDING;
        int widthDifference = graphWidth - actualGraphWidth;
        
        int x = GRAPH_INSETS.left + this.getInsets().left + (widthDifference / 2);
        int y = this.getHeight() - this.getInsets().bottom - GRAPH_INSETS.bottom;
        
        
        //drawing the array
        for (int i = 0; i < array.length; i++) {
            int height = (int) ( (array[i] * 1.0 / highestNum) * graphHeight ) ;
            //System.out.println(i + " " + height);
            
            if (contains(i, sorter.getSelectedIndicies())) {
                offScreen.setColor(SELECTED_NUMBER_COLOR);
            } else if (sorter.getLastPairSwappedIncedies() != null && (sorter.getLastPairSwappedIncedies()[0] == i || sorter.getLastPairSwappedIncedies()[1] == i) ) {
                offScreen.setColor(SWAPPED_NUMBER_COLOR);
            } else {
                offScreen.setColor(NUMBER_COLOR);
            }
            
            offScreen.fillRect(x, y - height, columnWidth, height);
            
            x += columnWidth + NUMBER_PADDING;
        }

        g.drawImage(offScreenImage, 0, 0, null);
    }
    
    public final void setSorter(SteppableSorter sorter) {
        this.sorter = sorter;
        
        int[] array = sorter.getArray();
        
        this.highestNum = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] > highestNum)
                highestNum = array[i];
        }
        
    }
    
    
    private boolean contains(int i, int[] arr) {
        if (arr == null)
            return false;
        for (int n : arr) {
            if (i == n)
                return true;
        }
        return false;
    }
    
    private SteppableSorter sorter;

    private int highestNum;
    private Graphics offScreen;
    private Image offScreenImage;
    
    private static final Color BACKGROUND_COLOR = new Color(20,20,20);
    private static final Color NUMBER_COLOR = new Color(0, 120, 255);
    private static final Color SELECTED_NUMBER_COLOR = new Color(0, 255, 180);
    private static final Color SWAPPED_NUMBER_COLOR = new Color(255,0,255);
    private static final int NUMBER_PADDING = 3;
    private static final Insets GRAPH_INSETS = new Insets(15,15,15,15);
    
}

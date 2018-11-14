package visualsorting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;

/**
 * Visual of a steppable sorter
 * @author Calvin Cramer
 */
public class MainFrame extends JFrame{
    
    private static final Font MONO = new Font("Courier New", Font.PLAIN, 16);
    
    private SteppableSorter sorter;

    private int highestNum;
    private Graphics2D offScreen;
    private Image offScreenImage;
    
    private static final Color BACKGROUND_COLOR = new Color(20,20,20);
    private static final Color NUMBER_COLOR = new Color(0, 120, 255);
    private static final Color SELECTED_NUMBER_COLOR = new Color(0, 255, 180);
    private static final Color SWAPPED_NUMBER_COLOR = new Color(255,0,255);
    private static final double NUMBER_PADDING = 0.8;
    private static final Insets GRAPH_INSETS = new Insets(15,15,15,15);
    
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
            createOffScreen();
        }
    }
    
    private void createOffScreen() {
        offScreenImage = this.createImage(this.getWidth(), this.getHeight());
        offScreen = (Graphics2D) offScreenImage.getGraphics();
        offScreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        offScreen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        offScreen.setFont(MONO);
    }

    @Override
    public void paint(Graphics g) {
        
        if (sorter == null) {
            System.out.println("null sorter in call to paint window");
            return;
        }
        
        if (this.offScreenImage == null || this.offScreen == null) {
            createOffScreen();
        }

        //clearing the off screen buffer
        offScreen.setColor(Color.BLACK);
        offScreen.fillRect(0, 0, offScreenImage.getWidth(null), offScreenImage.getHeight(null));
        
        //getting the array from the sorter to draw
        int[] array = sorter.getArray();
        
        //set up math
        int graphWidth = this.getWidth() - this.getInsets().left - this.getInsets().right - GRAPH_INSETS.left - GRAPH_INSETS.right;
        int graphHeight = this.getHeight() - this.getInsets().top - this.getInsets().bottom - GRAPH_INSETS.top - GRAPH_INSETS.bottom;
        

        double columnWidth = (graphWidth * 1.0 / array.length ) - NUMBER_PADDING;        
        double x = GRAPH_INSETS.left + this.getInsets().left;
        double y = this.getHeight() - this.getInsets().bottom - GRAPH_INSETS.bottom;
          
        //drawing the array
        Rectangle2D.Double tempRect = new Rectangle2D.Double();
        for (int i = 0; i < array.length; i++) {
            int height = (int) ( (array[i] * 1.0 / highestNum) * graphHeight ) ;
            
            if (contains(i, sorter.getSelectedIndicies()))
                offScreen.setColor(SELECTED_NUMBER_COLOR);
            else if (sorter.getLastPairSwappedIncedies() != null && (sorter.getLastPairSwappedIncedies()[0] == i || sorter.getLastPairSwappedIncedies()[1] == i) )
                offScreen.setColor(SWAPPED_NUMBER_COLOR);
            else
                offScreen.setColor(NUMBER_COLOR);
            
            //offScreen.fillRect(x, y - height, (int) columnWidth, height);
            tempRect.setRect(x, y - height, columnWidth, height);
            offScreen.fill(tempRect);
            
            x += columnWidth + NUMBER_PADDING;
        }
        
        //testing liens
        offScreen.setColor(Color.PINK);
        offScreen.drawLine(0, 0, 100000, 100000);
        offScreen.drawLine(0, this.getHeight(), this.getHeight(), 0);
        offScreen.setColor(Color.MAGENTA);
        offScreen.drawLine(this.getWidth(), 0, this.getWidth() - this.getHeight(), this.getHeight());
        offScreen.drawLine(this.getWidth(), this.getHeight(), this.getWidth() - this.getHeight(), 0);
        
        //draw text for info
        y = 50;
        x = 15;
        int textHeight = offScreen.getFontMetrics().getHeight();
        
        offScreen.setColor(Color.WHITE);
        offScreen.drawString("Name: " + sorter.getSorterName(), (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Size: " + sorter.array.length, (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Comparisons: " + sorter.numComparisons, (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Swaps: " + sorter.numSwaps, (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Array Accesses: " + sorter.numArrayAccesses, (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Clock Speed: " + VisualSorting.CLOCK_SPEED + "ms", (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Time Elapsed: " + commifyString("" + (System.currentTimeMillis() - VisualSorting.startTime)) + "ms", (int) x, (int) y);
        
        g.drawImage(offScreenImage, 0, 0, null);
    }
    
    /**
     * Places commas in a string according to where they would be in an integer number
     * @param numStr
     * @return 
     */
    public String commifyString(String numStr) {
        int i = numStr.length() - 3;
        while (i >= 1) {
            numStr = numStr.substring(0, i) + "," + numStr.substring(i);
            i -= 3;
        }
        return numStr;
    }
    
    /**
     * Sets the sorter, which is the algorithm that sorts the array.
     * @param sorter  the sorter
     */
    public final void setSorter(SteppableSorter sorter) {
        this.sorter = sorter;
        
        int[] array = sorter.getArray();
        
        this.highestNum = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] > highestNum)
                highestNum = array[i];
        }
        
    }
    
    /**
     * Determines if the array contains the number i
     * @param i
     * @param arr
     * @return 
     */
    private boolean contains(int i, int[] arr) {
        if (arr == null)
            return false;
        for (int n : arr) {
            if (i == n)
                return true;
        }
        return false;
    }
}

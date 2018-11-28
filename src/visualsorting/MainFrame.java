package visualsorting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;

/**
 * Visual of a steppable sorter
 * @author Calvin Cramer
 */
public class MainFrame extends JFrame{
    
    //private static final Font MONO = new Font("Courier New", Font.PLAIN, 16);
    
    private SteppableSorter sorter;
    private VisualSorting vs;
    private Options options;

    private int highestNum;
    private Graphics2D offScreen;
    private Image offScreenImage;
    
    private double graphWidth;
    private double graphHeight;
    private double columnWidth;
    //private static final Color BACKGROUND_COLOR = new Color(20,20,20);

    //private static final double NUMBER_PADDING = 0.0;
    //private static final Insets GRAPH_INSETS = new Insets(15,15,15,15);
    
    public MainFrame(SteppableSorter sorter, VisualSorting vs, Options options) {
        this.init(sorter.getSorterName());
        this.setSorter(sorter);
        this.vs = vs;
        this.options = options;
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
        
        //redo some math
        this.graphWidth = this.getWidth() - this.getInsets().left - this.getInsets().right - options.GRAPH_INSETS.left - options.GRAPH_INSETS.right;
        this.graphHeight = this.getHeight() - this.getInsets().top - this.getInsets().bottom - options.GRAPH_INSETS.top - options.GRAPH_INSETS.bottom;
        this.columnWidth = (graphWidth * 1.0 / sorter.getArray().length ) - options.GAP_WIDTH;    
    }
    
    private void createOffScreen() {
        offScreenImage = this.createImage(this.getWidth(), this.getHeight());
        offScreen = (Graphics2D) offScreenImage.getGraphics();
        if (options.ANTI_ALIAS)
            offScreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        else 
            offScreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        
        if (options.ANTI_ALIAS_FONT)
            offScreen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        else 
            offScreen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        offScreen.setFont(new Font(options.FONT_FAMILY, Font.PLAIN, options.FONT_SIZE));
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
        offScreen.setColor(options.BACKGROUND_COLOR);
        offScreen.fillRect(0, 0, offScreenImage.getWidth(null), offScreenImage.getHeight(null));
        
        //getting the array from the sorter to draw
        int[] array = sorter.getArray();
        
        //set up math
        //double y = this.getHeight() - this.getInsets().bottom - options.GRAPH_INSETS.bottom;
        
        //drawing the array
        Rectangle2D.Double tempRect = new Rectangle2D.Double();
        for (int i = 0; i < array.length; i++) {
            //set color
            offScreen.setColor(sorter.getColorAt(i));
            tempRect.setRect(getX(i), getTop(i), columnWidth, getHeight(i));
            offScreen.fill(tempRect);
            
        }
        double x;
        //draw swap lines
        if (options.SHOW_SWAP_ARROWS) {
            for (Triplet<Integer, Integer, Color> p : this.sorter.getSwapIndicies()) {
                offScreen.setColor(p.getThird());
                
                double x1 = getX(p.getFirst()) + columnWidth / 2;
                double y1 = getTop(p.getFirst());
                double x2 = getX(p.getSecond()) + columnWidth / 2;
                double y2 = getTop(p.getSecond());
                
                this.drawSwapLine(x1, y1, 
                        x2, y2, 
                        (x1 + x2) / 2,
                        Math.min(y1, y2 ) - this.getHeight() * 0.1);
            }
        }
        
        //testing lines
        /*
        offScreen.setColor(Color.PINK);
        offScreen.drawLine(0, 0, 100000, 100000);
        offScreen.drawLine(0, this.getHeight(), this.getHeight(), 0);
        offScreen.setColor(Color.MAGENTA);
        offScreen.drawLine(this.getWidth(), 0, this.getWidth() - this.getHeight(), this.getHeight());
        offScreen.drawLine(this.getWidth(), this.getHeight(), this.getWidth() - this.getHeight(), 0);
        */
        
        //draw text for info
        int textHeight = offScreen.getFontMetrics().getHeight();
        double y = textHeight + 30;
        x = 15;
        
        offScreen.setColor(options.TEXT_COLOR);
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
        offScreen.drawString("Clock Speed: " + options.CLOCK_SPEED + "ms", (int) x, (int) y);
        y += textHeight;
        if (vs.startTime == -1)
            offScreen.drawString("Time Elapsed: 0", (int) x, (int) y);
        else
            offScreen.drawString("Time Elapsed: " + Util.commifyString("" + (System.currentTimeMillis() - vs.startTime)) + "ms", (int) x, (int) y);
        
        g.drawImage(offScreenImage, 0, 0, null);
    }
    
    /**
     * Calculates the leftmost coordinate of the index'th number
     * @param index
     * @return 
     */
    private double getX(int index) {
        return options.GRAPH_INSETS.left + this.getInsets().left + index * (columnWidth + options.GAP_WIDTH);
    }
    
    private double getTop(int index) {
        return this.getHeight() - this.getInsets().bottom - options.GRAPH_INSETS.bottom - (sorter.array[index] * 1.0 / highestNum) * graphHeight;
    }
    
    private double getHeight(int index) {
        return (sorter.array[index] * 1.0 / highestNum) * graphHeight;
    }
    
    /**
     * Draws a swap arrow between the two points
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    public void drawSwapLine(double x1, double y1, double x2, double y2, double cntrX, double cntrY) {
        CubicCurve2D.Double path = new CubicCurve2D.Double(
                x1, y1,     //first point
                cntrX, cntrY,     //control point 1
                cntrX, cntrY,     //control point 2
                x2, y2);    //second point
        offScreen.draw(path);
        
        //TODO
        //draw arrows
        //get slope of each point
        //arrow length = min(barWidth, 5)
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
}

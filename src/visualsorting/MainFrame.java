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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.JFrame;

/**
 * Visual of a steppable sorter
 * @author Calvin Cramer
 */
public class MainFrame extends JFrame{
    
    private SteppableSorter sorter;
    private VisualSorting vs;
    private final Options options;
    private int highestNum;
    private Graphics2D offScreen;
    private Image offScreenImage;
    private double graphWidth;
    private double graphHeight;
    private double columnWidth;
    
    public MainFrame(SteppableSorter sorter, VisualSorting vs, Options options) {
        this.init(sorter.getSorterName());
        this.setSorter(sorter);
        this.vs = vs;
        this.options = options;
        this.frameResized();
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
                frameResized();
            }
        });
        
    }
    
    
    @Override
    public void update(Graphics g) {
        
    }
    
    
    private void frameResized() {
        //makes sure the off screen buffer is updated to the new size of the frame
        //try to create off screen image
        if (offScreenImage == null) 
            createOffScreen();
        //if still not able, give up
        if (offScreenImage == null)
            return;
        
        if (offScreenImage.getWidth(null) < this.getWidth() || offScreenImage.getHeight(null) < this.getHeight())
            createOffScreen();
        
        
        //redo some math
        Insets insets = (Insets) options.getOption("GRAPH_INSETS").getData();
        Double gap_width = (Double) options.getOption("GAP_WIDTH").getData();
        this.graphWidth = this.getWidth() - this.getInsets().left - this.getInsets().right - insets.left - insets.right;
        this.graphHeight = this.getHeight() - this.getInsets().top - this.getInsets().bottom - insets.top - insets.bottom;
        this.columnWidth = (graphWidth * 1.0 / sorter.getArray().length ) - gap_width;    
    }
    
    
    private void createOffScreen() {
        Boolean anti_alias = (Boolean) options.getOption("ANTI_ALIAS").getData();
        Boolean anti_alias_font = (Boolean) options.getOption("ANTI_ALIAS_FONT").getData();
        String font_family = (String) options.getOption("FONT_FAMILY").getData();
        Integer font_size = (Integer) options.getOption("FONT_SIZE").getData();
        
        offScreenImage = this.createImage(this.getWidth(), this.getHeight());
        if (this.offScreenImage == null)
            return; //couldn't create offscreen image, due to frame not visible yet?
        offScreen = (Graphics2D) offScreenImage.getGraphics();
        if (anti_alias)
            offScreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            offScreen.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            offScreen.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            offScreen.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            offScreen.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        }
        else {
            offScreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            
        }
        
        if (anti_alias_font)
            offScreen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        else 
            offScreen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        offScreen.setFont(new Font(font_family, Font.PLAIN, font_size));
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
        
        //gather options
        Color background_color = (Color) options.getOption("BACKGROUND_COLOR").getData();
        Boolean show_swap_arrows = (Boolean) options.getOption("SHOW_SWAP_ARROWS").getData();
        Color text_color = (Color) options.getOption("TEXT_COLOR").getData();
        Integer clock_speed = (Integer) options.getOption("CLOCK_SPEED").getData();

        //clearing the off screen buffer
        offScreen.setColor(background_color);
        offScreen.fillRect(0, 0, offScreenImage.getWidth(null), offScreenImage.getHeight(null));
        
        //getting the array from the sorter to draw
        //convert to double
        List<Number> array = sorter.getArray();
        
        //set up math
        //double y = this.getHeight() - this.getInsets().bottom - options.GRAPH_INSETS.bottom;
        
        //drawing the array
        Rectangle2D.Double tempRect = new Rectangle2D.Double();
        for (int i = 0; i < array.size(); i++) {
            //set color
            offScreen.setColor(sorter.getColorAt(i));
            tempRect.setRect(getX(i), getTop(i), columnWidth, getHeight(i));
            offScreen.fill(tempRect);
            
        }
        double x;
        //draw swap lines
        if (show_swap_arrows) {
            for (Triplet<Integer, Integer, Color> p : this.sorter.getSwapIndicies()) {
                offScreen.setColor(p.getThird());
                
                if (p.getFirst() > p.getSecond())   //first is the lowest x value
                    p = new Triplet(p.getSecond(), p.getFirst(), p.getThird());
                else if (p.getFirst() == p.getSecond()) //dont draw arrows pointing to the same place
                    continue;
                
                double x1 = getX(p.getFirst()) + columnWidth / 2;
                double y1 = getTop(p.getFirst());
                double x2 = getX(p.getSecond()) + columnWidth / 2;
                double y2 = getTop(p.getSecond());
                double width = Math.abs(x2 - x1);
                double height = Math.abs(x2 - x1);
                double top = Math.min(y1, y2) - height;
                
                
                this.drawSwapLine(x1, y1, 
                        x2, y2, 
                        x1 + width / 4, top,
                        x2 - width / 4, top);
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
        
        offScreen.setColor(text_color);
        offScreen.drawString("Name: " + sorter.getSorterName(), (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Size: " + sorter.getArray().size(), (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Comparisons: " + sorter.numComparisons, (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Swaps: " + sorter.numSwaps, (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Array Accesses: " + sorter.numArrayAccesses, (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Clock Speed: " + clock_speed + "ms", (int) x, (int) y);
        y += textHeight;
        offScreen.drawString("Time Elapsed: " + Util.commifyString("" + vs.currentTime) + "ms", (int) x, (int) y);
        
        g.drawImage(offScreenImage, 0, 0, null);
    }
    
    
    /**
     * Calculates the leftmost coordinate of the index'th number
     * @param index
     * @return 
     */
    private double getX(int index) {
        Insets insets = (Insets) options.getOption("GRAPH_INSETS").getData();
        Double gap_width = (Double) options.getOption("GAP_WIDTH").getData();
        return insets.left + this.getInsets().left + index * (columnWidth + gap_width);
    }
    
    
    private double getTop(int index) {
        Insets insets = (Insets) options.getOption("GRAPH_INSETS").getData();
        return this.getHeight() - this.getInsets().bottom - insets.bottom - (sorter.array[index] * 1.0 / highestNum) * graphHeight;
    }
    
     
    /**
     * Calculates the height of the number at a specific index
     * The height corresponds to the visual hight on the window
     * @param index
     * @return 
     */
    private double getHeight(int index) {
        List<Number> s = sorter.getArray();
        return (s.get(index).doubleValue() / sorter.getMax().doubleValue()) * graphHeight;
    }
    
    
    /**
     * Draws a swap arrow between the two points
     * Credit to: https://stackoverflow.com/questions/15620590/polygons-with-double-coordinates
     * for double polygons
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    public void drawSwapLine(double x1, double y1, double x2, double y2, double cntrX1, double cntrY1, double cntrX2, double cntrY2) {
        offScreen.setColor(options.SWAP_ARROW_COLOR);
        
        CubicCurve2D.Double path = new CubicCurve2D.Double(
                x1, y1,     //first point
                cntrX1, cntrY1,     //control point 1
                cntrX2, cntrY2,     //control point 2
                x2, y2);    //second point
        offScreen.draw(path);
        
        //TODO: MAKE THESE AN OPTION
        double lineLength = Math.min(15, columnWidth);
        double arrowAngleHalf = Math.PI / 11;
        //draw arrows
        //first arrow
        double slopeStart = this.getSlopeOfCubicBezierCurve(0.0, path);
        double slopeStartLeft  = slopeStart - arrowAngleHalf;
        double slopeStartRight = slopeStart + arrowAngleHalf;
        Point2D.Double startLeft  = new Point2D.Double(x1 + lineLength * Math.cos(slopeStartLeft), y1 + lineLength * Math.sin(slopeStartLeft));
        Point2D.Double startRight = new Point2D.Double(x1 + lineLength * Math.cos(slopeStartRight), y1 + lineLength * Math.sin(slopeStartRight));
        
        Path2D.Double arrowStart = new Path2D.Double();
        arrowStart.moveTo(x1, y1);
        arrowStart.lineTo(startLeft.x, startLeft.y);
        arrowStart.lineTo(startRight.x, startRight.y);
        arrowStart.closePath();
        offScreen.fill(arrowStart);
        
        //second arrow
        double slopeEnd = this.getSlopeOfCubicBezierCurve(1.0, path);
        slopeEnd += Math.PI;
        double slopeEndLeft  = slopeEnd - arrowAngleHalf;
        double slopeEndRight = slopeEnd + arrowAngleHalf;
        Point2D.Double endLeft  = new Point2D.Double(x2 + lineLength * Math.cos(slopeEndLeft), y2 + lineLength * Math.sin(slopeEndLeft));
        Point2D.Double endRight = new Point2D.Double(x2 + lineLength * Math.cos(slopeEndRight), y2 + lineLength * Math.sin(slopeEndRight));
        
        Path2D.Double arrowEnd = new Path2D.Double();
        arrowEnd.moveTo(x2, y2);
        arrowEnd.lineTo(endLeft.x, endLeft.y);
        arrowEnd.lineTo(endRight.x, endRight.y);
        arrowEnd.closePath();
        offScreen.fill(arrowEnd);
    }
    
    
    /**
     * Calculates the slope of a cubic Bezier curve at a point in time along its curve
     * time = 0 corresponds to the start point, time = 1 corresponds to the end point
     * @param time
     * @param curve
     * @return 
     */
    public double getSlopeOfCubicBezierCurve(double time, CubicCurve2D curve) {
        if (time < 0.0)
            return Double.NaN;
        if (time > 1.0)
            return Double.NaN;
        
        double dx = 3 * Math.pow((1-time), 2) * (curve.getCtrlX1() - curve.getX1())
                + 6 * (1-time) * time * (curve.getCtrlX2() - curve.getCtrlX1())
                + 3 * time * time * (curve.getX2() - curve.getCtrlX2());
        double dy = 3 * Math.pow((1-time), 2) * (curve.getCtrlY1() - curve.getY1())
                + 6 * (1-time) * time * (curve.getCtrlY2() - curve.getCtrlY1())
                + 3 * time * time * (curve.getY2() - curve.getCtrlY2());
        
        return Math.atan2(dy, dx);
    }
    
    /**
     * Sets the sorter, which is the algorithm that sorts the array.
     * @param sorter  the sorter
     */
    public final void setSorter(SteppableSorter sorter) {
        this.sorter = sorter;
    }
}

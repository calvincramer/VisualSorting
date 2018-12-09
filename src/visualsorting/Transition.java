package visualsorting;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * A transition for a shape between two points
 * @author CalvinLaptop
 */
public class Transition {
    private RectangularShape shape;
    private Point2D.Double startPoint;
    private Point2D.Double currentPoint;
    private Point2D.Double endPoint;
    private double totalTransitionTime;
    private double currentTime;
    private boolean transitionDone;
    private TransitionType transitionType;
    
    public enum TransitionType {
        LINEAR, SIGMOID, TANH, INVERSE, INVERSEINVERSE
    }
    
    
    public Transition(Point2D.Double start, Point2D.Double end, double transitionTime, RectangularShape shape, TransitionType transitionType) {
        this.startPoint = start;
        this.currentPoint = (Point2D.Double) this.startPoint.clone();
        this.endPoint = end;
        this.totalTransitionTime = transitionTime;
        
        this.shape = shape;
        this.currentTime = 0.0;
        this.transitionType = transitionType;
        
        if (this.currentTime <= 0)
            this.transitionDone = true;
        else 
            this.transitionDone = false;
    }
    
    
    //set shape to final location
    public void finalizeTransition() {
        this.currentPoint = this.endPoint;
        this.transitionDone = true;
    }
    
    
    //step in time
    public void step(double timeElapsed) {
        if (this.transitionDone)
            return;
        
        //do a step
        
    }
    
    
    //draw shape to graphics
    public void draw(Graphics2D graphics) {
        graphics.draw(this.shape);
    }   
}

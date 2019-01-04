import acm.program.*;
import acm.graphics.*;
import java.awt.*;
/**
 *
 * Basit Balogun
 * 10/05/2017
 * A class for the blackhole that would be used in the SpaceTravel game 
 */
public class BlackHole extends GCompound implements Runnable {
    // constants
    private static final double DELAY = 20;
    //instance variables
    private SpaceTravel game; //the main game 
    private double  angle,size,coreSize;
    private int colorComp;
    boolean gameIsOver ;

    /** constructor to create the blackhole*/
    public BlackHole( double angle, SpaceTravel game) {
        //save the parameters used for the black hole as instance variables
        this.game = game;
        this.angle = angle;
        //instance variable 
        size = 320;
        colorComp = 0;
        double coreSize = 100;

        /**create a loop that create coincentric circles with the condition being 
         *while size(size of each circle) is greater than core size, which each circle having a  
         *slightly different shade of gray
         */
        while( size > coreSize) {

            GOval circle = new GOval(size,size);
            circle.setFillColor(new Color(colorComp,colorComp,colorComp));
            circle.setColor(new Color(colorComp,colorComp,colorComp));
            circle.setFilled(true);
            add(circle,-size/2,-size/2);
            size = size - 10;
            colorComp = colorComp + 10;
        }
        
        //use GOval to draw the core of the blackhole
        GOval blackHoleCore = new GOval (coreSize,coreSize);
        blackHoleCore.setFillColor(Color.BLACK);
        blackHoleCore.setFilled(true);
        add(blackHoleCore,-coreSize/2,-coreSize/2);
        
        
        //add blackhole using png image
        // GImage bHole = new GImage("blackhole.png");
        // bHole.setSize(-size/2,-size/2);
        // add(bHole,0,0);
        
    }    

    /** the method which animates the blackhole*/
    public void run(){
        //if gameIsOver is false, animate the blackHoles
        while (gameIsOver == false) {
            oneTimeStep();
            pause(DELAY);
            gameIsOver = game.checkStatus();
        }   
    }
    
    /** return size*/
    public double getHoleSize(){
    return size;
    }
    
    /** return the current angle */
    public double getAngle() {
        return angle;
    }

    /** set the angle */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    // in each time step, move the ball and bounce if hit the wall
    private void oneTimeStep() {   
        movePolar(1, angle); // move the ball
        game.checkCollision(this);//check of blackHole collides with the boundary in SpaceTravel Class 
    }
}
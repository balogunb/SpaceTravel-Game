import acm.graphics.*;
import java.awt.*;
import acm.util.*;
import java.util.*;
import acm.program.*;

/**
 *
 * Basit Balogun
 * 10/05/2017
 */
public class SpaceTravel extends GraphicsProgram {

    // initial size of the window
    public static int 
    APPLICATION_WIDTH = 1100,
    APPLICATION_HEIGHT = 720;

    //instance variables 
    private GOval icePlanet;
    private GOval firePlanet;
    private GOval ironPlanet;
    BlackHole blackHole1;
    BlackHole blackHole2;
    private boolean isDragging = false;
    public boolean gameIsOver = false ;
    private GLabel destination;
    private GLabel youWon;
    private GLabel startGame;
    private GPoint lastPoint;
    int starLimit;
    private int gameWidth = 1100;//width of the game 
    private int gameHeight = 700;//height of the game 
    private RandomGenerator rand = RandomGenerator.getInstance();
    double planetSize = 60;

    /** the run method, control the animation */
    public void run() {
        drawGraphics(); //draw the initial graphics 
        addMouseListeners();
    }

    public boolean drawGraphics() {

        //draw background including stars, using random generator to generate stars 
        // draw background 
        GImage background = new GImage("background.jpg");
        background.setSize(1100,720);
        add(background,0,0);
        starLimit = 700;
        setBackground(Color.BLACK);

        while ( starLimit-- > 0)  {
            GRect star = new GRect(rand.nextDouble(0,gameWidth),//random horizontal point
                    rand.nextDouble(0,gameHeight),//random vertical point
                    1,1);
            star.setColor(rand.nextColor());
            star.setFilled(true);
            add(star);
        }

        //draw icePlanet, firePlanet and ironPlanet using GOval
        icePlanet = new GOval(20,20,planetSize,planetSize);
        icePlanet.setColor(new Color(166,166,255));
        icePlanet.setFilled(true);
        add(icePlanet);

        ironPlanet = new GOval(20,icePlanet.getY()+planetSize*1.2,planetSize,planetSize);
        ironPlanet.setColor(new Color(150,150,150));
        ironPlanet.setFilled(true);
        add(ironPlanet);

        firePlanet = new GOval(20,ironPlanet.getY()+planetSize*1.2,planetSize,planetSize);
        firePlanet.setColor(new Color(229,0,0));
        firePlanet.setFilled(true);
        add(firePlanet);

        //draws blackholes using  black hole costructor 
        blackHole1 = new BlackHole(rand.nextDouble(0,360)//random angle 
        ,this);
        add(blackHole1,800,390);
        new Thread(blackHole1).start();

        blackHole2 = new BlackHole(rand.nextDouble(0,360)//random angle 
        ,this);
        add(blackHole2,700,200);
        new Thread(blackHole2).start();

        //create label for the destination to be reach for the game to be won 
        destination = new GLabel("Destination");
        destination.setColor(Color.WHITE);
        destination.setFont(new Font("SANS_SERIF", Font.BOLD,20));
        add(destination,gameWidth -destination.getWidth(),gameHeight/2 -destination.getHeight()/2);

        //create labels youWon and startGame  
        youWon = new GLabel("You Won!!!");
        youWon.setColor(Color.BLUE);
        youWon.setFont(new Font("SANS_SERIF", Font.BOLD,40));
        youWon.setVisible(false);
        add(youWon,gameWidth/2 -youWon.getWidth()/2,gameHeight/2 -youWon.getHeight()/2);

        startGame = new GLabel("Click to start new game ");
        startGame.setColor(Color.WHITE);
        startGame.setFont(new Font("SANS_SERIF", Font.BOLD,20));
        add(startGame,50,600);
        startGame.setVisible(true);

        return gameIsOver = true;
    }

    /** handles the mouse event when mouse is pressed */
    public void mousePressed(GPoint point) {

        //checks if mouse Pressed inside of of the icePlanet
        if (icePlanet.contains(point)){//icePlanet is pressed on
            isDragging = true; 
            lastPoint = point; 
        }
        else{
            isDragging = false;
        }

        //if gameIsOver, return game to the initial display and label youWon and startGame visiblities to false
        if(gameIsOver) {
            removeAll();
            drawGraphics();
            youWon.setVisible(false);
            startGame.setVisible(false);
        }
        gameIsOver = false; //if mouse is clicked set gameIsOver to false to restart the game 
    }

    /** handles the movement of the ice planet with the mouse  */
    public void mouseDragged(GPoint point) {
        //checks if gameover is true
        if (gameIsOver == true){
            isDragging = false;//if gameover is true isDragging is false 
        }

        //if isDragging is true move icePlanet with mouse
        if (isDragging ){
            icePlanet.move(point.getX()-lastPoint.getX(),point.getY()-lastPoint.getY());
            lastPoint = point;
            push();
            while ( starLimit-- > 0)  {
                GRect star = new GRect(rand.nextDouble(0,gameWidth),//random horizontal point
                        rand.nextDouble(0,gameHeight),//random vertical point
                        1,1);
                star.setColor(rand.nextColor());
                star.setFilled(true);
                add(star);
            }
        }
    }

    /**handles the planets' pushing */
    private void push() {
        //calculate overlap
        double distanceIceIron = GMath.distance(icePlanet.getX()+planetSize/2, icePlanet.getY()+planetSize/2,
                ironPlanet.getX()+planetSize/2, ironPlanet.getY()+planetSize/2);

        double angleIceIron =GMath.angle(icePlanet.getX()+planetSize/2, icePlanet.getY()+planetSize/2,
                ironPlanet.getX()+planetSize/2, ironPlanet.getY()+planetSize/2);

        double overlapIceIron = planetSize -  distanceIceIron; 

        double distanceIronFire = GMath.distance(ironPlanet.getX()+planetSize/2, ironPlanet.getY()+planetSize/2,
                firePlanet.getX()+planetSize/2, firePlanet.getY()+planetSize/2);

        double angleIronFire = GMath.angle(ironPlanet.getX()+planetSize/2, ironPlanet.getY()+planetSize/2,
                firePlanet.getX()+planetSize/2, firePlanet.getY()+planetSize/2);

        double overlapIronFire = planetSize - distanceIronFire;

        double distanceIceFire = GMath.distance(icePlanet.getX()+planetSize/2, icePlanet.getY()+planetSize/2,
                firePlanet.getX()+planetSize/2, firePlanet.getY()+planetSize/2);

        double overlapIceFire = planetSize - distanceIceFire;

        //check if icePlanet overlaps ironPlanet, if true, push ironPlanet away
        if(overlapIceIron > 0){ //two planets collide
            ironPlanet.movePolar(overlapIceIron, angleIceIron);
        }

        //check if ironPlanet overlaps firePlanet, if true,push firePlanet away
        if(overlapIronFire > 0){ //two planets collide
            firePlanet.movePolar(overlapIronFire, angleIronFire);
        }      
    }

    /**Handles blackhole's collision with the wall  */
    public void checkCollision(BlackHole hole){
        //instant variable 
        double angle = hole.getAngle();//gets angle of the blackHole
        double size = hole.getWidth();//gets the diameter of the blackhole

        //if blackhole hits right boundary 
        if (hole.getX() + size/2 > gameWidth){
            hole.setLocation(gameWidth - size/2,hole.getY() ); // move to right edge 
            hole.setAngle(180 -angle); //reflect along the y axis 
        }

        //if blackHole hits left boundary
        if (hole.getX()- size/2  < 0){
            hole.setLocation( size/2,hole.getY() ); // move to left edge 
            hole.setAngle(180 -angle); //reflect along the y axis 
        }

        //if blackHole hits bottom boundary
        if (hole.getY() + size/2 > gameHeight){
            hole.setLocation(hole.getX(),gameHeight - size/2);//move to bottom edge 
            hole.setAngle(-angle);//reflect along the x axis 
        }

        //if blackHole hits top boundary 
        if (hole.getY()- size/2  < 0){
            hole.setLocation(hole.getX(), size/2 );//move to top edge 
            hole.setAngle(-angle);//reflect along the x axis 
        }

    }

    /** checks for scenarios that would make the game over*/
    public boolean checkStatus() {
        //instant variable
        double holeSize = 320;//the diameter of a blackHole


        //calculates overlap 
        // double distanceIceFire = GMath.distance(icePlanet.getX()+planetSize/2, icePlanet.getY()+planetSize/2,
        // firePlanet.getX()+planetSize/2, firePlanet.getY()+planetSize/2);
        // double overlapIceFire = planetSize - distanceIceFire;
        // double distanceIceHole1 = GMath.distance(icePlanet.getX()+planetSize/2, icePlanet.getY()+planetSize/2,
        // blackHole1.getX(), blackHole1.getY());
        // double overlapIceHole1 = holeSize/2 + planetSize/2 - distanceIceHole1;
        // double distanceIceHole2 = GMath.distance(icePlanet.getX()+planetSize/2, icePlanet.getY()+planetSize/2,
        // blackHole2.getX(), blackHole2.getY());
        // double overlapIceHole2 = holeSize/2 + planetSize/2 - distanceIceHole2;
        // double distanceFireHole1 = GMath.distance(firePlanet.getX()+planetSize/2, firePlanet.getY()+planetSize/2,
        // blackHole1.getX(), blackHole1.getY());
        // double overlapFireHole1 = holeSize/2 + planetSize/2 - distanceFireHole1;
        // double distanceFireHole2 = GMath.distance(firePlanet.getX()+planetSize/2, firePlanet.getY()+planetSize/2,
        // blackHole2.getX(), blackHole2.getY());
        // double overlapFireHole2 = holeSize/2 + planetSize/2 - distanceFireHole2;
        // double distanceIronHole1 = GMath.distance(ironPlanet.getX()+planetSize/2, ironPlanet.getY()+planetSize/2,
        // blackHole1.getX(), blackHole1.getY());
        // double overlapIronHole1 = holeSize/2 + planetSize/2 - distanceIronHole1;
        // double distanceIronHole2 = GMath.distance(ironPlanet.getX()+planetSize/2, ironPlanet.getY()+planetSize/2,
        // blackHole2.getX(), blackHole2.getY());
        // double overlapIronHole2 = holeSize/2 + planetSize/2 - distanceIronHole2;

        //if firePlanet overlaps destination set gameIsOver to true and labels "you won" and "start game" to visible
        if(firePlanet.getBounds().intersects(destination.getBounds())){
            gameIsOver = true;
            youWon.setVisible(true);
            startGame.setVisible(true);
        }

        //if icePlanet overlaps firePlanet, set gameIsOver to true and label "startGame" to visible   
        if(icePlanet.getBounds().intersects(firePlanet.getBounds())){
            gameIsOver = true;
            startGame.setVisible(true);
            remove(icePlanet);       
        }


        //if icePlanet overlaps blackHole1, set gameIsOver to true and label "startGame" to visible
        if(icePlanet.getBounds().intersects(blackHole1.getBounds())){
            gameIsOver = true;
            startGame.setVisible(true);
            remove(icePlanet);       
        }


        //if icePlanet overlaps blackHole2, set gameIsOver to true and label "startGame" to 
        if(icePlanet.getBounds().intersects(blackHole2.getBounds())){
            gameIsOver = true;
            startGame.setVisible(true);
            remove(icePlanet);       
        }


        //if firePlanet overlaps blackHole1, set gameIsOver to true and label "startGame" to visible
        if(firePlanet.getBounds().intersects(blackHole1.getBounds())){
            gameIsOver = true;
            startGame.setVisible(true);
            remove(firePlanet);       
        }

        //if firePlanet overlaps blackHole2, set gameIsOver to true and label "startGame" to visible
        if(firePlanet.getBounds().intersects(blackHole2.getBounds())){
            gameIsOver = true;
            startGame.setVisible(true);
            remove(firePlanet);       
        }

       

        //if ironPlanet overlaps blackHole1, set gameIsOver to true and label "startGame" to visible
        if(ironPlanet.getBounds().intersects(blackHole1.getBounds())){
            gameIsOver = true;
            startGame.setVisible(true);
            remove(ironPlanet);       
        }

        
        

        //if ironPlanet overlaps blackHole2, set gameIsOver to true and label "startGame" to visible
        if(ironPlanet.getBounds().intersects(blackHole2.getBounds())){
            gameIsOver = true;
            startGame.setVisible(true);
            remove(ironPlanet);       
        }

        return gameIsOver;
    }
    
}

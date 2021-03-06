/*
 * File name: Boat.
 * File purpose: Class that controls boat movement and energy.
 */
package game.character;

import game.GameEngine;
import game.InputController;
import game.GameWindow;
import game.movement.Location;
import game.movement.AngledAcceleration;
import game.sprite.Sprite;
import game.Util;
import java.awt.geom.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Boat extends Moveable {

    static Logger logging = Logger.getLogger(Boat.class);
    
    final String ASSERTENERGY = "Energy cant be negative";
    final String LOGSETENERGY = "setEnergy value: ";
    final String LOGGETENERGY = "getEnergy value: ";
    final String LOGCOLLISION = "Reducing energy, collision";
    final String MSGERROMOUSE = "Error! Mouse bug";
    final String LOGDXCLICK = "dx click:";
    final String LOGDYCLICK = "dy click:";
    final String LOGMOUSE = "mouse angle:";
    final String ASSERTMOUSE = "Angle cant be more than -4 or 4";
    final int mouseMaxHeigh = 1920;
    final int mouseMaxWidth = 1080;
    final int mouseMinHeigh = -1920;
    final int mouseMinWidth = -1080;
    final int maxAngleDelta = 4;
    final int minAngleDelta = -4;
    
    private Location pivotPoint = null; //sets the x y point to the boat
    private int energy = 100; //energy of the boat
    

    /*
     * basic constructor
     */
    public Boat() {
    	
    }
    /*
     * function that finalizes any object
     * @param object object that will be finalized
     */
    private void finalizeObject(Object object){
    	object = null;
    }
    
    /*
     * function that reduces energy after collision
     * @param character   boat that is defined as a player
     */
    public void collision(Character character) {
        logging.debug(LOGCOLLISION);
        reduceEnergy();
    }

	/*
	 * (non-Javadoc)
	 * @see game.character.Moveable#update()
	 */
	@Override
	public void update() {
	    InputController controller = null;
	    controller = getController();
	    assert(controller != null);
	    
	    if (controller.keyPressEventsPending()) {
	    	// Executes the requested controller events
	        try{
	            InputController.Control pressedControl = controller.getPressedControl();
	            processKeyPressRotating(pressedControl);
	        }catch(NullPointerException|IndexOutOfBoundsException e){
	            System.out.println("Erro: " + e);
	            
	        }
	    } else {
	        setLocation(getMoveBehaviour().go(getLocation()));
	    }
	
	    if (controller.keyHeldEventsPending()) {
	        int count = 0;
	        while (count <= controller.getNumberOfHeldControls()) {
	        	// Processing all the held keys events
	            InputController.Control control = controller.getHeldControl(count);
	            processKeyPressRotating(control);
	            count++;
	        }
	    }
	
	    if (controller.isMouseHeld()) {
	        processMouse();
	    }
	
	    setTransform(pivotPoint);
	
	    if (checkScreenEdge()) {
	        this.getMoveBehaviour().setVelocity(getMoveBehaviour().getVelocity() / 10);
	    }
	
	    GameWindow.getInstance()
	            .updateControlPanel(this);
	    finalizeObject(controller);
	}
 
    /*
     * method that reduce energy of the boat
     */
    private void reduceEnergy() {
    	
        int reduceEnergy = 0;
        reduceEnergy = getEnergy(); //auxiliary int for reducing energy
        assert(reduceEnergy != 0);
        
        reduceEnergy--;

        setEnergy(reduceEnergy);

        if (reduceEnergy <= 0) {
        	// Making sure that the boat isn't running without energy
            GameEngine.getInstance().gameOver();
        } else {
        	// Making constants energy reductions
            GameWindow.getInstance().setEnergyBarLevel(reduceEnergy);
        }

    }

    /*
     * function that sets the angle that the boat is navigating
     * @param value  initial angle of the boat
     * @return value  new angle defined by function
     */
    private double pinAngle(double value) {
        /*
         * compares if value is absolute value is bigger than pi
         * then adjusts according for later use
         */
    	if (Math.abs(value) > Math.PI) {
            while (value > Math.PI) {
                value = value - (2 * Math.PI);
            }
            while (value < -Math.PI) {
                value = value + (2 * Math.PI);
            }
        }
    	
        return value;

    }
    
    
    /*
     * function that processes mouse click
     */
    private void processMouse() {
    	logging.setLevel(Level.INFO);
    	
        
        Point2D mousePointer = null; 
        mousePointer = this.getController().getMouseLocation(); //mouse pointing
        assert(mousePointer != null);
        
        Location destination = null;
        destination = new Location(mousePointer.getX(), mousePointer.getY()); //game coordinates
        assert(destination != null);
        
        double axyY = destination.getY() - y();
        double axyX = destination.getX() - x();
        assert(axyX < mouseMaxHeigh && axyX > mouseMinHeigh) : MSGERROMOUSE;
        assert(axyY < mouseMaxWidth && axyY > mouseMinWidth) : MSGERROMOUSE;  
        logging.debug("dx click:" + axyX);
        logging.debug("dy click:" + axyY);
        assert(axyX < mouseMaxHeigh && axyX > mouseMinHeigh) : MSGERROMOUSE;
        assert(axyY < mouseMaxWidth && axyY > mouseMinWidth) : MSGERROMOUSE;  
        logging.debug(LOGDXCLICK + axyX);
        logging.debug(LOGDYCLICK + axyY);
        double destinationAngle = Math.atan2(axyY, axyX);

        AngledAcceleration mouseMove = (AngledAcceleration) getMoveBehaviour();
        double angleDelta = destinationAngle - mouseMove.getAngle();

        angleDelta = (double) pinAngle(angleDelta);
        logging.debug("mouse angle:" + angleDelta);
        assert(angleDelta > minAngleDelta && angleDelta < maxAngleDelta ) : "Angle cant be more than -4 or 4";

        angleDelta = pinAngle(angleDelta);
        logging.debug(LOGMOUSE + angleDelta);
        assert(angleDelta > minAngleDelta && angleDelta < maxAngleDelta ) : ASSERTMOUSE;
        /*
         * checks if angleDelta is bigger than pi
         * then moves the boat according the comparison
         */
        if (Math.abs(angleDelta) < (Math.PI / 2.0)) {
            if ((angleDelta < Math.PI) && (angleDelta > 0)) {
                setLocation(mouseMove.goRight(getLocation()));
            }

            if ((angleDelta < 0) && (angleDelta > -Math.PI)) {
                setLocation(mouseMove.goLeft(getLocation()));
            }
            //accelerate
            setLocation(mouseMove.goUp(getLocation()));
        } else {
            mouseMove.setVelocity(mouseMove.getVelocity() * 0.95);

            if ((angleDelta > 0)) {

                setLocation(mouseMove.goRight(getLocation()));
            }

            if ((angleDelta < 0)) {
                setLocation(mouseMove.goLeft(getLocation()));
            }

        }



        setLocation(mouseMove.goUp(getLocation()));
        finalizeObject(mousePointer);
        finalizeObject(destination);

    }
    
    @SuppressWarnings("unused")
    private void processKeyPressSquare(InputController.Control keypress) {
        
        logging.debug("keypressed: " + keypress);
        switch (keypress) {
        // Sets up the behavior for each pressed key
            case UP:
                setLocation(getMoveBehaviour().goUp(getLocation()));

                break;
            case DOWN:
                setLocation(getMoveBehaviour().goDown(getLocation()));
                break;
            case LEFT:
                setLocation(getMoveBehaviour().goLeft(getLocation()));
                break;
            case RIGHT:
                setLocation(getMoveBehaviour().goRight(getLocation()));
                break;
            case BRAKE:
                setLocation(getMoveBehaviour().brake(getLocation()));
                break;
            case STORM:
                break;
            case PAUSE: //don't update
//      GameEngine.getInstance().togglePause();
                break;
            default:

                //do nothing
                break;
        }

    }
    
    /*
     * function that processes the WASD and arrows controllers
     * @param keypress   key pressed by player
     */
    private void processKeyPressRotating(InputController.Control keypress) {
        logging.setLevel(Level.INFO);
        logging.debug("keypressed: " + keypress);
        
        /*
         * processes the pressed key then moves
         * the boat according to the key pressed
         */
        try{
        switch (keypress) {
            case UP:
                setLocation(getMoveBehaviour().goUp(getLocation()));
                break;
            case DOWN:
                setLocation(getMoveBehaviour().goDown(getLocation()));
                break;
            case LEFT:
                setLocation(getMoveBehaviour().goLeft(getLocation()));
                break;
            case RIGHT:
                setLocation(getMoveBehaviour().goRight(getLocation()));
                break;
            case BRAKE:
                setLocation(getMoveBehaviour().brake(getLocation()));
                break;
            case STORM:
                break;
            case PAUSE: //don't update
//      GameEngine.getInstance().togglePause();
                break;
            default:

                //do nothing
                break;
        }
        }catch(NullPointerException e){
            System.out.println("Erro: " + e);
        }
    }

    /*
     * (non-Javadoc)
     * @see game.character.Character#setSprite(game.sprite.Sprite)
     */
    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
        pivotPoint = Util.getBoatPivotPoint(sprite);
    }
    
    /*
     * function that sets the energy of the boat
     * @param energy
     */
    public void setEnergy(int energy) {
        assert(energy >= 0) : ASSERTENERGY;
        logging.debug( LOGSETENERGY + energy);
        this.energy = energy;
    }

    /*
     * function that returns the energy of the boat
     * @return energy   the energy of the boat
     */
    public int getEnergy() {
        logging.debug( LOGGETENERGY + energy);
        return energy;
        
    }

}

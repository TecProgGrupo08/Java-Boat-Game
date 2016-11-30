/*
 * File name: Character.
 * File purpose: Class that controls position, area and collisions of the main boat.
 */
package game.character;

import game.InputController;

import game.movement.Location;
import game.movement.Movement;
import game.sprite.Sprite;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class Character {
	static Logger logging = Logger.getLogger(Character.class); //basic log system
    private Location myLocation = null; //location X Y of the character main boat
    private Movement moveBehaviour = null; // behaviour of the movement
    private Sprite sprite = null; // sets the hitbox of the character boat
    private InputController controller = null; 
    final double initialPosition = 0.0;

    public abstract void collide();
    public abstract void update();
   
    /*
     * function that finalizes any object
     * @param object object that will be finalized
     */
    private void finalizeObject(Object object){
    	object = null;
    }
    
    public boolean collides(Character character) {
        if (character.equals(this)) {
            return false;
        }else{
        	//do nothing
        }
        
        Area intersectArea = null;
        intersectArea = new Area(getTransformedArea());
        assert(intersectArea != null);
        
        Area auxiliarAreaBoat = null;
        auxiliarAreaBoat = character.getTransformedArea();
        assert(auxiliarAreaBoat != null);
        
        intersectArea.intersect(auxiliarAreaBoat);
        boolean checkArea = intersectArea.isEmpty();
        assert(checkArea == false || checkArea == true);
        
        if(checkArea == true){
        	return false;
        }else{
        	return true;
        }
    }
    

    /**
     *
     * @param data an ArrayList<CharacterBase> used to check for collisions
     * @return true if this Character collided with one of characters
     */
    
    public void setTransform(Location rotateCentre) {


        AffineTransform boatRotationAuxiliar = null;
        
        boatRotationAuxiliar = (AffineTransform) AffineTransform.getTranslateInstance(
                getLocation().getX(), getLocation().getY());
        
        assert(boatRotationAuxiliar != null);
        
        double centreHeight = 0;
        double centreWidth = 0;
        
        if (rotateCentre == null) {
            centreWidth = centreX();
            centreHeight = centreY();
        } else {
            centreWidth = rotateCentre.getX();
            centreHeight = rotateCentre.getY();
        }

        boatRotationAuxiliar.rotate(getMoveBehaviour().getAngle(),
                centreWidth,
                centreHeight);


        sprite.setTransform(boatRotationAuxiliar);
        sprite.setTransformedArea(sprite.getUntransformedArea().createTransformedArea(boatRotationAuxiliar));
        finalizeObject(boatRotationAuxiliar);
    }

    public Rectangle getBounds() {
        return sprite.getBounds();
    }
    
    public double x(){
        return getBounds().getCenterX();

    }
        
    public double y(){
        return getBounds().getCenterY();

    }

    public Area getTransformedArea() {
        return sprite.getTransformedArea();
    }

    public void collide(Character character) {
    }

    /**
     *
     * @param data an ArrayList<CharacterBase> used to check for collisions
     * @return true if this Character collided with one of characters
     */
    public boolean detectCollision(ArrayList<Character> data) {
        
    	ArrayList<Character> charactersMoving = null;
        charactersMoving = data;
        assert(data != null);
        
        boolean collision = false;

        int length = 0;
        length = charactersMoving.size();
        assert(length != 0);
        
        for (int i = 0; i < length; i++) {
            Character character = (Character) charactersMoving.get(i);

            if (collision = collides(character)) { //checks the collision with the
                character.collide();			   // character
            }else{
            	//do nothing
            }
        }
        finalizeObject(charactersMoving);
        return collision;
    }

    public InputController getController() {
        controller = InputController.getInstance();
    	return controller;
    }

    public Area getUntransformedArea() {
        return sprite.getUntransformedArea();
    }

    public void setUntransformedArea(Area area) {
        sprite.setUntransformedArea(area);
    }

    public void setLocation(Location location) {
    	//set to info to avoid loop (location is re-created once a second)
    	logging.setLevel(Level.INFO);
    	logging.debug("location set " + location.getX() + " " + location.getY());
        myLocation = location;

    }

    void setLocation(double x, double y) {
        if (myLocation == null) {
            myLocation = new Location(x, y);
        } else {
        	// Defines the coordinates to place and object on the map.
            this.myLocation.setLocation(x, y);
        }
    }

    /**
     * Creates a new instance of Character
     */
    public Character() {
        myLocation = new Location(initialPosition, initialPosition);
    }

    public Movement getMoveBehaviour() {
        return moveBehaviour;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setMoveBehaviour(Movement moveBehaviour) {
        this.moveBehaviour = moveBehaviour;
    }

    public Location getLocation() {
    	assert(myLocation != null) : "myLocation is null";
        return myLocation;
    }

    public double getX() {
    	assert(myLocation.getX() > -100) : "getX() is negative";
        return myLocation.getX();
    }

    public double getY() {
    	assert(myLocation.getY() > -100) : "getY() is negative";
        return myLocation.getY();
    }

    public double centreY() {
    	assert(getHeight() > 0) : "getHeight() is negative";
        return getHeight() / 2;
    }

    public double centreX() {
    	assert(getWidth() > 0) : "getWidth() is negative";
        return getWidth() / 2;
    }

    public double getHeight() {
    	assert(getBounds().getHeight() > 0) : "getBounds.getHeight() is negative";
        return getBounds().getHeight();
    }

    public double getWidth() {
    	assert(getBounds().getWidth() > 0) : "getBounds.get.Widht() is negative";
        return getBounds().getWidth();
    }
}

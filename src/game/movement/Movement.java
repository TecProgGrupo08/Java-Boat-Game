/*
 *  File name: Movement
 *  File purpose: Class that contains all the movement properties.
 */

package game.movement;

import game.GameEngine;
import org.apache.log4j.Logger;

public class Movement {

    public Movement() {
    }
    
    static Logger logging = Logger.getLogger(Movement.class);
    
    private double velocity = 0;
    private double xVelocity = 0;
    private double yVelocity = 0;
    
    private double acceleration = 0;
    
    private double angularVelocity = 0;
    private double angle = 0;
    
    private double maxVelocity = 0;
    
    private double angularMaxVelocity = 0;
    private double angularAcceleration = 0;
    
    final String LOG_BRAKE = "The boat was broke.";
    final String LOG_UP = "The boat is moving up.";
    final String LOG_DOWN = "The boat is moving down.";
    final String LOG_RIGHT = "The boat is moving right.";
    final String LOG_LEFT = "The boat is moving left.";
    final String LOG_ACCELETATE = "The boat is acceleting.";
    final String LOG_GO = "Go to the location seted";
    /**
     * Calibrates received values to not exceed the maximum allowed 
     * 
     * @param valueToVerify - the x or y velocity
     * @param limit - the maximum value allowed for the velocity
     * @return value
     */
    
    private double pinValue(double valueToVerify, double limit) {
    	
    	
        if (valueToVerify > 0.0) {
            if (valueToVerify > limit) {
            	valueToVerify = limit;
            }
        } else {
            if (valueToVerify < -limit) {
            	valueToVerify = -limit;
            }
        }
        return valueToVerify;
    }

    /**
     * Determines a new location of the object
     * @param location
     * @return
     */
    public Location go(Location location) {
    	
    	assert (location!= null) : "Null location";
    	logging.debug(LOG_GO);
    	
    	try {

    		xVelocity = pinValue(xVelocity, maxVelocity); // Making sure that the xVelocity doesn't surpass it's maximum limit
    		yVelocity = pinValue(yVelocity, maxVelocity); // Making sure that the yVelocity doesn't surpass it's maximum limit
    	
    	}catch(NumberFormatException error){
  		  GameEngine.endGame("Number format error");
    	}
    	
        double axisXlocation = location.getX(); // Position in the axis X
        double axisYlocation = location.getY(); //Position in the axis Y
        axisXlocation += xVelocity;
        axisYlocation += yVelocity;
        location.setLocation(axisXlocation, axisYlocation);
        return location;
    }

    public Location brake(Location location) {
    	
    	assert(location != null) : "Null location";
    	logging.debug(LOG_BRAKE);
    	
        double axisXlocation = location.getX(); // Position in the axis X
        double axisYlocation = location.getY(); // Position in the axis Y

        axisXlocation += xVelocity;
        axisYlocation += yVelocity;
        location.setLocation(axisXlocation, axisYlocation);
        return location;
    }

    /**
     * Set the maximum velocity of this game character in the x axis
     *
     */
    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;

    }

    /**
     * Performs a move representing 'right'
     *
     * @param location a Location representing the current position of the
     * physical object
     * @return Returns an updated Location object
     */
    public Location goRight(Location location) {
    	
    	assert(location != null) : "Null location";
    	logging.debug(LOG_RIGHT);
    	
        double x = location.getX();
        velocity += acceleration;
        velocity = pinValue(velocity, maxVelocity);  // Making sure that the xVelocity doesn't surpass it's maximum limit

        x = x + velocity;
        location.setX(x);

        return location;
    }

    /**
     * Performs a move representing 'Left'
     *
     * @param location a Location representing the current position of the
     * physical object
     * @return Returns an updated Location object
     */
    public Location goLeft(Location location) {

    	assert(location != null) : "Null location";
        logging.debug(LOG_LEFT);
        
        velocity -= acceleration;
        
    	// If the velocity exceeds the allowed limit, then it should receive the limit as it's own value.
        if ((-velocity) > (-maxVelocity)) {
            velocity = -maxVelocity; // The max velocity is a negative number because it is going left.
        }
        double x = location.getX();
        x = x - velocity;
        location.setX(x);

        return location;
    }

    /**
     * Performs a move representing 'Down'
     *
     * @param location a Location representing the current position of the
     * physical object
     * @return Returns an updated Location object
     */
    public Location goDown(Location location) {
    	
    	assert(location != null) : "Null location";
        logging.debug(LOG_DOWN);
    	
        velocity += acceleration;
        // If the velocity exceeds the allowed limit, then it should receive the limit as it's own value.
        if ((velocity) > (maxVelocity)) {
            velocity = maxVelocity;
        }
        double y = location.getY();
        y = y + velocity;
        location.setY(y);
        return location;
    }

    /**
     * Performs a move representing 'up'
     *
     * @param location a Location representing the current position of the
     * physical object
     * @return Returns an updated Location object
     */
    public Location goUp(Location location) {
        
        logging.debug(LOG_UP);
    	
    	velocity -= acceleration;
        // If the velocity exceeds the allowed limit, then it should receive the limit as it's own value.
        if ((-velocity) > (-maxVelocity)) {
            velocity = -maxVelocity; // The max velocity is a negative number because it is going up.
        }
        double y = location.getY();
        y = y + velocity;
        location.setY(y);
        return location;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double getAngularMaxVelocity() {
        return angularMaxVelocity;
    }

    public void setAngularMaxVelocity(double angularMaxVelocity) {
        this.angularMaxVelocity = angularMaxVelocity;
    }

    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public double getXVelocity() {
        return xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }

    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }
}

/*
 *  File name: AngledAcceleration
 *  File purpose: Class responsible for managing physics by axis.
 */


package game.movement;

import org.apache.log4j.Logger;

import game.GameEngine;

public class AngledAcceleration extends Movement {

    
	private double angularAcceleration = 0;
    private double friction = 0;
    private double brake = 0;
    private double angularFriction = 0;

    
    final String LOG_UP = "The boat is moving up with acceleration.";
    final String LOG_ACCELERATE = "The boat is accelerating.";
    final String LOG_DOWN = "The boat is moving down with acceleration.";
    final String LOG_TURN = "The boat is turning with acceleration";
    final String LOG_RIGHT = "The boat is moving right.";
    final String LOG_LEFT = "The boat is moving left.";
    final String LOG_ANGULAR_VELOCITY = "The angular velocity was seted.";
    final String LOG_MAX = "Max speed dont is grather than the limit";
    final String LOG_GO = "Go to the location seted";
    final String LOG_RECALCULATE_VELOCITY = "New velocity was seted";
    final String LOG_BRAKE = "The boat was broke.";
    final String LOG_RECALCULATE_X = "X velocity was recalculated";
    final String LOG_RECALCULATE_Y = "Y velocity was recalculated";
    
    static Logger logging = Logger.getLogger(AngledAcceleration.class);
    
    @Override
    public Location goUp(Location location) {
    	/*
    	 *get the velocity and the acceleration to determine the 
         *new location of the object (if going forward)
         */
    	
    	assert (location != null) : "Null location";
    	logging.debug(LOG_UP);
    	
        this.setVelocity(getVelocity() + getAcceleration());
        location = changedAccelerate(location);
        return location;
    }

    private Location changedAccelerate(Location location) {
    	/*
    	 *sets the velocity based on the acceleration to determine 
    	 *the new location of the object (if theres no acceleration) 
    	 */
    	
    	assert (location != null) : "Null location";
    	logging.debug(LOG_ACCELERATE);
    	
        setXVelocity(Math.cos(getAngle()) * getVelocity());
        setYVelocity(Math.sin(getAngle()) * getVelocity());

        location = super.go(location);
        
        assert(location != null) : "Location is null!";
        return location;
    }

    @Override
    public Location goDown(Location location) {
    	/*
    	 *get the velocity and the acceleration to determine the 
         *new location of the object (if going backwards)
         */
    	logging.debug(LOG_DOWN);
        this.setVelocity(getVelocity() - getAcceleration());
        location = changedAccelerate(location);
        
        assert(location != null):"location is null!";
        return location;

    }
    
    /**
     * Executes movement changes in angle and velocity  when the boat is making a turn
     * 
     * @param location - object that points out the object's location
     * @return location
     */

    public Location turn(Location location) {

    	assert (location != null) : "Null location";
    	logging.debug(LOG_TURN);
    	
        double angle = getAngle();
        double angularVelocity = getAngularVelocity();
        angle = angle + angularVelocity;
        angle = clampAngle(angle);
        
        /*
         * Changes the angle of the boat (if going left or right)
         */
        double cosine = 0.0; // This variable helps to calculate the velocity in the axis X
        double sin = 0.0; // This variable helps to calculate the velocity in the axis Y
        
        cosine = Math.cos(getAngle());
        sin = Math.sin(getAngle());
        
        double velocity = getVelocity();
        double yVelocity = 0;
        double xVelocity = 0;
        
        xVelocity = cosine * velocity; // This is the equation to find the decomposed velocity to axis X
        yVelocity = sin * velocity; // Equation to velocity at axis Y.

        setAngle(angle);
        setAngularVelocity(angularVelocity);
        setVelocity(velocity);
        setXVelocity(xVelocity);
        setYVelocity(yVelocity);
        
        assert(location != null):"location is null!";
        return location;
    }

    /**
     * increases angular velocity (accelerates in a clockwise direction)
     */
    @Override
    public Location goRight(Location location) {
    	
    	logging.debug(LOG_RIGHT);
    	
        setNewAngularVelocity("+");
        return turn(location);
    }

    /**
     * decreases angular velocity (accelerates in an anti clockwise direction)
     */
    @Override
    public Location goLeft(Location location) {
    	
    	assert (location != null) : "Null location";
    	logging.debug(LOG_LEFT);
        setNewAngularVelocity("-");
        return turn(location);
    }

    private void setNewAngularVelocity(String type) {
    	
    	assert(type != null):"Null type of movement";
    	logging.info(LOG_ANGULAR_VELOCITY);
    	
        double velocity;
        if (type == "+") {
        	//the type + means that it is going right
            velocity = getAngularVelocity() + getAngularAcceleration();
        } else {
        	//it is going left
            velocity = getAngularVelocity() - getAngularAcceleration();
        }
        double newVelocity = pin(velocity, getAngularMaxVelocity());

        setAngularVelocity(newVelocity);
    }

    private double pin(double valueToVerify, double limit) {
    	
    	logging.info(LOG_MAX);
    	
        if (valueToVerify >= limit) {
            valueToVerify = limit;
        } else if (valueToVerify < -limit) {
            valueToVerify = -limit;
        }

        return valueToVerify;
    }

    private double damp(double velocity, double friction) {
        if (velocity > 0.0) {
        	//this is how friction acts over the movement
            velocity = velocity - friction;
            if (velocity < 0.0) {
                velocity = 0.0;
            }
        } else {
        	//the friction is always in the opposite way (signal) of the movement
            velocity = velocity + friction;
            if (velocity > 0.0) {
            	//the friction can only make the velocity drop to zero, it can't generate movement in the opposite way
                velocity = 0.0;
            }
        }

        return velocity;
    }

    @Override
    public Location go(Location location) {
    	
    	logging.info(LOG_GO);
    	
        recalculateAngularVelocity();
        recalculateAngle();

        recalculateVelocity();
        recalculateYVelocity();
        recalculateXVelocity();

        location = super.go(location);
        
        assert(location != null) : "location is null!";
        return location;
    }

    private void recalculateVelocity() {
    	
    	logging.info(LOG_RECALCULATE_VELOCITY);
    	
    	try {
    		double velocity = damp(getVelocity(), this.friction);
    		setVelocity(velocity);
    	}catch (NumberFormatException error ){
    		 GameEngine.endGame("Number format error");
    	}
    	
    }

    private void recalculateAngularVelocity() {
    	
    	logging.info(LOG_RECALCULATE_VELOCITY);
    	
		try {
			double angularVelocity = getAngularVelocity();
			setAngularVelocity(damp(angularVelocity, this.angularFriction));
		} catch (NumberFormatException error) {
			GameEngine.endGame("Number format error");
		}
    }

    private void recalculateAngle() {
        double angle = getAngle();
        angle = angle + getAngularVelocity();
        angle = clampAngle(angle);
        setAngle(angle);
    }

    private double clampAngle(double angle) {
    	
        if (angle > 0) {
            while (angle > Math.PI) {
                angle = angle - (2 * Math.PI);
            }
        }
        if (angle < 0) {
            while (angle < (-Math.PI)) {
                angle = angle + (2 * Math.PI);
            }
        }
        return angle;
    }

    public AngledAcceleration() {
    }

    @Override
    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    @Override
    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public double getFriction() {
        return friction;
    }

    public double getBrake() {
        return brake;
    }

    public void setBrake(double brake) {
        this.brake = brake;
    }

    public double getAngularFriction() {
        return angularFriction;
    }

    public void setAngularFriction(double angularFriction) {
        this.angularFriction = angularFriction;
    }

    private void recalculateXVelocity() {
        
    	logging.info(LOG_RECALCULATE_X);
    	
        double cosine = 0.0;
        double velocity = 0.0;
        
        cosine = Math.cos(getAngle());
        velocity = getVelocity();
        
        double xVelocity = 0.0;
        xVelocity = cosine * velocity;
        setXVelocity(xVelocity);
    }

    private void recalculateYVelocity() {
       
    	logging.info(LOG_RECALCULATE_Y);
    	
        double sin = 0.0;
        double velocity = 0.0;
        
        sin = Math.sin(getAngle());
        velocity = getVelocity();
        
        double yVelocity = 0.0;
        yVelocity = sin * velocity;
        setYVelocity(yVelocity);
    }
}

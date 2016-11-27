/*
 * File name: Moveable.
 * File pourpose: Class that defines the moveable area.
 */
package game.character;

import game.movement.Movement;
import game.Renderer;
import game.sprite.SpriteImage;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public abstract class Moveable extends Character
{
	
	static Logger logging = Logger.getLogger(Moveable.class);
    protected boolean immune = false; //boolean that sets player to receive damage
    private Renderer renderer = Renderer.getInstance();
    static final double averageRandom = 0.5;
    static final double collisionInertia = 3.8; //constant speed after collision
    
    public Moveable()
    {
    }

    @Override
    public abstract void update();

    protected boolean checkScreenEdge()
    {
        double h = (double) renderer.getHeight();
        double w = (double) renderer.getWidth();

        @SuppressWarnings("unused")
		double centreX = getSprite().getTransformedArea().getBounds().getCenterX(); //This variable hold the position of the center of the hitbox of the object in X
        double centreY = getSprite().getTransformedArea().getBounds().getCenterY(); //This variable hold the position of the center of the hitbox of the object in Y

        double x = getLocation().getX();
        double y = getLocation().getY();
        
        boolean hitEdge = false;
//if over right side
        if (x > w)
        {
        	logging.debug("right side hit");
            getLocation().setX(w - this.getUntransformedArea().getBounds2D().getWidth());
            hitEdge = true;
        }
//if gone over bottom
        if (centreY > h)
        {
        	logging.debug("bottom side hit");
            getLocation().setY(h - this.getUntransformedArea().getBounds2D().getHeight());
            hitEdge = true;
        }
//if gone over left
        if (x < 0.0)
        {
        	logging.debug("left side hit");
            getLocation().setX(0);
            hitEdge = true;
        }
//if gone over top
        if (y < 0.0)
        {
        	logging.debug("top side hit");
            getLocation().setY(0);
            hitEdge = true;
        }
        assert(hitEdge == false || hitEdge == true) : "hitEdge is invalid!";
        return hitEdge;
    }

    @Override
    public void collide()
    {


        if (immune == false)
        {
        	// Defining all the character's behavior during a collision
            SpriteImage boatImage = (SpriteImage) getSprite();
            boatImage.setFrame(1);
            immune = true;
            Movement moveAction = getMoveBehaviour();
            //getMoveBehaviour().angle+=(Math.random()-averageRandom);
            moveAction.setAngularVelocity(moveAction.getAngularVelocity() + (Math.random() - averageRandom) * 0.4); // Generates a random coefficient to reproduce the collision spin
            moveAction.setAngle(moveAction.getAngle() + (Math.random() - averageRandom) * 0.1);  // Generates a random coefficient to define a new angle for the boat
            moveAction.setVelocity(-moveAction.getVelocity() * (collisionInertia * Math.random())); // Uses the inertia of the boat movement to define a new velocity during the collision spin

            //control the limits of the speed
            if (moveAction.getVelocity() > moveAction.getMaxVelocity())
            {
                moveAction.setVelocity(moveAction.getMaxVelocity());
            }

            if (moveAction.getVelocity() < -moveAction.getMaxVelocity())
            {
                moveAction.setVelocity(-moveAction.getMaxVelocity());
            }
            Timer timer = new Timer(); // setting immunity time
            timer.schedule
		  (new TimerTask()
                     {

                         @Override
                         public void run()
                         {
                             SpriteImage boatImage = (SpriteImage) getSprite();
                             boatImage.setFrame(0);
                             immune = false;
                         }
                     }
		, 2000);
        }
    }

}

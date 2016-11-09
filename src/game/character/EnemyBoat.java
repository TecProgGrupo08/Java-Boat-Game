/*
 * File name: EnemyBoat.
 * File purpose: Class that controls the AI's boat movement.
 */
package game.character;

import game.movement.Location;
import game.movement.Movement;
import java.awt.geom.*;

/**
 *
 * @author Mark
 */
public class EnemyBoat extends Moveable
{
    private int randomLength = 0;
    private int turnDuration = 15;
    private boolean changeDirection = false;
    final int lengthMultiplier = 50;
    private int counter = 0;
	
	    /** Creates a new instance of CharacterComputerBoat */
    public EnemyBoat()
    {

    }
      
    /*
     * function that finalizes any object
     * @param object object that will be finalized
     */
    private void finalizeObject(Object object){
    	object = null;
    }

    @Override
    public void update()
    {
        if (changeDirection == true)
        {
            turnDuration--;

            switch (counter) //randomizes the movement of the enemy boat
            {
                case 0:
                    setLocation(getMoveBehaviour().goRight(getLocation()));
                    break;
                case 1:
                    setLocation(getMoveBehaviour().goLeft(getLocation()));
                    break;
                default:
                    setLocation(getMoveBehaviour().goUp(getLocation()));
                    break;
            }
            if (turnDuration <= 0)
            {
                turnDuration = 15;
                counter = (int) (2 * Math.random());
                changeDirection = false;
            }
        }
        else
        {
            randomLength--;
            setLocation(getMoveBehaviour().goUp(getLocation()));

            if (randomLength <= 0)
            {
                randomLength = (int) (Math.random() * lengthMultiplier);
                changeDirection = true;

            }

        }

        setLocation(getMoveBehaviour().go(getLocation()));


        Rectangle2D enemyBoat = (Rectangle2D) this.getSprite().getUntransformedArea().getBounds2D();
        setTransform(new Location(enemyBoat.getCenterX(), enemyBoat.getCenterY()));
        if (checkScreenEdge())
        {
            getMoveBehaviour().setAngle(Math.PI + this.getMoveBehaviour().getAngle());
        }
        
        finalizeObject(enemyBoat);
    }

    


    @Override
    public void collide()
    {
        Movement moveAction = (Movement) getMoveBehaviour();
//		moveAction.setAngle(Math.random() + moveAction.getAngle());
        moveAction.setVelocity(moveAction.getVelocity() * 0.99);
        double random = Math.random();
        if (random > 0.5){
            for (int x = 1; x < 10; x++){
            	//sets the new location after a collision
                setLocation(moveAction.goRight(getLocation()));
            }

        }
        else{
            for (int x = 1; x < 10; x++){
            	//sets the new location after a collision
                setLocation(moveAction.goLeft(getLocation()));
            }

        }
        for (int x = 1; x < 3; x++){
        		//sets the new location after a collision
            setLocation(moveAction.goUp(getLocation()));
        }
    
        finalizeObject(moveAction);
    }
    
}

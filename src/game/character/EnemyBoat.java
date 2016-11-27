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
public class EnemyBoat extends Moveable {
	private int randomLength = 0;
	private int turnDuration = 15;
	private boolean changeDirection = false;
	final int lengthMultiplier = 50;
	private int counter = 0;
	final static double averageRandom = 0.5;
	final static int initialMove = 1;
	final static int lastRightLeftMove = 10;
	final static int lastUpMove = 3;

	/** Creates a new instance of CharacterComputerBoat */
	public EnemyBoat() {

	}

	/*
	 * function that finalizes any object
	 * 
	 * @param object object that will be finalized
	 */
	private void finalizeObject(Object object) {
		object = null;
	}
	
	@Override
	public void update() {
		// Check if the direction was change
		if (changeDirection == true) {
			turnDuration--;

			// update the course of the boat
			switch (counter) {
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

			// randomizes the movement of the enemy boat
			if (turnDuration <= 0) {
				turnDuration = 15;
				counter = (int) (2 * Math.random());
				changeDirection = false;
			}
		}
		// case the direction was not changed
		else {
			randomLength--;
			setLocation(getMoveBehaviour().goUp(getLocation()));

			// Check and update the movement
			if (randomLength <= 0) {
				randomLength = (int) (Math.random() * lengthMultiplier);
				changeDirection = true;

			}

		}

		setLocation(getMoveBehaviour().go(getLocation()));

		Rectangle2D enemyBoat = (Rectangle2D) this.getSprite().getUntransformedArea().getBounds2D();
		setTransform(new Location(enemyBoat.getCenterX(), enemyBoat.getCenterY()));

		// Verify if the boar hit edge
		if (checkScreenEdge()) {
			getMoveBehaviour().setAngle(Math.PI + this.getMoveBehaviour().getAngle());
		}

		finalizeObject(enemyBoat);
	}

	@Override
	public void collide() {
		Movement moveAction = (Movement) getMoveBehaviour();
		// moveAction.setAngle(Math.random() + moveAction.getAngle());
		moveAction.setVelocity(moveAction.getVelocity() * 0.99);
		double random = Math.random();
		if (random > averageRandom) {
			for (int x = initialMove; x < lastRightLeftMove; x++) {
				// sets the new location after a collision
				setLocation(moveAction.goRight(getLocation()));
			}

		} else {
			for (int x = initialMove; x < lastRightLeftMove; x++) {
				// sets the new location after a collision
				setLocation(moveAction.goLeft(getLocation()));
			}

		}
		for (int x = initialMove; x < lastUpMove; x++) {
			// sets the new location after a collision
			setLocation(moveAction.goUp(getLocation()));
		}

		finalizeObject(moveAction);
	}

}

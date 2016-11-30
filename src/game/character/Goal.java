/*
 * File name: Goal.
 * File purpose: Class that defines the behavior of the goal.
 */
package game.character;

import game.GameEngine;
import java.util.*;
class Goal extends Stationary {
	
	//If the colision is the goal you win
    @Override
    public boolean detectCollision(ArrayList<Character> moving) {
        boolean collision = false;
        int length = moving.size();
        
        //iterates the length of the movement 
        for (int i = 0; i < length; i++) {
        	
            Character character = moving.get(i);
            boolean detectCollision = collides(character);
            
            //check if occurred collision on the current move
            if (collision = detectCollision) { 
            	
            	//check if this collision is between harboar and boat 
                if (character.equals(GameEngine.getCharacter("Boat"))) {
                    GameEngine.getInstance().win();
                
                 //check if this collision is between an obstacle and the boat
                } else {
                    character.collide();
                }
            }

        }
        //check if the collision is a possible option
        assert(collision != false || collision != true) : "collision is invalid!";
        return collision;
    }
}

/*
 * File name: Goal.
 * File purpose: Class that defines the behavior of the goal.
 */
package game.character;

import game.GameEngine;
import java.util.*;
class Goal extends Stationary {

    @Override
    public boolean detectCollision(ArrayList<Character> moving) {
        boolean collision = false;
        int length = moving.size();

        for (int i = 0; i < length; i++) {
            Character character = moving.get(i);
            boolean detectCollision = collides(character);
            if (collision = detectCollision) {
                if (character.equals(GameEngine.getCharacter("Boat"))) {
                    GameEngine.getInstance().win();

                } else {
                    character.collide();
                }
            }

        }
        
        return collision;
    }
}

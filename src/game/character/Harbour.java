/*
 * File name: Harbour.
 * File purpose: Class that defines the behavior of the harbour.
 */
package game.character;

import java.util.ArrayList;

public class Harbour extends Stationary {
    public Harbour() {
    }

    @Override
    public boolean detectCollision(ArrayList<Character> moving) {
        return false;
    }
}

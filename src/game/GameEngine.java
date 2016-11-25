package game;

import game.character.Cast;
import game.movement.Location;
import game.movement.Movement;
import game.sprite.Sprite;
import game.character.Factory;
import game.character.Character;

import java.util.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
  
public class GameEngine implements Runnable {

	static Logger logging = Logger.getLogger(GameEngine.class);

    private static Cast cast = game.character.Cast.getInstance(); // Object that cast all characters on map.
    public static Renderer renderer; // Object that render boat in map.
    private static GameEngine gameEngine; // Engine of the game
    private static InputController controller = InputController.getInstance(); // Object that defines controllers of game.

    private static int minObstacles; // Minimum number of obstacles that have in the map.
    private static int maxObstacles; // Maximum number of obstacles that have in the map.
    private static int obstacleSize; // Size of obstacles that have in the map.
    
    final int MIN_NUMBER_OF_OBSTACLES = 10; // Constant of minimum number of obstacles that have in the map. 
    final int MAX_NUMBER_OF_OBSTACLES = MIN_NUMBER_OF_OBSTACLES + 5; // Constant of maximum number of obstacles that have in the map.
    final int OBSTACLES_SIZE_ON_MAP = 20; // Constant of size of obstacles that have in the map.
    
    public static Character getCharacter(String type){
        return GameEngine.getInstance().getCharacters().get(type);
    }
    
    /**
     * Private contructor of game engine
     * 
     * @param cast - cast all characters on map.
     * @param renderer - render the boats in map..
     * @param controller - define the input controllers to the game.
     * @param minObstacles - Minimum number of obstacles that have in the map.
     * @param maxObstacles - Maximum number of obstacles that have in the map.
     * @param obstacleSize - size of obstacles that have in the map..
     */
    
    private GameEngine() {
        cast = game.character.Cast.getInstance(); 
        renderer = Renderer.getInstance();
        controller = InputController.getInstance();
        minObstacles = MIN_NUMBER_OF_OBSTACLES;
        maxObstacles = MAX_NUMBER_OF_OBSTACLES;
        obstacleSize = OBSTACLES_SIZE_ON_MAP;


    }

    public static GameEngine getInstance() {
    	logging.setLevel(Level.INFO);
    	logging.debug("Entering in Singleton on GameEngine");
    	//Verify if the object of GameEngine was already created.
        if (gameEngine != null) {
        	logging.debug("The instance of GameEngine already created!");
            return gameEngine;

        } else {
            synchronized (GameEngine.class) {
            	//If the gameEngine is null a instance is created.
                if (gameEngine == null) {
                	logging.debug("The instance of GameEngine hass been created!");
                    gameEngine = new GameEngine();
                }
            }

        }

    	logging.info("Returning object of GameEngine.");
        return gameEngine;
    }

    private Factory characterFactory; // Criator of characters
    private Factory factory() {
    	//This if verify if the factory object has already been created.
        if (this.characterFactory == null) {
            this.characterFactory = new Factory();
        }

        return this.characterFactory;

    }

    private Character addCharacter(String name, String type) {
    	
    	assert(type != null) : "Null character type";
    	assert(name != null) : "Null character name";
        Character character = create(type);
        cast.put(name, character);
        assert(character != null) : "character is null!";
        return character;
    }

    private Character create(String type) {
    	
    	assert(type != null) : "Null character type";
        Character character = factory().createCharacter(type);
        assert(character != null) : "character is null!";
        return character;
    }
    
    /**
     * Initialize the obstacles of map
     * 
     * @param obstacle - object of obstacles
     * @param min - Minimum number of obstacles that have in the map.
     * @param max - Maximum number of obstacles that have in the map.
     * @param numberOfObstacles - size of obstacles that have in the map.
     */
    private void setupObstacles() {
    	logging.debug("The method of obstacles creation was activated!");
        Character obstacle;

        int min = (int) Util.getMinimumNumberOfObstacles();
        int max = (int) Util.getMaxiumNumberOfObstacles();

        assert( min < Util.getMaxiumNumberOfObstacles());
        assert( max > min);
        
        int numberOfObstacles = (int) (Math.random() * (max - min));  // Creates the number of objects between the maximum and minimum allowed
        assert( numberOfObstacles >= min);
        assert( numberOfObstacles <= max);
        
        logging.info("Total of obstacles created: " +numberOfObstacles);
    	//This for determined by the number of obstacles the type of obstacle.
        for (int x = 0; x
                < numberOfObstacles + 1; x++) {

            if (Math.random() > 0.5) {
                obstacle = (Character) create("BUOY");
            } else {
                obstacle = (Character) create("OCTOPUS");

            }
        	logging.debug("Type of obstacle were defined!");

            Location location = new Location(
                    Math.random() * renderer.getWidth(),
                    Math.random() * renderer.getHeight());

            obstacle.setLocation(location);
            Sprite s = (Sprite) obstacle.getSprite();
            obstacle.getSprite().getTransform().setToTranslation(location.getX(), location.getY());

            s.setUntransformedArea(
                    s.getUntransformedArea().createTransformedArea(obstacle.getSprite().
                    getTransform()));

        	logging.debug("Obstacle location were defined!");

            cast.put("Obstacle" + String.valueOf(x), obstacle);
        	logging.info("Obstacle created!");
        	finalizeObject(min);
        	finalizeObject(max);
        	finalizeObject(numberOfObstacles);
        }

    }

    public void initialize() {
        Util.loadImages();

        addCharacter("Harbour", "HARBOUR");
        addCharacter("Goal", "GOAL");
        addCharacter("Island", "ISLAND");

        Character boat = addCharacter("Boat", "BOAT");
        GameWindow.getInstance().initializeControlPanel(boat);

        for (int x = 0; x < 2; x++) {
            cast.put("ComputerBoat" + String.valueOf(x), create("COMPUTER_BOAT"));
        }

        setupObstacles();


        //draw the sea
        renderer.setBackgroundImage(Util.imageResources.get("SEA"));


    }
    boolean storm = false;

    /**
     * Change the environment of the map
     * 
     * @param obstacle - Array of obstacles that have in the map
     */
    public void storm() {
        ArrayList<Character> obstacles = GameEngine.cast.getObstacles();
        int x = obstacles.size();
        for (int i = 0; i < x; i++) {
            Character o = obstacles.get(i);
            o.getSprite().setShowSprite(!o.getSprite().isSpriteShown());
        }

        storm = !storm;
        
        //This structure controller verify if the player want to see evening map.
        if (storm) {
            renderer.setBackgroundImage(Util.imageResources.get("NIGHT")); // When the player hit the storm button, then the night map is loaded

        } else {
            renderer.setBackgroundImage(Util.imageResources.get("SEA")); //  If the storm is not activated, the map should just be normal "SEA"
        }

    }

    final String GAME_OVER_GAME = "Game Over!"; // Game Over Message.
    final String WIN_GAME = "You Win!"; // Win Message.
    
    public void gameOver() {
        endGame(GAME_OVER_GAME);
    }

    public void win() {
        endGame(WIN_GAME);

    }

    public static void endGame(String message) {
    	
    	assert (message != null) : "Null message for end game";
    	
        if (cast.setBoatVulnerable()) {
            javax.swing.JOptionPane.showMessageDialog(null, message); // Creates a dialog with the user with the defined message
            cast.setBoatImmune();

        }

    }

    public static final int SLEEP_LENGTH = 16;//16 ms equates to ~60 frames per second
    @Override
    public void run() {
        while (true) {

            ArrayList<Character> moving = cast.getMovingCharacters();
            ArrayList<Character> obstacles = cast.getObstacles();

            int x = obstacles.size();
            for (int i = 0; i < x; i++) {
                Character character = obstacles.get(i);

                character.update();
                character.detectCollision(moving);
            }
            
            x = moving.size();
            assert (x > 0): "Moving size is incompatible ";
            
            for (int i = 0; i < x; i++) {
                try{
            		Character character = moving.get(i);
            		character.update();
            		character.detectCollision(moving);
                }catch(NullPointerException|IndexOutOfBoundsException e){
                	assert(true);
                	e.printStackTrace();
                }
            }

            renderer.repaint();
            try {
                Thread.sleep(SLEEP_LENGTH);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public HashMap<String, Character> getCharacters() {
        return cast;
    }
    
    private void finalizeObject(Object object) {
    	object = null;
    }
}

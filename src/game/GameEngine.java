package game;

import game.character.Cast;
import game.movement.Location;
import game.sprite.Sprite;
import game.character.Factory;
import game.character.Character;


import java.util.*;

public class GameEngine implements Runnable {

    private static Cast cast = game.character.Cast.getInstance();               //Mold the map.Set Characters, obstacles ...
    public static Renderer renderer;                                            //Render Characters and backgroung 
    static InputController controller = InputController.getInstance();  //Handle the input comands of the user
    private static int minObstacles;                                            //Defines the minimun amount of enemys on the map
    private static int maxObstacles;                                            //Defines the maximum amount of enemys on the map
    private Factory characterFactory;                                           //Create characters 
    private static int obstacleSize;                                            
    public static final int SLEEP_LENGTH = 16;                                  //16 ms equates to ~60 frames per second
    static GameEngine gameEngine;

    final int MIN_NUMBER_OF_OBSTACLES = 10;
    final int MAX_NUMBER_OF_OBSTACLES = MIN_NUMBER_OF_OBSTACLES + 5;
    final int OBSTACLES_SIZE_ON_MAP = 20;
    
    final String GAME_OVER_GAME = "Game Over!";
    final String WIN_GAME = "You Win!";
    private final int NAME_MAX_SIZE = 50;
    
    public static Character getCharacter(String type){
        return GameEngine.getInstance().getCharacters().get(type);
    }

    public GameEngine() {
        cast = game.character.Cast.getInstance();
        renderer = Renderer.getInstance();
        controller = InputController.getInstance();
        minObstacles = MIN_NUMBER_OF_OBSTACLES;
        maxObstacles = MAX_NUMBER_OF_OBSTACLES;
        obstacleSize = OBSTACLES_SIZE_ON_MAP;


    }

    public static GameEngine getInstance() {
  
        if(gameEngine == null) {

            synchronized (GameEngine.class) {
                
                if (gameEngine == null) {
                    gameEngine = new GameEngine();
                }
            }
            return gameEngine;

        } else {

            return gameEngine;
        
        }
        
    }

    private Factory factory() {

        if (this.characterFactory == null) {
            this.characterFactory = new Factory();
        }
        
        return this.characterFactory;
    }

    private Character create(String type) {

        assert(type != null);
        Character character = factory().createCharacter(type);
        return character;
    }

    private Character addCharacter(String name, String type) {
        
        assert(name != null);
        assert(name.length() <= NAME_MAX_SIZE);
        assert(type != null);
        assert(type.length() <= NAME_MAX_SIZE);
        Character character = create(type);
        cast.put(name, character);
        return character;
    }

    private void setupObstacles() {
        Character obstacle;

        int min = Util.getMinimumNumberOfObstacles();
        assert(min >= 0);
        int max = Util.getMaxiumNumberOfObstacles();
        assert(max >= 0);
        
        int numberOfObstacles = (int) (Math.random() * (max - min));
        
        for (int x = 0; x
                < numberOfObstacles + 1; x++) {
            if (Math.random() > 0.5) {
                obstacle = create("BUOY");
            } else {
                obstacle = create("OCTOPUS");

            }

            Location l = new Location(
                    Math.random() * renderer.getWidth(),
                    Math.random() * renderer.getHeight());

            obstacle.setLocation(l);
            Sprite s = obstacle.getSprite();
            obstacle.getSprite().getTransform().setToTranslation(l.getX(), l.getY());

            s.setUntransformedArea(
                    s.getUntransformedArea().createTransformedArea(obstacle.getSprite().
                    getTransform()));


            cast.put("Obstacle" + String.valueOf(x), obstacle);
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

    public void storm() {
        ArrayList<Character> obstacles = GameEngine.cast.getObstacles();
        int x = obstacles.size();
        for (int i = 0; i < x; i++) {
            Character o = obstacles.get(i);
            o.getSprite().setShowSprite(!o.getSprite().isSpriteShown());
        }

        storm = !storm;
        if (storm) {
            renderer.setBackgroundImage(Util.imageResources.get("NIGHT"));

        } else {
            renderer.setBackgroundImage(Util.imageResources.get("SEA"));
        }

    }

    public void gameOver() {
        endGame(GAME_OVER_GAME);
    }

    public void win() {
        endGame(WIN_GAME);

    }

    private void endGame(String message) {
        assert(message != null);
        if (cast.setBoatVulnerable()) {
            javax.swing.JOptionPane.showMessageDialog(null, message);
            cast.setBoatImmune();

        }

    }

    @Override
    public void run() {
        while (true) {

            ArrayList<Character> moving = cast.getMovingCharacters();
            ArrayList<Character> obstacles = cast.getObstacles();

            assert(moving != null);
            assert(obstacles != null);

            int x = obstacles.size();

            assert(x >= 0);
            
            for (int i = 0; i < x; i++) {

                
                assert(obstacles.get(i) != null);
                Character character = obstacles.get(i);

                character.update();
                character.detectCollision(moving);
            }
            
            x = moving.size();
            
            for (int i = 0; i < x; i++) {
                try{
                    
                    assert(moving.get(i) != null);
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
}

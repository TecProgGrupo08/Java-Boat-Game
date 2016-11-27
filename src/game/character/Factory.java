/*
 * File name: Factory.
 * File purpose: Class that initializes elements of the game.
 */
package game.character;

import game.GameEngine;
import game.movement.Location;
import game.movement.Movement;
import game.movement.AngledAcceleration;
import game.movement.Swaying;
import game.Renderer;
import game.sprite.Sprite;
import game.sprite.Buoy;
import game.sprite.SpriteImage;
import game.Util;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import org.apache.log4j.Logger;


public class Factory {
	
	static Logger logging = Logger.getLogger(Factory.class);
	static final int staticLocationRender = 30;
	static final int initialLocation = 0;
	static final int horizontalGoal = 100; // horizontal coordinate for placing goal
	static final int verticalGoal = 50;    // horizontal coordinate for placing goal
	public Factory() {
    }
    
    /*
     * function that finalizes any object
     * @param object object that will be finalized
     */
    private void finalizeObject(Object object){
    	object = null;
    }
    
    public Obstacle createBuoy() {
    	
    	// Generates a buoy on a random place of the stage
        Renderer renderer = Renderer.getInstance();
        Obstacle buoy = new Obstacle(); 
        double randomX = (Math.random() * renderer.getWidth());
        double randomY = (Math.random() * renderer.getHeight());
        buoy.setLocation(randomX, randomY);

        Movement sway = new game.movement.Swaying(null, randomX, randomY, randomY, buoy, 1, 2);
        buoy.setMoveBehaviour(sway);
        
        // Define hitbox properties for the buoy
        int size = Util.getObstacleSize();
        Area area = new Area(new java.awt.geom.Ellipse2D.Double(randomX - size / 2, randomY - size / 2, size, size));
        AffineTransform transform = new AffineTransform();
        transform.setToTranslation(randomX, randomY);

        Sprite sprite = initializeSprite(area, buoy, size);

        buoy.setSprite(sprite);

        assert(buoy != null) : "buoy is null!";
        logging.info("Buoy created!");
        
        //Clear temporary objects of the memory
        finalizeObject(renderer);
        finalizeObject(sprite);
        finalizeObject(transform);
        finalizeObject(sway);
        finalizeObject(area);
        return buoy;

    }
    
    /*
     * function that initializes the sprites
     * @param area	location of sprite in map
     * @param buoy	type of sprite
     * @param size	size of sprite
     * @return sprite sprite object initialized
     */
    private Sprite initializeSprite(Area area, Obstacle buoy, int size){
        Sprite sprite = new Buoy(buoy);
        sprite.setUntransformedArea(area);
        sprite.setTransformedArea(area);
        sprite.setShowSprite(true);
        sprite.setHeight(size);
        sprite.setWidth(size);
        
        return sprite;
    }
    /*
     * function that initializes the characters of the game
     * @param type	type of character needed to be created
     * @return character	character created
     */
    public Character createCharacter(String type) {

    	
        Character character = null;
        
        //determine which character should be created
        switch (type) {
            case "BOAT":
                character = createBoat();
                break;
            case "COMPUTER_BOAT":
                character = createComputerBoat();
                break;
            case "HARBOUR":
                character = createHarbour();
                break;
            case "BUOY":
                character = createBuoy();
                break;
            case "ISLAND":
                character = createIsland();
                break;
            case "OCTOPUS":
                character = createOctopus();
                break;
            case "GOAL":
                character = createGoal();
                break;

        }

        assert(character != null) : "character is null!";
        
        return character;
    }
    
    /*
     * create an area give the specific location 
     * @param location - Places on the map to be filled
     * @param generalPath - Adds a point to the path by drawing a straight line from the current coordinates
     * @param x - the specified X coordinate
     * @param y - the specified Y coordinate
     * @return area - new update map
     */

    private Area createAreaFromLocations(int[] locations, GeneralPath generalPath) {
        
    	assert(locations != null) : "There are no locations";
    	assert(generalPath != null) : "No generalPath defined";
    	
    	int count = 0;
        int x = locations[count];
        int y = locations[count + 1];
        generalPath.moveTo(x, y);
        count += 2;
        while (count < locations.length) {

            x = locations[count];
            y = locations[count + 1];
            count += 2;
            generalPath.lineTo(x, y);
        }
        generalPath.closePath();
        Area area = new Area(generalPath);
        
        /*
         * If area not null, it's a success creating an valid area
         */

        assert(area != null) : "Area is null";
        
        return area;
    }
    
    /*
     * create an boat
     * @param boat - new boat created
     * @param renderer - gets map info
     * @param x - the specified X coordinate
     * @param y - the specified Y coordinate
     * @param move - the initial features of movement for the boat
     * @param images - unload the images of background
     * @param boatImages - unload the images of the boat
     * @param location - place where boat is on the map
     * @param swayMove - the initial features of sway for the boat
     * @return boat - new boat in the map
     */
    
    private Boat createBoat() {
        Boat boat = new Boat();
        Renderer renderer = Renderer.getInstance();

        int x = 10;
        int y = Renderer.getInstance().getHeight() - staticLocationRender;
        Location location = new Location(x, y);

        boat.setLocation(location);

        Image[] boatImages = new Image[2];
      
        boatImages[0] = Util.imageResources.get("BOAT");
        boatImages[1] = Util.imageResources.get("BOAT_EXPLODE");
      
        SpriteImage boatSprite = new SpriteImage(boatImages, boat);

        
        boatSprite.setTransformation(x, y, Util.getBoatArea(boatImages[0]));


        boat.setLocation(new Location(staticLocationRender, renderer.getHeight()));



        boat.setSprite(boatSprite);

        Movement move = Util.getBoatMovePresets();

        //Add a swaying motion to the boat
        Movement swayMove = new Swaying((AngledAcceleration) move, initialLocation, initialLocation, Math.random(), boat, 0.2, 0.3);
        boat.setMoveBehaviour(swayMove);
        
        /*
         * If boat not null, it's was a success creating an boat
         */
        
        assert(boat != null): "Boat is null!";
        logging.info("Boat created!");
        
        // Clear all the temporary objects
        finalizeObject(renderer);
        finalizeObject(swayMove);
        finalizeObject(move);
        finalizeObject(boatSprite);
        finalizeObject(boatImages);
        
        return boat;
    }

    /*
     * create an obstacle boat 
     * @param Enemyboat - new boat created
     * @param renderer - gets map info
     * @param x - the specified X coordinate
     * @param y - the specified Y coordinate
     * @param computerBoatMove - the initial features of movement for the enemyBoat
     * @param boatImages - unload the images of the enemyBoat
     * @return computerBoat - new boat in the map
     */
    private EnemyBoat createComputerBoat() {
    	
        EnemyBoat computerBoat = new EnemyBoat();


        Renderer renderer = Renderer.getInstance();

        computerBoat.setLocation(
                Math.random() * renderer.getWidth(),
                Math.random() * renderer.getHeight());



        Image[] boatImages = new Image[2];
        boatImages[0] = Util.imageResources.get("BOAT2");
        boatImages[1] = Util.imageResources.get("BOAT_EXPLODE");


        SpriteImage computerBoatSprite = new SpriteImage(boatImages, computerBoat);
        computerBoatSprite.setShowSprite(true);

        
        int x = 450;
        int y = 400;

        computerBoatSprite.setTransformation(x, y, Util.getBoatArea(boatImages[0]));
        computerBoat.setSprite(computerBoatSprite);

        Movement computerBoatMove = (Movement) Util.angledAccelerationPresets();
        computerBoat.setMoveBehaviour(computerBoatMove);
        
        /*
         * If computerBoat not null, it's was a success creating an computerBoat
         */

        assert(computerBoat != null) : "computerBoat is null!";
        logging.info("Computer boat created!");
        
        finalizeObject(renderer);
        finalizeObject(boatImages);
        finalizeObject(computerBoatSprite);
        finalizeObject(computerBoatMove);
        
        return computerBoat;
    }

    private Harbour createHarbour() {


        @SuppressWarnings("unused")
		Renderer renderer = Renderer.getInstance();


        Harbour harbour = new Harbour();
        game.sprite.Harbour harbourSprite = new game.sprite.Harbour(harbour);

        GeneralPath generalPath = new GeneralPath();


        int[] locations = Util.getHarbourData();
        Area area = (Area) createAreaFromLocations(locations, generalPath);


        harbour.setLocation(new Location(initialLocation, initialLocation));

        harbourSprite.setUntransformedArea(area);
        AffineTransform transform = new AffineTransform();
        transform.setToTranslation(initialLocation, initialLocation);
        harbourSprite.setTransform(transform);
        harbourSprite.setTransformedArea(area.createTransformedArea(transform));

        harbourSprite.setShowSprite(false);


        harbour.setSprite(harbourSprite);

        assert(harbour != null) : "Harbour is null!";
        logging.info("Harbour created!");
        
        return harbour;
    }

    private Island createIsland() {

        @SuppressWarnings("unused")
		GameEngine ge = GameEngine.getInstance();
        @SuppressWarnings("unused")
		Renderer renderer = Renderer.getInstance();

        Island island = new Island();
        island.setLocation(new Location(initialLocation, initialLocation));

        int[] locations = (int[]) Util.getIslandData();
        GeneralPath generalPath = new GeneralPath();
        Area area = createAreaFromLocations(locations, generalPath);
        
        AffineTransform transform = new AffineTransform();
        transform.setToIdentity();
        
        game.sprite.Island islandSprite = initializeIslandSprite(area, transform, island);

        island.setSprite(islandSprite);

        assert(island != null) : "Island is null!";
        logging.info("Island created!");
        
        return island;

    }
    
    private game.sprite.Island initializeIslandSprite(Area area, AffineTransform transform, Island island){
    	game.sprite.Island islandSprite = new game.sprite.Island(island);

        islandSprite.setUntransformedArea(area);
        islandSprite.setTransform(transform);
        islandSprite.setTransformedArea(area.createTransformedArea(transform));
        islandSprite.setShowSprite(false);
		
    	return islandSprite;
    }

    private Character createOctopus() {

    	// Place the octapus on the stage
        Image[] images = new Image[1];
        images[0] = (Image) Util.imageResources.get("OCTOPUS");
        Character octopus = new Obstacle();
        SpriteImage octopusSprite = new SpriteImage(images, octopus);
        
        Renderer renderer = Renderer.getInstance();
        Ellipse2D boundingEllipse = new Ellipse2D.Double(initialLocation, initialLocation,
                images[0].getWidth(renderer),
                images[0].getHeight(renderer));

        Area area = new Area(boundingEllipse);

        //MoveAngledAccelerate move, double x, double y, double pRandomPhase, CharacterBase owner,
        //double swayH, double swayV)
        Swaying move = new Swaying(null, initialLocation, initialLocation, Math.random(), octopus, 100.0, 1.0);
        octopus.setMoveBehaviour(move);

        octopusSprite.setUntransformedArea(area);
        AffineTransform transform = new AffineTransform();

        transform.setToIdentity();

        octopusSprite.setTransform(transform);
        octopusSprite.setTransformedArea(area.createTransformedArea(transform));
        octopusSprite.setSqueezeImageIntoTransformedArea(true);
        octopusSprite.setShowSprite(true);
        octopus.setSprite(octopusSprite);

        assert(octopus != null) : "Octopus is null!";
        logging.info("Octopus created!");
        
        return octopus;

    }

    private game.character.Goal createGoal() {


        game.character.Goal goal = new game.character.Goal();


        game.sprite.Goal goalSprite = new game.sprite.Goal(goal);

        Area area = new Area(new Rectangle(initialLocation, initialLocation, horizontalGoal, verticalGoal));

        goalSprite.setUntransformedArea(area);
        AffineTransform transform = new AffineTransform();
 
        Renderer renderer = Renderer.getInstance();
        transform.setToTranslation(renderer.getWidth() - horizontalGoal, renderer.getHeight() - verticalGoal);
        goalSprite.setTransform(transform);

        goalSprite.setTransformedArea(area.createTransformedArea(transform));

        goalSprite.setShowSprite(true);


        goal.setSprite(goalSprite);

        assert(goal != null) : "goal is null";
        logging.info("goal created!");
        
        return goal;

    }
}
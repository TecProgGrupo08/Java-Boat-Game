package game;

import game.movement.AngledAcceleration;
import game.movement.Random;
import game.movement.Location;
import game.movement.Movement;
import game.sprite.Sprite;
import java.awt.geom.Area;
import java.util.*;
import java.awt.*;
import java.net.URL;

/**
 *
 * @author Mark
 */
public class Util {

    private static MediaTracker mt = null; //global variable, not a god practice
    public static HashMap<String, Image> imageResources = new HashMap<>();

    public static Location getBoatPivotPoint(Sprite sprite) {

        assert(sprite != null);
        return new Location(sprite.getWidth() / 4, sprite.getHeight() / 2);
    }

    public static Area getBoatArea(Image img) {

        assert(img != null);
        
        Renderer renderer = Renderer.getInstance();
        assert(renderer != null);

        int boatWidth = img.getWidth(renderer);
        int boatHeight = img.getHeight(renderer);

        Area a = new Area(new Rectangle(0, 0, boatWidth * 2 / 3,
                boatHeight));
        assert(a != null);

        Polygon triangle = new Polygon();
        assert(triangle != null);

        triangle.addPoint((int) (boatWidth * 4 / 5), 0);
        triangle.addPoint((int) (boatWidth * 4 / 5), boatHeight);
        triangle.addPoint(boatWidth, (int) (boatHeight / 2));

        a.add(new Area(triangle));

        return a;

    }

    public static Movement angledAccelerationPresets() {

        AngledAcceleration angledAcceleration = new AngledAcceleration();
        assert(angledAcceleration != null);
        
        angledAcceleration.setVelocity(0.414);
        angledAcceleration.setMaxVelocity(1.414);
        angledAcceleration.setAngle(0.0);
        angledAcceleration.setAngularVelocity(0.0);
        angledAcceleration.setAngularAcceleration(0.00237);
        angledAcceleration.setAngularMaxVelocity(0.0424);
        angledAcceleration.setAngularFriction(0.00065);
        angledAcceleration.setVelocity(0.00065);
        angledAcceleration.setAcceleration(0.00326);
        angledAcceleration.setFriction(0.001);
        angledAcceleration.setBrake(0.25);

        return new Random(angledAcceleration);
    }

    public static void loadImages() {
        URL url = null;

        Renderer renderer = Renderer.getInstance();
        assert(renderer != null);

        mt = new MediaTracker(renderer);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        url = ClassLoader.getSystemResource("game/images/seaharbour.jpg");
        assert(url != null);
        
        Image imgBackground = toolkit.getImage(url);
        assert(imgBackground != null);

        mt.addImage(imgBackground, 1);

        url = ClassLoader.getSystemResource("game/images/boat_sm.gif");
        assert(url != null);

        Image imgBoat = toolkit.getImage(url);
        assert(imgBoat != null);

        imgBoat.setAccelerationPriority(1.0f);
        mt.addImage(imgBoat, 2);

        url = ClassLoader.getSystemResource("game/images/boat_sm2.gif");
        assert(url != null);

        Image imgBoat2 = toolkit.getImage(url);
        assert(imgBoat2 != null);
        imgBoat2.setAccelerationPriority(1.0f);

        mt.addImage(imgBoat2, 3);

        url = ClassLoader.getSystemResource("game/images/night.gif");
        assert(url != null);

        Image imgNight = toolkit.getImage(url);
        assert(imgNight != null);

        mt.addImage(imgNight, 4);


        url = ClassLoader.getSystemResource("game/images/boatexplode_sm.gif");
        assert(url != null);

        Image imgBoatExplode = toolkit.getImage(url);
        assert(imgBoatExplode != null);
        
        mt.addImage(imgBoatExplode, 5);

        url = ClassLoader.getSystemResource("game/images/octopus_sm.gif");
        assert(url != null);

        Image imgOctopus = toolkit.getImage(url);
        assert(imgOctopus != null);

        mt.addImage(imgOctopus, 7);


        try {
            mt.waitForAll();

            Util.imageResources.put("SEA", imgBackground);
            Util.imageResources.put("NIGHT", imgNight);
            Util.imageResources.put("BOAT", imgBoat);
            Util.imageResources.put("BOAT2", imgBoat2);
            Util.imageResources.put("BOAT_EXPLODE", imgBoatExplode);
            Util.imageResources.put("OCTOPUS", imgOctopus);
            System.out.println("Loaded images");


        } catch (Exception err) {
            System.out.println("Exception while loading");
            System.exit(-1);
        }

    }

    public static Movement getBoatMovePresets() {

        AngledAcceleration boatMove = new AngledAcceleration();
        assert(boatMove != null);

        boatMove.setAcceleration(0.0116);
        boatMove.setMaxVelocity(2.214);
        boatMove.setFriction(0.00448);
        boatMove.setAngle(-0.8);
        boatMove.setAngularVelocity(0.0);
        boatMove.setAngularAcceleration(0.00129);
        boatMove.setAngularMaxVelocity(0.0296);
        boatMove.setAngularFriction(0.000421);

        return boatMove;
    }

    public static int[] getIslandData() {


        int[] i = {167, 198,
            221, 248,
            218, 312,
            211, 366,
            191, 440,
            184, 500,
            195, 558,
            230, 558,
            224, 499,
            282, 377,
            319, 269,
            399, 240,
            395, 188,
            361, 140,
            306, 108,
            233, 132,
            181, 158,
            164, 192};
        
        assert(i != null);
        assert(i[0] == 167);

        return i;
    }

    public static int[] getHarbourData() {

        int[] i = {550, 2, 561, 54, 569, 77, 552, 137, 540, 148, 558, 175, 615, 225, 610, 260, 625, 380, 618, 461, 605, 483, 676, 484, 673, 0, 555, 1};
        
        assert(i != null);
        assert(i[0] == 550);
        
        return i;
    }

    public static int getObstacleSize() {
    	final int OBSTACLES_SIZE_ON_MAP = 20;
    	
    	return OBSTACLES_SIZE_ON_MAP;
    }

    static int getMinimumNumberOfObstacles() {
    	final int MIN_NUMBER_OF_OBSTACLES = 50;
        
    	return MIN_NUMBER_OF_OBSTACLES;
    }

    static int getMaxiumNumberOfObstacles() {
    	final int MAX_NUMBER_OF_OBSTACLES = 60;
    	
        return MAX_NUMBER_OF_OBSTACLES;
    }

    /**
     * Creates a new instance of Util
     */
    public Util() {
    }
}
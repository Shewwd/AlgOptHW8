package zorq;

import java.awt.*;
import java.util.*;

/**
 * Consolidate location for constants used within the game.  
 * These should perhaps be in the classes that use them and/or
 * tunable in a settings screen. 
 * @author John Hastings
 *
 */
public class Constants {
	
	/**
	 * Run the controllers in their own threads
	 */
	public static boolean MULTITHREADED = false;
	
	/** Show a scrolling background image? **/
	public static boolean SCROLLING_BACKGROUND = true;
	
	/** Show a scrolling background image? **/
	public static boolean DISPLAY_SHIP_STATS = true;
	public static boolean DISPLAY_GAME_INFO = true;
	//public static boolean asApplet = false;
	
	/** Run game in headless mode **/
	public static boolean HEADLESS_MODE = false;
	public static Random rnd = new Random();
	
	/** WIDTH of display = screen width **/
	public static final int DISPLAY_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	
	/** HEIGHT of display = screen size - 40 **/
	public static final int DISPLAY_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height; //-40;
	
	public static final int BONUS_SPAWN_RATE = 3000;
//	static {
//		BombBonus.setBombBonusRate(BONUS_SPAWN_RATE);
//	}
	
	/** Amount of X scaling to fit universe on the display window **/
	//public static final float scaleX = (float)Constants.DISPLAY_WIDTH/Universe.getWidth();
	/** Amount of Y scaling to fit universe on the display window **/
	//public static final float scaleY = (float)Constants.DISPLAY_HEIGHT/Universe.getHeight();
	
	public static final int numberOfAsteroids = 0; //2;

	public static final float asteroidRadius = 32.0f;
	//public static final int numberOfSplits = 3;
	
	public static final float maxShipSpeed = 5.0f;
	public static final float maxShipAccel = 1.0f;
	
	/** Log game **/
	public static boolean LOG = false; //true;
	public static Logger logWriter = new Logger();
	
	public static final boolean weapons = true;
	
	public static final boolean headsUpDisplay = true;
	public static final boolean showShipNames = true;
	public static final boolean SOUND = false;
	public static final boolean FAST_EXPLOSION = true;
	
	public static final String centerShip = "HumanPlayer";
	//public static final String centerShip = "none";
	//public static final String centerShip = "Bill";
	
	public static final int KILL_POINTS = 1000;
	public static final int RESPAWN_PENALTY = 200;
	
	public static final Color shieldColor = Color.GREEN;
	public static final Color fuelColor = Color.ORANGE;
	public static final Color bulletsColor = Color.RED;
	public static final Color livesColor = Color.YELLOW;
	public static final Color laserColor = Color.WHITE;
	public static final Color bombColor = Color.MAGENTA;
	//public static final Color shieldColor = Color.GREEN;
	//public static final Color shieldColor = Color.GREEN;

	public static Image font;
	//public static Image explode;
	
	/*
	public static AudioClip audioHop, audioOuch, audioCarCrash, audioLevel;
	static {
	audioHop = getAudioClip(getCodeBase(), "sound/boing.au");
	audioOuch = getAudioClip(getCodeBase(), "sound/ouch.au");
	audioCarCrash = getAudioClip(getCodeBase(), "sound/carcrash.au");
	audioLevel = getAudioClip(getCodeBase(), "sound/newlevel.au");
	}
	*/
}

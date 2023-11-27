package zorq;

import java.awt.Graphics;
import java.util.*;

import zorq.base.*;
import zorq.base.effects.*;

/**
 * Game universe with game objects
 * 
 * <p>Using enum singleton pattern
 * 
 * @author hastings
 *
 */
public enum Universe {
	    INSTANCE;
	
//public class Universe {
//	public static final Universe INSTANCE = new Universe();
	
	//private final transient Vector<FlyingObject> flyingObjects;
	
	private final transient Vector<ShipController> shipControllers;

	private final transient Collection<Ship> ships;
	private final transient Vector<Bullet> bullets;
	private final transient Vector<Asteroid> asteroids;
	private final transient Vector<FlyingObject> sleepingObjects;
	private final transient Vector<FlyingObject> miscFlyingObjects;
	
	//private final transient Vector<Wall> walls;
	private EffectCollection<Exhaust> exhausts;  //was: private final transient Vector<Exhaust> exhausts;
	private EffectCollection<Explosion> explosions;  //was: private final transient Vector<Explosion> explosions;
	
	private final transient Vector<Warp> warps;
	private final transient Vector<Cloak> cloaks;
	
	private volatile transient Vector<String> displayText;
	
	/**
	 * Width of the virtual universe.
	 */
	private static int width = 2400;
	
	/**
	 * Height of the virtual universe.
	 */
	private static int height = 1600;
		
	public static final int MIN_WIDTH = 800;
	public static final int MIN_HEIGHT = 533;
	public static final int MAX_WIDTH = 14400;
	public static final int MAX_HEIGHT = 9600;
	
	public static int viewPortXOffSet = 0;
	public static int viewPortYOffSet = 0;
	
	private boolean[] keyDown;
	
	private Universe(){
		//flyingObjects = new Vector<FlyingObject>();
		ships = Collections.synchronizedSet( new HashSet<Ship>() ); //HashSet is not synchronized for multithreading
		bullets = new Vector<Bullet>();
		asteroids = new Vector<Asteroid>();
		sleepingObjects = new Vector<FlyingObject>();
		miscFlyingObjects = new Vector<FlyingObject>();
		//walls = new Vector<Wall>();
		
		exhausts = new EffectCollection<Exhaust>();
		explosions = new EffectCollection<Explosion>();
		warps = new Vector<Warp>();
		cloaks = new Vector<Cloak>();
		
		shipControllers = new Vector<ShipController>();
	}

	protected void moveObjects() {
		for (FlyingObject flyingObject : this.getFlyingObjects()) {
			flyingObject.next();
		}
	}

	protected void step () {
		exhausts.nextStep();
		exhausts.cleanUp();

		explosions.nextStep();
		explosions.cleanUp();
	}
	
	protected void stepUniverse() {
		this.moveObjects();
		this.step(); // step universe objects to the next frame. Probably should be combined with moveObjects
		checkForCollisions();
	}

	/**
	 * Only check for collisions between ships and other game objects.
	 */
	private void checkForCollisions() {

		//System.out.println("sleepingObjects size="+gd.sleepingObjects.size());
		Vector<FlyingObject> flyingObjects = this.getFlyingObjects();
		Vector<FlyingObject> addObjects = new Vector<FlyingObject>();
		Vector<FlyingObject> removeObjects = new Vector<FlyingObject>();
		for (int i = 0; i < flyingObjects.size(); i++) {  // looping through all to handle off universe

			FlyingObject flyingObject1 = flyingObjects.get(i);
			boolean remove0 = flyingObject1.handleOffUniverse(this);
			if (remove0) {
				removeObjects.add(flyingObject1);  //add to list of objects to remove at the end of the loop
				// If a ship flies off, add to sleeping to wake up later
				if (flyingObject1 instanceof Ship) this.addSleeping(flyingObject1);
			}

			if (! (flyingObject1 instanceof Ship)) continue;
			//for (int j = i + 1; j < flyingObjects.size(); j++) {
			for (int j = 0; j < flyingObjects.size(); j++) {
				FlyingObject flyingObject2 = flyingObjects.get(j);
if (flyingObject1 == flyingObject2) continue;
				//If there is a collision...
				if ( flyingObject1.intersects(flyingObject2) ) {
					
					//System.out.println("collision "+flyingObject1 + ", "+flyingObject2);
					// ... have the objects react according to what hit them
					boolean remove1 = flyingObject1.hitBy(flyingObject2, this);
					boolean remove2 = flyingObject2.hitBy(flyingObject1, this);

					if (remove1) removeObjects.add(flyingObject1);
					if (remove2) removeObjects.add(flyingObject2);

					if (flyingObject1 instanceof Asteroid)
						addObjects.addAll(((Asteroid)flyingObject1).spawn());
					if (flyingObject2 instanceof Asteroid)
						addObjects.addAll(((Asteroid)flyingObject2).spawn());
					if (remove1 && !remove0 && (flyingObject1 instanceof Ship))
						this.addSleeping(flyingObject1);
					if (remove2 && (flyingObject2 instanceof Ship ))
						this.addSleeping(flyingObject2);
				}
			}
		}
		this.addAll(addObjects);
		this.removeAll(removeObjects);
	}
	
	/**
	 * Check for collisions between every pair of game objects.
	 */
	private void checkForCollisions2() {

		//System.out.println("sleepingObjects size="+gd.sleepingObjects.size());
		Vector<FlyingObject> flyingObjects = this.getFlyingObjects();
		Vector<FlyingObject> addObjects = new Vector<FlyingObject>();
		Vector<FlyingObject> removeObjects = new Vector<FlyingObject>();
		for (int i = 0; i < flyingObjects.size(); i++) {  // looping through all to handle off universe

			FlyingObject flyingObject1 = flyingObjects.get(i);
			boolean remove0 = flyingObject1.handleOffUniverse(this);
			if (remove0) {
				removeObjects.add(flyingObject1);  //add to list of objects to remove at the end of the loop
				// If a ship flies off, add to sleeping to wake up later
				if (flyingObject1 instanceof Ship) this.addSleeping(flyingObject1);
			}

			for (int j = i + 1; j < flyingObjects.size(); j++) {
				FlyingObject flyingObject2 = flyingObjects.get(j);

				//If there is a collision...
				if ( flyingObject1.intersects(flyingObject2) ) {
					
					// ... have the objects react according to what hit them
					boolean remove1 = flyingObject1.hitBy(flyingObject2, this);
					boolean remove2 = flyingObject2.hitBy(flyingObject1, this);

					if (remove1) removeObjects.add(flyingObject1);
					if (remove2) removeObjects.add(flyingObject2);

					if (flyingObject1 instanceof Asteroid)
						addObjects.addAll(((Asteroid)flyingObject1).spawn());
					if (flyingObject2 instanceof Asteroid)
						addObjects.addAll(((Asteroid)flyingObject2).spawn());
					if (remove1 && !remove0 && (flyingObject1 instanceof Ship))
						this.addSleeping(flyingObject1);
					if (remove2 && (flyingObject2 instanceof Ship ))
						this.addSleeping(flyingObject2);
				}
			}
		}
		this.addAll(addObjects);
		this.removeAll(removeObjects);
	}
	
	protected void reset(){  // was public until Cornelius
		ships.clear();
		bullets.clear();
		asteroids.clear();
		sleepingObjects.clear();
		miscFlyingObjects.clear();
		//walls = new Vector<Wall>();
		
		//exaust.clear();
		//explosion.clear();
		exhausts = new EffectCollection<Exhaust>();
		explosions = new EffectCollection<Explosion>();
		warps.clear();
		cloaks.clear();
		
		shipControllers.clear();
	}

	protected void addSleeping(FlyingObject fo) { 
		sleepingObjects.add(fo);
	}

	protected void removeSleeping(FlyingObject fo) { 
		sleepingObjects.remove(fo);
	}
	
	protected void add(FlyingObject fo) { 
		if (fo instanceof Ship) ships.add((Ship)fo);
		else if (fo instanceof Bullet) bullets.add((Bullet)fo);
		else if (fo instanceof Asteroid) asteroids.add((Asteroid)fo);
		else miscFlyingObjects.add(fo);
	}
	
	protected void remove(FlyingObject fo) {
		if (fo instanceof Ship) ships.remove((Ship)fo);
		else if (fo instanceof Bullet) {
			bullets.remove((Bullet)fo);
		}
		else if (fo instanceof Asteroid) asteroids.remove((Asteroid)fo);
		else miscFlyingObjects.remove(fo);
	}
	
	protected void addAll(Vector<FlyingObject> v) {
		for (FlyingObject fo : v) {
			this.add(fo);
		}
	}
	
	protected void removeAll(Vector<FlyingObject> v) { 
		for (FlyingObject fo : v) {
			this.remove(fo);
		}
	}	

	// following mess used so that the caller of getFlyingObjects can be determined
	// (safer than having the object pass in itself to getFlyingObjects)
    private static final CallerResolver CALLER_RESOLVER; // set in <clinit>
    
	// subclass SecurityManager in order to make getClassContext() accessible
    private static final class CallerResolver extends SecurityManager {
        protected Class [] getClassContext () { return super.getClassContext (); }
    }
    
    static {
        try {
            // fails if the current SecurityManager does not allow
            // RuntimePermission ("createSecurityManager"):
            CALLER_RESOLVER = new CallerResolver ();
        }
        catch (SecurityException se) {
            throw new RuntimeException ("ClassLoaderResolver: could not create CallerResolver: " + se);
        }
    }
    
	public Vector<FlyingObject> getFlyingObjects() {
		
		Vector<FlyingObject> flyingObjects = new Vector<FlyingObject>();
		flyingObjects.addAll(miscFlyingObjects); // add miscFlyingObjects first so that black holes are drawn behind ships
		
		flyingObjects.addAll(ships);
		flyingObjects.addAll(bullets);
		flyingObjects.addAll(asteroids);
		//flyingObjects.addAll(miscFlyingObjects); // add miscFlyingObjects first so that black holes are drawn behind ships
		 
		return flyingObjects;
		//return (Vector<FlyingObject>)(flyingObjects.clone());
	}
	
	/*
	protected Vector<Bullet> getBullets() {
		return bullets;
	}
	*/

	protected Vector<FlyingObject> getSleeping() {
		return sleepingObjects;
	}

	/*
	protected Vector<Ship> getShips() {
		Vector<Ship> shs = new Vector<Ship>();
		shs.addAll(ships);
		return shs;
	}
	*/

	/*
	protected Vector<Asteroid> getAsteroids() {
		return asteroids;
	}
	*/

	/*
	public Vector<Wall> getWalls() {
		return walls;
	}
	*/

	protected Vector<String> getDisplayText() {  //was public until 11-18-2019
		return displayText;
	}

	protected void setDisplayText(Vector<String> displayText) {   //was public until 11-18-2019
		this.displayText = displayText;
	}

	protected Vector<ShipController> getShipControllers() {   //was public until 11-18-2019
		return shipControllers;
	}

	public boolean[] getKeyDown() { return keyDown; }
	public void setKeyDown(boolean[] keyDown) { this.keyDown = keyDown; }

	//public Vector<Exhaust> getExaust() {return exaust;}
	protected void addExhaust(float x, float y) { exhausts.add(new Exhaust(x,y)); }   //was public until 11-18-2019
	protected void paintExhausts(Graphics g) {   //was public until 11-18-2019
		exhausts.paint(g); 
	}

	//public Vector<Explosion> getExplosion() {return explosion;}
	public void addExplosion(float x, float y) { explosions.add(new Explosion(x,y)); }
	protected void paintExplosions(Graphics g) {    //was public until 11-18-2019
		explosions.paint(g); 
	}
	
	protected Vector<Warp> getWarp() {return warps;}   //was public until 11-18-2019

	protected Vector<Cloak> getCloak() {return cloaks;}

	public static int getHeight() { return Universe.height; }
	public static int getWidth() { return Universe.width; }
	protected static void setHeight(int height) {Universe.height=height;}
	protected static void setWidth(int width) {Universe.width=width;}
}

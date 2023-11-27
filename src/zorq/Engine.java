package zorq;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import java.text.NumberFormat;
import javax.swing.JOptionPane;

import zorq.base.*;
import zorq.base.effects.*;

/**
 * Game engine with main game loop.
 * 
 * This class should be refactored to include a "step" method to step the 
 * universe to the next frame. This would help support unit testing
 * 
 * <p>Using singleton pattern
 * 
 * @author John Hastings
 *
 */
public final class Engine implements KeyListener {

	private static Engine instance = null;  

	protected synchronized static Engine getInstance(Visualizer v, Vector<String> controllers) throws Exception {
		if(instance == null) instance = new Engine(v, controllers);
		return instance;
	}

	/** Visualizer game display. **/  
	private final Visualizer v;
	private boolean spawnBonuses;
	private boolean running;

	/** Universe of game objects. **/  
	public final Universe u;

	private final boolean[] keyDown;
	//private float bonusScale;

	// one thread for calling each controller.
	// initialize after initializing universe and controllers
	private final ControllerThread [] controllerThreads;

	/** Collection of ship controllers. **/
	final Vector<String> controllers; 
	long frames;
	//Image bulletImage;
	private final Random rnd;

	/**
	 * Create the game engine, and connect it to the visualizer/display and ship controllers.
	 * 
	 * For testing purposes, the visualizer can be set to null to step the universe forward
	 */
	private Engine(Visualizer v, Vector<String> controllers) throws Exception {

		this.v = v;		//this.controllers = controllers;

		if (controllers == null || controllers.size() == 0) {
			this.controllers = new Vector<String>();
			String[] names = { 
					"ExampleShipController",
					"ExampleShipController2",
			"HumanPlayer"};

			for (String s : names)
				this.controllers.add("zorq.controllers." + s);

		}
		else this.controllers = controllers;

		u = Universe.INSTANCE;

		keyDown = new boolean[256];
		for (int i = 0; i < keyDown.length; i++)
			keyDown[i] = false;

		u.setKeyDown(keyDown);

		rnd = Constants.rnd;  //initialize before calling init
		init();

		// create a thread for each controller
		// must be called after ship controllers are created in init
		if (Constants.MULTITHREADED ) {
			Vector<ShipController> sc = u.getShipControllers();
			int numControllers = sc.size();
			controllerThreads = new ControllerThread[numControllers];
			for (int i=0; i<numControllers; i++)  {
				controllerThreads[i] = new ControllerThread();
				//System.out.println("created " + controllerThreads[i]);
				controllerThreads[i].setShipController(sc.get(i));
			}
		}
		else controllerThreads = null;

	}

	protected void setSpawnBonuses(boolean spawnBonuses) {
		this.spawnBonuses = spawnBonuses;
	}
	/**
	 * Initialize the game engine before running it.  Includes loading the game images and connecting the controllers to ships.
	 */
	private void init() throws Exception {

		spawnBonuses = true;
		u.reset();

		if(Constants.LOG) Constants.logWriter.writeHeader();

		//Ship.shipImage = Toolkit.getDefaultToolkit().getImage("images/ships.png");

		Exhaust.exhaustImage = Toolkit.getDefaultToolkit().getImage("images/exhaust.png");
		Explosion.explosionImage = Toolkit.getDefaultToolkit().getImage("images/explode.png");
		Warp.warpImage = Toolkit.getDefaultToolkit().getImage("images/warp.png");
		Cloak.cloakImage = Toolkit.getDefaultToolkit().getImage("images/empburst.png");
		//BlackHole.holeImage = Toolkit.getDefaultToolkit().getImage("images/blackhole2.png");
		//BombBonus.bombImage = Toolkit.getDefaultToolkit().getImage("images/bomb.png");
		//PointsBonus.pointsImage = Toolkit.getDefaultToolkit().getImage("images/dollar.png");
		//BulletBonus.bulletBonusImage = Toolkit.getDefaultToolkit().getImage("images/bullet.png");
		//FuelBonus.fuelBonusImage = Toolkit.getDefaultToolkit().getImage("images/fuel2.png");
		//ShieldBonus.shieldBonusImage = Toolkit.getDefaultToolkit().getImage("images/shield.png");
		//LaserBonus.laserBonusImage = Toolkit.getDefaultToolkit().getImage("images/laser.png");
		//Bullet.bulletImage = Toolkit.getDefaultToolkit().getImage("images/bombs.png");
		//EmpBonus.empBonusImage = Toolkit.getDefaultToolkit().getImage("images/emp.png");
		//MagnetBonus.magnetBonusImage = Toolkit.getDefaultToolkit().getImage("images/magnet2.png");
		//ShipJumpBonus.shipJumpBonusImage = Toolkit.getDefaultToolkit().getImage("images/stargate.png");

		Constants.font = Toolkit.getDefaultToolkit().getImage("images/font.png");


		int i =0;

		for (String s: controllers) {
			try {
				//System.out.println("s = " + s);
				Class c = Class.forName(s);
				Constructor cs = c.getConstructor((new Class[] { Ship.class }));

				int x = 0;
				int y = 0;
				while(true){
					x = 40 + rnd.nextInt(Universe.getWidth() - 80);
					y = 40 + rnd.nextInt(Universe.getHeight() - 80);	

					boolean close = false;
					for(FlyingObject fo : u.getFlyingObjects()){
						float distance = (float)Math.sqrt(
								Math.pow(x - fo.getX(),2.0) + 
								Math.pow(y - fo.getY(),2.0));

						if(distance < 30)
							close = true;
					}


					if(!close)
						break;
				}

				Ship ship = new Ship(x, y, i);
				ship.setRadius(14.0f);

				u.add(ship);
				ShipController sc = (ShipController) cs.newInstance((new Object[] { ship }));
				ship.setShipController(sc);
				u.getShipControllers().add(sc);

				if(Constants.LOG) Constants.logWriter.logShipCreate(ship);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}

		/*
		AIShip ai = new AIShip(100,100,0);
		//ai.makeInvisible();
		gd.add(ai);
		ShipController sc = new TedAIShipController(ai);
		gd.getShipController().add(sc);
		if(Constants.log) Constants.logWriter.logShipCreate(ai);

		AIShip ai2 = new AIShip(800,600,0);
		//ai2.makeInvisible();
		gd.add(ai2);
		ShipController sc2 = new ScavengerAIShipController(ai2);
		gd.getShipController().add(sc2);
		if(Constants.log) Constants.logWriter.logShipCreate(ai2);
		 */

		//*/

		/*
		 * for(int i = 0; i < 10; i++){ ship = new
		 * Ship(175+rnd.nextInt(Constants.width),175+rnd.nextInt(Constants.height));
		 * ship.setShipImage(shipImage); gd.getShips().add(ship);
		 * gd.getShipController().add(new ExampleShipController(ship)); }
		 * 
		 * for(int i = 0; i < 10; i++){ ship = new
		 * Ship(175+rnd.nextInt(Constants.width),175+rnd.nextInt(Constants.height));
		 * ship.setShipImage(shipImage); gd.getShips().add(ship);
		 * gd.getShipController().add(new ExampleShipController2(ship)); }
		 */

		// This should be using the universe width if asteroids are readded
		for (int j = 0; j < Constants.numberOfAsteroids; j++) {
			Asteroid a = new Asteroid(rnd.nextInt(Constants.DISPLAY_WIDTH), rnd.nextInt(Constants.DISPLAY_HEIGHT));
			a.setRadius(Constants.asteroidRadius);
			a.setHeading(2.0f * (float) Math.PI * rnd.nextFloat());
			a.setSpeed(2.0f + 2* rnd.nextFloat());
			u.add(a);
		}

		//bonusScale = this.controllers.size();

	}

	public void stop() { running = false; }


	/**
	 * Runs the game loop of the game engine.  Spawns game objects, displays scores, checks for collisions, moves objects.
	 */
	public void run() {

		running = true;
		//boolean display = false;
		//long startTime;
		long lastUpdate=System.nanoTime();

		int fps = 0;
		double timePassed=0;
		long sleepTime=0;

		long delay = 33;
		//long delay = 400;

		frames = 0;
		//boolean done = false;
		boolean paused = false;

		while (running) {

			// only handle keyboard and fps stats in graphical mode
			if (v != null) 
			{
				// get keyboard input to pause or change rate of game
				if(u.getKeyDown()[KeyEvent.VK_P]){
					paused = !paused;
					u.getKeyDown()[KeyEvent.VK_P] = false;
				}
				if(u.getKeyDown()[KeyEvent.VK_ESCAPE]){
					u.getKeyDown()[KeyEvent.VK_ESCAPE] = false;
					if (JOptionPane.showConfirmDialog(Main.INSTANCE.getMainFrame(), 
							"Are you sure to exit the game?", "Really Closing?", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
						System.exit(0);
					}
				}
				if(u.getKeyDown()[KeyEvent.VK_O]){
					delay++;
					u.getKeyDown()[KeyEvent.VK_O] = false;
				}
				if(u.getKeyDown()[KeyEvent.VK_L]){
					if (delay > 0)
						delay--;
					u.getKeyDown()[KeyEvent.VK_L] = false;
				}
				if(u.getKeyDown()[KeyEvent.VK_T]){
					Constants.DISPLAY_SHIP_STATS = !Constants.DISPLAY_SHIP_STATS;
					u.getKeyDown()[KeyEvent.VK_T] = false;
				}
				if(u.getKeyDown()[KeyEvent.VK_R]){
					Constants.DISPLAY_GAME_INFO = !Constants.DISPLAY_GAME_INFO;
					u.getKeyDown()[KeyEvent.VK_R] = false;
				}

				// ensure animation maintains the proper rate
				timePassed = (System.nanoTime()-lastUpdate)/1000000.0;
				lastUpdate=System.nanoTime();
				fps = (int) (1d / (double) timePassed * 1000d);

				updateGameInfo(fps, (long)timePassed+sleepTime);  // display the current game state along with key hints
				//}

			}
			if (!paused) {
				u.stepUniverse();
				stepUniverse2();
				updateDisplay();
			}

			// only handle game rate and fps stats in graphical mode
			if (v != null ) {
				timePassed = (System.nanoTime()-lastUpdate)/1000000;
				fps = (int) (1d / (double) (timePassed + sleepTime) * 1000d);

				sleepTime = Math.max(delay - (long)timePassed, 0);
			}

			frames++;

			// only smooth game rate in graphical mode
			if (v != null) {
				try { Thread.sleep(sleepTime, 0); } 
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
	}

	/** 
	 * move objects, step the game forward, check for collisions (remove objects)
	 * 
	 * <p>ensure that the controllers can't call this!!!
	 * 
	 * timePassedMs corresponds to the cycle time show in the graphical game stats
	 */
	//protected void stepUniverse(long timePassedMs) {
	/*
	protected void stepUniverse() {
		u.moveObjects();
		u.step(); // step universe objects to the next frame. Probably should be combined with moveObjects
		checkForCollisions();
		
		//moveObjects(timePassedMs);
		//u.moveObjects();
		//u.step(); // step universe objects to the next frame. Probably should be combined with moveObjects
	}
	*/
	
	/**
	 * spawn bonuses, wake up sleeping objects if time, trigger controller moves
	 */
	private void stepUniverse2() {
		if (spawnBonuses) spawnBonuses();
		handleSleeping();  // wake up appropriate sleeping objects
		controllersAct();
	}

	private void controllersAct() {
		if (Constants.MULTITHREADED) {
			//System.out.println("thread size = " + controllerThreads.length);

			u.getShipControllers().parallelStream() // <-- This one uses parallel
		    .forEach(sc -> 
		    { try {
		    	controllerAct(sc);
		    }
		    catch (Exception e) {
		    	System.out.println("problem processing " + sc);
		    	e.printStackTrace();
		    }
		    });
			/*
			try{
				for (ControllerThread ct: controllerThreads)  {
					//System.out.println("starting " + ct);
					if (ct.hasRun) ct.run();
					else ct.start();
				}
				for (ControllerThread ct: controllerThreads) {
					ct.join();
				}
			} catch(Exception e){
				//System.out.println("exception");
				//e.printStackTrace();
			}
			*/
		}
		else {
			for (ShipController sc : u.getShipControllers())
				try {
			    	controllerAct(sc);
			    }
			    catch (Exception e) {
			    	System.out.println("problem processing " + sc);
			    	e.printStackTrace();
			    }
				//calculateAcceleration(sc);
		}
	}
	
	// a thread for calling the ship controller makemove.... 
	// from calculateAcceleration
	class ControllerThread extends Thread {
		ShipController sc = null;
		boolean hasRun = false; // boolean to make sure that start is only called the first time
		void setShipController(ShipController sc) {this.sc = sc; }
		boolean getHasRun() { return hasRun; }

		public void run() {
			//System.out.println("run thread = " + this);
			controllerAct(sc); 
			//System.out.println("done with thread = " + this);
			hasRun = true;
		}
	}

	private void spawnBonuses() {
		//if (rnd.nextFloat()*20000 < 1) {


		//BombBonus bb = new BombBonus(0, 0, 0);
		//return Package.getPackage(packageName);

		SpawnFactory.makeSpawn(u);


	}

	// update the game display text in the universe is displayed by the visualizer
	// cycleTime = timePassed in milliseconds
	// maybe some/all of this should go in Visualizer???
	private void updateGameInfo(int fps, long cycleTime) {
		// only update game results/info every 30 frames
		if (frames%20 != 0) return;
		
		// display game key hints, data and scores
		u.setDisplayText(new Vector<String>());
		u.getDisplayText().add("O:slow L:fast P:pause Esc:exit");
		u.getDisplayText().add("T:toggle stats R:toggle scores");
		u.getDisplayText().add("FPS:  " + fps);
		u.getDisplayText().add("Cycle Time:  " + cycleTime + "ms");

		if (!Constants.DISPLAY_GAME_INFO) return;
		
		ShipController arr[] = new ShipController[u.getShipControllers().size()];
        arr = u.getShipControllers().toArray(arr);
        Arrays.sort(arr);
		for(ShipController sc : arr){	
			u.getDisplayText().add(sc.getName()+":  " + sc.getShip().getScore());
		}
	}

	/**
	 * Wake up any sleeping objects and add them back to the collection of relevant flying objects.
	 */
	private void handleSleeping() {
		Vector <FlyingObject> addGroup = new Vector<FlyingObject>();
		for (FlyingObject fo : u.getSleeping()) {
			if (fo instanceof Ship ) {
				Ship sh = (Ship) fo;
				if (!sh.isSleeping()) {
					//System.out.println("waking "+sh.getShipController().getName());
					sh.setAlive(true);
					sh.setX( rnd.nextInt(Universe.getWidth())); 
					sh.setY( rnd.nextInt(Universe.getHeight())); 
					addGroup.add(fo);
				}
				else
					sh.decrementSleep();
			}
		}
		for (FlyingObject fo : addGroup) {
			u.removeSleeping(fo);
			u.add(fo);
		}
	}

	/**
	 * Check for collisions between every pair of game objects.
	 */
	/*
	private void checkForCollisions() {

		//System.out.println("sleepingObjects size="+gd.sleepingObjects.size());
		Vector<FlyingObject> flyingObjects = u.getFlyingObjects();
		Vector<FlyingObject> addObjects = new Vector<FlyingObject>();
		Vector<FlyingObject> removeObjects = new Vector<FlyingObject>();
		for (int i = 0; i < flyingObjects.size(); i++) {  // looping through all to handle off universe

			FlyingObject flyingObject1 = flyingObjects.get(i);
			boolean remove0 = flyingObject1.handleOffUniverse(u);
			if (remove0) {
				removeObjects.add(flyingObject1);  //add to list of objects to remove at the end of the loop
				// If a ship flies off, add to sleeping to wake up later
				if (flyingObject1 instanceof Ship) u.addSleeping(flyingObject1);
			}

			for (int j = i + 1; j < flyingObjects.size(); j++) {
				FlyingObject flyingObject2 = flyingObjects.get(j);

				//If there is a collision...
				if ( flyingObject1.intersects(flyingObject2) ) {
					
					// ... have the objects react according to what hit them
					boolean remove1 = flyingObject1.hitBy(flyingObject2, u);
					boolean remove2 = flyingObject2.hitBy(flyingObject1, u);

					if (remove1) removeObjects.add(flyingObject1);
					if (remove2) removeObjects.add(flyingObject2);

					if (flyingObject1 instanceof Asteroid)
						addObjects.addAll(((Asteroid)flyingObject1).spawn());
					if (flyingObject2 instanceof Asteroid)
						addObjects.addAll(((Asteroid)flyingObject2).spawn());
					if (remove1 && !remove0 && (flyingObject1 instanceof Ship))
						u.addSleeping(flyingObject1);
					if (remove2 && (flyingObject2 instanceof Ship ))
						u.addSleeping(flyingObject2);
				}
			}
		}
		u.addAll(addObjects);
		u.removeAll(removeObjects);
	}
	*/

	private NumberFormat numberFormat = NumberFormat.getInstance(); //.format(frames);
	private long lastTime = System.currentTimeMillis();
	private long newTime = 0;
	private long elapsed = 0;
	private long FRAME_STEP = 10000; // update headless stats after that many frames

	/**
	 * Update the display based on the current game objects.
	 */
	private void updateDisplay() { 
		if (v != null) 
			v.updateDisplay(u);
		else {
			if (frames%FRAME_STEP == 0) {
				newTime = System.currentTimeMillis();
				elapsed = newTime-lastTime;
				System.out.println("frames = " + numberFormat.format(frames) + "   elapsed="+ elapsed+"ms " +
						(int)((double)FRAME_STEP/elapsed*1000)+" fps "+" fo="+u.getFlyingObjects().size());
				//System.out.println("frames = " + frames + "   elapsed="+ (newTime-lastTime)/1000+"s");
				lastTime = newTime;
				if (frames%(FRAME_STEP*20) == 0) System.gc();
			}
		}
	}

	// allows the ship controller to act
	private void controllerAct(ShipController shipController) {

		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Double> times = new ArrayList<Double>();

		// Is this correct?  Refers to the display sizes
		if(shipController.getName().equals(Constants.centerShip)){
			Universe.viewPortXOffSet = Constants.DISPLAY_WIDTH/2 - (int)shipController.getShip().getX();
			Universe.viewPortYOffSet = (int)shipController.getShip().getY() - Constants.DISPLAY_HEIGHT/2 ;
		}

		//for (ShipController shipController : gd.getShipController()) {

		ControllerAction ca = null;

		double calculationTime;
		if (shipController.getShip().isDisabled()) {
			calculationTime = 0;
		}
		if (shipController.getShip().isAlive()) {
			calculationTime = System.nanoTime();
			ca = shipController.makeLoggedMove(u);
			calculationTime = System.nanoTime() - calculationTime;
			if(Constants.LOG) Constants.logWriter.logShipAction(shipController.getShip(),ca,calculationTime);
		} 
		else {
			calculationTime = 0;
		}

		if(calculationTime != 0){
			if(names.size() == 0){
				names.add(shipController.getName());
				times.add(calculationTime);
			}
			else {
				int i;
				for(i = 0; i < names.size(); i++){
					if(times.get(i).doubleValue() > calculationTime){
						names.add(i,shipController.getName());
						times.add(i,calculationTime);
						break;
					}
				}
				if(i == names.size()){
					names.add(shipController.getName());
					times.add(calculationTime);
				}

			}
		}

		Ship ship = shipController.getShip();
		if (ca != null) {
			// look at moving the next two lines.  Clean this up.  Do these go here???
			if (!ship.isAlive() || ship.isDisabled()) return;
			if (ship.isMagnetic()) ship.magnetNearbyBonuses(u);
			ca.act(ship, u);
		}
		else  // ignore any invalid actions -- added 11-20-2019
			;
	}

	//private void moveObjects(long timePassedMs) {
	/*
	private void moveObjects() {
		for (FlyingObject flyingObject : u.getFlyingObjects()) {
			flyingObject.next();
		}
	}
	*/

	public void keyTyped(KeyEvent arg0) { }

	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() > 0 && arg0.getKeyCode() < keyDown.length)
			keyDown[arg0.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() > 0 && arg0.getKeyCode() < keyDown.length)
			keyDown[arg0.getKeyCode()] = false;
	}

}

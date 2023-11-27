package zorq.controllers;

import zorq.*;
import zorq.base.*;

/** 
 * Example of a <b>very</b> basic ship controller which targets the nearest ship
 * and fires a bullet every 30 frames (until out of rounds).  The controller also
 * alternates between flying and stopped every 100 frames.  Notice that a ship can
 * stop on a dime, reorient it's direction and start moving again after 100 frames.
 * 
 * <p>This controller loops through the game objects. You will need to do this to
 * decide on an action. Notice the use of instanceof. You can use this to reason differently
 * about each game object based on their class membership. Also, you will want to skip past
 * your own ship as this controller does.
 * 
 * <p>This controller does not properly lead the moving targets because it's using 
 * getX and getY. There are getNextX(i) and getNextY(i) methods which attempt
 * to address this. You might adjust i based on distance.
 * 
 * <p>
 * Declare instance variables as needed for your controller. For example, if you decide
 * on a goal which takes more than one action to implement, you might need instance
 * variables to keep track of that. This example has just two: stop, i
 * 
 * <p>This class attempts to demonstrate the idea of self documenting code with embedded html formatted text.
 * 
 * <p>Possible approaches to implementing a controller include:
 * <ul>
 * <li>if-then-else statements which look for certain situations
 * <li>Loop through the game objects, and for each one, create a corresponding Action 
 * (you would define this), and add to a priority queue. At the end, remove the highest priority
 * action from the queue (it may or may not be the same as a multi-stage action which has already been initiated)
 * <li>Or, some other idea.  Be creative!  It's ok to try different approaches.
 * <li>For a more research oriented idea (I don't recommend this approach right now), perhaps a state machine for keeping track of the current state of the controller and how transitions to new states occur:
 * <a href="https://www.baeldung.com/java-enum-simple-state-machine">https://www.baeldung.com/java-enum-simple-state-machine</a>
 * <a href="https://cleanjava.wordpress.com/2012/02/25/enum-as-state-machine/">https://cleanjava.wordpress.com/2012/02/25/enum-as-state-machine</a>
 * </ul>
 * 
 * @author John Hastings
 **/
public final class ExampleShipController2 extends ShipController{
	
	public ExampleShipController2(Ship ship) {
		super(ship);
	}

	@Override
	public String getName() { return "Slightly less stupid"; }
	private boolean stop = false;
	private int i = 0;  // frame counter used only for this stupid controller, resets after 1000 frames
	
	/**
	 * Called by the engine after each frame to get the next move of the
	 * ship/controller. This method can loop through the flying objects to determine an 
	 * action to return.
	 * 
	 * <p>This example controller fires a bullet every 30 frames, and alternates 
	 * between flying toward a target and stopping, and making a flight adjustment.  Note that only one
	 * action can be returned each time makeMove is called.  So, you might need to use status variables to keep track of what was previously done,
	 * and what remains to be done.
	 */
	@Override
	public ControllerAction makeMove(Universe u) {
				
		i = (i+1)%1000;
		
		if(i%30 == 0){ return new FireBullet(); }
			
		FlightAdjustment fa = new FlightAdjustment();

		FlyingObject closestObject = null;
		float closestObjectDistance = Float.MAX_VALUE;

		// loop through all flying objects
		// find the closest ship.  This controller is ignoring bonuses.
		for (FlyingObject flyingObject : u.getFlyingObjects()) {
			
			// this controller is only looks at other ships and ignores everything else
			if(ship.equals(flyingObject) ||             //skip my own ship
					!(flyingObject instanceof Ship))    //only look for other ships
				continue;
			
			float distance = Util.distance (ship.getX(),ship.getY(),flyingObject.getX(),flyingObject.getY() );
			
			if(distance < closestObjectDistance){
				closestObjectDistance = distance;
				closestObject = flyingObject;
			}	
		}
		
		// if 100 frames have passed, switch from flying to stopped or vice versa
		if (i%100 == 0) { stop = !stop; }

		if (stop) {
			fa.setStop(true);
			fa.setAcceleration(0f);
		}
		else {
			fa.setAcceleration(.05f);
		}

		// only update the facing if closestObject is not null
		// should lead the ship based on expected future location
		if (closestObject != null ) {
			// set the facing toward the closest object
			float angle = Util.calcAngle(ship.getX(), ship.getY(), 
					closestObject.getX(), closestObject.getY());
			fa.setFacing(angle);
		}

		return fa;
	}

}

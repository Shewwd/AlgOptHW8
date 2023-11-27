package zorq.controllers;

import java.util.PriorityQueue;

import zorq.*;
import zorq.base.ShipController;

import java.util.Arrays;
import java.util.Comparator;

/** 
 * Example of the <b>beginnings</b> of a ship controller which combines a priority 
 * queue and a finite state machine. 
 *
 * <p>The priority queue holds potential controller  actions ordered with the numerically
 * smallest action at the front. This score is derived by multiplying a priority by the
 * distance to an object in the universe. The implement scoring is quite simplistic
 * with only current actions being to move toward bonuses generically (with no priority
 * on different bonuses) or to aim at ships. Other priorities might be added to 
 * implement other goals. In addition, the weighting used for bonuses (5) and ships (1)
 * is just a starting point. Ships received a weighting of 1 to ensure that nearby ships
 * receive a higher priority (due to their inherent danger) and nearby bonuses. Also,
 * the weighting doesn't factor in the current "state" of the ship. For example, a ship
 * might be low on ammo, but good on fuel, and decide to transition to a state of
 * FLEE (or EVADE?) and fly away. Thus, the priorities could vary depending on the state.
 * 
 * <p>The controller only has two states: REGULAR and FIRE_NOW (meaning that a ship has 
 * been targeted and the ship is ready to fire the next time an action is requested).
 * These states could be expanded according to the philosophy you want to implement.
 * 
 * <p>This controller loops through the game objects. For each one, it determines a
 * corresponding action, and adds the action to a priority queue. At the end, the
 * highest priority action is selected and a controller action is constructed and
 * returned.
 * 
 * <p>Note that ship only sets acceleration once and then coasts. So it doesn't attain
 * top speed.
 *
 * <p>You will need to loop through the game objects to
 * decide on an action. Notice the use of instanceof. You can use this to reason differently
 * about each game object based on their class membership. Also, you will want to skip past
 * your own ship as this controller does.
 * 
 * <p>This controller attempts to lead the moving targets using getNextX(i) and getNextY(i) 
 * methods. You might adjust i based on distance.
 * 
 * <p>This class attempts to demonstrate the idea of self documenting code with embedded html formatted text.
 * 
 * <p>Other approaches to implementing a controller could include:
 * <ul>
 * <li>if-then-else statements which look for certain situations
 * <li>An AI or hybrid AI approach which attempts to learn during game play.
 * <li>Or, some other idea.  Be creative!  It's ok to try different approaches.
 * </ul>
 * 
 * @author John Hastings
 **/

public class ExampleShipControllerPQ extends ShipController {

	private State currentState; // current state of the controller
	private int bulletWait; // how long should the ship wait before firing again?

	// adjust this depending on how fast often you want to repeat firing. Might you want to switch
	// this to a variable that would depend on your ammo supply?
	private final int INIT_BULLET_WAIT = 50;

	// Constants for state management
	private enum State {
		REGULAR, FIRE_NOW
	}

	private enum Action {
		MOVE_TOWARD, AIM_AT
	}

	public ExampleShipControllerPQ(Ship ship) {
		super(ship);
		currentState = State.REGULAR;
		bulletWait = 0;
	}

	private class PriorityAction {
		private final Action action;
		private final FlyingObject fo;
		private final int priority;

		public PriorityAction(Action action, FlyingObject fo, int priority) {
			this.action = action;
			this.fo = fo;
			this.priority = priority; 
		}
	}

	@Override 
	public ControllerAction makeMove(Universe universe) {

		if (currentState == State.FIRE_NOW) {
			currentState = State.REGULAR;
			bulletWait = INIT_BULLET_WAIT;
			return new FireBullet();
		}

		if (bulletWait > 0) bulletWait--;

		// Create priority queue.  Smaller nums will take priority
		PriorityQueue<PriorityAction> queue = new PriorityQueue<>(
				new Comparator<PriorityAction>() {
					public int compare(PriorityAction a1, PriorityAction a2) {
						return a1.priority - a2.priority; 
					}
				}
				);

		// Analyze universe and add actions to queue
		for (var obj : universe.getFlyingObjects()) {
			// calc distance to obj
			float distance = Util.distance (ship.getX(),ship.getY(),obj.getX(),obj.getY() );
			if(ship.equals(obj) ) continue;     //skip my own ship

			// do something smarter based on the status of the ship:
			else if (obj instanceof Bonus && !(obj instanceof BombBonus || obj instanceof BlackHole)
					&& (distance < 800) // what's appropriate here
					) { 

				// weight bonuses with a higher weight so they have a lower priority
				// and a greater distance lowers the priority as well
				//System.out.println("adding bonus " + 5*(int)distance);
				//queue.add(new Action( moveToward(obj), 5 * (int)distance));
				queue.add(new PriorityAction( Action.MOVE_TOWARD, obj, 5 * (int)distance));
			}

			// look at firing or evading depending on ship status?
			// weight enemy ships with a lower weight so they appear at the front of the
			// priority queue (and thus higher priority)
			else if (obj instanceof Ship
					&& distance < 800) { // What's appropriate here 
				//System.out.println("adding aiming " + 1*(int)distance);
				//queue.add(new Action( aimAt(obj), 1 * (int)distance));
				// do something smarter based on regular bullets or lasers?
				queue.add(new PriorityAction( Action.AIM_AT, obj, 1 * (int)distance));
			}
		}

		// Return highest priority action
		if (queue.isEmpty() ) return null;
		
		/* Print out actions to diagnose issues
		for (Object o: queue.toArray()) {
			PriorityAction a1 = (PriorityAction) o;
			System.out.print(a1.action + " "+a1.priority+", ");
		}
		System.out.println();
		*/

		// grab top priority action and construct a returnable controller action from that
		// this might also be the place where the controller 'state' might be changed
		PriorityAction a = queue.poll();
		ControllerAction ca = null;
		switch(a.action) {
		case MOVE_TOWARD:
			ca = moveToward(a.fo);
			break;
		case AIM_AT:
			ca = aimAt(a.fo);
			break;
		}
		return ca; 
	}

	// perhaps break this into two states.  Stop to get bearings. And then fly directly to object
	private ControllerAction moveToward(FlyingObject obj) {
		FlightAdjustment fa = new FlightAdjustment();
		float angle = Util.calcAngle(ship.getX(), ship.getY(), 
				obj.getX(), obj.getY());
		fa.setFacing(angle);
		//System.out.println("angle="+angle+",heading="+ship.getHeading()+",speed="+ship.getSpeed()+
		//		",diff="+(Math.abs(angle-ship.getHeading())%(2.0*Util.PI)));
		if (ship.getSpeed() == 0) { // if speed is zero, fly toward target
			//	System.out.println("take off");
			fa.setAcceleration(1.0f);
		}
		//else if ( Math.abs(angle - ship.getHeading()) < 0.001) { // if the angle is close
		// otherwise determine the difference between the heading of the ship and
		// the angle to the object.  Do this mod 2 * PI because one might be positive and 
		// the other might be negative
		else if (Math.abs(angle-ship.getHeading())%(2.0*Util.PI) < 0.001) {
			//	System.out.println("coast");
			fa.setAcceleration(0.0f); // coast toward the target
		}
		else  {// otherwise stop for one frame so the ship doesn't potentially circle the target
			//	System.out.println("stop");
			fa.setStop(true);
		}
		return fa;
	}

	// set the facing toward the object in order to fire
	private ControllerAction aimAt(FlyingObject obj) {
		FlightAdjustment fa = new FlightAdjustment();

		// Try to lead the enemy ship if it's moving.
		// A finer calculation could possibly be made based on distance, speed, etc
		float angle = Util.calcAngle(ship.getX(), ship.getY(), 
				obj.getNextX(10), obj.getNextY(10));
		fa.setFacing(angle);
		if (bulletWait == 0 && ship.canFire())
			currentState = State.FIRE_NOW; // maybe this is not in the right place. Go into switch in makeMove?
		return fa;
	}

	@Override
	public String getName() { return "Example Ship PQ"; }
}

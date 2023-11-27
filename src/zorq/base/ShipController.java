package zorq.base;

import zorq.ControllerAction;
import zorq.Ship;
import zorq.Universe;

/**
 * Parent class for all ship controllers.
 * 
 * @author hastings
 *
 */
public abstract class ShipController implements Comparable<ShipController> {
	
	protected final Ship ship;
	private ControllerAction lastAction = null;
	
	public ShipController(Ship ship){ this.ship = ship; }

	public abstract String getName();
	
	public final ControllerAction makeLoggedMove(Universe gd) {
		lastAction = this.makeMove(gd); 
		return lastAction;}
	
	// final.  Can't override.  for sorting controllers for scoreboard
	public final int compareTo(ShipController o) {
		int otherScore = o.getShip().getScore();
		int myScore = this.getShip().getScore();
		return otherScore - myScore;
	}
	
	/**
	 * 
	 * @return the last action the controller took
	 */
	public final ControllerAction getLastAction() { return lastAction; }
	
	/** Called by the engine to get the next move of the ship/controller. This method 
	 * can call loop through the flying objects and to determine an action to return.
	 * 
	 * <b>Note</b>: Perhaps this should be modified to not pass the entire universe into the 
	 * controllers and only pass the game objects!!!
	 * 
	 * @param gd - game universe
	 * @return controller action
	 */
	public abstract ControllerAction makeMove(Universe gd);

	/** @return the ship tied to this controller */
	public final Ship getShip() { return ship; }
}

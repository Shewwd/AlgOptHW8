package zorq;

import zorq.graphics.ShipJumpBonusDrawer;

/**
 * Give ship an expiring bonus to jump/teleport to a new location.
 * @author John Hastings
 *
 */
public final class ShipJumpBonus extends Bonus {

	
	public ShipJumpBonus(float x, float y, int amount) { 
		super(x, y, amount); 
		drawer = new ShipJumpBonusDrawer(this);
		}
}

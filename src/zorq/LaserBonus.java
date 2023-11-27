package zorq;

import java.awt.Color;

import zorq.graphics.LaserBonusDrawer;

/**
 * Resupplies ship with laser shot capabilities.
 * 
 * @author John Hastings
 */
public final class LaserBonus extends Bonus {

	
	public LaserBonus(float x, float y, int amount) {
		super(x, y, amount);
		drawer = new LaserBonusDrawer(this);
	}

	public Color getColor() {return Constants.laserColor; }
	
}

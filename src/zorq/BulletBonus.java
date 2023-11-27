package zorq;

import java.awt.*;

import zorq.graphics.BulletBonusDrawer;

/**
 * Bonus object which resupplies ships with bullets.
 * @author John Hastings
 */
public final class BulletBonus extends Bonus {
	
	int points;
	
	public BulletBonus(float x, float y, int amount) {
		super(x, y, amount);
		this.drawer = new BulletBonusDrawer(this);
	}

	public Color getColor() { return Constants.bulletsColor; }

}

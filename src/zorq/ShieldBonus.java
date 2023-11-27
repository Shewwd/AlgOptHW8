package zorq;

import java.awt.*;

import zorq.graphics.FuelBonusDrawer;
import zorq.graphics.ShieldBonusDrawer;

/**
 * Resupply ship's shield energy.
 * @author John Hastings
 *
 */
public final class ShieldBonus extends Bonus {

	public ShieldBonus(float x, float y, int amount) {
		super(x, y, amount);
		drawer = new ShieldBonusDrawer(this);
	}

	public Color getColor() { return Constants.shieldColor; }

}

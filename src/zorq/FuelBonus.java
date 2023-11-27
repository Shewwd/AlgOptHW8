package zorq;

import java.awt.*;
import java.util.Random;

import zorq.graphics.FuelBonusDrawer;

/**
 * Resupplies ship with fuel.
 * @author John Hastings
 */
public final class FuelBonus extends Bonus {

	public FuelBonus(float x, float y, int amount) {
		super(x, y, amount);
		drawer = new FuelBonusDrawer(this);
	}

	public Color getColor() { return Constants.fuelColor; }

	static void makeSpawn(Universe u) {
		int bonusScale = u.getShipControllers().size();
		Random rnd = Constants.rnd;
		if (rnd.nextFloat()*Constants.BONUS_SPAWN_RATE/bonusScale < 1) {
			int x = rnd.nextInt(Universe.getWidth());
			int y = rnd.nextInt(Universe.getHeight());
			FuelBonus f = new FuelBonus(x, y, 100*(5+rnd.nextInt(6)));
			u.add(f);
		}
	}
}

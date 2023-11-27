package zorq;

import zorq.graphics.MagnetBonusDrawer;

/**
 * Gives ship a timed magnet which sweeps up nearby good bonuses
 * @author John Hastings
 */
public final class MagnetBonus extends Bonus {
	
	public MagnetBonus(float x, float y, int amount) {
		super(x, y, amount);
		drawer = new MagnetBonusDrawer(this);
	}	
}

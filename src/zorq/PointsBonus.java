package zorq;

import java.awt.*;

import zorq.graphics.PointsBonusDrawer;

/**
 * Gives ship a score boost.
 * @author John Hastings
 *
 */
public final class PointsBonus extends Bonus {
	
	
	public PointsBonus(float x, float y, int amount) {
		super(x, y, amount);
		this.drawer = new PointsBonusDrawer(this);
	}

	public Color getColor() { return Color.CYAN; }
	

}

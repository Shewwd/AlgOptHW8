package zorq;

import java.awt.*;

/**
 * Gives ship additional lives. Not currently used.
 * @author John Hastings
 */

@Deprecated
public class LifeBonus extends Bonus {
	
	public LifeBonus(float x, float y, int amount) { super(x, y, amount); }
	public Color getColor() { return Color.YELLOW; }
}

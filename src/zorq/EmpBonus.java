package zorq;

import java.awt.Image;

import zorq.graphics.EmpBonusDrawer;

/**
 * A bonus object which provides a ship with EMP blast capabilities.
 * @author John Hastings
 *
 */
public final class EmpBonus extends Bonus {

	public static Image empBonusImage;

	//int points;

	public EmpBonus(float x, float y, int amount) { 
		super(x, y, amount);
		drawer = new EmpBonusDrawer(this);
		}

	//public Color getColor() { return Constants.bulletsColor; }
}

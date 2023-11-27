package zorq.base.effects;

import java.awt.Image;

/**
 * Displays ship exhaust visuals.
 * @author John Hastings
 *
 */
public final class Exhaust extends Effect {
	
	public static Image exhaustImage;
	protected Image getImage() { return Exhaust.exhaustImage; }
	
	public Exhaust(float x, float y){
		super(19, 1, 19, 16, 16);
		this.x = x;
		this.y = y;
	}
}

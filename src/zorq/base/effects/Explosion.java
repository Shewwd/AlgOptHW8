package zorq.base.effects;

import java.awt.Image;

/**
 * Displays explosion visuals.
 * @author John Hastings
 *
 */
public final class Explosion extends Effect {
	
	public static Image explosionImage;
	protected Image getImage() { return Explosion.explosionImage; }
	
	public Explosion(float x, float y){
		super(44, 11, 4, 80, 80);
		this.x = x;
		this.y = y;
	}
}

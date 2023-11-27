package zorq.base.effects;

import java.awt.Image;

/**
 * Displays ship jump visuals.
 * @author John Hastings
 *
 */
public final class Warp extends Effect  {	

	public static Image warpImage;
	protected Image getImage() { return Warp.warpImage; }

	public Warp(float x, float y){
		super(44, 3, 6, 48, 48);
		this.x = x;
		this.y = y;
		this.step = STEPS;
	}
}

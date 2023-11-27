package zorq.base.effects;

import java.awt.Image;

/**
 * Not currently used.
 * @author John Hastings
 */

@Deprecated
public class Cloak extends Effect{
	
	public static Image cloakImage;
	protected Image getImage() { return Cloak.cloakImage; }
	
	public Cloak(float x, float y){
		super(44, 2, 5, 80, 80);
		this.x = x;
		this.y = y;
		this.step = 44;
	}
}

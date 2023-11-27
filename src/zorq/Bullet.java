package zorq;

import java.awt.*;

import zorq.graphics.BulletDrawer;

/**
 * Bullet flying object.
 * 
 * @author hastings
 *
 */
public class Bullet extends FlyingObject {

	public static final float INIT_SPEED = 8.0f;
	private String creator;
	private Ship shipCreator;
	//private static final int MAX_LIFE = (int)(100*Constants.scaleX);
	protected static final int MAX_LIFE = 50; //(int)(100*Constants.scaleX);
	protected int life = 0;

	public Bullet(float x, float y, Ship shipCreator, int type) {
		super(x, y);
		this.creator = shipCreator.getShipController().getName();
		this.shipCreator = shipCreator;

		wraps = true;
		life = MAX_LIFE;
		drawer = new BulletDrawer(this, type);
	}

	@Override
	public Color getColor() { return Color.GRAY; }

	public int getLife() { return life; }
	
	@Override
	public final float getRadius() { return 1.0f; }

	/*
	@Override
	public void paint(Graphics g) {
		if (life <= 0) return;
		drawer.paint(g);
	}
	*/

	protected boolean handleOffUniverse(Universe gd) {
		//if (this.offUniverse() || life <= 0) {
		if (life <= 0) {
			//			gd.getBullets().remove(this);
			//gd.getFlyingObjects().removeElement(this);
			return true;
		}
		else return false;
	}

	protected boolean hitBy (FlyingObject fo, Universe u) {
		if (fo instanceof Bonus) return false;  // bullets should pass through bonuses
		return true;
	}

	public String getCreator(){
		return this.creator;
	}

	public Ship getShipCreator(){
		return this.shipCreator;
	}

	protected void next() {
		life--;  // moved from paint
		super.next();
	}
}

package zorq;

import java.awt.*;

import zorq.graphics.EMPDrawer;

public final class EMP extends FlyingObject {

	private Ship shipCreator;
	protected static final int MAX_LIFE = 4;
	protected int life = 0;
	private static final int RADIUS_STEP=50;

	public EMP(float x, float y, Ship shipCreator) {
		super(x, y);
		//this.creator = shipCreator.getShipController().getName();
		this.shipCreator = shipCreator;
		life = MAX_LIFE;
		radius = 20;
		drawer = new EMPDrawer(this);
	}

	public int getLife() { return life; }
	
	public Ship getShipCreator(){
		return this.shipCreator;
	}

	// have the object react to being hit by another flying object
	// return true if the object should be removed after being hit
	protected boolean hitBy (FlyingObject fo, Universe u) {
		return false;
	}

	public Color getColor() { return Color.GREEN; }
	public float getRadius() { return radius; }

	protected boolean handleOffUniverse(Universe gd) {
		if (this.offUniverse() || life <= 0) {
			return true;
		}
		else return false;
	}

	protected void next() {
		life--;  // moved from paint
		radius+=RADIUS_STEP;
		super.next();
	}
}

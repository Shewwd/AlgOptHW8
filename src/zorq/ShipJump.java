package zorq;

import zorq.base.effects.Warp;

/**
 * Ship jump action returned by controllers from their makeMove method.
 * Works only when the ship canTeleport method returns true.
 * 
 * @author hastings
 *
 */
public final class ShipJump extends ControllerAction {
	
	private float x;
	private float y;
	
	public ShipJump(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getX() { return x; }
	public float getY() { return y; }

	@Override
	protected void act(Ship s, Universe u) {
		if (s.canTeleport()) {
			s.goTeleport();

			//u.getWarp().add(new Warp(this.getX(),this.getY()));
			//gd.getWarp().add(new Warp(sj.getX(),sj.getY()));

			s.setX(this.getX());
			s.setY(this.getY());
		}
	}

}

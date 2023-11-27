package zorq;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import zorq.graphics.BlackHoleDrawer;

/** Flying object which vacuums up nearby game objects
 * 
 * @author John Hastings
 *
 */
public final class BlackHole extends FlyingObject{

	Universe u;

	public static Image holeImage;
	public static final int RADIUS = 25;

	public BlackHole(float x, float y, Universe u) {
		super(x, y);
		this.u = u;
		radius = RADIUS;
		drawer = new BlackHoleDrawer(this);
	}

	protected boolean handleOffUniverse(Universe gd) {
		//System.out.println("off universe = " + offUniverse());
		return this.offUniverse();
	}

	protected boolean hitBy (FlyingObject fo, Universe u) { return false; }

	public Color getColor() {
		return Color.RED;
	}

	public float getRadius() { return radius; }

	//static final float MAX_DIST = Util.distance(0, 0, Constants.width, Constants.height );
	static final float MAX_DIST = Util.distance(0, 0, Universe.getWidth(), Universe.getHeight() );

	private static final float ANGLE_AFFECT = RADIUS/25f; // was .1f;
	private static final float GRAVITY_AFFECT = RADIUS/5f;

	private void affectFlyingObjects() {
		for (FlyingObject fo : u.getFlyingObjects()) {
			if(fo != this){
				float foX = fo.getX();
				float foY = fo.getY();
				float dist = Util.distance(x, y, fo.getX(), fo.getY() );
				if (dist > BlackHole.RADIUS*8) continue;
				float angle = Util.calcAngle(x,y,foX, foY);
				angle = angle - ANGLE_AFFECT* (float)Math.pow(1-dist/MAX_DIST,8);
				dist = dist - GRAVITY_AFFECT*(float)Math.pow(1-dist/MAX_DIST,8); //(180*
				float nextX = x+(float)Math.cos(angle)*dist; //foX + (x - foX)/(180*(1-(MAX_DIST-dist)/MAX_DIST)); 
				float nextY = y+(float)Math.sin(angle)*dist; //foY + (y - foY)/(180*(1-(MAX_DIST-dist)/MAX_DIST));

				fo.setX(nextX);
				fo.setY(nextY);
			}

		}
	}

	/*
	public void paint(Graphics g) {
		drawer.paint(g);
	}
	*/

	protected void next() {
		affectFlyingObjects();
		super.next();
	}

}

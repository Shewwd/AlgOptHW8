package zorq;

import java.awt.Color;
import java.awt.Image;

import zorq.graphics.FlyingObjectDrawer;

/**
 * Parent class of all game FlyingObjects. Bonus and Ship are subclasses.
 * 
 * @author John Hastings
 *
 */
public abstract class FlyingObject {

	protected float facing;
	protected float radius;

	protected float heading;
	protected float speed;
	protected float acceleration;

	protected float x;
	protected float y;

	protected FlyingObjectDrawer drawer;
	public FlyingObjectDrawer getDrawer() { return drawer; }
	/**
	 * Does the object wrap around if it moves off the screen?
	 */
	protected boolean wraps;

	public FlyingObject(float x, float y) {
		this.x = x;
		this.y = y;

		facing = 0;
		heading = 0;
		speed = 0;
		radius = 0;
		wraps = false;
	}

	/** 
	 * Have the object react to being hit by another flying object.
	 * Return true if the object should be removed from the universe after being hit.
	 * 
	 * @param fo - flying object
	 * @param u - game universe
	 * @return true if the object should be removed due to collision
	 */
	protected abstract boolean hitBy(FlyingObject fo, Universe u);

	protected abstract Color getColor();

	/**
	 * Return the radius of the flying object.
	 */
	public abstract float getRadius();

	/**
	 * Return the loaded image for the game object.
	 */
	protected Image getImage() {return null;};

	protected void setRadius(float r) {this.radius = r; }

	/**
	 * Paint the flying object.
	 * @param g - graphics "pen"
	 */
	//public abstract void paint(Graphics g);

	/** @return the facing */
	public float getFacing() { return facing; }
	/** @return the heading */
	public float getHeading() { return heading; }
	/** @return the speed */
	public float getSpeed() { return speed; }
	/** @return the x coordinate of the location */
	public float getX() { return x; }
	/** @return the y coordinate of the location */
	public float getY() { return y; }
	protected void setFacing(float facing) { this.facing = facing; }
	protected void setHeading(float heading) { this.heading = heading; }
	protected void setSpeed(float speed) { this.speed = speed; }
	protected void setX(float x) { this.x = x; }
	protected void setY(float y) { this.y = y; }
	/** @return the x coordinate at the next frame based on the current heading */
	public float getNextX() { return  x + speed * (float) Math.cos(heading); }
	/** @return the y coordinate at the next frame based on the current heading */
	public float getNextY() { return  y + speed * (float) Math.sin(heading); }
	/** @return the estimated x coordinate in <b>i</b> frames based on the current heading */
	public float getNextX(int i) { return  x + speed * i * (float) Math.cos(heading); }
	/** @return the estimated y coordinate in <b>i</b> frames based on the current heading */
	public float getNextY(int i) { return  y + speed * i * (float) Math.sin(heading); }
	/** @return the acceleration */
	public float getAcceleration() { return acceleration; }
	protected void setAcceleration(float acceleration) { this.acceleration = acceleration; }

	protected boolean offLeftUniverse() { return (x-radius < 0); }
	protected boolean offRightUniverse() { return (x+radius> Universe.getWidth()); }
	protected boolean offBottomUniverse() { return (y-radius<0); }
	protected boolean offTopUniverse() { return (y+radius>Universe.getHeight()); }
	protected boolean offUniverse() {
		return (offLeftUniverse() || offRightUniverse() || offBottomUniverse() || offTopUniverse());
	}


	/**
	 * Return true if the object should be removed for leaving the universe.
	 * @param u - game universe
	 */
	protected abstract boolean handleOffUniverse(Universe u);

	/**
	 * Return the distance to the flying object.
	 * @param fo - flying object
	 */
	private float distance (FlyingObject fo) {
		return Util.distance(this.getNextX(), this.getNextY(), fo.getNextX(), fo.getNextY() );
	}

	private float next_distance_squared (FlyingObject fo) {
		return Util.distance_squared(this.getNextX(), this.getNextY(), fo.getNextX(), fo.getNextY() );
	}

	/**
	 * Return true if the object intersects the flying object.
	 * @param fo - flying object
	 */
	protected boolean intersects (FlyingObject fo) {
		//if (fo instanceof Ship || this instanceof Ship) ;
		//else if (this instanceof Bullet && fo instanceof Bullet) ;
		//else if (this instanceof BlackHole || fo instanceof BlackHole) ;
		//else return false;
		//System.out.println("this="+this + " fo="+fo);
		//(this.equals(fo)) System.out.println("problem!!!!");
		//If there is a collision
		/*
		float distance = this.distance(fo);
		boolean collides = distance < this.getRadius() + fo.getRadius();
		if (collides) {
			if (this instanceof Ship && (fo instanceof Bullet || fo instanceof Laser)) {
				Bullet b = (Bullet)fo;
				Ship s = (Ship)this;
				if (b.getShipCreator() == this) {
					System.out.println("b= "+b+" creator="+b.getShipCreator().getShipController().getName());
					System.out.println("ding. ding. S r = " + s.getRadius() +
							" b r="+b.getRadius()+" d="+distance);
					System.out.println("s x,y="+s.getShipController().getName()+" "+s.getX()+","+s.getY()+
							" b x,y="+b.getX()+","+b.getY());
					System.exit(0);
					return false;
				}
			}
			else if ( (this instanceof Bullet || this instanceof Laser) && fo instanceof Ship) {
				Ship s = (Ship)fo;
				Bullet b = (Bullet)this;
				if (b.getShipCreator() == s) {
					System.out.println("b= "+b+" creator="+b.getShipCreator().getShipController().getName());
					System.out.println("ding. ding. S r = " + s.getRadius() +
							" b r="+b.getRadius()+" d="+distance);
					System.out.println("s x,y="+s.getShipController().getName()+" "+s.getX()+","+s.getY()+
							" b x,y="+b.getX()+","+b.getY());
					System.exit(0);
					return false;
				}
			}
		}
		return collides;
		*/

		float distance_squared = this.next_distance_squared(fo);
		float my_radius = this.getRadius();
		float fo_radius = fo.getRadius();
		boolean collides = distance_squared < 2.0f*my_radius*my_radius + 2.0f*fo_radius*fo.radius;
		/*
		if (collides) {
			if (this instanceof Ship && (fo instanceof Bullet || fo instanceof Laser)) {
				Bullet b = (Bullet)fo;
				Ship s = (Ship)this;
				if (b.getShipCreator() == this) {
					
					System.out.println("ding. ding. S r = " + s.getRadius() +
							" b r="+b.getRadius()+" dsq="+distance_squared);
					return false;
				}
			}
			else if ( (this instanceof Bullet || this instanceof Laser) && fo instanceof Ship) {
				Ship s = (Ship)fo;
				Bullet b = (Bullet)this;
				if (b.getShipCreator() == s) {
					System.out.println("ding. ding. S r = " + s.getRadius() +
							" b r="+b.getRadius()+" dsq="+distance_squared);
					return false;
				}
			}
		}
		*/
		return collides;
		
		//return (distance_squared < 2.0f*my_radius*my_radius + 2.0f*fo_radius*fo.radius);

	}

	/**
	 * Step the object to the next frame
	 */
	protected void next() {
		//System.out.println("in FO next = " + this.getClass());
		this.setX(getNextX());
		this.setY(getNextY());
		// if it's an object that should wrap once leaving screen,
		// handle that
		if (wraps) {
			// wrap the ship around if off universe
			x = (x<0)?Universe.getWidth()+x : x % Universe.getWidth();
			y = (y<0)?Universe.getHeight()+y : y% Universe.getHeight();
		}
	}

}

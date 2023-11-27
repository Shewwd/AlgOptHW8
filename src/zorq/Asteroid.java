package zorq;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

@Deprecated
public class Asteroid extends FlyingObject{

	public Asteroid(float x, float y) {
		super(x, y);
		radius = 4.0f;
	}

	public Asteroid(float x, float y, float radius) {
		this(x, y);
		//System.out.println("new ast radius = " + radius);
		this.radius = radius;
	}

	@Override
	public Color getColor() { return Color.RED; }
	
	@Override
	public float getRadius() { return radius; }

	// this should be moved if asteroids are reintroduced
	public void paint(Graphics g) {
		g.setColor(this.getColor());
		g.fillOval((int)(x-this.getRadius()),
				Constants.DISPLAY_HEIGHT - (int)(y+this.getRadius()),
														(int)(radius*2.0f),
														(int)(radius*2.0f));
		
		for(int i = 0; i <= 25; i+=5){
			float tx = x + (float)Math.cos(heading+Math.PI) * i;
			float ty = y + (float)Math.sin(heading+Math.PI) * i;
			

			g.setColor(new Color((int)(255 * ((30 - i) / 30f)),0,0));
			
			g.fillOval((int)(tx-this.getRadius()* ((25 - i) / 30f)),		
					Constants.DISPLAY_HEIGHT - (int)(ty+this.getRadius()* ((25 - i) / 30f)),
															(int)(this.getRadius()*2.0f * ((25 - i) / 30f)),
															(int)(this.getRadius()*2.0f * ((25 - i) / 30f)));
			
		}
		
	}
	
	protected boolean handleOffUniverse(Universe gd) {
		if (this.offLeftUniverse()) {
			setHeading((float) (1.0 * Math.PI - this.getHeading()));
			setX(1+this.radius);
		}
		else if (this.offRightUniverse()) {
			setHeading((float) (1.0 * Math.PI - this.getHeading()));
			setX(Universe.getWidth()-this.radius);
		}
		else if (this.offBottomUniverse()) {
			setHeading((float) (2.0 * Math.PI - this.getHeading()));
			setY(1+this.radius);
		}
		else if (this.offTopUniverse()) {
			setHeading((float) (2.0 * Math.PI - this.getHeading()));
			setY(Universe.getHeight()-this.radius);
		}
		return false;
	}

	public Vector<Asteroid> spawn() {
		Random rnd = Constants.rnd;
		Vector<Asteroid> v = new Vector<Asteroid>();
		if (this.getRadius() > 2) {
			// add new smaller asteroids:
			for (int k = 0; k < 2; k++) {
				Asteroid a = new Asteroid(
						this.getX(), 
						this.getY(),
						this.getRadius() / 2);
				a.setHeading(heading - .5f + rnd.nextFloat());
				this.setX(this.getX() + (float)(Math.cos(this.getHeading())*this.getRadius()));
				this.setY(this.getY() + (float)(Math.sin(this.getHeading())*this.getRadius()));
				a.setSpeed(speed);
				v.add(a);
				//u.getAsteroids().add(a);
				//u.getFlyingObjects().add(a);
			}
		}
		return v;
	}
	
	// have the object react to being hit by another flying object
	protected boolean hitBy (FlyingObject fo, Universe u) {

		//Random rnd = Constants.rnd;

		//float heading =0;
		//float speed = 0;
		
		/*
		else {
			this.heading = this.getHeading();
			speed = this.getSpeed();
		}
		*/
			
		/*
		if (this.getRadius() > 2) {
			// splinter by first removing larger asteroids
			//u.getAsteroids().removeElement(this);
			u.getFlyingObjects().remove(fo);
			// add new smaller asteroids:
			for (int k = 0; k < 2; k++) {
				Asteroid a = new Asteroid(
						this.getX(), 
						this.getY(),
						this.getRadius() / 2);
				a.setHeading(heading - .5f + rnd.nextFloat());
				this.setX(this.getX() + (float)(Math.cos(this.getHeading())*this.getRadius()));
				this.setY(this.getY() + (float)(Math.sin(this.getHeading())*this.getRadius()));
				a.setSpeed(speed);
				//u.getAsteroids().add(a);
				//u.getFlyingObjects().add(a);
			}
		}
		else {
			// completely remove small asteroids
			//u.getAsteroids().removeElement(this);
			u.getFlyingObjects().removeElement(this);
		}
		*/
		return true;
	}
}

package zorq;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import zorq.graphics.BonusDrawer;

/**
 * Parent class of all game Bonuses.
 * 
 * @author John Hastings
 */
public class Bonus extends FlyingObject implements SpawningObject {

	/*
	static {
		SpawnFactory.registerSpawningObject(BombBonus.class);
	}
	*/
	
	private int width;  // are these for collision detection or graphics?
	private int height;
	protected int amount;
	protected Image image;

	public Bonus(float x, float y, int amount) {
		super(x, y);
		width = 40;
		height = 20;
		this.amount = amount;
		drawer = new BonusDrawer(this);
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getAmount() { return amount; }

	@Override
	// return true if the object should be removed after being hit
	protected boolean hitBy(FlyingObject fo, Universe u) {
		if (fo instanceof Ship || fo instanceof BlackHole)
			return true;
		return false;
	}

	// This should be moved to the drawer class
	@Override
	public Color getColor() { return null; }
	public float getRadius() { return 20; }
	protected boolean handleOffUniverse(Universe gd) { return false; }

	public void paint(Graphics g) {
		//this.getDrawer().paint(g);
		drawer.paint(g);
	}
}

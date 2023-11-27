package zorq;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import zorq.base.ShipController;
import zorq.base.effects.*;
import zorq.graphics.ShipDrawer;

/**
 * Ship flying object.
 * 
 * @author john hastings
 *
 */
public final class Ship extends FlyingObject {

	protected int score;
	private boolean alive;
	protected ShipController shipController;
	private int fireBulletWait;
	private static int BULLET_FIRE_WAIT = 5;
	protected int bulletFireWait;

	protected int bullets;
	public static final int INIT_BULLETS = 50;
	private int highestBullets = INIT_BULLETS;

	protected int laserEnergy; // amount of laser shots
	public static final int INIT_LASER = 5;
	public static final int HIGHEST_LASER = 10;

	protected int fuel;
	public static final int INIT_FUEL = 2000;
	private int highestFuel = INIT_FUEL;
	protected int lives;
	public static final int INIT_LIVES = 3;
	private int highestLives = INIT_LIVES;
	private int empCountdown = 0;
	private int jumpCountdown = 0;
	private int magnetCountdown = 0;
	private int disabledCountdown = 0;
	protected static final int INIT_DISABLED = 150;
	public static final int INIT_MAGNETIC = 150;
	public static final int INIT_EMP = 150;
	public static final int INIT_JUMP = 150;

	protected boolean shieldUp;
	protected int shield;
	public static final int SHIELD_HEALTH_FOR_LIFE = 1000;
	private int highestShield = SHIELD_HEALTH_FOR_LIFE;

	protected int sleepTime;
	private static final int SLEEP_TIME = 150;

	protected static Image shipImage;
	protected int type;
	protected int weaponLevel;

	/**
	 * @return Returns the shipImage.
	 */
	public Image getShipImage() { return shipImage; }
	/** @return true if the ship can turn on its shield */
	public boolean canShield() { return shield > 0; }
	/** @return true if the shield is currently up */
	public boolean isShieldUp() { return shieldUp; }

	protected void toggleShield() { 
		if (!isShieldUp() && canShield())
			shieldActivated();
		else shieldDeactivated();
	}

	private void shieldActivated() { shieldUp = true;	}
	private void shieldDeactivated() { shieldUp = false;	}

	/** @return true if the ship can fire a bullet */
	public boolean canFire() { return fireBulletWait == 0 && bullets > 0; }
	/** @return true if the ship can fire a laser */
	public boolean canFireLaser() { return fireBulletWait == 0 && laserEnergy > 0; }
	/** @return true if the ship can do an EMP */
	public boolean canFireEMP() { return empCountdown > 0; }

	protected void bulletFired() { 
		fireBulletWait = bulletFireWait(); 
		bullets -= 1;
	}

	/** Ship has just fired a laser. **/
	protected void laserFired() { 
		fireBulletWait = bulletFireWait(); 
		laserEnergy -= 1;
	}

	protected void empFired() {  empCountdown = 0;  }

	protected int bulletFireWait() { return BULLET_FIRE_WAIT; }

	/**
	 * Create the new ship at the given x,y coordinates.  Type picks one of the 8 sets of ship images.
	 */
	public Ship(float x, float y, int type) {
		super(x, y);
		this.type = type;
		alive = true;
		fireBulletWait = 0;
		score = 0;
		wraps = true;
		resetLife();
		drawer = new ShipDrawer(this);
	}

	protected void resetLife() {
		shield = SHIELD_HEALTH_FOR_LIFE;
		shieldUp = false;
		sleepTime = 0;
		weaponLevel = 1;
		laserEnergy = INIT_LASER;
		bullets = INIT_BULLETS;
		fuel = INIT_FUEL;
		empCountdown = 0;
		magnetCountdown = 0;
		disabledCountdown = 0;
		if (lives == 0) lives = INIT_LIVES;  // reset lives after complete destruction & respawn
	}

	/** @return the current ship score */
	public int getScore() {return score; }
	protected boolean isSleeping() { return sleepTime > 0; }
	protected boolean isDisabled() { return disabledCountdown > 0; }
	protected boolean isMagnetic() { return magnetCountdown > 0; }

	protected boolean intersects(FlyingObject fo) {
		if (isAlive())
			return super.intersects(fo);
		else return false;
	}

	private boolean hitRecently = false;

	protected boolean hitBy (FlyingObject fo, Universe u) {
		if (fo == this) return false;

		hitRecently = true;
		//if (fo instanceof Ship) {
		/*
		if (fo instanceof BlackHole) {
			lives--;
			if (lives == 0) die();
			else semiDie();
			return true;
		}
		 */
		//System.out.println("fo = "+fo);
		if (fo instanceof LaserBonus) {
			laserEnergy += ((LaserBonus)fo).getAmount();
			if(laserEnergy > HIGHEST_LASER)
				laserEnergy = HIGHEST_LASER;
			return false;
		}
		else if (fo instanceof BulletBonus) {
			bullets += ((BulletBonus)fo).getAmount();
			weaponLevel++;
			if(bullets > INIT_BULLETS)
				highestBullets = bullets;
			return false;
		}
		else if (fo instanceof FuelBonus) {
			fuel += ((FuelBonus)fo).getAmount();
			if(fuel > INIT_FUEL)
				highestFuel = fuel;
			return false;
		}
		/*
		else if (fo instanceof LifeBonus && !(this instanceof AIShip) ) {			
			lives += ((LifeBonus)fo).getAmount();
			score += Constants.RESPAWN_PENALTY;
			if(lives > INIT_LIVES)
				highestLives = lives;
			return false;
		}
		 */
		else if (fo instanceof PointsBonus) {
			score += ((PointsBonus)fo).getAmount();
			return false;
		}
		else if (fo instanceof EmpBonus) {
			empCountdown = INIT_EMP;
			return false;
		}
		else if (fo instanceof ShipJumpBonus) {
			jumpCountdown = INIT_JUMP;
			return false;
		}
		else if (fo instanceof MagnetBonus) {
			magnetCountdown = INIT_MAGNETIC;
			return false;
		}
		else if (fo instanceof ShieldBonus) {
			shield += ((ShieldBonus)fo).getAmount();
			if(shield > SHIELD_HEALTH_FOR_LIFE)
				highestShield = shield;
			return false;
		}
		else if (fo instanceof EMP) {
			EMP e = (EMP)fo;
			if (e.getShipCreator() == this) return false;
			//System.out.println("this="+this.getShipController().getName());
			this.disabledCountdown = INIT_DISABLED;
			if (this.isShieldUp()) this.toggleShield(); 
			return false;
		}
		else if (this.isShieldUp()) {

			if (shield > 100)
				shield -=100;
			else  {
				shield = 0;
				this.toggleShield();  // turn shield off
			}
			return false;
		}
		else {
			lives--;
			if(Constants.LOG) Constants.logWriter.logShipDeath(this,fo);
			//u.getExplosion().add(new Explosion(this.getX(),this.getY()));
			u.addExplosion(this.getX(),this.getY());  // ****should be throwing an event with the universe or engine being a listener
			if (Constants.SOUND) Sound.soundShipExplode.play();

			if (fo instanceof Bullet) {
				int deduction = (int)(score/4);
				if (deduction<0)
					((Bullet)fo).getShipCreator().score += Constants.KILL_POINTS;  // old code for fixed kill bonus
				else {
					((Bullet)fo).getShipCreator().score += deduction;
					score -= deduction;
				}
			}
			//if (lives == 0) die();
			//else semiDie();
			semiDie();
			return true;
		} 	
	}	

	/**
	 * If the ship is currently magnetic, vacuum up nearby bonuses.
	 */
	protected void magnetNearbyBonuses(Universe u) {
		if (!this.isMagnetic()) return;
		//final float MAX_DIST = Util.distance(0, 0, Universe.WIDTH, Universe.HEIGHT );
		final int DIST_FACTOR = 15;
		//Universe u = this.getShipController().
		for (FlyingObject fo : u.getFlyingObjects()) {
			if(fo instanceof Bonus && !(fo instanceof BombBonus)){

				float foX = fo.getX();
				float foY = fo.getY();
				float dist = Util.distance(x, y, fo.getX(), fo.getY() );
				if (dist > this.getRadius()*DIST_FACTOR) continue;
				float angle = Util.calcAngle(foX, foY, x,y);
				dist = this.getRadius(); 
				float nextX = foX+(float)Math.cos(angle)*dist; //foX + (x - foX)/(180*(1-(MAX_DIST-dist)/MAX_DIST)); 
				float nextY = foY+(float)Math.sin(angle)*dist; //foY + (y - foY)/(180*(1-(MAX_DIST-dist)/MAX_DIST));
				//System.out.print("x,y="+nextX+","+nextY);
				//System.out.println("next x,y="+nextX+","+nextY);
				fo.setX(nextX);
				fo.setY(nextY);				
			}
		}
	}

	/**
	 * Put the ship to sleep for some time.
	 */
	protected void die() {
		setAlive(false);
		sleepTime = Integer.MAX_VALUE;
		//sleepTime = SLEEP_TIME;
	}

	/**
	 * Put the ship to sleep for some time and deduct the score by the penalty.
	 */
	protected void semiDie() {
		setAlive(false);
		sleepTime = SLEEP_TIME;
		this.score -= Constants.RESPAWN_PENALTY;
	}

	@Override
	protected Color getColor() {
		return null;
	}

	protected boolean isAlive() { return alive; }

	/**
	 * Set the alive instance variable to indicate if the ship is currently alive.
	 */
	protected void setAlive(boolean alive) {   //was public until 11-18-2019 *****
		this.alive = alive;
		if(!alive){
			heading = 0;
			speed = 0;
		}
		else {
			resetLife();
		}
	}

	@Override
	public float getRadius() { return 12.0f; }   // ***** should refer to a constant or otherwise

	protected void decrementSleep() { if (isSleeping()) sleepTime--; }
	protected void decrementDisabled() { disabledCountdown = Integer.max(--disabledCountdown, 0); }
	protected void decrementMagnet() { magnetCountdown = Integer.max(--magnetCountdown, 0); }
	protected void decrementEMP() { empCountdown = Integer.max(--empCountdown, 0); }
	protected void decrementJump() { jumpCountdown = Integer.max(--jumpCountdown, 0); }

	/** @return true if the ship can teleport to a new location */
	public boolean canTeleport() { return jumpCountdown > 0 ;}
	protected void goTeleport() { jumpCountdown = 0; }

	/**
	 * Return the ship controller for the ship.
	 */
	public ShipController getShipController() { return shipController; }

	/**
	 * Set the ship controller for the ship.
	 */
	protected void setShipController(ShipController shipController) { this.shipController = shipController; }

	// not killing ships that go off the screen. they will wrap around
	protected boolean handleOffUniverse(Universe gd) {
		return false;
	}

	/** @return the amount of shield health remaining */
	public int getShieldHealth(){ return this.shield; }
	/** @return the number of bullets remaining */
	public int getBulletsAmmount(){ return this.bullets; }
	/** @return the amount of fuel remaining */
	public int getFuelAmmount(){ return this.fuel; }
	/** @return the number of laser shots remaining */
	public int getLaserAmount() { return this.laserEnergy; }
	@Deprecated
	public int getLives(){ return this.lives; }
	@Deprecated
	public int getType(){ return this.type; }

	/** @return the amount of frames before the EMP bonus expires */
	public int getEmpCountdown() { return empCountdown; }
	/** @return the amount of frames before the jump bonus expires */
	public int getJumpCountdown() { return jumpCountdown; }
	/** @return the amount of frames before the magnet bonus expires */
	public int getMagnetCountdown() { return magnetCountdown; }

	protected boolean getHitRecently() { return hitRecently; }
	/**
	 * @return Returns the weaponLevel.
	 */
	public int getWeaponLevel() { return weaponLevel; }

	/**
	 * Change acceleration, reduce fuel based on amount of acceleration.
	 */
	protected void setAcceleration(float acceleration) {  //was public until 11-18-2019 *****
		if (fuel > 0) {
			this.acceleration = acceleration;
			fuel -= Math.abs((acceleration * 10f));
			if (fuel < 0)
				fuel = 0;
		} else
			this.acceleration = 0;
	}

	protected void next() {
		//System.out.println("in ship next = " + this.getClass());
		if (this.isAlive()) {
			decrementDisabled();
			decrementMagnet();
			//decrementEMP();
			decrementJump();
			if (isSleeping())
				return;

			score++;

			if (fireBulletWait > 0) fireBulletWait--;

			//if (shieldUp()) {shieldLeft--; shieldCurrent--;}
			if (isShieldUp()) { 
				// score--;  // previously no points were earned while the shield was up
				if (shield > 0) shield -= 1;
				else toggleShield();
			}

			// make the ship move as regular
			super.next();
		}
	}

}
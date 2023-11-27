package zorq.controllers;

import java.awt.event.KeyEvent;
import java.util.Random;

import zorq.Constants;
import zorq.ControllerAction;
import zorq.FireBullet;
import zorq.FireEMP;
import zorq.FireLaser;
import zorq.FlightAdjustment;
import zorq.Ship;
import zorq.ShipJump;
import zorq.ToggleShield;
import zorq.Universe;
import zorq.base.*;

// **** DO NOT USE THIS CONTROLLER AS AN EXAMPLE.  IT EXISTS ONLY
// **** TO ALLOW A HUMAN PLAYER TO INTERACT WITH THE CONTROLLERS.
public final class HumanPlayer extends ShipController{ 

	public HumanPlayer(Ship ship) { super(ship); }

	@Override
	public String getName() { return "HumanPlayer"; }
	
	private boolean hasShielded = false;
	@Override
	public ControllerAction makeMove(Universe gd) {
		
		if (!hasShielded && ship.canShield()){
			hasShielded = true;
			return new ToggleShield();
		}
		
		if(gd.getKeyDown()[KeyEvent.VK_SPACE]){
			if (ship.canFire()) {
				return new FireBullet();
			}
		}

		if(gd.getKeyDown()[KeyEvent.VK_Z]){
			if (ship.canFireLaser()) {
				return new FireLaser();
			}
		}

		if(gd.getKeyDown()[KeyEvent.VK_E]){
			if (ship.canFireEMP()) {
				return new FireEMP();
			}
		}

		if(gd.getKeyDown()[KeyEvent.VK_T]){
			if (ship.canTeleport()) {
				Random r = new Random();
				return new ShipJump( r.nextInt(Universe.getWidth()), r.nextInt(Universe.getHeight()));
			}
		}
		
		FlightAdjustment fa = new FlightAdjustment();
		
		float facing = ship.getFacing();
		if(gd.getKeyDown()[KeyEvent.VK_A]){
			facing+=.3;
		}
		else if(gd.getKeyDown()[KeyEvent.VK_D]){
			facing-=.3;
		}
		
		fa.setAcceleration(gd.getKeyDown()[KeyEvent.VK_W] ? Constants.maxShipAccel : gd.getKeyDown()[KeyEvent.VK_S] ? -Constants.maxShipAccel : 0.0f);
		fa.setFacing(facing);
		
		return fa;
	}
	
	/*
	@Override
	public Bullet fireBullet(Universe gd) {
		if(gd.getKeyDown()[KeyEvent.VK_SPACE]){
			
			
			float x = ship.getX() + (ship.getSpeed()+5.0f) * (float)Math.sin(ship.getFacing());
		    float y = ship.getY() + (ship.getSpeed()+5.0f) * (float)Math.cos(ship.getFacing());
			
			Bullet bullet = new Bullet(x,y);
			bullet.setHeading(ship.getFacing());
			bullet.setSpeed(8.0f);
			
			
			return bullet;
		}
		else{
			return null;
		}
	}
	*/

}
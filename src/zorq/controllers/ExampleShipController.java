package zorq.controllers;

import java.util.Random;
import java.util.Vector;

import zorq.ControllerAction;
import zorq.Engine;
import zorq.FireBullet;
import zorq.FlightAdjustment;
import zorq.Ship;
import zorq.ToggleShield;
import zorq.Universe;
import zorq.Util;
import zorq.Visualizer;
import zorq.base.ShipController;

/** Bare bones ship controller (placeholder).
 * Do not model anything you do after this controller.
 **/
public final class ExampleShipController extends ShipController{

	
	private boolean fireBullet = false;
	private boolean toggleShield = true;
	
	public ExampleShipController(Ship ship) {
		super(ship);
	}

	@Override
	public String getName() { return "Really Stupid"; }

	/**
	 * This is the method that the engine calls to get the next move of the
	 * ship. It can call other methods and then return the move to make.
	 */
	@Override
	public ControllerAction makeMove(Universe u) {
				
		if(fireBullet){
			fireBullet = false;
			return new FireBullet();
		}
		else
			fireBullet = true;
		
		if (toggleShield) {
			toggleShield = false;
			return new ToggleShield();
		}
		else
			toggleShield = true;
		
		FlightAdjustment fa = new FlightAdjustment();
		Random rnd = new Random();
		
		fa.setAcceleration(.05f);
		fa.setFacing(6.28318531f * rnd.nextFloat());
		
		return fa;
	}


}

package zorq;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zorq.ControllerAction;
import zorq.FireBullet;
import zorq.FlightAdjustment;
import zorq.Ship;
import zorq.Universe;
import zorq.Util;
import zorq.base.*;
import zorq.controllers.ExampleShipController2;

/**
 * This junit test class minimally tests the ExampleShipController2 class and also
 * uses the Engine to step the game forward to affect changes in the Universe.
 * Ideally this test class should test each situation that a ship might find itself in by
 * ensuring that the controller acts as expected.  For example, given two nearby bonuses, does the controller
 * take the expected action?  Or, given a nearby enemy ship (with a certain status), does the controller
 * behave as expected?
 * 
 * @author John Hastings
 *
 */
class ExampleShipController2TestEngine {

	Universe u;
	Ship ship;
	ShipController controller;
	ControllerAction action;
	//Engine e;

	@BeforeAll
	static void beforeAll() { } // Run once before all test methods

	/**
	 * Run before each of the test methods to configure the environment.
	 * NOTE: the order of execution of the test cases is not predetermined.
	 * Put things unique to specific tests in the separate test methods.
	 */
	@BeforeEach
	void beforeEach() {
		
		//System.out.println("Before each test method");
		// Configure the base environment for the test cases

		// add controllers to test
		Vector<String> controllers = new Vector<String>();
		controllers.add("zorq.controllers.ExampleShipController2");
		//e = null;
		
		// Create the engine which will create the universe, controllers and a ship for each controller
		//try { e = Engine.getInstance(null, controllers); }
		//catch (Exception e1) { e1.printStackTrace(); }
		
		//e.setSpawnBonuses(false);
		//u = e.u;  // local link to the universe only if needed
		controller = u.getShipControllers().get(0);  // the first controller
		ship = controller.getShip();  // ship tied to this controller under test
		ship.setX(100); ship.setY(100);  // move ship to 100,100

		// Add other objects to the test universe
		// Note; these can be added as needed in the individual test methods
		// If this should be a ship tied to another controller to test, that should
		// be added to the set of controllers above.
		// The following test cases are basic, so additional controllers were not needed.
		Ship ship2 = new Ship(200,100,1);  // second ship for testing
		u.add(ship2);
	}

	@AfterEach
	void afterEach() { } // Run after each of the test methods

	@AfterAll
	static void afterAll() { } // Run once after all test methods have completed

	/** Test the very first action by the controller for a ship to the right of a FlyingObject.
	 * Make sure it's a flight adjustment, and has the expected settings.
	 * <p>
	 * Notice that when comparing reals, there might be a slight difference in results,
	 * so a delta (acceptable) error/difference can be included.
	 */
	@Test
	void testFirstMoveRightofEnemy() {
		System.out.println("Testing first move with ship to the right of an enemy");
		ship.setX(300);  // move ship to the right of enemy ship
		u.stepUniverse(); // step the universe forward one increment
		action = controller.getLastAction();  // first action
		assertTrue(action instanceof FlightAdjustment); // first action
		FlightAdjustment fa = (FlightAdjustment)action; //expected flight adjustment
		assertEquals(fa.getFacing(),(float)Util.PI, 0.0000005);
		assertEquals(fa.getAcceleration(),.05f);
	}

	/** Test the very first action by the controller for a ship to the left of a FlyingObject.
	 * Make sure it's a flight adjustment, and has the expected settings.
	 * <p>
	 * Notice that when comparing reals, there might be a slight difference in results,
	 * so a delta (acceptable) error/difference can be included.
	 */
	@Test
	void testFirstMoveLeftofEnemy() {
		System.out.println("Testing first move with ship to the left of an enemy");
		u.stepUniverse(); // step the universe forward one increment
		action = controller.getLastAction();  // first action
		assertTrue(action instanceof FlightAdjustment); // first action
		FlightAdjustment fa = (FlightAdjustment)action; //expected flight adjustment
		assertEquals(fa.getFacing(), 0f, 0.0000005);
		assertEquals(fa.getAcceleration(),.05f);
	}

	/** Test the thirtieth action by the controller. This example controller always 
	 * fires a bullet every thirty frames.
	 */
	@Test
	void testThirtiethMove() {
		System.out.println("Testing 30th move move");
		for (int i=0; i<30; i++)
			u.stepUniverse(); // step the universe forward one increment
		action = controller.getLastAction();  // last/30th action
		assertTrue(action instanceof FireBullet); 
	}
	
	/** Testing 60th move for no particular reason other than to show that it's
	 * a repeat of the 30th move.
	 */
	@Test
	void testSixtiethMove() {
		System.out.println("Testing 60th move move");
		for (int i=0; i<60; i++)
			u.stepUniverse(); // step the universe forward one increment
		action = controller.getLastAction();  // last/60th action
		assertTrue(action instanceof FireBullet); 
	}

}

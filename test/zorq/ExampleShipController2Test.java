package zorq;

import static org.junit.jupiter.api.Assertions.*;

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
 * This junit test class minimally tests the ExampleShipController2 class.
 * Ideally this test class should test each situation that a ship might find itself in by
 * ensuring that the controller acts as expected.  For example, given two nearby bonuses, does the controller
 * take the expected action?  Or, given a nearby enemy ship (with a certain status), does the controller
 * behave as expected?
 * 
 * @author hastings
 *
 */
class ExampleShipController2Test {

	Universe u;
	Ship ship;
	ShipController controller;
	ControllerAction action;

	@BeforeAll
	static void beforeAll() { } // Run once before all test methods

	/**
	 * Run before each of the test methods to configure the environment.
	 * NOTE: the order of execution of the test cases is not predetermined
	 */
	@BeforeEach
	void beforeEach() {
		//System.out.println("Before each test method");
		// Configure the base environment for the test cases
		// Create universe, add ship to it, and link controller to ship
		u = Universe.INSTANCE;
		ship = new Ship(100,100,0);  // ship tied to this controller under test
		u.add(ship);
		controller = new ExampleShipController2(ship);

		// Add other objects to the test universe
		// Note; these can be added as needed in the individual test methods
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
		action = controller.makeMove(u);  // first action
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
		action = controller.makeMove(u);  // first action
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
			action = controller.makeMove(u);
		assertTrue(action instanceof FireBullet); // second action
	}
	
	/** Testing 60th move for no particular reason other than to show that it's
	 * a repeat of the 30th move.
	 */
	@Test
	void testSixtiethMove() {
		System.out.println("Testing 60th move move");
		for (int i=0; i<60; i++)
			action = controller.makeMove(u);
		assertTrue(action instanceof FireBullet); // second action
	}

}

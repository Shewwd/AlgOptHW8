package zorq;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zorq.Bullet;
import zorq.Constants;
import zorq.FireBullet;
import zorq.FlightAdjustment;
import zorq.FlyingObject;
import zorq.Ship;
import zorq.Universe;
import zorq.base.ShipController;
import zorq.controllers.ExampleShipController2;

class FireBulletTest {

	Universe u;
	Ship ship;
	FireBullet fb;
	ShipController sc;
	
	@BeforeEach
	void beforeEach() {
		//System.out.println("Before each test method");
		// Configure the base environment for the test cases
		// Create universe, add ship to it, and link controller to ship
		u = Universe.INSTANCE;
		u.reset();
		ship = new Ship(100,100,0);  // ship tied to this controller under test
		u.add(ship);
		sc = new ExampleShipController2(ship);
		ship.setShipController(sc);
	}
	
	@Test
	void testFireEastSitting() {
		//System.out.println("Testing first move with ship to the right of an enemy");
		//ship.setX(300);  // move ship to the right of enemy ship
		ship.setHeading(0);
		ship.setSpeed(0);
		fb = new FireBullet();
		//System.out.println(ship);
		//System.out.println(ship.getShipController());
		fb.act(ship, u);
		Vector<FlyingObject> flyingObjects = u.getFlyingObjects();
			//System.out.println(fo);
		for (FlyingObject fo: flyingObjects) {
			System.out.println("fo = " + fo);
		}
		assertTrue(flyingObjects.get(0) instanceof Ship); // first action
		Ship s = (Ship)flyingObjects.get(0);
		assertTrue(ship.equals(s));
		assertTrue(flyingObjects.get(1) instanceof Bullet);
		Bullet b = (Bullet)flyingObjects.get(1);
		System.out.println("b x,y="+b.getX()+" "+b.getY());
		assertEquals(b.getFacing(), 0 , 0.0000005);
		assertEquals(b.getSpeed(), Bullet.INIT_SPEED, 0.0000005);
		assertEquals(b.getX(), 117, 0.0000005);
		assertEquals(b.getY(), 100, 0.0000005);
		float previousX = b.getX();
		u.stepUniverse();
		flyingObjects = u.getFlyingObjects();
		assertTrue(flyingObjects.get(0) instanceof Ship); // first action
		s = (Ship)flyingObjects.get(0);
		assertTrue(ship.equals(s));
		assertTrue(flyingObjects.get(1) instanceof Bullet);
		b = (Bullet)flyingObjects.get(1);
		System.out.println("b x,y="+b.getX()+" "+b.getY());
		System.out.println("s x,y="+s.getX()+" "+s.getY());
		assertEquals(b.getX(), previousX+Bullet.INIT_SPEED, 0.0000005);
		assertEquals(b.getY(), 100, 0.0000005);
		assertEquals(s.getX(), 100, 0.0000005);
		assertEquals(s.getY(), 100, 0.0000005);
		System.out.println(u.getSleeping());
		
		//FlightAdjustment fa = (FlightAdjustment)action; //expected flight adjustment
		//assertEquals(fa.getFacing(),(float)Util.PI, 0.0000005);
		//assertEquals(fa.getAcceleration(),.05f);
	}
	
	@Test
	void testFireEastFlying() {
		//System.out.println("Testing first move with ship to the right of an enemy");
		//ship.setX(300);  // move ship to the right of enemy ship
		ship.setHeading(0);
		ship.setSpeed(Constants.maxShipSpeed);
		fb = new FireBullet();
		//System.out.println(ship);
		//System.out.println(ship.getShipController());
		fb.act(ship, u);
		Vector<FlyingObject> flyingObjects = u.getFlyingObjects();
			//System.out.println(fo);
		for (FlyingObject fo: flyingObjects) {
			System.out.println("fo = " + fo);
		}
		assertTrue(flyingObjects.get(0) instanceof Ship); // first action
		Ship s = (Ship)flyingObjects.get(0);
		assertTrue(ship.equals(s));
		assertTrue(flyingObjects.get(1) instanceof Bullet);
		Bullet b = (Bullet)flyingObjects.get(1);
		System.out.println("b x,y="+b.getX()+" "+b.getY());
		assertEquals(b.getFacing(), 0 , 0.0000005);
		assertEquals(b.getSpeed(), Bullet.INIT_SPEED + Constants.maxShipSpeed, 0.0000005);
		assertEquals(b.getX(), 117, 0.0000005);
		assertEquals(b.getY(), 100, 0.0000005);
		float previousX = b.getX();
		u.stepUniverse();
		flyingObjects = u.getFlyingObjects();
		assertTrue(flyingObjects.get(0) instanceof Ship); // first action
		s = (Ship)flyingObjects.get(0);
		assertTrue(ship.equals(s));
		assertTrue(flyingObjects.get(1) instanceof Bullet);
		b = (Bullet)flyingObjects.get(1);
		System.out.println("b x,y="+b.getX()+" "+b.getY());
		System.out.println("s x,y="+s.getX()+" "+s.getY());
		assertEquals(b.getX(), previousX+Bullet.INIT_SPEED+ Constants.maxShipSpeed, 0.0000005);
		assertEquals(b.getY(), 100, 0.0000005);
		assertEquals(s.getX(), 100 + Constants.maxShipSpeed, 0.0000005);
		assertEquals(s.getY(), 100, 0.0000005);
		System.out.println(u.getSleeping());
	}

		@Test
		void testFireSittingThenFlyEast() {
			ship.setHeading(0);
			ship.setSpeed(0);
			fb = new FireBullet();
			ship.setSpeed(5);
			fb.act(ship, u);
			u.stepUniverse();
			Vector<FlyingObject> flyingObjects = u.getFlyingObjects();
				//System.out.println(fo);
			for (FlyingObject fo: flyingObjects) {
				System.out.println("fo = " + fo);
			} 
			ship.setSpeed(20);
			FlightAdjustment fa = new FlightAdjustment(ship.getHeading(), 20.0f);
			fa.act(ship, u);
			u.stepUniverse();
			Ship s = (Ship)flyingObjects.get(0);
			Bullet b = (Bullet)flyingObjects.get(1);
			System.out.println("b x,y="+b.getX()+" "+b.getY());
			System.out.println("s x,y="+s.getX()+" "+s.getY());
			System.out.println("ship x,y="+ship.getX()+" "+ship.getY());
			System.out.println("sleepers = "+u.getSleeping());
		}

}

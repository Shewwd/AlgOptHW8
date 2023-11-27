package zorq;

import java.util.Random;

public class SpawnFactory {
	
	static void makeSpawn(Universe u) {
		Random rnd = Constants.rnd;
		int bonusScale = u.getShipControllers().size();
		
		//BombBonus.makeSpawn(u);
		//FuelBonus.makeSpawn(u);
		if (rnd.nextFloat()*3000 < 1) {
			int type = rnd.nextInt(4);
			int x=0, y=0;
			float heading=0;
			//System.out.println("type="+type);
			
			switch (type) {
			case 0:  // from top
				x = rnd.nextInt(Universe.getWidth()-2*BlackHole.RADIUS)+BlackHole.RADIUS;
				y = Universe.getHeight()-BlackHole.RADIUS;
				heading = 3.0f * (float) Math.PI / 2.0f; //5.0f * (float) Math.PI / 4.0f;
				break;
			case 1:  // from right
				x = Universe.getWidth()-BlackHole.RADIUS;
				y = rnd.nextInt(Universe.getHeight()-2*BlackHole.RADIUS)+BlackHole.RADIUS;
				heading = (float) Math.PI;  // go west
				break;
			case 2:  // from bottom
				x = rnd.nextInt(Universe.getWidth()-2*BlackHole.RADIUS)+BlackHole.RADIUS;
				y = 0+BlackHole.RADIUS;
				heading = (float) Math.PI / 2.0f;  // go north
				break;
			case 3:  // from left
				x=0+BlackHole.RADIUS;
				y = rnd.nextInt(Universe.getHeight()-2*BlackHole.RADIUS)+BlackHole.RADIUS;
				heading = 0;  // go east
				break;
			}
			BlackHole bh = new BlackHole(x, y, u);
			bh.setHeading(heading);
			bh.setSpeed(1.0f);
			u.add(bh);
		}

		/*
			if (rnd.nextFloat()*1000 < 1) {
				LifeBonus b = new LifeBonus(rnd.nextInt(Universe.WIDTH), rnd.nextInt(Universe.HEIGHT), 1); //(1+rnd.nextInt(2)));
				gd.add(b);
			}
		 */

		// Randomly spawn bonuses. Scale the frequency based on the number of players
		if (rnd.nextFloat()*6000/bonusScale < 1) {
			ShipJumpBonus b = new ShipJumpBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 5*(1+rnd.nextInt(5)));
			u.add(b);
		}

		if (rnd.nextFloat()*6000/bonusScale < 1) {
			EmpBonus b = new EmpBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 5*(1+rnd.nextInt(5)));
			u.add(b);
		}

		if (rnd.nextFloat()*6000/bonusScale < 1) {
			MagnetBonus b = new MagnetBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 5*(1+rnd.nextInt(5)));
			u.add(b);
		}

		if (rnd.nextFloat()*3000/bonusScale < 1) {
			BulletBonus b = new BulletBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 5*(1+rnd.nextInt(5)));
			u.add(b);
		}

		if (rnd.nextFloat()*3000/bonusScale < 1) {
			FuelBonus f = new FuelBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 100*(5+rnd.nextInt(6)));
			u.add(f);
		}

		if (rnd.nextFloat()*3000/bonusScale < 1) {
			PointsBonus b = new PointsBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 100*(5+rnd.nextInt(6)));
			u.add(b);
		}

		if (rnd.nextFloat()*3000/bonusScale < 1) {
			ShieldBonus b = new ShieldBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 100*(1+rnd.nextInt(10)));
			u.add(b);
		}

		if (rnd.nextFloat()*3000/bonusScale < 1) {
			LaserBonus b = new LaserBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 1*(1+rnd.nextInt(5)));
			u.add(b);
		}

		if (rnd.nextFloat()*3000/bonusScale < 1) {
			BombBonus b = new BombBonus(rnd.nextInt(Universe.getWidth()), rnd.nextInt(Universe.getHeight()), 0);
			u.add(b);
		}
	}

	//private static ArrayList<Supplier<? extends SpawningObject>> registeredSpawners = 
	//		new ArrayList<Supplier<? extends SpawningObject>>();
	
	//private static ArrayList<? extends SpawningObject> registeredSpawners = 
	//		new ArrayList<? extends SpawningObject>();
	
	/*
	protected static void registerSpawningObject(Supplier<? extends SpawningObject> c) {
		registeredSpawners.add(c);
		System.out.println("registered " + c);
	}
	*/
	
	/*
	protected static void registerSpawningObject(<? extends SpawningObject> c) {
		registeredSpawners.add(c);
		System.out.println("registered " + c);
	}
	
	public static void makeSpawn(Universe u) {
		System.out.println("in spawn factory");
		if (registeredSpawners.size() > 0) {
			System.out.println("found spawners");
			Supplier<? extends SpawningObject> supplier = registeredSpawners.get(0);
			
			if (supplier != null) supplier.get(u);
		}
		else
			return;
		
	}
	*/
}

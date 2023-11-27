package zorq;

import java.awt.*;
import java.util.Random;
import java.util.function.Supplier;

import zorq.graphics.BombBonusDrawer;

/** 
 * Game object which blows up ships that hit it
 * @author John Hastings
 *
 */
public final class BombBonus extends Bonus implements SpawningObject {
//	static int bomb_bonus_rate = 3000;
//	protected static void setBombBonusRate(int rate) {
//		bomb_bonus_rate = rate;
//	}
//public class BombBonus extends Bonus implements Supplier<SpawningObject>, SpawningObject {
//public class BombBonus extends Bonus {

	/*
	static { SpawnFactory.registerSpawningObject(new BombBonusSupplier()); }
	static { SpawnFactory.registerSpawningObject(new BombBonusSupplier()); }
	
	static void makeSpawn(Universe u) {
		int bonusScale = u.getShipControllers().size();
		Random rnd = Constants.rnd;
		if (rnd.nextFloat()*Constants.BONUS_SPAWN_RATE/bonusScale < 1) {
			int x = rnd.nextInt(Universe.getWidth());
			int y = rnd.nextInt(Universe.getHeight());
			BombBonus b = new BombBonus(x, y, 0);
			u.add(b);
		}
	}
	*/
	
	public BombBonus(float x, float y, int amount) {
		super(x, y, amount);
		drawer = new BombBonusDrawer(this);
	}

	public Color getColor() { return Constants.bombColor; }
	
	// return true if the object should be removed after being hit
	protected boolean hitBy(FlyingObject fo, Universe u) {
		if (fo instanceof EMP) return true;
		else if (fo instanceof Ship) return true;
		else return super.hitBy(fo, u);
	}
}

/*
class BombBonusSupplier implements Supplier<BombBonus>{

	@Override
	public BombBonus get() {
		System.out.println("HELLO!!!!");
		//BombBonus.makeSpawn(u);
		// TODO Auto-generated method stub
		return null;
	}
}
*/

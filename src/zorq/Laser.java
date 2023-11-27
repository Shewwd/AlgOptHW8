package zorq;

/**
 * Laser flying object.
 * 
 * @author hastings
 *
 */
public final class Laser extends Bullet {

	public Laser(float x, float y, Ship shipCreator) {
		super(x,y,shipCreator,2);
		life = MAX_LIFE/5;
	}
}

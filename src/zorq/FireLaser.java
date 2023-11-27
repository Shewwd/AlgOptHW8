package zorq;

import zorq.base.effects.Sound;

/**
 * Fire laser action returned by controllers from their makeMove method.
 * Works only when the ship canFireLaser method returns true.
 * 
 * @author hastings
 *
 */
public final class FireLaser extends ControllerAction {

	private static final int BULLET_LEAD = 25; 
	
	@Override
	protected void act(Ship s, Universe u) {
		if (s.canFireLaser()) {
			s.laserFired();
			//System.out.println(s.getShipController().getName()+ " fired laser");

			if (Constants.SOUND) Sound.soundFireWeapon.play();

			for (int i=0; i<20; i++) {
				float x = s.getX() + (s.getRadius()+ BULLET_LEAD+(i*20)) * (float)Math.cos(s.getFacing());
				float y = s.getY() + (s.getRadius()+ BULLET_LEAD+(i*20)) * (float)Math.sin(s.getFacing());

				Laser laser = new Laser(x,y,s);
				laser.setHeading(s.getFacing());
				laser.setSpeed(s.getSpeed()*1.5f + Bullet.INIT_SPEED);
				//laser.setSpeed(s.getSpeed()*1.5f);
				//laser.setBulletImage(bulletImage);
				u.add(laser);
			}
		}
		
	}
	
	
}

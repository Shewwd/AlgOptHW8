package zorq;

import zorq.base.effects.Sound;

/**
 * Fire bullet action returned by controllers from their makeMove method.
 * Works only when the ship canFire method returns true.
 * 
 * @author hastings, rokusek
 *
 */
public final class FireBullet extends ControllerAction {

	private static final int BULLET_LEAD = 25; 
	
	@Override
	protected void act(Ship sh, Universe u) {
		//System.out.println(sh.getShipController().getName()+ " wants to fire");
		if (sh.canFire()) {
			//System.out.println(sh.getShipController().getName()+ " will fire");
			sh.bulletFired();
			if (Constants.SOUND) Sound.soundFireWeapon.play();

			if(sh.getWeaponLevel() == 1)
			{

				float x = sh.getX() + (sh.getRadius()+ BULLET_LEAD) * (float)Math.cos(sh.getFacing());
				float y = sh.getY() + (sh.getRadius()+ BULLET_LEAD) * (float)Math.sin(sh.getFacing());

				Bullet bullet = new Bullet(x,y,sh,sh.getWeaponLevel());
				bullet.setHeading(sh.getFacing()); //- .1f);
				bullet.setSpeed(findSpeed(sh));
				u.add(bullet);
				//System.out.println("b= "+bullet+" s x,y="+sh.getShipController().getName()+" "+sh.getX()+","+sh.getY()+
				//		" b x,y="+bullet.getX()+","+bullet.getY());
			}
			else
			{
				float locIncrement = Util.PI/(sh.getWeaponLevel()-1);
				float headIncrement = .2f/(sh.getWeaponLevel()-1);

				for(int i = 0; i < sh.getWeaponLevel(); i++)
				{
					float x = sh.getX() + (sh.getRadius()-6) * (float)Math.cos(sh.getFacing() + locIncrement*i - Util.PI/2);
					float y = sh.getY() + (sh.getRadius()-6) * (float)Math.sin(sh.getFacing() +  locIncrement*i - Util.PI/2);

					x = x + (sh.getRadius()+ BULLET_LEAD) * (float)Math.cos(sh.getFacing());
					y = y + (sh.getRadius()+ BULLET_LEAD) * (float)Math.sin(sh.getFacing());

					Bullet bullet = new Bullet(x,y,sh,sh.getWeaponLevel());
					bullet.setHeading(sh.getFacing() + (i*headIncrement - .1f)*(float)Math.ceil(sh.getWeaponLevel()%2/2 + 
							(Math.abs(sh.getWeaponLevel() - 2*(i+1)))*(Math.abs(sh.getWeaponLevel() - 2*(i)))/
							((float)Math.pow(sh.getWeaponLevel(), 2))));

					bullet.setSpeed(findSpeed(sh));
					u.add(bullet);
					//System.out.println("b= "+bullet+"s x,y="+sh.getShipController().getName()+" "+sh.getX()+","+sh.getY()+
					//		" b x,y="+bullet.getX()+","+bullet.getY());
				}
			}
		}
	}

	protected float findSpeed(Ship ship){
		//System.out.println("speed = " + ship.getSpeed());
		return Bullet.INIT_SPEED + ship.getSpeed();
		/*
		float pi = (float)Math.PI;

		float facing =(float)( ship.getFacing() % (2*Util.PI) ),
				heading = (float)( ship.getHeading() % (2*Util.PI) );

		if (facing < 0)
			facing = 2*pi + facing;
		if (heading < 0)
			heading = 2*pi + heading;

		float hMf = heading - facing,
				fMh = facing - heading;

		if(heading > facing){
			if (hMf < pi/4){//add speed
				return 8f + ship.getSpeed(); 
			}
			else if (hMf > 3*pi/4 && hMf < 5*pi/4){//minus speed
				return 8f - ship.getSpeed(); 
			}
			else return 8f;
		}//end if
		else{
			if (fMh > 3*pi/4 && fMh < 5*pi/4){//minus speed
				return 8f - ship.getSpeed(); 
			}
			else if (fMh < pi/4){//add speed
				return 8f + ship.getSpeed(); 
			}
			else return 8f;
		}//end else
		*/
	}

}

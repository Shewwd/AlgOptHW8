package zorq;

/**
 * Fire EMP action returned by controllers from their makeMove method.
 * Works only when the ship canFireEMP method returns true.
 * 
 * @author hastings
 *
 */
public final class FireEMP extends ControllerAction {

	@Override
	protected void act(Ship s, Universe u) {
		if (s.canFireEMP()) {
			s.empFired();
			//if (Constants.SOUND) Sound.soundFireWeapon.play();

			float x = s.getX();
			float y = s.getY();

			EMP emp = new EMP(x,y,s);
			u.add(emp);
		}
	}
}

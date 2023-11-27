package zorq;

/**
 * Toggle shield action returned by controllers from their makeMove method.
 * Turns the shield off if it was on, and on if it was off (and the ship canShield).
 * 
 * @author jdh
 *
 */
public final class ToggleShield extends ControllerAction {

	@Override
	protected void act(Ship s, Universe u) {
		s.toggleShield();
	} }

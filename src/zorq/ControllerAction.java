package zorq;

/**
 * Parent class of all actions returned by ship controllers from their makeMove method.
 * 
 * @author hastings
 *
 */
public abstract class ControllerAction { 
	abstract protected void act(Ship s, Universe u);
}

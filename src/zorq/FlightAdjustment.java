package zorq;

/**
 * Flight adjustment action returned by controllers from their makeMove method
 * 
 * @author hastings
 *
 */
public final class FlightAdjustment extends ControllerAction {

	private float facing;
	private float acceleration;
	private boolean stop;

	private FlightAdjustment(float facing, float acceleration, boolean stop) {
		this.facing = facing;
		this.acceleration = acceleration;
		this.stop = stop;
	}

	public FlightAdjustment(float facing, float acceleration) { this(facing, acceleration, false); }
	public FlightAdjustment() { this(0, 0, false); }
	public FlightAdjustment(boolean stop) { this(0, 0, stop); }
	public boolean getStop() { return stop; }
	public void setStop(boolean stop) { this.stop = stop; }
	public float getAcceleration() { return acceleration; }
	public void setAcceleration(float acceleration) { this.acceleration = acceleration; }
	public float getFacing() { return facing; }
	public void setFacing(float facing) { this.facing = facing; }

	@Override
	protected void act(Ship s, Universe u) {
		s.setFacing(this.getFacing()%(2f*Util.PI)); // < 0 ? fa.getFacing() + (float)Math.PI*2.0f :fa.getFacing());

		float accel;
		if (this.getStop()) { if (s.getFuelAmmount() > 0) s.setSpeed(0); }
		else {
			//ship.setFacing(fa.getFacing() < 0 ? fa.getFacing() + (float)Math.PI*2.0f :fa.getFacing());

			accel = this.getAcceleration();
			accel = accel > Constants.maxShipAccel ? Constants.maxShipAccel : acceleration;

			s.setAcceleration(accel);    // try to change acceleration (fuel dependent)
		}

		accel = s.getAcceleration();
		float dX = (float) (s.getSpeed() * Math.sin(s.getHeading()) + accel
				* Math.sin(this.getFacing()));
		float dY = (float) (s.getSpeed() * Math.cos(s.getHeading()) + accel
				* Math.cos(s.getFacing()));

		float speed = (float) Math.sqrt((Math.pow(dX, 2) + Math.pow(dY, 2)));

		s.setSpeed(speed > Constants.maxShipSpeed ? Constants.maxShipSpeed : speed);

		s.setHeading((float) (Math.atan2(dX , dY)));
		
	}
}

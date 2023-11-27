package zorq;

/** Utility class of math functions.
 * 
 * @author hastings
 **/
public final class Util {

	public static final float PI = (float)Math.PI;

	/**
	 * Calculate distance between two points using the distance formula.
	 * @param x1 x for object 1
	 * @param y1 y for object 1
	 * @param x2 x for object 2
	 * @param y2 y for object 2
	 * @return distance between (x1,y1) and (x2,y2)
	 */
	public static float distance(float x1, float y1, float x2, float y2){
		return (float)Math.sqrt(
				Math.pow(x1 - x2,2.0) + 
				Math.pow(y1 - y2,2.0));
	}

	/**
	 * Return the square of the distance between the objects.
	 * <p>No sqrt as it's TOO slow.
	 * 
	 */
	public static float distance_squared(float x1, float y1, float x2, float y2){
		return (x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2);
	}
	/**
	 * Calculate the angle between two points. 
	 * @param x1 - object 1 x
	 * @param y1 - object 1 y
	 * @param x2 - object 2 x
	 * @param y2 - object 2 y
	 * @return angle between (x1,y1) and (x2,y2) in radians
	 * 
	 * <p>
	 * This can be used to set the facing of a ship to fly toward an object.
	 * <pre>
	 * Util.calcAngle(ship.getX(), ship.getY(), 
	 *                object.getX(), object.getY());
	 * </pre>
	 * <p>
	 * <p>This method can also be used to set the ship facing in order to target an enemy ship by leading the target as follows.
	 * The amount of lead given to the shot is given by (int) (enemyShipDistance / 10):
	 * <pre>
	 * Util.calcAngle(ship.getX(),ship.getY(), 
	 * enemyShip.getNextX((int) (enemyShipDistance / 10)),enemyShip.getNextY((int) (enemyShipDistance / 10)));
	 * </pre>
	 */
	public static float calcAngle(float x1, float y1, float x2, float y2)
	{
		float x = x2 - x1;
		float y = y2 - y1;
		float angle = (float)Math.atan2(x,y);
		angle = Util.PI * 2f - angle + Util.PI / 2f;
		return (angle%(Util.PI*2f));
		
		/*
		float dx = x2-x1;
		float dy = y2-y1;
		double angle=0.0d;

		// Calculate angle
		if (dx == 0.0)
		{
			if (dy == 0.0)
				angle = 0.0;
			else if (dy > 0.0)
				angle = Math.PI / 2.0;
			else
				angle = Math.PI * 3.0 / 2.0;
		}
		else if (dy == 0.0)
		{
			if (dx > 0.0)
				angle = 0.0;
			else
				angle = Math.PI;
		}
		else
		{
			if (dx < 0.0)
				angle = Math.atan(dy/dx) + Math.PI;
			else if (dy < 0.0)
				angle = Math.atan(dy/dx) + (2*Math.PI);
			else
				angle = Math.atan(dy/dx);
		}

		// Return Radians
		return (float)angle;
		*/
	}
	
	// Not used???
	@Deprecated
	private static float findAngle(float Ax, float Ay, float Bx, float By, float Cx, float Cy){
		/*
		float x = x1 - x2;
		float y = y1 - y2;

		//if(y >= 0)
			return (float)Math.atan2(x,y);
		//else //if(y < 0)
		//	return (float)Math.atan2(x,y) + PI;
		 */

		float dotProduct = dotProduct(Ax, Ay, Bx, By, Cx, Cy);
		float crossProduct = crossProductLength(Ax, Ay, Bx, By, Cx, Cy);
		return (float)Math.atan2(crossProduct, dotProduct);
	}

	// Not used???
	@Deprecated
	private static float dotProduct( float Ax, float Ay, float Bx, float By, float Cx, float Cy){
		float BAx = Ax - Bx;
		float BAy = Ay - By;
		float BCx = Cx - Bx;
		float BCy = Cy - By;
		return BAx * BCx + BAy * BCy;
	}

	// Not used???
	@Deprecated
	private static float crossProductLength( float Ax, float Ay, float Bx, float By, float Cx, float Cy){

		float BAx = Ax - Bx;
		float BAy = Ay - By;
		float BCx = Cx - Bx;
		float BCy = Cy - By;

		return BAx * BCy - BAy * BCx;
	}
	
	// Not used???
	/**
	 * 
	 * @param angle1
	 * @param magnitude1
	 * @param angle2
	 * @param magnitude2
	 * @return the angle, if you want the magnitude, too bad....
	 */
	@Deprecated
	private static float addVector(
			float angle1, float magnitude1, 
			float angle2, float magnitude2){

		float magnitude1x = magnitude1*(float)Math.sin(angle1);
		float magnitude1y = magnitude1*(float)Math.cos(angle1);

		float magnitude2x = magnitude2*(float)Math.sin(angle2);
		float magnitude2y = magnitude2*(float)Math.cos(angle2);

		float rrx = magnitude1x-(-1)*magnitude2x-0;
		float rry = magnitude1y-(-1)*magnitude2y-0;

		//float magnitude = (float)Math.sqrt(rrx*rrx-(-1)*rry*rry-0);
		float angle = (float)Math.atan2(rrx,rry);
		return angle;
	}

	/**
	 * 
	 * <b>Use calcAngle instead.</b>
	 * 
	 * <p>Find the angle between two points.  
	 * 
	 * @param x1 - object 1 x
	 * @param y1 - object 1 y
	 * @param x2 - object 2 x
	 * @param y2 - object 2 y
	 * @return angle between (x1,y1) and (x2,y2) in radians
	 * 
	 * <p>This method can be used to set the ship facing in order to target an enemy ship by leading the target as follows.
	 * The amount of lead given to the shot is given by (int) (enemyShipDistance / 10):
	 * <pre>
	 * Util.PI * 2f - Util.findAngle(ship.getX(),ship.getY(), 
	 * enemyShip.getNextX((int) (enemyShipDistance / 10)),enemyShip.getNextY((int) (enemyShipDistance / 10)))
	 * + Util.PI / 2;
	 * </pre>
	 */
	@Deprecated
	public static float findAngle(float x1, float y1, float x2, float y2 ){

		float x = x2 - x1;
		float y = y2 - y1;

		return (float)Math.atan2(x,y);
		// else //if(y < 0)
		//	 return ((float)Math.atan2(x,y) + PI)/;
		/*
		if (y >= 0)
			return (float) Math.atan(x / y);
		else
			return (float) Math.atan(x / y) + PI;
		// else return 0;
		 */
	}
}




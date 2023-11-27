package zorq.graphics;

import java.awt.Graphics;

import zorq.Constants;
import zorq.EMP;
import zorq.FlyingObject;
import zorq.Visualizer;

public final class EMPDrawer extends FlyingObjectDrawer {

	public EMPDrawer(FlyingObject fo) {  super(fo);  }

	
	@Override
	public void paint(Graphics g) {
		EMP emp = (EMP)fo;
		if (emp.getLife() <= 0) return;
		//radius+=RADIUS_STEP;
		g.setColor(emp.getColor());

		float scaleX = Visualizer.getScaleX();
		float scaleY = Visualizer.getScaleY();
		int sX = (int)(emp.getX()*scaleX);
		int sY = (int)(emp.getY()*scaleY);
		int sWidth = (int)(emp.getRadius()*2*scaleX);
		int sHeight = (int)(emp.getRadius()*2*scaleY);
		g.drawOval(
				(int)(sX-sWidth/2),
				Constants.DISPLAY_HEIGHT - (int)(sY+sHeight/2),
				sWidth ,
				sHeight);
	}

}

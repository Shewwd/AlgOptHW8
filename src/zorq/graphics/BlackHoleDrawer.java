package zorq.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import zorq.BlackHole;
import zorq.Constants;
import zorq.FlyingObject;
import zorq.Visualizer;

public final class BlackHoleDrawer extends FlyingObjectDrawer {

	public static Image holeImage = Toolkit.getDefaultToolkit().getImage("images/blackhole2.png");
	public Image getImage() {return holeImage;};
	public BlackHoleDrawer(FlyingObject fo) { super(fo); }

	@Override
	public void paint(Graphics g) {
		BlackHole bh = (BlackHole)fo;
		int width = 300;
		int height = 308;

		float scaleX = Visualizer.getScaleX();
		float scaleY = Visualizer.getScaleY();
		int sWidth = (int)(width*scaleX);
		int sHeight = (int)(height*scaleY);
		int sX = (int)(bh.getX()*scaleX);
		int sY = (int)(bh.getY()*scaleY);
		g.drawImage(
				holeImage,
				sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (0+(int)sY-sHeight/2),
				sWidth+sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (sHeight+sY-sHeight/2),

				0,
				height,
				width,
				0,
				null);	
	}

}

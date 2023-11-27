package zorq.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import zorq.Bullet;
import zorq.Constants;
import zorq.FlyingObject;
import zorq.Visualizer;

public final class BulletDrawer extends FlyingObjectDrawer {

	public static Image bulletImage = Toolkit.getDefaultToolkit().getImage("images/bombs.png");
	public Image getImage() {return bulletImage;};
	
	int step = 10;
	int type = 0;
	
	// type is the weapon level
	public BulletDrawer(FlyingObject fo, int type) { 
		super(fo);
		this.type = type;
	}

	@Override
	public void paint(Graphics g) {
		Bullet bullet = (Bullet)fo;
		if (bullet.getLife() <=0) return;
		if(step == -1)
			step = 10;

		int col = 10;
		//int rows = 1;

		int width = 16;
		int height = 16;

		int xMod=0;
		int yMod=0;
		for(int j = 0; j < (10-step)-1; j++){
			xMod++;
			if(xMod == col){
				xMod = 0;
				yMod++;
			}
		}

		xMod*=width;
		yMod*=height;

		//yMod+=16*(shipCreator.getType()%8);
		yMod+=16*type;

		float scaleX = Visualizer.getScaleX();
		float scaleY = Visualizer.getScaleY();
		int sWidth = (int)(width*scaleX);
		int sHeight = (int)(height*scaleY);
		int sX = (int)(bullet.getX()*scaleX);
		int sY = (int)(bullet.getY()*scaleY);
		g.drawImage(
				this.getImage(),
				sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (0+(int)sY-sHeight/2),
				sWidth+sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (sHeight+sY-sHeight/2),
				0+xMod,
				height+yMod,
				width+xMod,
				0+yMod,

				//		Color.BLACK,
				null);

		this.step--;

	}

}

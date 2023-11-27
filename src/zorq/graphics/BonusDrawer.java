package zorq.graphics;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import zorq.Bonus;
import zorq.Constants;
import zorq.FlyingObject;
import zorq.Visualizer;

public class BonusDrawer extends FlyingObjectDrawer {

	public BonusDrawer(FlyingObject fo) {
		super(fo);
	}

	@Override
	public void paint(Graphics g) {
		Bonus bonus = (Bonus)fo;
		Image image = this.getImage(); 

		float scaleX = Visualizer.getScaleX();
		float scaleY = Visualizer.getScaleY();
		if (image == null) {  // if Drawer doesn't have an img, use text
			g.setColor(bonus.getColor()); // bonus color should be moved

			int sX = (int)(bonus.getX()*scaleX);
			int sY = (int)(bonus.getY()*scaleY);
			int sWidth = (int)(bonus.getWidth()*scaleX);
			int sHeight = (int)(bonus.getHeight()*scaleY);
			g.drawRect(
					(int)(sX-sWidth/2),
					Constants.DISPLAY_HEIGHT - (int)(sY+sHeight/2),
					sWidth ,
					sHeight);
			Font f = g.getFont();  // save original font
			g.setFont(new Font("TimesRoman", Font.PLAIN, (int)(20*scaleX)));
			g.drawString(Integer.toString(bonus.getAmount()),sX-sWidth/2 + 2,Constants.DISPLAY_HEIGHT -sY + sHeight/2);
			g.setFont(f);  // restore original font

		}
		else {
			int width = 36;
			int height = 36;

			int sWidth = (int)(width*scaleX);
			int sHeight = (int)(height*scaleY);
			int sX = (int)(bonus.getX()*scaleX);
			int sY = (int)(bonus.getY()*scaleY);
			g.drawImage(
					image,
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

}

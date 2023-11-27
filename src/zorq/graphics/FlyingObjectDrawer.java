package zorq.graphics;

import java.awt.Graphics;
import java.awt.Image;

import zorq.FlyingObject;

public abstract class FlyingObjectDrawer {
	FlyingObject fo;
	
	FlyingObjectDrawer(FlyingObject fo) {this.fo=fo;}
	public Image getImage() {return null; }
	
	public abstract void paint(Graphics g);
}

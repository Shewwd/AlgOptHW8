package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class PointsBonusDrawer extends BonusDrawer {
	public static Image pointsImage = Toolkit.getDefaultToolkit().getImage("images/dollar.png");;
	public Image getImage() {return pointsImage;};
	public PointsBonusDrawer(FlyingObject fo) { super(fo); }
}

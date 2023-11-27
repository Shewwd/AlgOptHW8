package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class LaserBonusDrawer extends BonusDrawer {
	public static Image laserBonusImage = Toolkit.getDefaultToolkit().getImage("images/laser.png");
	public Image getImage() {return laserBonusImage;};
	public LaserBonusDrawer(FlyingObject fo) { super(fo); }

}

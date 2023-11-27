package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class BulletBonusDrawer extends BonusDrawer {
	public static Image bulletBonusImage = Toolkit.getDefaultToolkit().getImage("images/bullet.png");
	public Image getImage() {return bulletBonusImage;};
	public BulletBonusDrawer(FlyingObject fo) { super(fo); }

}

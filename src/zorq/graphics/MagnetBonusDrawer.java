package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class MagnetBonusDrawer extends BonusDrawer {
	public static Image magnetBonusImage = Toolkit.getDefaultToolkit().getImage("images/magnet2.png");
	public Image getImage() {return magnetBonusImage;};
	public MagnetBonusDrawer(FlyingObject fo) { super(fo); }

}

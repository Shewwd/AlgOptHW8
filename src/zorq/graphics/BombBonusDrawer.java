package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class BombBonusDrawer extends BonusDrawer {
	public static Image bombImage = Toolkit.getDefaultToolkit().getImage("images/bomb.png");
	public Image getImage() {return bombImage;};
	public BombBonusDrawer(FlyingObject fo) { super(fo); }

}

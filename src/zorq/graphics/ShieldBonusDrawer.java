package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class ShieldBonusDrawer extends BonusDrawer {
	public static Image shieldBonusImage = Toolkit.getDefaultToolkit().getImage("images/shield.png");
	public Image getImage() {return shieldBonusImage;};
	public ShieldBonusDrawer(FlyingObject fo) { super(fo); }

}

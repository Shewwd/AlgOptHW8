package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class ShipJumpBonusDrawer extends BonusDrawer {
	public static Image shipJumpBonusImage = Toolkit.getDefaultToolkit().getImage("images/stargate.png");
	public Image getImage() {return shipJumpBonusImage;};
	public ShipJumpBonusDrawer(FlyingObject fo) { super(fo); }

}

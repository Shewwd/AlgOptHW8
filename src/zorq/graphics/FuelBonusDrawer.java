package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class FuelBonusDrawer extends BonusDrawer {

	public static Image fuelBonusImage = Toolkit.getDefaultToolkit().getImage("images/fuel2.png");;
	public Image getImage() {return fuelBonusImage;};
	public FuelBonusDrawer(FlyingObject fo) { super(fo); }

}

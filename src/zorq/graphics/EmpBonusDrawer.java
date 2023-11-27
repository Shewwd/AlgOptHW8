package zorq.graphics;

import java.awt.Image;
import java.awt.Toolkit;

import zorq.FlyingObject;

public final class EmpBonusDrawer extends BonusDrawer {
	public static Image empBonusImage = Toolkit.getDefaultToolkit().getImage("images/emp.png");
	public Image getImage() {return empBonusImage;};
	public EmpBonusDrawer(FlyingObject fo) { super(fo); }

}

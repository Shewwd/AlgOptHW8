package zorq.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import zorq.Constants;
import zorq.FlyingObject;
import zorq.Ship;
import zorq.Visualizer;

public final class ShipDrawer extends FlyingObjectDrawer {

	protected static Image shipImage = Toolkit.getDefaultToolkit().getImage("images/ships.png");
		
	public ShipDrawer(FlyingObject fo) { super(fo); }
	public Image getImage() { return shipImage; }
	
	@Override
	public void paint(Graphics g) {

		float scaleX = Visualizer.getScaleX();
		float scaleY = Visualizer.getScaleY();
		Ship ship = (Ship)fo;
		int col = 10;
		int rows = 4;

		int width = 36;
		int height = 36;

		//g.setColor(this.getColor());
		g.setColor(Color.GRAY);

		if(Constants.DISPLAY_SHIP_STATS) {

			// computing again ... BAD!!!
			int sX = (int)(ship.getX()*scaleX);
			int sY = Constants.DISPLAY_HEIGHT - (int)(ship.getY()*scaleY) + 20;


			//g.drawString(shipController.getName(),
			//		sX,
			//		sY);

			// Unfortunate direct link to display
			//System.out.println("ship="+ship);
			Visualizer.drawString(g,ship.getShipController().getName(),sX-(ship.getShipController().getName().length()*7)/2,sY+7,2);
			
			// display shield strength
			int length = (int) ((ship.getShieldHealth() / (double) Ship.SHIELD_HEALTH_FOR_LIFE) * 50);
			g.setColor( shieldColor(false));
			g.fillRect(sX-25,sY,length,3);				
			g.setColor(Color.WHITE);
			g.drawRect(sX-25,sY,50,3);

			// display fuel
			length = (int) ((ship.getFuelAmmount() / (double) Ship.INIT_FUEL) * 50);
			sY -= 5; //Constants.height - (int)(y*Constants.scaleY) + 10;
			g.setColor( Constants.fuelColor);
			g.fillRect(sX-25,sY,length,3);
			g.setColor(Color.WHITE);
			g.drawRect(sX-25,sY,50,3);

			// display bullets
			length = (int) ((ship.getBulletsAmmount() / (double) Ship.INIT_BULLETS) * 50);
			sY -= 5; //Constants.height - (int)(y*Constants.scaleY) + 10;
			g.setColor( Constants.bulletsColor);
			g.fillRect(sX-25,sY,length,3);
			g.setColor(Color.WHITE);
			g.drawRect(sX-25,sY,50,3);

			// display lives
			length = (int) ((ship.getLives() / (double) Ship.INIT_LIVES) * 50);
			sY -= 5; //Constants.height - (int)(y*Constants.scaleY) + 10;
			g.setColor( Constants.livesColor);
			g.fillRect(sX-25,sY,length,3);
			g.setColor(Color.WHITE);
			g.drawRect(sX-25,sY,50,3);

		}

		int xMod=0;
		int yMod=0;

		float angle = ship.getFacing();


		angle-= Math.PI/2.0f;
		angle-= Math.PI/16.0f;
		//System.out.println(angle);

		while(angle >= Math.PI*2){
			angle-=Math.PI*2;
		}

		if(angle < 0)
			angle+=Math.PI*2.0f;

		//System.out.println(25/((float)(col * rows)));

		int i;
		for(i = 1; i <= (col * rows); i++){

			if(angle < 2.0f*Math.PI*(i/((float)(col * rows)))){
				//	System.out.println(i);


				break;
			}	
		}

		i = 40 - i;

		for(int j = 0; j < i-1; j++){
			xMod++;
			if(xMod == col){
				xMod = 0;
				yMod++;
			}
		}

		yMod+=(ship.getType()%8)*4;

		//System.out.println(xMod + "\t" + yMod);

		xMod*=width;
		yMod*=height;

		/*
    img - the specified image to be drawn
    dx1 - the x coordinate of the first corner of the destination rectangle.
    dy1 - the y coordinate of the first corner of the destination rectangle.
    dx2 - the x coordinate of the second corner of the destination rectangle.
    dy2 - the y coordinate of the second corner of the destination rectangle.
    sx1 - the x coordinate of the first corner of the source rectangle.
    sy1 - the y coordinate of the first corner of the source rectangle.
    sx2 - the x coordinate of the second corner of the source rectangle.
    sy2 - the y coordinate of the second corner of the source rectangle.
    observer - object to be notified as more of the image is scaled and converted.
		 */


		int sWidth = (int)(width*scaleX);
		int sHeight = (int)(height*scaleY);
		int sX = (int)(ship.getX()*scaleX);
		int sY = (int)(ship.getY()*scaleY);
		g.drawImage(
				shipImage,
				sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (0+(int)sY-sHeight/2),
				sWidth+sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (sHeight+sY-sHeight/2),

				0+xMod,
				height+yMod,
				width+xMod,
				0+yMod,
				null);

		//LOOK AT THIS, SPECIFICALLY HIT RECENTLY FOR DRAWING SHIELD DIFFERENTLY
		// draw shield
		if (ship.isShieldUp()) {

			/*
			if(ship.hitRecently){
				g.setColor(Color.WHITE);
				hitRecently = false;
			}
			else{
				*/
				g.setColor( shieldColor(true));
			//}

			//				 computing again ... BAD!!!
			sWidth = (int)(width*scaleX);
			sHeight = (int)(height*scaleY);
			sX = (int)(ship.getX()*scaleX);
			sY = (int)(ship.getY()*scaleY);
			int wid = (int)(40*scaleX);
			int high = (int)(40*scaleY);
			g.fillOval(
					(int)(sX-(sWidth/2) + 1) ,
					Constants.DISPLAY_HEIGHT - (int)(sY+(sHeight/2)+ 1),
					wid - 1,
					high - 1 );
		}

	}
	
	protected Color amountColor ( int amount, int max, boolean trans){
		Color c = null;
		float amountRed = (float) ((1.0 - amount/(double)max));
		float amountGreen = (float) ((amount/(double)max));
		float amountBlue = 0;
		if (amountRed > 255) amountRed=255;
		if (amountGreen > 255) amountGreen =255;
		try {
			if (trans) c = new Color(amountRed,amountGreen,amountBlue,0.25f);
			else c = new Color(amountRed,amountGreen,amountBlue,1f);
		}
		catch (Exception e) {System.out.println("red="+amountRed+" green="+amountGreen+" blue="+amountBlue); }

		return c;
	}

	public Color shieldColor( boolean trans) {
		if (trans)
			return amountColor(100, Ship.SHIELD_HEALTH_FOR_LIFE, trans); //new Color(0f,255f,0f,0.25f);
		else return Constants.shieldColor;
		//return amountColor(shield, SHIELD_HEALTH_FOR_LIFE, trans);
	}

	protected Color fuelColor( boolean trans) { return Constants.fuelColor; }
	protected Color bulletsColor( boolean trans) { return Constants.bulletsColor; }


}

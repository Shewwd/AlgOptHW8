package zorq;

import java.awt.*;
import java.util.*;

import javax.swing.JPanel;

import zorq.base.effects.*;

/**
 * JPanel to which the game graphics are drawn.
 * 
 * <p>Using singleton pattern
 * 
 * @author hastings
 *
 */
public final class Visualizer extends JPanel {

	protected static final Visualizer INSTANCE = new Visualizer();
	
	private static final long serialVersionUID = 6793798862082812020L;

	private Universe u;
	private Graphics bufferGraphics;
	private Image offscreen;
	private Dimension dim;
	//private int curX, curY;
	private Image [] backImgs;
	//private Image backImage1, backImage2, backImage3, backImage4, curBackImage;
	private int dx1, dx2, dy1, dy2, srcx1, srcx2, srcy1, srcy2, dx3, dy3, dx4, dy4;
	private int IMAGE_WIDTH=0;
	private int IMAGE_HEIGHT=0;
	private static final int INCREMENT=1;
	private int leadImgIdx, rearImgIdx;
	
	/** Amount of X,Y scaling to fit universe on the display window **/
	private static float scaleX = 0;
	private static float scaleY = 0;
	
	private Visualizer () {
		super();
		backImgs = new Image[1];  // played with switching backgrounds
		backImgs[0] = Toolkit.getDefaultToolkit().getImage("images/back0.jpg");
		//backImgs[1] = Toolkit.getDefaultToolkit().getImage("images/back1.jpg");
		//backImgs[2] = Toolkit.getDefaultToolkit().getImage("images/back2.jpg");
		//backImgs[3] = Toolkit.getDefaultToolkit().getImage("images/back3.jpg");
		leadImgIdx=0;
		rearImgIdx=0;
		setPreferredSize(new Dimension(Constants.DISPLAY_WIDTH,Constants.DISPLAY_HEIGHT));
	}
	
	public static float getScaleX() { 
		if (scaleX == 0)
			scaleX = (float)Constants.DISPLAY_WIDTH/Universe.getWidth();
		return scaleX; 
		}
	public static float getScaleY() { 
		if (scaleY == 0)
			scaleY = (float)Constants.DISPLAY_HEIGHT/Universe.getHeight();
		return scaleY; }
	
	private void moveBackground() {
        if (dx1 > dim.width) {
            dx1 = 0 - dim.width;
            dx2 = 0;
            leadImgIdx = (leadImgIdx+1)%1;
        } else {
            dx1 += INCREMENT;
            dx2 += INCREMENT;
        }
        if (dx3 > dim.width) {
            dx3 = 0 - dim.width;
            dx4 = 0;
            rearImgIdx = (rearImgIdx+1)%1;
        } else {
            dx3 += INCREMENT;
            dx4 += INCREMENT;
        }
    }
	
	public void paint(Graphics g) {

		if (u == null)
			return;

		if (IMAGE_WIDTH<=0) {
			IMAGE_WIDTH = backImgs[0].getWidth(this);
			IMAGE_HEIGHT = backImgs[0].getHeight(this);

			dim = getSize();
			//System.out.println("IMAGE_WIDTH="+IMAGE_WIDTH+" IMAGE_HEIGHT="+IMAGE_HEIGHT+" dim.width="+dim.width+" dim.height="+dim.height);
			//srcx2 = IMAGE_WIDTH; ///(IMAGE_HEIGHT/dim.height)*dim.width;

			dx1 = 0;
			dy1 = 0;
			dx2 = dim.width; //IMAGE_WIDTH;
			dy2 = dim.height; // IMAGE_HEIGHT;
			srcx1 = 0;
			srcy1 = 0;
			srcx2 = IMAGE_WIDTH; ///(IMAGE_HEIGHT/dim.height)*dim.width;
			srcy2 = IMAGE_HEIGHT;
			//srcx2 = dim.width;
			//srcy2 = dim.height;
			dx3 = dx2;
			dy3 = 0;
			dx4 = dx3+dim.width;
			dy4 = dim.height;
		}
		
		if (bufferGraphics == null) {
			dim = getSize();
			offscreen = createImage(dim.width, dim.height);
			bufferGraphics = offscreen.getGraphics();
		}
		
		if (Constants.SCROLLING_BACKGROUND) {
			moveBackground();
			bufferGraphics.drawImage(backImgs[leadImgIdx], dx1, dy1, dx2, dy2, srcx1, srcy1, srcx2, srcy2, this);
			bufferGraphics.drawImage(backImgs[rearImgIdx], dx3, dy3, dx4, dy4, srcx1, srcy1, srcx2, srcy2, this);
		}
		else {
			bufferGraphics.setColor(Color.BLACK);
			bufferGraphics.fillRect(0, 0, dim.width, dim.height);
		}
		
		ArrayList<Effect> toBeRemoved = new ArrayList<Effect>();
		
		// Paint the ship exhausts
		u.paintExhausts(bufferGraphics);
			
		/*
		try {
			for (Exhaust exhaust : gd.getExaust()) {
				if (exhaust.getStep() < 1)
					toBeRemoved.add(exhaust);
				else
					exhaust.paint(bufferGraphics);

			}
			for (Object o : toBeRemoved) {
				gd.getExaust().remove(o);
			}
		} catch (ConcurrentModificationException e) {
			// OHHHHHHHHHH NOOOOOOOOOO
		}
		*/

		// try {
		for (FlyingObject flyingObject : u.getFlyingObjects()) {

			if (flyingObject == null) System.out.println("null fo " + flyingObject);
			if (flyingObject.getDrawer() == null) System.out.println("null drawer " + flyingObject);
			flyingObject.getDrawer().paint(bufferGraphics);
			bufferGraphics.setColor(Color.WHITE);
		}
		// }
		// catch (Exception e) {}

		// bufferGraphics.setColor(Color.WHITE);
		bufferGraphics.setColor(Color.BLACK);

		if (Constants.headsUpDisplay) {
			
			// Sometimes throws an exception when running at high frame rate
			for (int i = 0; i < u.getDisplayText().size(); i++) {	
				try {
					drawString(bufferGraphics,u.getDisplayText().get(i),10, 11 * (i + 1) + 5,1);
				}
				catch (java.lang.ArrayIndexOutOfBoundsException e) {}
			}
			
		}

		u.paintExplosions(bufferGraphics);
		
		/*
		toBeRemoved = new ArrayList();
		for (Explosion explosion : gd.getExplosion()) {
			if (explosion.getStep() < 1)
				toBeRemoved.add(explosion);
			else
				explosion.paint(bufferGraphics);

		}
		
		for (Object o : toBeRemoved) {
			gd.getExplosion().remove(o);
		}
		*/

		toBeRemoved = new ArrayList<Effect>();
		for (Warp warp : u.getWarp()) {
			if (warp.getStep() < 1)
				toBeRemoved.add(warp);
			else
				warp.paint(bufferGraphics);

		}
		for (Object o : toBeRemoved) {
			u.getWarp().remove(o);
		}

		toBeRemoved = new ArrayList<Effect>();
		for (Cloak cloak : u.getCloak()) {
			if (cloak.getStep() < 1)
				toBeRemoved.add(cloak);
			else
				cloak.paint(bufferGraphics);

		}
		for (Object o : toBeRemoved) {
			u.getCloak().remove(o);
		}

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

		g.drawImage(offscreen, 0, 0, this);

	}

	public void update(Graphics g) {
		paint(g);
		Toolkit.getDefaultToolkit().sync();
		// getToolkit().sync();
	}

	/**
	 * Update the display based on the current game objects.
	 */
	public void updateDisplay(Universe gd) {
		this.u = gd;
		this.repaint();
	}
	
	public static void drawString(Graphics g,String s,int x, int y,int type ){
		
		for(int i = 0; i < s.length(); i++){
			
			if((int)s.charAt(i) == 32)
				continue;
			
			int xOff = 7 * ((int)s.charAt(i) - 32);
			int yOff = 16 * type;
			
			if((int)s.charAt(i) > 79){
				xOff-= 336;
				yOff+=8;
			}
			
			g.drawImage(
					Constants.font,
					x + (i*7),
					y,
					x + 7+(i*7),
					y + 8,
					0+xOff,
					0+yOff,
					7+xOff,
					8+yOff,
					null);
		}
	}

}

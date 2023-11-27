package zorq.base.effects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import zorq.Constants;
import zorq.Universe;
import zorq.Visualizer;

/**
 * Base effect class. Subclasses include Explosion, Exhaust
 * @author John Hastings
 *
 */
public abstract class Effect {

	protected int step=1;
	protected float x;
	protected float y;

	final int STEPS;
	final int IMAGE_ROWS;
	final int IMAGE_COLS;
	final int IMAGE_HEIGHT;
	final int IMAGE_WIDTH;

	/**
	 * @param steps - Number of frames to display in animation. Usually the same as the number of images in tiled image file.
	 * @param image_rows - Number of rows in tiled image file
	 * @param image_cols - Number of columns in tiled image file
	 * @param image_height - Height of images within tiled image file.
	 * @param image_width - Width of images in tiled image file.
	 */
	protected Effect(int steps, int image_rows, int image_cols, int image_height, int image_width) {
		STEPS = steps;
		IMAGE_ROWS = image_rows;
		IMAGE_COLS = image_cols;
		IMAGE_HEIGHT = image_height;
		IMAGE_WIDTH = image_width;
		step = STEPS;
	}

	/**
	 * 
	 * @return Image to be displayed
	 */
	abstract protected Image getImage();

	public int getStep() { return step; }
	//public void setStep(int s) { this.step = s; }
	public void nextStep () { this.step--; }

	public void paint(Graphics g){
		//int step = this.step;

		g.setColor(Color.WHITE);

		int col = IMAGE_COLS;
		//int rows = IMAGE_ROWS;
		int width = IMAGE_WIDTH;
		int height = IMAGE_HEIGHT;

		int xMod=0;
		int yMod=0;
		for(int j = 0; j < (STEPS-step)-1; j++){
			xMod++;
			if(xMod == col){
				xMod = 0;
				yMod++;
			}
		}

		xMod*=width;
		yMod*=height;

		float scaleX = Visualizer.getScaleX();
		float scaleY = Visualizer.getScaleY();
		int sWidth = (int)(width*scaleX);
		int sHeight = (int)(height*scaleY);
		int sX = (int)(x*scaleX);
		int sY = (int)(y*scaleY);
		g.drawImage(
				this.getImage(),
				sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (0+(int)sY-sHeight/2),
				sWidth+sX-sWidth/2,
				Constants.DISPLAY_HEIGHT - (sHeight+sY-sHeight/2),
				0+xMod,
				height+yMod,
				width+xMod,
				0+yMod,
				null);

		//this.step--;
		/*
		if (Constants.FAST_EXPLOSION) {
			this.step--;
			this.step--;
			this.step--;
		}	
		 */
	}

}

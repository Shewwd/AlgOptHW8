package zorq.base.effects;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.*;

public class Sound {

	    private AudioClip clip;

	    public static Sound soundShipExplode = new Sound("data/sounds/shipExplode.wav");
	    public static Sound soundFireWeapon = new Sound("data/sounds/weaponsFire.wav");
	    
	    public Sound (String fileName) {
	        try { 
	            ClassLoader classloader = this.getClass().getClassLoader();
	            //System.out.println("filename="+fileName);
	            URL url = classloader.getResource(fileName);
	            //System.out.println("url="+url);
	            clip = Applet.newAudioClip(url);
	        } 
	        catch (Exception e) { e.printStackTrace();  }
	    }

	    public void play() { clip.play(); }
}

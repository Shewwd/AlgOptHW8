package zorq.base.effects;

import java.awt.Graphics;
import java.util.*;

/**
 * 
 * Stores a collection of game effects of one type. Methods are synchronized to avoid concurrent modification exceptions
 * from simultaneous access/modification of game objects from the engine and visualizer (painting).
 *
 * @param <T> A game effect. Must be a subclass of Effect.
 * 
 */
public class EffectCollection<T extends Effect> {

	private final transient Collection<T> effects;

	public EffectCollection() {
		effects = 
				new TreeSet<T>(
						new Comparator<T>(){
							public int compare(T e1, T e2) {
								return e1.toString().compareTo(e2.toString());
							}
						});
	}

	/** 
	 * Remove effects which have expired.
	 */
	public synchronized void cleanUp() { 

		// Works for Java 8. But not eclipse 3.8
		effects.removeIf( e -> e.getStep() < 1); 

		// Loop for older java:
		/*
		Iterator<T> itr = effects.iterator();
		while (itr.hasNext()) {
			T e = itr.next(); // must be called before you can call i.remove()
			if (e.getStep() < 1) itr.remove();
		}
		*/
	}

	public synchronized void nextStep() {
		for (Effect e : effects) e.nextStep();
	}
	
	public synchronized void add(T e) { effects.add(e); }

	public synchronized void paint(Graphics g){
		//System.out.println("effects size="+effects.size());
		//cleanUp();  // remove effects which are no longer current
		for (T e : effects)
			e.paint(g);
	}
}

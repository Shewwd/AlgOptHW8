package zorq;

import java.io.FileWriter;
import java.io.IOException;

public final class Logger {
	
	private FileWriter logWriter;
	public String logName;
	public Logger(){
		if(Constants.LOG){
			try {
				logName = "log/" + System.currentTimeMillis() + ".log";
				logWriter = new FileWriter(logName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void writeHeader(){		
		try {
			logWriter.write(
					System.nanoTime() + 
					"|" + "Header" + 
					"|" + "Constants.width" + 
					"|" + "Constants.height" + 
					"|" + "Constants.numberOfAsteroids" + 
					"|" + "Constants.asteroidRadius" + 
					"|" + "Constants.maxShipSpeed" + 
					"|" + "Constants.maxShipAccel" + 
					"|" + "Constants.width" + 
					"|" + "Constants.width" + 
					"|" + "Constants.weapons" +
					"\n");

			logWriter.write(
					System.nanoTime() + 
					"|" + "Header" + 
					"|" + Constants.DISPLAY_WIDTH + 
					"|" + Constants.DISPLAY_HEIGHT + 
					"|" + Constants.numberOfAsteroids + 
					"|" + Constants.asteroidRadius + 
					"|" + Constants.maxShipSpeed + 
					"|" + Constants.maxShipAccel + 
					"|" + Constants.DISPLAY_WIDTH + 
					"|" + Constants.DISPLAY_WIDTH + 
					"|" + Constants.weapons +
					"\n");

			logWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logShipDeath(Ship ship, FlyingObject killer){
		try {
			logWriter.write(
					System.nanoTime() + 					
					"|" + "ShipDeath" + 
					"|" + ship.getShipController().getName() + 
					"|" + (killer instanceof Bullet ? ((Bullet)killer).getCreator() : killer)  + 
					"\n");
			logWriter.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
	public void logShipCreate(Ship ship){
		try {
			logWriter.write(
					System.nanoTime() + 					
					"|" + "ShipCreate" + 
					"|" + ship.getShipController().getName() + 
					"|" + ship  + 
					"\n");
			logWriter.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public void logShipAction(Ship ship, ControllerAction ca, double time){
		try {
			logWriter.write(
					System.nanoTime() + 					
					"|" + "ShipAction" + 
					"|" + ship.getShipController().getName() + 
					"|" + ca  + 
					"|" + time  + 
					"\n");
			logWriter.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			logWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

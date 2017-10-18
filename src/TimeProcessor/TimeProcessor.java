package TimeProcessor;

import Building.Building;
import DataStore.DataStore;

public final class TimeProcessor {

	private static TimeProcessor instance;
	private long duration;
	private long startTime;
	
	private TimeProcessor() {
		this.initialize();
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static TimeProcessor getInstance() {
		if (instance == null) instance = new TimeProcessor();
		return instance;
	}
	
	//Initializes all things necessary for the class
	private void initialize() {
		this.setDuration();
		this.setStartTime();
	}
	
	private void setDuration() {
		this.duration = DataStore.getInstance().getDurationLong();
	}
	
	private void setStartTime() {
		this.startTime = 0;
	}
	
	
	public void begin() {
		long currentTime = startTime;
		long sleepTime = DataStore.getInstance().getSleepTime();
		while (currentTime <= duration) {
			//DoSimWork -> generate ppl and shit
			
			Building.getInstance().update(sleepTime);
			
			// artificial sleep...
			try { Thread.sleep(sleepTime);}
			catch(InterupetedException ex) {}
		}
		
	}
	
}

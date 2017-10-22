package TimeProcessor;

import Building.Building;
import DataStore.DataStore;
import Interfaces.RiderInterface;
import Rider.Rider;
import java.util.concurrent.ThreadLocalRandom;


public final class TimeProcessor {

	private static TimeProcessor instance;
	private long currentTimeMillis;
	private int riderIdIncrementer; //I stashed the rider generator stuff here for now and trashed the facade
	
	private TimeProcessor() {}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static TimeProcessor getInstance() {
		if (instance == null) instance = new TimeProcessor();
		return instance;
	}
	
	//Function called in the main to initiate the simulation
	public void begin() {
		
		//Store the amount of time that the simulation should sleep for between action cycles
		long sleepTime = DataStore.getInstance().getSleepTime();
		
		//Runs the simulation for the designated amount of time
		while (this.currentTimeMillis <= DataStore.getInstance().getDurationMillis()) {
			
			//If needed, create riders, add to their start floor, and make floor request
			riderSimWork();
			
			//Notify building to update elevator activity
			Building.getInstance().update(sleepTime);
		
			//Simulation sleeps for designated amount of time
			try { 
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			
			//Increment the current time by the designated amount
			this.currentTimeMillis += sleepTime;
		}
		
		// Print out final results and exit program
		Building.getInstance().reportData();
		System.exit(0);
	}
	
	//Generates riders, adds them to a floor, and notifies the building of a floor request
	private void riderSimWork() {
		
		if (DataStore.getInstance().getTestNumber() == 1) {
			
			//Generate people with requests according to Test 1 from project description
			if (this.getCurrentTimeMillis() == 0) {
				RiderInterface rider = this.generateRider(1, 10);
				this.addRiderToFloor(rider, rider.getStartFloor());
				System.out.println(this.getTimeString() + "Person " + rider.getId() + " pressed " + rider.getDirection() + " on Floor " + rider.getStartFloor());
				Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection());
			}
			
			
		} else if (DataStore.getInstance().getTestNumber() == 2) {
			//Generate people with requests according to Test 2 from project description
		} else if (DataStore.getInstance().getTestNumber() == 3) {
			//Generate people with requests according to Test 3 from project description
		} else if (DataStore.getInstance().getTestNumber() == 4) {
			//Generate people with requests according to Test 4 from project description
		}
		
		//Generates first rider at start of simulation, and also at a designated recurrence time
//		if (this.currentTimeMillis == 0 || this.currentTimeMillis % DataStore.getInstance().getRiderGenerationTime() == 0) {
//			RiderInterface rider = this.generateRandomRider();
//			this.addRiderToFloor(rider);
//			Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection());
//		}	
	}
	
	//Returns a rider with random start/destination floors
	private RiderInterface generateRandomRider() {
		
		//Get start/destination floors
		int randomStart = ThreadLocalRandom.current().nextInt(1, DataStore.getInstance().getNumFloors() + 1);
		int randomDestination = ThreadLocalRandom.current().nextInt(1, DataStore.getInstance().getNumFloors() + 1);
		
		//Make sure start and destination are not same
		while (randomStart == randomDestination) {
			randomDestination = ThreadLocalRandom.current().nextInt(1, DataStore.getInstance().getNumFloors() + 1);
		} 
		
		//Create and return rider
		RiderInterface newRider = new Rider(generateRiderId(), randomStart, randomDestination);
		return newRider;
	}

	//This isn't being used in riderSimWork but kept it here just in case
	private RiderInterface generateRider(int startFloor, int destinationFloor) {
		
		//Create, print, and return rider
		RiderInterface newRider = new Rider(generateRiderId(), startFloor, destinationFloor);
		System.out.println(this.getTimeString() + "Person " + newRider.getId() + " created on Floor " + newRider.getStartFloor() + ", wants to go " + newRider.getDirection() + " to Floor " + newRider.getDestinationFloor());
		return newRider;
		
	}
	
	//Generates rider IDs using an incrementer starting at 1
	private String generateRiderId() {
		this.riderIdIncrementer++;
		String id = "r" + this.riderIdIncrementer;
		return id;
	}
	
	//Passes the building a rider to add to the rider's current floor
	//Maybe move the rider generation and all rider functions into the building???
	private void addRiderToFloor(RiderInterface rider, int floor) {
		Building.getInstance().addRiderToFloor(rider, floor);
	}
	
	//Returns the current time
	public long getCurrentTimeMillis() {
		return this.currentTimeMillis;
	}
	
	//Prints the formatted current time string
	public String getTimeString() {
		
		//Build string
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.currentTimeMillis % 100_000_000);
		stringBuilder.append(this.currentTimeMillis % 10_000_000);
		stringBuilder.append(":");
		stringBuilder.append(this.currentTimeMillis % 1_000_000);
		stringBuilder.append(this.currentTimeMillis % 100_000);
		stringBuilder.append(":");
		stringBuilder.append(this.currentTimeMillis % 10_000);
		stringBuilder.append(this.currentTimeMillis % 1_000);
		stringBuilder.append("  ");
		
		//Parse to string and return
		String timeString = stringBuilder.toString();
		return timeString;
		
	}
	
}

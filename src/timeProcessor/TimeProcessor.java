package timeProcessor;

import building.Building;
import dataStore.DataStore;
import gui.ElevatorDisplay;
import interfaces.RiderInterface;
import rider.Rider;
import errors.*;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;


public final class TimeProcessor {

	private static TimeProcessor instance;
	private long currentTimeMillis;
	private HashMap<Integer, Long> idleTimes;
	private HashMap<Integer, Long> doorOpenTimes;
	private int riderIdIncrementer; //I stashed the rider generator stuff here for now and trashed the facade
	
	private TimeProcessor() {
		
		//Make functions for these
		idleTimes = new HashMap<Integer, Long>();
		for (int i = 1; i <= DataStore.getInstance().getNumElevators(); i++) {
			idleTimes.put(i, (long) 0);
		}
		doorOpenTimes = new HashMap<Integer, Long>();
		for (int i = 1; i <= DataStore.getInstance().getNumElevators(); i++) {
			doorOpenTimes.put(i, (long) 0);
		}
	}
	
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
				
			try {
				
				//Notify building to update elevator activity
				Building.getInstance().update(sleepTime);
			
				//Simulation sleeps for designated amount of time
				Thread.sleep(sleepTime);
			
			} catch (InvalidArgumentException e1) {
				System.out.println(e1.getMessage());
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
				
			//Increment the current time by the designated amount
			this.currentTimeMillis += sleepTime;
		}
		
		// Print out final results and exit program
		Building.getInstance().reportData();
		ElevatorDisplay.getInstance().shutdown();
		System.exit(0);
	}
	
	//Generates riders, adds them to a floor, and notifies the building of a floor request
	private void riderSimWork() {
		
		try {
		
			if (DataStore.getInstance().getTestNumber() == 1) {
				
				//Generate people with requests according to Test 1 from project description
				if (this.getCurrentTimeMillis() == 0) {
					RiderInterface rider = this.generateRider(1, 10);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
				}
				
				
			} else if (DataStore.getInstance().getTestNumber() == 2) {
				
				//Generate people with requests according to Test 2 from project description
				if (this.getCurrentTimeMillis() == 0) {
					RiderInterface rider = this.generateRider(1, 20);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
				}
				
				if (this.getCurrentTimeMillis() == 12000) {
					RiderInterface rider = this.generateRider(15, 19);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
				}
				
				
			} else if (DataStore.getInstance().getTestNumber() == 3) {
				
				//Generate people with requests according to Test 3 from project description
				if (this.getCurrentTimeMillis() == 0) {
					RiderInterface rider = this.generateRider(1, 20);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
				}
				
				if (this.getCurrentTimeMillis() == 3000) {
					RiderInterface rider = this.generateRider(1, 10);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 2);
				}
				
			} else if (DataStore.getInstance().getTestNumber() == 4) {
				
				//Generate people with requests according to Test 4 from project description
				if (this.getCurrentTimeMillis() == 0) {
					RiderInterface rider = this.generateRider(1, 20);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
				}
				
				if (this.getCurrentTimeMillis() == 1000) {
					RiderInterface rider = this.generateRider(1, 20);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 2);
				}
				
				if (this.getCurrentTimeMillis() == 2000) {
					RiderInterface rider = this.generateRider(1, 20);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 3);
				}
				
				if (this.getCurrentTimeMillis() == 3000) {
					RiderInterface rider = this.generateRider(1, 20);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 4);
				}
				
				if (this.getCurrentTimeMillis() == 6000) {
					RiderInterface rider = this.generateRider(1, 10);
					this.addRiderToFloor(rider, rider.getStartFloor());
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
				}
				
			}
			
			//Generates first rider at start of simulation, and also at a designated recurrence time
	//		if (this.currentTimeMillis == 0 || this.currentTimeMillis % DataStore.getInstance().getRiderGenerationTime() == 0) {
	//			RiderInterface rider = this.generateRandomRider();
	//			this.addRiderToFloor(rider);
	//			Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection());
	//		}
		} catch (InvalidArgumentException e) {
			System.out.println(e.getMessage());
		}
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
		String id = "P" + this.riderIdIncrementer;
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
	
	//Idle time managing functions
	public long getIdleTime(int elevatorNumber) {
		//TODO error handling
		return this.idleTimes.get(elevatorNumber);
	}
	
	public void updateIdleTime(int elevatorNumber) {
		//TODO error handling
		this.idleTimes.put(elevatorNumber, this.idleTimes.get(elevatorNumber) + DataStore.getInstance().getSleepTime());
	}
	
	public void resetIdleTime(int elevatorNumber) {
		//TODO error handling
		this.idleTimes.put(elevatorNumber, (long) 0);
	}
	
	//Door open time managing functions
	public long getDoorOpenTime(int elevatorNumber) {
		//TODO error handling
		return this.doorOpenTimes.get(elevatorNumber);
	}
	
	public void updateDoorOpenTime(int elevatorNumber) {
		//TODO error handling
		this.doorOpenTimes.put(elevatorNumber, this.doorOpenTimes.get(elevatorNumber) + DataStore.getInstance().getSleepTime());
	}
	
	public void resetDoorOpenTime(int elevatorNumber) {
		//TODO error handling
		this.doorOpenTimes.put(elevatorNumber, (long) 0);
	}
	
	//Prints the formatted current time string
	public String getTimeString() {
		
		long seconds = this.currentTimeMillis / 1000;
		long hours = seconds / 3600;
		seconds -= hours * 3600;
		long minutes = seconds / 60;
		seconds -= minutes * 60;
		
		return String.format("%d:%02d:%02d  ", hours, minutes, seconds);
		
	}
	
}

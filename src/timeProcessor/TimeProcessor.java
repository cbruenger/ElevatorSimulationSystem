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
	private int numFloors; //Might not need this
	
	private TimeProcessor() {
		
		try {
			
			this.createIdleTimesHashMap();
			this.createDoorOpenTimesHashMap();
			this.setNumFloors();
			
		} catch (AlreadyExistsException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (BadInputDataException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void createIdleTimesHashMap() throws AlreadyExistsException, BadInputDataException {
		
		if (this.idleTimes != null) {
			throw new AlreadyExistsException("The idleTimes HashMap has already been created in TimeProcessor\n");
		}
		try {
			int numElevators = this.getNumElevators();
			this.idleTimes = new HashMap<Integer, Long>();
			for (int i = 1; i <= numElevators; i++) {
				idleTimes.put(i, (long) 0);
			}
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for numElevators value\n"); 
	    }
	}
	
	private void createDoorOpenTimesHashMap() throws AlreadyExistsException, BadInputDataException {
		
		if (this.doorOpenTimes != null) {
			throw new AlreadyExistsException("The doorOpenTimes HashMap has already been created in TimeProcessor\n");
		}
		try {
			int numElevators = this.getNumElevators();
			this.doorOpenTimes = new HashMap<Integer, Long>();
			for (int i = 1; i <= numElevators; i++) {
				doorOpenTimes.put(i, (long) 0);
			}
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for numElevators value\n"); 
	    }
		
	}
	
	private int getNumElevators() throws BadInputDataException {
		
		try { 
			int numElevators = Integer.parseInt(DataStore.getInstance().getNumElevators()); 
			if (numElevators > 0)
				return numElevators;
			else
				throw new BadInputDataException("TimeProcessor received a value less than 1 for numElevators from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for numElevators value\n"); 
	    }
	}
	
	private void setNumFloors() throws BadInputDataException {
		
		try { 
			int numFloorsTemp = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (numFloorsTemp > 1)
				this.numFloors = numFloorsTemp;
			else
				throw new BadInputDataException("TimeProcessor received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numFloors value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for numFloors value\n"); 
	    }
		
	}
	
	private long getSleepTime() throws BadInputDataException {
		
		try {
			long sleepTime = Integer.parseInt(DataStore.getInstance().getSleepTime()) * 1000;
			if (sleepTime >= 0)
				return sleepTime;
			else
				throw new BadInputDataException("TimeProcessor received a negative value for sleepTime from DataStore\n");
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's sleepTime value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for sleepTime value\n"); 
	    }
	}
	
	private long getDurationMillis() throws BadInputDataException {
		
		try {
			long durationMillis = Integer.parseInt(DataStore.getInstance().getDuration()) * 1000;
			if (durationMillis > 0)
				return durationMillis;
			else
				throw new BadInputDataException("TimeProcessor received a value less than or equal to 0 for duration from DataStore\n");
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's duration value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for duration value\n"); 
	    }
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static TimeProcessor getInstance() {
		if (instance == null) instance = new TimeProcessor();
		return instance;
	}
	
	//Function called in the main to initiate the simulation
	public void begin() {
		
		try {
			//Store the amount of time that the simulation should sleep for between action cycles
			long sleepTime = this.getSleepTime();
			
			//Runs the simulation for the designated amount of time
			while (this.currentTimeMillis <= this.getDurationMillis()) {
						
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
			
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Generates riders, adds them to a floor, and notifies the building of a floor request
	private void riderSimWork() {
		
		try {
		
			//if (DataStore.getInstance().getTestNumber() == 1) {
				
				//Generate people with requests according to Test 1 from project description
				if (this.getCurrentTimeMillis() == 0) {
					RiderInterface rider = this.generateRandomRider();
					this.addRiderToFloor(rider, rider.getStartFloor());
					//replace this line with controller call taking startFloor/direction
					//Controller will be the one to call this next line. Rename it to 
					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
				}
				
				
//			} else if (DataStore.getInstance().getTestNumber() == 2) {
//				
//				//Generate people with requests according to Test 2 from project description
//				if (this.getCurrentTimeMillis() == 0) {
//					RiderInterface rider = this.generateRider(1, 20);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
//				}
//				
//				if (this.getCurrentTimeMillis() == 12000) {
//					RiderInterface rider = this.generateRider(15, 19);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
//				}
//				
//				
//			} else if (DataStore.getInstance().getTestNumber() == 3) {
//				
//				//Generate people with requests according to Test 3 from project description
//				if (this.getCurrentTimeMillis() == 0) {
//					RiderInterface rider = this.generateRider(1, 20);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
//				}
//				
//				if (this.getCurrentTimeMillis() == 3000) {
//					RiderInterface rider = this.generateRider(1, 10);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 2);
//				}
//				
//			} else if (DataStore.getInstance().getTestNumber() == 4) {
//				
//				//Generate people with requests according to Test 4 from project description
//				if (this.getCurrentTimeMillis() == 0) {
//					RiderInterface rider = this.generateRider(1, 20);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
//				}
//				
//				if (this.getCurrentTimeMillis() == 1000) {
//					RiderInterface rider = this.generateRider(1, 20);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 2);
//				}
//				
//				if (this.getCurrentTimeMillis() == 2000) {
//					RiderInterface rider = this.generateRider(1, 20);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 3);
//				}
//				
//				if (this.getCurrentTimeMillis() == 3000) {
//					RiderInterface rider = this.generateRider(1, 20);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 4);
//				}
//				
//				if (this.getCurrentTimeMillis() == 6000) {
//					RiderInterface rider = this.generateRider(1, 10);
//					this.addRiderToFloor(rider, rider.getStartFloor());
//					Building.getInstance().elevatorRequested(rider.getStartFloor(), rider.getDirection(), 1);
//				}
//				
//			}
			
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
		int randomStart = ThreadLocalRandom.current().nextInt(1, this.numFloors + 1);
		int randomDestination = ThreadLocalRandom.current().nextInt(1, this.numFloors + 1);
		
		//Make sure start and destination are not same
		while (randomStart == randomDestination) {
			randomDestination = ThreadLocalRandom.current().nextInt(1, this.numFloors + 1);
		} 
		
		//Create, print and return rider
		RiderInterface newRider = new Rider(generateRiderId(), randomStart, randomDestination);
		System.out.println(this.getTimeString() + "Person " + newRider.getId() + " created on Floor " + newRider.getStartFloor() + ", wants to go " + newRider.getDirection() + " to Floor " + newRider.getDestinationFloor());
		return newRider;
		
	}

	//This isn't being used in riderSimWork but kept it here just in case
//	private RiderInterface generateRider(int startFloor, int destinationFloor) {
//		
//		//Create, print, and return rider
//		RiderInterface newRider = new Rider(generateRiderId(), startFloor, destinationFloor);
//		System.out.println(this.getTimeString() + "Person " + newRider.getId() + " created on Floor " + newRider.getStartFloor() + ", wants to go " + newRider.getDirection() + " to Floor " + newRider.getDestinationFloor());
//		return newRider;
//		
//	}
	
	//Generates rider IDs using an incrementer starting at 1
	private String generateRiderId() {
		this.riderIdIncrementer++;
		String id = "P" + this.riderIdIncrementer;
		return id;
	}
	
	//Passes the building a rider to add to the rider's current floor
	//Maybe move the rider generation and all rider functions into the building???
	private void addRiderToFloor(RiderInterface rider, int floor) {
		try {
			Building.getInstance().addRiderToFloor(rider, floor);
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Returns the current time
	public long getCurrentTimeMillis() {
		return this.currentTimeMillis;
	}
	
	//Idle time managing functions
	public long getIdleTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.idleTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's idleTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for get\n");
		}
		return this.idleTimes.get(elevatorNumber);
	}
	
	public void updateIdleTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.idleTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's idleTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for update\n");
		}
		try {
			long sleepTime = this.getSleepTime();
			this.idleTimes.put(elevatorNumber, this.idleTimes.get(elevatorNumber) + sleepTime);
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void resetIdleTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.idleTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's idleTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for reset\n");
		}
		this.idleTimes.put(elevatorNumber, (long) 0);
	}
	
	//Door open time managing functions
	public long getDoorOpenTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.doorOpenTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's doorOpenTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for get\n");
		}
		return this.doorOpenTimes.get(elevatorNumber);
	}
	
	public void updateDoorOpenTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.doorOpenTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's doorOpenTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for update\n");
		}
		try {
			long sleepTime = this.getSleepTime();
			this.doorOpenTimes.put(elevatorNumber, this.doorOpenTimes.get(elevatorNumber) + sleepTime);
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void resetDoorOpenTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.doorOpenTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's doorOpenTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for reset\n");
		}
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
package timeProcessor;

import building.Building;
import dataStore.DataStore;
//import controller.ElevatorController;
import gui.ElevatorDisplay;
import interfaces.RiderInterface;
import rider.Rider;
import errors.*;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/* The TimeProcessor class is called in the main and handles the
 * statistical creation of people, the time management of all
 * elevators actions, and the simulation of the entire program
 */
public class TimeProcessor {

	//Class variables and data structures
	private static TimeProcessor instance;
	private long currentTimeMillis;
	private HashMap<Integer, Long> idleTimes;
	private HashMap<Integer, Long> doorOpenTimes;
	private HashMap<Integer, Long> floorMovementTimes;
	private int personIdIncrementer;
	
	/*////////////////////////////////////////////////////
	 * 													*
	 * 		Constructor and Singleton Instance Getter	*
	 * 													*
	 *////////////////////////////////////////////////////
	
	//Constructor, initializes necessary components
	private TimeProcessor() {
		try {
			this.createIdleTimesHashMap();
			this.createDoorOpenTimesHashMap();
			this.createFloorMovementTimesHashMap();
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
	
	//Returns the instance of this class, initializes if 1st time called
	public static TimeProcessor getInstance() {
		if (instance == null) instance = new TimeProcessor();
		return instance;
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Methods called by the Constructor		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Creates the HashMap used for managing the times elevators remain IDLE before returning to floor 1
	private void createIdleTimesHashMap() throws AlreadyExistsException, BadInputDataException {
		if (this.idleTimes != null)
			throw new AlreadyExistsException("The idleTimes HashMap has already been created in TimeProcessor\n");
		try {
			int numElevators = this.getNumElevators();
			this.idleTimes = new HashMap<Integer, Long>();
			for (int i = 1; i <= numElevators; i++)
				idleTimes.put(i, (long) 0);
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for numElevators value\n"); 
	    }
	}
	
	//Creates the HashMap used for managing the times elevators' doors are open
	private void createDoorOpenTimesHashMap() throws AlreadyExistsException, BadInputDataException {
		if (this.doorOpenTimes != null)
			throw new AlreadyExistsException("The doorOpenTimes HashMap has already been created in TimeProcessor\n");
		try {
			int numElevators = this.getNumElevators();
			this.doorOpenTimes = new HashMap<Integer, Long>();
			for (int i = 1; i <= numElevators; i++)
				doorOpenTimes.put(i, (long) 0);
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for numElevators value\n"); 
	    }
	}
	
	//Creates the HashMap used for managing the times elevators move from one floor to another
	private void createFloorMovementTimesHashMap() throws AlreadyExistsException, BadInputDataException {
		if (this.floorMovementTimes != null)
			throw new AlreadyExistsException("The floorMovementTimes HashMap has already been created in TimeProcessor\n");
		try {
			int numElevators = this.getNumElevators();
			this.floorMovementTimes = new HashMap<Integer, Long>();
			for (int i = 1; i <= numElevators; i++)
				floorMovementTimes.put(i, (long) 0);
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for numElevators value\n"); 
	    }
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		Simulation Running Method		*
	 * 										*
	 *////////////////////////////////////////
	
	//Function called in the main to initiate the simulation and report data
	public void runSimulation() {
		
		try {
			//Store the amount of time that the simulation should sleep for between action cycles
			long sleepTime = this.getSleepTime();
			
			//Initial print statement
			System.out.println(this.getTimeString() + "PROGRAM STARTING, AWAITING RIDER GENERATION");
			
			//Runs the simulation for the designated amount of time
			while (this.currentTimeMillis <= this.getDurationMillis()) {
						
				//If needed, create people and pass them to the building
				peopleSimWork();
					
				try {
					
					//Notify building to update elevator activity
					Building.getInstance().update();
				
					//Simulation sleeps for designated amount of time
					Thread.sleep(sleepTime);
				
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					System.exit(-1);
				}
					
				//Increment the current time by the designated sleep time
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
	
	/*////////////////////////////////////////
	 * 										*
	 * 		People Handling Methods  		*
	 * 										*
	 *////////////////////////////////////////
	
	//Generates people according to statistical chance, adds them to a floor
	private void peopleSimWork() {
		try {
			//Variables required for statistical person creation
			int peoplePerMinute = this.getPeoplePerMinute();
			int personGenerationChancePerCycle = (int) Math.ceil(((float)peoplePerMinute/60) * 100);
			int randomInt = (int) Math.ceil(Math.random() * 100);
			
			//Creates person and passes to building
			if (randomInt <= personGenerationChancePerCycle) {
				RiderInterface rider = this.generateRandomPerson();
				this.passPersonToBuilding(rider, rider.getStartFloor());
			}
		} catch (BadInputDataException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Returns a person with random start/destination floors
	private RiderInterface generateRandomPerson() throws BadInputDataException {
		try {
			//Variables required for creating a person
			int numFloors = this.getNumFloors();
			int randomStart = ThreadLocalRandom.current().nextInt(1, numFloors + 1);
			int randomDestination = ThreadLocalRandom.current().nextInt(1, numFloors + 1);
			
			//Make sure start and destination are not same
			while (randomStart == randomDestination) {
				randomDestination = ThreadLocalRandom.current().nextInt(1, numFloors + 1);
			} 
			
			//Create return person
			RiderInterface person = new Rider(generatePersonId(), randomStart, randomDestination);
			return person;
			
		} catch (BadInputDataException e) {
			throw e;
		}
	}
	
	//Generates people IDs using an incrementer starting at 1
	private String generatePersonId() {
		this.personIdIncrementer++;
		String id = "P" + this.personIdIncrementer;
		return id;
	}
	
	//Passes the building a person to put on the person's start floor
	private void passPersonToBuilding(RiderInterface person, int floor) {
		try {
			Building.getInstance().addPersonToFloor(person, floor);
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
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Elevator Time Management Methods 		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Returns the amount of time an elevator has been IDLE
	public long getIdleTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.idleTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's idleTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for get\n");
		}
		return this.idleTimes.get(elevatorNumber);
	}
	
	//Updates the amount of time an elevator has been IDLE by the designated sleep time
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
	
	//Resets the amount of time an elevator has been IDLE to 0
	public void resetIdleTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.idleTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's idleTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for reset\n");
		}
		this.idleTimes.put(elevatorNumber, (long) 0);
	}
	
	//Returns the amount of time an elevator's doors have been open
	public long getDoorOpenTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.doorOpenTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's doorOpenTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for get\n");
		}
		return this.doorOpenTimes.get(elevatorNumber);
	}
	
	//Updates the amount of time an elevator's doors have been open by the designated sleep time
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
	
	//Resets the amount of time an elevator's doors have been open to 0
	public void resetDoorOpenTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.doorOpenTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's doorOpenTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for reset\n");
		}
		this.doorOpenTimes.put(elevatorNumber, (long) 0);
	}
	
	//Returns the amount of time an elevator has been moving from one floor to another
	public long getFloorMovementTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.floorMovementTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's floorMovementTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for get\n");
		}
		return this.floorMovementTimes.get(elevatorNumber);
	}
	
	//Updates the amount of time an elevator has been moving from one floor to another by the designated sleep time
	public void updateFloorMovementTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.floorMovementTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's floorMovementTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for update\n");
		}
		try {
			long sleepTime = this.getSleepTime();
			this.floorMovementTimes.put(elevatorNumber, this.floorMovementTimes.get(elevatorNumber) + sleepTime);
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Resets the amount of time an elevator has been moving from one floor to another to 0
	public void resetFloorMovementTime(int elevatorNumber) throws InvalidArgumentException {
		if (!this.floorMovementTimes.containsKey(elevatorNumber)) {
			throw new InvalidArgumentException("TimeProcessor's floorMovementTimes HashMap doesn't contain elevatorNumber " + elevatorNumber + " for reset\n");
		}
		this.floorMovementTimes.put(elevatorNumber, (long) 0);
	}
		
	/*////////////////////////////////////////
	 * 										*
	 * 		Time Stamp Printing Method		*
	 * 										*
	 *////////////////////////////////////////
	
	//Called by various classes for narration of events
	public String getTimeString() {
		long seconds = this.currentTimeMillis / 1000;
		long hours = seconds / 3600;
		seconds -= hours * 3600;
		long minutes = seconds / 60;
		seconds -= minutes * 60;
		return String.format("%d:%02d:%02d  ", hours, minutes, seconds);
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		DataStore Retrieval Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	//Accesses DataStore and parses the number of elevators to an int, checks validity and returns it
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
	
	//Accesses DataStore and parses the number of floors to an int, checks validity and returns it
	private int getNumFloors() throws BadInputDataException {
		try { 
			int numFloors = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (numFloors > 1)
				return numFloors;
			else
				throw new BadInputDataException("TimeProcessor received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's numFloors value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("FloorImpl received null from DataStore for numFloors value\n"); 
	    }
	}
	
	//Accesses DataStore and parses the number of people to generate per minute to an int, checks validity and returns it
	private int getPeoplePerMinute() throws BadInputDataException {
		try {
			int ridersPerMinute = Integer.parseInt(DataStore.getInstance().getPeoplePerMinute());
			if (ridersPerMinute > 0)
				return ridersPerMinute;
			else
				throw new BadInputDataException("TimeProcessor cannot accept number less than 1 for getPeoplePerMinute from DataStore\n");
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("TimeProcessor could not parse DataStore's getPeoplePerMinute value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("TimeProcessor received null from DataStore for getPeoplePerMinute value\n"); 
	    }
	}
	
	//Accesses DataStore and parses the sleep time to a long, checks validity and returns it
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
	
	//Accesses DataStore and parses the duration to a long, checks validity and returns it
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
}

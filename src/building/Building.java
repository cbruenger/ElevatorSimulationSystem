package building;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import errors.*;
import dataStore.DataStore;
import elevator.Elevator;
import elevator.ElevatorDTO;
import enumerators.MyDirection;
import static enumerators.MyDirection.*;
import floor.Floor;
import gui.ElevatorDisplay;
import interfaces.ElevatorInterface;
import interfaces.FloorInterface;
import interfaces.RiderInterface;


public class Building {
	
	//Class variables and data structures
	private static Building building;
	private int numFloors;
	private int numElevators;
	private HashMap<Integer, FloorInterface> floors;
	private HashMap<Integer,ElevatorInterface> elevators;
	private ArrayList<RiderInterface> decommissionedPeople;
	
	/*////////////////////////////////////////////////////
	 * 													*
	 * 		Constructor and Singleton Instance Getter	*
	 * 													*
	 *////////////////////////////////////////////////////
	
	//Constructor, initializes necessary components
	private Building() {
		try {
			this.setNumFloors();
			this.setNumElevators();
			this.createFloors();
			this.createElevators();
			this.initializeDecommissionedPeopleArrayList();
			this.initializeGui();
		} catch (BadInputDataException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Returns the instance of this class, initializes if 1st time called
	public static Building getInstance() {
		if (building == null) building = new Building();
		return building;
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Methods called by the Constructor		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Accesses DataStore and parses the number of floors to an int, checks validity and assigns class variable
	private void setNumFloors() throws BadInputDataException {
		try { 
			int temp = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (temp > 1)
				this.numFloors = temp;
			else
				throw new BadInputDataException("Building cannot accept a value less than 2 for numFloors\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("Building could not parse DataStore's numFloors value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("Building received null from DataStore for numFloors value\n"); 
	    }
	}
	
	//Accesses DataStore and parses the number of elevators to an int, checks validity and assigns class variable
	private void setNumElevators() throws BadInputDataException {
		try { 
			int temp = Integer.parseInt(DataStore.getInstance().getNumElevators());
			if (temp > 0)
				this.numElevators = temp;
			else
				throw new BadInputDataException("Building cannot accept a value less than 1 for numElevators\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("Building could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("Building received null from DataStore for numElevators value\n"); 
	    }
	}
	
	//Creates the HashMap to contain elevators, creates elevators and adds them to the HashMap 
	private void createElevators() throws AlreadyExistsException {
		if (this.elevators != null)
			throw new AlreadyExistsException("The elevators HashMap has already been created in building\n");		
		this.elevators = new HashMap<Integer,ElevatorInterface>();
		for (int i = 1; i <= this.numElevators; i++)
			this.elevators.put(i, new Elevator(i));
	}
	
	//Creates the HashMap to contain floors, creates floors and adds them to the HashMap 
	private void createFloors() throws AlreadyExistsException {
		if (this.floors != null)
			throw new AlreadyExistsException("The floors HashMap has already been created in building\n");
		this.floors = new HashMap<Integer,FloorInterface>();
		for (int i = 1; i <= this.numFloors; i++)
			this.floors.put(i, new Floor(i));
		}
	
	//Creates the ArrayList which will contain people exiting elevators
	private void initializeDecommissionedPeopleArrayList() throws AlreadyExistsException {
		if (this.decommissionedPeople != null)
			throw new AlreadyExistsException("The decommissionedPeople ArrayList has already been created in building\n");
		this.decommissionedPeople = new ArrayList<RiderInterface>();
	}
	
	//Initializes GUI with the designated number of floors and elevators, puts all elevators IDLE on floor 1
	private void initializeGui() {
		ElevatorDisplay.getInstance().initialize(this.numFloors);
		for (int i = 1; i <= this.numElevators; i++) {
			ElevatorDisplay.getInstance().addElevator(i, 1);
			ElevatorDisplay.getInstance().setIdle(i);
		}
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		Elevator Managing Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	//Notifies floors to update their activity, called once per wake cycle
	public void update() { 
		for (int elevatorNumber : this.elevators.keySet()) {
			this.elevators.get(elevatorNumber).update();
		}
	}
	
	//Called by the elevator controller to assign a pickup for a given direction on a given floor to a given elevator
	public void assignElevatorForPickup(int floor, MyDirection direction, int elevatorNumber) throws InvalidArgumentException {
		if (floor < 1 || floor > this.numFloors)
			throw new InvalidArgumentException("Building's elevatorRequested method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
		if (direction == null || direction == IDLE)
			throw new InvalidArgumentException("Building's elevatorRequested method cannot accept null or IDLE for direction parameter\n");
		if (elevatorNumber < 1 || elevatorNumber > this.numElevators)
			throw new InvalidArgumentException("Building's elevatorRequested method cannot accept elevatorNumber less than 1 or greater than " + DataStore.getInstance().getNumElevators() + "\n");
		this.elevators.get(elevatorNumber).addPickupRequest(direction, floor);
	}
	
	//Called by elevator controller, forwards a Data Transfer Object from a given elevator to the controller
	public ElevatorDTO getElevatorDTO(int elevatorNumber) throws InvalidArgumentException {
		if (elevatorNumber < 1 || elevatorNumber > this.numElevators)
			throw new InvalidArgumentException("Building's getElevatorDTO method cannot accept number less than 1 or greater than " + this.numElevators + " for elevatorNumber arg\n");
		try {
			ElevatorDTO elevatorDTO = this.elevators.get(elevatorNumber).getDTO();
			if (elevatorDTO == null)
				throw new UnexpectedNullException("Building's getElevatorDTO method received null DTO for elevator " + elevatorNumber + "\n");
			else
				return elevatorDTO;
		} catch (UnexpectedNullException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	/*////////////////////////////////////
	 * 									*
	 * 		Floor Managing Methods		*
	 * 									*
	 *////////////////////////////////////
	
	//Called by the TimeProcessor, puts a given person onto their given start floor
	public void addPersonToFloor(RiderInterface person, int startFloor) throws InvalidArgumentException, AlreadyExistsException {
		if (startFloor < 1 || startFloor > this.numFloors)
			throw new InvalidArgumentException("Building's addPersonToFloor method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
		if (person == null) {
			throw new InvalidArgumentException("Building's addPersonToFloor method cannot accept null for person arg\n");
		}
		FloorInterface floor = floors.get(startFloor);
		floor.addWaitingPerson(person);	
	}
	
	/* Called by elevators, forwards an ArrayList from a given floor to the calling elevator. The ArrayList
	 * contains a given amount of people (availableCapacity) who are going in a given direction */
	public ArrayList<RiderInterface> getWaitersFromFloor(int floor, MyDirection direction, int availableCapacity) throws InvalidArgumentException, BadInputDataException, UnexpectedNullException {
		try {
			//Retrieve the total capacity of an elevator from the DataStore and parse to an int
			int totalCapacity = Integer.parseInt(DataStore.getInstance().getElevatorCapacity());
			
			//Validate args 
			if (floor < 1 || floor > this.numFloors)
				throw new InvalidArgumentException("Building's getWaitersFromFloor method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
			if (direction == null)
				throw new InvalidArgumentException("Building's getWaitersFromFloor method cannot accept null value for direction\n");
			if (availableCapacity < 0 || availableCapacity > totalCapacity) {
				throw new InvalidArgumentException("Building's getWaitersFromFloor method cannot accept number less than 0 or greater than " + totalCapacity + " for availableCapacity\n");
			}
			
			//Retrieve people from floor, validate and return
			ArrayList<RiderInterface> peopleToTransfer = this.floors.get(floor).getWaitersByDirection(direction, availableCapacity);
			if (peopleToTransfer == null)
				throw new UnexpectedNullException("Building's getWaitersFromFloor method received null value when retreiving riders from floor\n");
			return peopleToTransfer;
			
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("Building's getWaitersFromFloor method could not parse DataStore's elevatorCapacity value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("Building's getWaitersFromFloor method received null from DataStore for elevatorCapacity value\n"); 
	    }
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Exited People Decommissioning Method		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Called by elevators, a method for receiving an ArrayList of people exiting, adds all people to the decommissionedPeople ArrayList
	public void decommissionPeople(ArrayList<RiderInterface> people) throws InvalidArgumentException, AlreadyExistsException {
		
		//Validate people arg
		if (people == null)
			throw new InvalidArgumentException("Building's decommissionPeople method cannot accept null for people arg\n");
		for (RiderInterface person : people) {
			if (this.decommissionedPeople.contains(person))
				throw new AlreadyExistsException("Person already exists in building's decommissionedPeople ArrayList\n");
		}
		
		//Add people decommissionedRiders ArrayList
		for (RiderInterface rider: people) {
			this.decommissionedPeople.add(rider);
			this.floors.get(rider.getDestinationFloor()).addPersonToDecommissionedList(rider.getId());
		}
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Simulation Data Reporting Methods		*
	 * 												*
	 *////////////////////////////////////////////////
	
	/* Calls a HashSet building method containing all decommissioned people's ride times,
	 * and then calls methods to print the various calculated data */
	public void reportData() {
		try {
			HashMap<String, ArrayList<Long>> rideTimes = this.getRideTimesHashSet();
			this.waitTimes();
			this.averageRideTimes(rideTimes);
			this.maxRideTimes(rideTimes);
			this.minRideTimes(rideTimes);
			this.peopleTimes();
		} catch (UnexpectedNullException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (InvalidArgumentException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Builds and returns a HashMap containing all decommissioned people's ride times
	private HashMap<String, ArrayList<Long>> getRideTimesHashSet() throws UnexpectedNullException {
		if (this.decommissionedPeople == null)
			throw new UnexpectedNullException("Building's decommissionedRiders ArrayList is null when reporting data\n");
		HashMap<String, ArrayList<Long>> rideTimes = new HashMap<String, ArrayList<Long>>();
		for (RiderInterface person : this.decommissionedPeople) {
			if (!rideTimes.containsKey(person.getStartFloor()+"-"+person.getDestinationFloor())) {
				ArrayList<Long> rideTimesList = new ArrayList<Long>();
				rideTimesList.add(person.getRideTime());
				rideTimes.put(person.getStartFloor()+"-"+person.getDestinationFloor(), rideTimesList);
			} else {
				rideTimes.get(person.getStartFloor()+"-"+person.getDestinationFloor()).add(person.getRideTime());
			}
		}
		return rideTimes;
	}

	//Prints the average/min/max wait times of decommissioned people by floor
	private void waitTimes() throws UnexpectedNullException {
		if (this.decommissionedPeople == null)
			throw new UnexpectedNullException("Building's decommissionedRiders ArrayList is null when reporting data\n");
		HashMap<Integer, ArrayList<Long>> floorWaitTimes = new HashMap<Integer, ArrayList<Long>>();
		for (RiderInterface person : this.decommissionedPeople) {
			if (!floorWaitTimes.containsKey(person.getStartFloor())) {
				ArrayList<Long> waitTimesList = new ArrayList<Long>();
				waitTimesList.add(person.getWaitTime());
				floorWaitTimes.put(person.getStartFloor(), waitTimesList);
			} else {
				floorWaitTimes.get(person.getStartFloor()).add(person.getWaitTime());
			}
		}
		System.out.println("");
		System.out.println("************ Wait Times By Floor ************");
		System.out.printf("%-20s %-20s %-20s %-20s\n", "Start Floor","Average Wait Time","Min Wait Time","Max Wait Time");
		for (int i = 1; i <= this.numFloors; i++) {
			if (!floorWaitTimes.containsKey(i)) {
				System.out.printf("%-20s %-20s %-20s %-20s\n", "Floor "+i, "NA", "NA", "NA");
			} else {
				long max = Long.MIN_VALUE;
				long min = Long.MAX_VALUE;
				long sum = 0;
				for (long j : floorWaitTimes.get(i)) {
					if (j > max) max = j;
					if (j < min) min = j;
					sum += j;
				}
				long maxWaitTime = max/1000;
				long minWaitTime = min/1000;
				long averageWaitTime = (sum / floorWaitTimes.get(i).size())/1000;
				System.out.printf("%-20s %-20s %-20s %-20s\n", "Floor "+i, averageWaitTime+" seconds", minWaitTime+" seconds", maxWaitTime+" seconds");
			}
		}	
	}
	
	//Prints the average ride times by floor
	private void averageRideTimes(HashMap<String, ArrayList<Long>> rideTimes) throws InvalidArgumentException {
		if (rideTimes == null)
			throw new InvalidArgumentException("Building's averageRideTimes method cannot accept null rideTimes arg\n");
		System.out.println("");
		System.out.println("************ Average Ride Times By Floor ************");
		System.out.printf("%-5s", "Floor");
		for (int floor=1; floor<=this.numFloors; floor++) {
			System.out.printf("%-5s", floor);
		}
		System.out.printf("\n");
		
		for (int i = 1; i <= this.numFloors; i++) {
			System.out.printf("%-5s", i);
			for (int j = 1; j <= this.numFloors; j++) {
				if (i == j) System.out.printf("%-5s", "X");
				else {
					if (!rideTimes.containsKey(i+"-"+j)) System.out.printf("%-5s", "0");
					else {
						long sum = 0;
						for (long k : rideTimes.get(i+"-"+j)) {
							sum += k;
						}
						long averageRideTime = (sum / rideTimes.get(i+"-"+j).size())/1000;
						System.out.printf("%-5d", averageRideTime);
					}
				}
			}
		System.out.printf("\n");
		}
	}
	
	//Prints the maximum ride times by floor
	private void maxRideTimes(HashMap<String, ArrayList<Long>> rideTimes) throws InvalidArgumentException {
		if (rideTimes == null)
			throw new InvalidArgumentException("Building's maxRideTimes method cannot accept null rideTimes arg\n");
		//Printing Min ride times
		System.out.println("");
		System.out.println("************ Maximum Ride Times By Floor ************");
		System.out.printf("%-5s", "Floor");
		for (int floor=1; floor<=this.numFloors; floor++) {
			System.out.printf("%-5s", floor);
		}
		System.out.printf("\n");
		
		for (int i = 1; i <= this.numFloors; i++) {
			System.out.printf("%-5s", i);
			for (int j = 1; j <= this.numFloors; j++) {
				if (i == j) System.out.printf("%-5s", "X");
				else {
					if (!rideTimes.containsKey(i+"-"+j)) System.out.printf("%-5s", "0");
					else {
						long max = Long.MIN_VALUE;
						for (long k : rideTimes.get(i+"-"+j)) {
							if (k > max) max = k;
						}
						long maxRideTime = max/1000;
						System.out.printf("%-5d", maxRideTime);
					}
				}
			}
			System.out.printf("\n");
		}
	}
	
	//Prints the minimum ride times by floor
	private void minRideTimes(HashMap<String, ArrayList<Long>> rideTimes) throws InvalidArgumentException {
		if (rideTimes == null)
			throw new InvalidArgumentException("Building's minRideTimes method cannot accept null rideTimes arg\n");
		//Printing Max ride times
		System.out.println("");
		System.out.println("************ Minimum Ride Times By Floor ************");
		//Print column names
		System.out.printf("%-5s", "Floor");
		for (int floor=1; floor<=this.numFloors; floor++) {
			System.out.printf("%-5s", floor);
		}
		System.out.printf("\n");
		
		for (int i = 1; i <= this.numFloors; i++) {
			System.out.printf("%-5s", i);
			for (int j = 1; j <= this.numFloors; j++) {
				if (i == j) System.out.printf("%-5s", "X");
				else {
					if (!rideTimes.containsKey(i+"-"+j)) System.out.printf("%-5s", "0");
					else {
						long min = Long.MAX_VALUE;
						for (long k : rideTimes.get(i+"-"+j)) {
							if (k < min) min = k;
						}
						long minRideTime = min/1000;
						System.out.printf("%-5d", minRideTime);
					}
				}
			}
			System.out.printf("\n");
		}
	}
	
	//Prints wait/ride/total time by person
	private void peopleTimes() throws UnexpectedNullException{
		if (this.decommissionedPeople == null) {
			throw new UnexpectedNullException("Building's decommissionedRiders ArrayList is null when reporting data\n");
		}
		HashMap<Integer, ArrayList<RiderInterface>> peoplesTimes = new HashMap<Integer, ArrayList<RiderInterface>>();
		for (RiderInterface person : this.decommissionedPeople) {
			Integer personID = Integer.parseInt(person.getId().substring(1));
			if (!peoplesTimes.containsKey(personID)) {
				ArrayList<RiderInterface> peopleList = new ArrayList<RiderInterface>();
				peopleList.add(person);
				peoplesTimes.put(personID, peopleList);
			} else {
				peoplesTimes.get(personID).add(person);
			}
		}
		//Printing Max ride times
		System.out.println("");
		System.out.println("************ Ride Times By Individual People ************");
		System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s\n", "Person","Start Floor", "End Floor", "Wait Time","Ride Time","Total Time");
		for (int i = 1; i <= Collections.max(peoplesTimes.keySet()); i++) {
			if (!peoplesTimes.containsKey(i)) {
				System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s\n", "Person "+i, "NA", "NA", "NA", "NA", "NA");
			} else {
				RiderInterface person = peoplesTimes.get(i).get(0);
				long rideTime = person.getRideTime()/1000;
				long waitTime = person.getWaitTime()/1000;
				long totalTime = rideTime+waitTime;
				System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s\n", "Person "+i, person.getStartFloor(), person.getDestinationFloor(), waitTime+" seconds", rideTime+" seconds", totalTime+" seconds");
			}
		}	
	}
}

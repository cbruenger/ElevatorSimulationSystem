package building;

import java.util.ArrayList;
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
//import timeProcessor.TimeProcessor;

public final class Building {
	
	//Class Variables
	private static Building building;
	private int numFloors;
	private int numElevators;
	private HashMap<Integer, FloorInterface> floors;
	private HashMap<Integer,ElevatorInterface> elevators;
	private ArrayList<RiderInterface> decommissionedRiders;
	//private int elevatorToAssign; //This elevator assignment var is just for the first phase
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	private Building() {
		
		try {
			this.setNumFloors();
			this.setNumElevators();
			this.createFloors();
			this.createElevators();
			this.initializeDecommissionedRidersArrayList();
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
	
	//A function to get an instance of the class. Initializes instance if needed
	public static Building getInstance() {
		if (building == null) building = new Building();
		return building;
	}
	
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
	
	//Initializes the Elevators
	private void createElevators() throws AlreadyExistsException {
		
		//Throw error if elevators have already been created
		if (this.elevators != null) {
			throw new AlreadyExistsException("The elevators HashMap has already been created in building\n");
		}
		
		//Initialize the ArrayList for elevators to be stored
		this.elevators = new HashMap<Integer,ElevatorInterface>();
		
		//Create the chosen number of elevators
		for (int i = 1; i <= this.numElevators; i++) {
			this.elevators.put(i, new Elevator(i));
		}
	}
	
	//Initializes ArrayList and adds floors
	private void createFloors() throws AlreadyExistsException {
		
		//Throw error if elevators have already been created
		if (this.floors != null) {
			throw new AlreadyExistsException("The floors HashMap has already been created in building\n");
		}
		
		//Initialize the HashMap for floors to be stored
		this.floors = new HashMap<Integer,FloorInterface>();
		
		//Creates the chosen number of floors
		for (int i = 1; i <= this.numFloors; i++) {
			this.floors.put(i, new Floor(i));
			}
		}
	
	private void initializeDecommissionedRidersArrayList() throws AlreadyExistsException {
		
		//Throw error if list has already been created
		if (this.decommissionedRiders != null) {
			throw new AlreadyExistsException("The decommissionedRiders ArrayList has already been created in building\n");
		}
		
		//Initialize ArrayList for past riders to be stored
		this.decommissionedRiders = new ArrayList<RiderInterface>();
	}
	
	//Initializes the first elevator to be assigned as the first in the list
//	private void initializeElevatorToAssign() {
//		// TODO Delete this method once assignment logic works in the elevatorController
//		this.elevatorToAssign = 1;
//	}
	
	//Initializes GUI with numFloors, numElevators and all elevators IDLE on floor 1
	private void initializeGui() {
		ElevatorDisplay.getInstance().initialize(this.numFloors);
		for (int i = 1; i <= this.numElevators; i++) {
			ElevatorDisplay.getInstance().addElevator(i, 1);
			ElevatorDisplay.getInstance().setIdle(i);
		}
	}
	
	
	//Function called by TimeProcessor for updating the elevator activity
	public void update(long sleepTime) throws InvalidArgumentException {
		
		//Throw error if sleepTime is not a positive value
		if (sleepTime < 0) {
			throw new InvalidArgumentException("Building update cannot accept a negative value for sleepTime\n");
		}
		
		//Notify each elevator 
		for (int elevatorNumber : this.elevators.keySet()) {
			this.elevators.get(elevatorNumber).update(sleepTime);
		}
	}
	
	
	public void decommissionRiders(ArrayList<RiderInterface> riders) throws InvalidArgumentException, AlreadyExistsException {
		
		//Check if riders ArrayList is null
		if (riders == null) {
			throw new InvalidArgumentException("Building's decommissionRiders method cannot accept a null parameter\n");
		}
		//Check if any riders in the ArrayList already exist in the building's decommissionedRiders ArrayList
		for (RiderInterface rider: riders) {
			if (this.decommissionedRiders.contains(rider)) {
				throw new AlreadyExistsException("Rider already exists in building's decommissionedRiders ArrayList\n");
			}
		}
		
		//Add riders to the building's decommissionedRiders ArrayList
		for (RiderInterface rider: riders) {
			this.decommissionedRiders.add(rider);
			this.floors.get(rider.getDestinationFloor()).addRiderToDecommissionedList(rider.getId());
		}
	}
	
	
	public void assignElevatorForPickup(int floor, MyDirection direction, int elevatorNumber) throws InvalidArgumentException {
		
		//Throw error if floor is invalid
		if (floor < 1 || floor > this.numFloors) {
			throw new InvalidArgumentException("Building's elevatorRequested method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
		}
		
		//Throw error if direction is null or IDLE
		if (direction == null || direction == IDLE) {
			throw new InvalidArgumentException("Building's elevatorRequested method cannot accept null or IDLE for direction parameter\n");
		}
		
		//Throw error if elevatorNumber is invalid
		if (elevatorNumber < 1 || elevatorNumber > this.numElevators) {
			throw new InvalidArgumentException("Building's elevatorRequested method cannot accept elevatorNumber less than 1 or greater than " + DataStore.getInstance().getNumElevators() + "\n");
		}
		this.elevators.get(elevatorNumber).addPickupRequest(direction, floor);
	}
	
	
	public void addRiderToFloor(RiderInterface rider, int startFloor) throws InvalidArgumentException, AlreadyExistsException {
		if (startFloor < 1 || startFloor > this.numFloors) {
			throw new InvalidArgumentException("Building's addRiderToFloor method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
		}
		FloorInterface floor = floors.get(startFloor);
		floor.addRider(rider);
		
	}
	
	//Elevator assignment incrementer for phase 1
//	public void incrementElevatorToAssign() {
//		
//		//If the current elevator is the last one, reassign to first, otherwise increment
//		if (this.elevatorToAssign % DataStore.getInstance().getNumElevators() == 0)
//			this.elevatorToAssign = 1;
//		else 
//			elevatorToAssign++;
//	}
	
	//Return an ArrayList of people who need to transfer from a floor to an elevator that is waiting
	//Also removes the people from the floor
	public ArrayList<RiderInterface> getWaitersFromFloor(int floor, MyDirection direction, int availableCapacity) throws InvalidArgumentException, BadInputDataException, UnexpectedNullException {
		try {
			int totalCapacity = Integer.parseInt(DataStore.getInstance().getElevatorCapacity());
			if (floor < 1 || floor > this.numFloors) {
				throw new InvalidArgumentException("Building's getWaitersFromFloor method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
			}
			if (direction == null) {
				throw new InvalidArgumentException("Building's getWaitersFromFloor method cannot accept null value for direction\n");
			}
			if (availableCapacity < 0 || availableCapacity > totalCapacity) {
				throw new InvalidArgumentException("Building's getWaitersFromFloor method cannot accept number less than 0 or greater than " + totalCapacity + " for availableCapacity\n");
			}
			ArrayList<RiderInterface> ridersToTransfer = this.floors.get(floor).getRidersByDirection(direction, availableCapacity);
			if (ridersToTransfer == null)
				throw new UnexpectedNullException("Building's getWaitersFromFloor method received null value when retreiving riders from floor\n");
			return ridersToTransfer;
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("Building could not parse DataStore's elevatorCapacity value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("Building received null from DataStore for elevatorCapacity value\n"); 
	    }
	}
	
//	public boolean waitersLeftBehind(int floorNum, MyDirection direction) throws InvalidArgumentException {
//		
//		
//		if (floorNum < 1 || floorNum > this.numFloors) {
//			throw new InvalidArgumentException("Building's waitersLeftBehind method cannot receive number less than 1 or greater than " + this.numFloors + " for floorNum arg\n");
//		}
//		if (direction == null) {
//			throw new InvalidArgumentException("Building's waitersLeftBehind method cannot receive null for direction arg\n");
//		}
//		
//		return this.floors.get(floorNum).waitersLeftBehind(floorNum, direction);
//////		try {
//////			boolean waitersLeftBool = this.floors.get(floorNum).waitersLeftBehind(floorNum, direction);
//////			return waitersLeftBool;
//////		} catch (InvalidArgumentException e) {
//////			System.out.println(e.getMessage());
//////			e.printStackTrace();
//////			System.exit(-1);
//////		}
//////		return false;
//	}
//	
//	//A function to get the IDs of the all floors.
//	//For temporary use in our main
//	public ArrayList<Integer> getFloorNumbersArray() {
//		
//		//Create ArrayList to return
//		ArrayList<Integer> floorNumbers = new ArrayList<Integer>();
//		
//		//Add elevator numbers to list
//		for (int floorNumber : this.floors.keySet()) {
//			floorNumbers.add(floorNumber);
//		}
//		
//		return floorNumbers;
//	}
	
	public ElevatorDTO getElevatorDTO(int elevatorNumber) throws InvalidArgumentException {
		if (elevatorNumber < 1 || elevatorNumber > this.numElevators) {
			throw new InvalidArgumentException("Building's getElevatorDTO method cannot accept number less than 1 or greater than " + this.numElevators + " for elevatorNumber arg\n");
		}
		try {
			ElevatorDTO elevatorDTO = this.elevators.get(elevatorNumber).getDTO();
			if (elevatorDTO == null) {
				throw new UnexpectedNullException("Building's getElevatorDTO method received null DTO for elevator " + elevatorNumber + "\n");
			} else {
				return elevatorDTO;
			}
		} catch (UnexpectedNullException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	//A function for printing the average wait/ride times for riders and end of simulation
	public void reportData() {
		try {
			this.waitTimes();

		} catch (UnexpectedNullException e) {
			System.out.println(e.getMessage());
		}
	}

	private void waitTimes() throws UnexpectedNullException {
		if (this.decommissionedRiders == null) {
			throw new UnexpectedNullException("Building's decommissionedRiders ArrayList is null when reporting data\n");
		}
		HashMap<Integer, ArrayList<Long>> floorWaitTimes = new HashMap<Integer, ArrayList<Long>>();
//		long maxWaitTime;
//		long minWaitTime;
//		double averageWaitTime;
		
		for (RiderInterface rider : this.decommissionedRiders) {
			if (!floorWaitTimes.containsKey(rider.getStartFloor())) {
				ArrayList<Long> waitTimesList = new ArrayList<Long>();
				waitTimesList.add(rider.getWaitTime());
				floorWaitTimes.put(rider.getStartFloor(), waitTimesList);
			} else {
				floorWaitTimes.get(rider.getStartFloor()).add(rider.getWaitTime());
			}
		}
		System.out.println("\n");
		System.out.printf("%-20s %-20s %-20s %-20s\n", "Start Floor","Average Wait Time","Min Wait Time","Max Wait Time");
		for (int i = 1; i <= this.numFloors; i++) {
			if (!floorWaitTimes.containsKey(i)) {
				System.out.printf("%-20s %-20s %-20s %-20s\n", "Floor "+i, "NA", "NA", "NA");
			} else {
				long max1 = Long.MIN_VALUE;
				long min1 = Long.MAX_VALUE;
				long sum = 0;
				for (long j : floorWaitTimes.get(i)) {
					if (j > max1) max1 = j;
					if (j < min1) min1 = j;
					sum += j;
				}
				long max2 = max1/1000;
				long min2 = min1/1000;
				long average = (sum / floorWaitTimes.get(i).size())/1000;
				System.out.printf("%-20s %-20s %-20s %-20s\n", "Floor "+i, average+" seconds", min2+" seconds", max2+" seconds");

			}
		}		
	}
	
}

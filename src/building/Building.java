package building;

import java.util.ArrayList;
import java.util.HashMap;
import errors.*;
import dataStore.DataStore;
import elevator.Elevator;
import enumerators.MyDirection;
import floor.Floor;
import gui.ElevatorDisplay;
import interfaces.ElevatorInterface;
import interfaces.FloorInterface;
import interfaces.RiderInterface;
import timeProcessor.TimeProcessor;

public final class Building {
	
	//Class Variables
	private static Building building;
	private HashMap<Integer, FloorInterface> floors;
	private HashMap<Integer,ElevatorInterface> elevators;
	private ArrayList<RiderInterface> decommissionedRiders;
	private int elevatorToAssign; //This elevator assignment var is just for the first phase
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	private Building() {
		try {
			this.initialize();
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static Building getInstance() {
		if (building == null) building = new Building();
		return building;
	}
	
	//Initializes class variables and all other classes necessary for the program
	private void initialize() throws AlreadyExistsException {
		this.createElevators();
		this.createFloors();
		this.initializeDecommissionedRidersArrayList();
		this.initializeElevatorToAssign();
		this.initializeGui();
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
		for (int i = 1; i <= DataStore.getInstance().getNumElevators(); i++) {
			
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
		for (int i = 1; i <= DataStore.getInstance().getNumFloors(); i++) {
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
	private void initializeElevatorToAssign() {
		// TODO Delete this method once assignment logic works in the elevatorController
		this.elevatorToAssign = 1;
	}
	
	//Initializes GUI with numFloors, numElevators and all elevators IDLE on floor 1
	private void initializeGui() {
		ElevatorDisplay.getInstance().initialize(DataStore.getInstance().getNumFloors());
		for (int i = 1; i <= DataStore.getInstance().getNumElevators(); i++) {
			ElevatorDisplay.getInstance().addElevator(i, 1);
			ElevatorDisplay.getInstance().setIdle(i);
		}
	}
	
	
	//Function called by TimeProcessor for updating the elevator activity
	public void update(Long sleepTime) throws InvalidArgumentException {
		
		//Throw error if sleepTime is not a positive value
		if (sleepTime < 0) {
			throw new InvalidArgumentException("Building update cannot accept a negative value for sleepTime\n");
		}
		
		//Notify each elevator 
		for (int elevatorNumber : this.elevators.keySet()) {
			this.elevators.get(elevatorNumber).update(sleepTime);
		}
	}
	
	
	public void decommissionRiders(ArrayList<RiderInterface> riders) throws InvalidArgumentException, AlreadyDecommissionedException {
		
		//Check if riders ArrayList is null
		if (riders == null) {
			throw new InvalidArgumentException("Building's decommissionRiders method cannot accept a null parameter\n");
		}
		//Check if any riders in the ArrayList already exist in the building's decommissionedRiders ArrayList
		for (RiderInterface rider: riders) {
			if (this.decommissionedRiders.contains(rider)) {
				throw new AlreadyDecommissionedException("Rider already exists in building's decommissionedRiders ArrayList\n");
			}
		}
		
		//Add riders to the building's decommissionedRiders ArrayList
		for (RiderInterface rider: riders) {
			this.decommissionedRiders.add(rider);
		}
	}
	
	//elevatorRequested(rider.getStartFloor(), rider.getDirection())
	public void elevatorRequested(int floor, MyDirection direction, int elevatorNumber) {
		this.elevators.get(elevatorNumber).addPickupRequest(direction, floor);
		//this.incrementElevatorToAssign();
	}
	
	
	public void addRiderToFloor(RiderInterface rider, int startFloor) {
		// TODO error handling
		FloorInterface floor = floors.get(startFloor);
		floor.addRider(rider);
	}
	
	//Elevator assignment incrementer for phase 1
	public void incrementElevatorToAssign() {
		
		//If the current elevator is the last one, reassign to first, otherwise increment
		if (this.elevatorToAssign % DataStore.getInstance().getNumElevators() == 0)
			this.elevatorToAssign = 1;
		else 
			elevatorToAssign++;
	}
	
	//Return an ArrayList of people who need to transfer from a floor to an elevator that is waiting
	//Also removes the people from the floor
	public ArrayList<RiderInterface> getWaitersFromFloor(int floor, MyDirection direction) {
		// TODO error handling
//		ArrayList<RiderInterface> riders = this.floors.get(floor).getRidersByDirection(direction);
//		for (RiderInterface rider: riders) {
//			this.floors.get(floor).removeRider(rider);
//			}
		//System.out.println(this.floors.get(floor).getRidersByDirection(direction));
		return this.floors.get(floor).getRidersByDirection(direction);
	}
	
	//A function to get the IDs of the all floors.
	//For temporary use in our main
	public ArrayList<Integer> getFloorNumbersArray() {
		
		//Create ArrayList to return
		ArrayList<Integer> floorNumbers = new ArrayList<Integer>();
		
		//Add elevator numbers to list
		for (int floorNumber : this.floors.keySet()) {
			floorNumbers.add(floorNumber);
		}
		
		return floorNumbers;
	}
	
	//A function for printing the average wait/ride times for riders and end of simulation
	public void reportData() {
		// TODO 
	}
	
	
}

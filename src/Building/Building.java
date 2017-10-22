package Building;

import java.util.ArrayList;
import java.util.HashMap;

import DataStore.DataStore;
import TimeProcessor.TimeProcessor;
import Elevator.Elevator;
import Floor.Floor;
import Interfaces.FloorInterface;
import Interfaces.RiderInterface;
import enumerators.Direction;
import gui.ElevatorDisplay;
import Interfaces.ElevatorInterface;

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
		this.initialize();
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static Building getInstance() {
		if (building == null) building = new Building();
		return building;
	}
	
	//Initializes class variables and all other classes necessary for the program
	private void initialize() {
		this.createElevators();
		this.createFloors();
		this.initializeDecommissionedRidersArrayList();
		this.initializeElevatorToAssign();
		this.initializeGui();
	}
	
	//Initializes the Elevators
	private void createElevators() {
		
		//Initialize the ArrayList for elevators to be stored
		this.elevators = new HashMap<Integer,ElevatorInterface>();
		
		//Create the chosen number of elevators
		for (int i = 1; i <= DataStore.getInstance().getNumElevators(); i++) {
//			StringBuilder stringBuilder = new StringBuilder(String.valueOf("el"));
//			stringBuilder.append(i);
//			String id = stringBuilder.toString();
			this.elevators.put(i, new Elevator(i));
		}
	}
	
	//Initializes ArrayList and adds floors
	private void createFloors() {
		
		//Initialize the HashMap for floors to be stored
		this.floors = new HashMap<Integer,FloorInterface>();
		
		//Creates the chosen number of floors
		for (int i = 1; i <= DataStore.getInstance().getNumFloors(); i++) {
			this.floors.put(i, new Floor(i));
			}
		}
	
	private void initializeDecommissionedRidersArrayList() {
		this.decommissionedRiders = new ArrayList<RiderInterface>();
	}
	
	//Initializes the first elevator to be assigned as the first in the list
	private void initializeElevatorToAssign() {
		// TODO error handling
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
	public void update(Long sleepTime) {
		
		//Notify each elevator 
		for (int elevatorId : this.elevators.keySet()) {
			this.elevators.get(elevatorId).update(sleepTime);
		}
	}
	
	public void decommissionRiders(ArrayList<RiderInterface> riders) {
		for (RiderInterface rider: riders) {
			this.decommissionedRiders.add(rider);
		}
	}
	
	//elevatorRequested(rider.getStartFloor(), rider.getDirection())
	public void elevatorRequested(int floor, Direction direction) {
		this.elevators.get(this.elevatorToAssign).addPickupRequest(direction, floor);
		this.incrementElevatorToAssign();
	}
	
	
	public void addRiderToFloor(RiderInterface rider, int startFloor) {
		// TODO error handling
		FloorInterface floor = floors.get(startFloor);
		floor.addRider(rider);
	}
	
	public void pressButton(int elevatorNumber, int floorNumber, Direction dir) {
		this.elevators.get(elevatorNumber);
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
	public ArrayList<RiderInterface> getWaitersFromFloor(int floor, Direction direction) {
		// TODO error handling
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

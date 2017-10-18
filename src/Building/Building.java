package Building;

import java.util.ArrayList;
import java.util.HashMap;

import DataStore.DataStore;
import Elevator.Elevator;
import Floor.Floor;
import Interfaces.FloorInterface;
import Interfaces.ElevatorInterface;

public final class Building {
	
	//Class Variables
	private static Building building;
	private HashMap<String, FloorInterface> floors;
	private ArrayList<ElevatorInterface> elevators;
	
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
	}
	
	//Initializes the Elevators
	private void createElevators() {
		//Initialize the ArrayList for elevators to be stored
		this.elevators = new ArrayList<ElevatorInterface>();
		
		//Create the chosen number of elevators
		for (int i = 1; i <= DataStore.getInstance().getNumElevators(); i++) {
			StringBuilder stringBuilder = new StringBuilder(String.valueOf("el"));
			stringBuilder.append(i);
			String id = stringBuilder.toString();
			this.elevators.add(new Elevator(id));
		}
	}
	
	//Initializes the Floor Facade
	private void createFloors() {
		//Initialize the HashMap for floors to be stored
		this.floors = new HashMap<String,FloorInterface>();
		
		//Creates the chosen number of floors
		for (int i = 1; i <= DataStore.getInstance().getNumFloors(); i++) {
			String floorNumber = Integer.toString(i);
			this.floors.put(floorNumber, new Floor(floorNumber));
			}
		}
		
	
}

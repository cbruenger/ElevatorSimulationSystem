package Building;

import Facades.ElevatorFacade;
import UserInputData.UserInputData;

public final class Building {
	
	//Class Variables
	private static Building thisBuilding;
	private int numFloors;
	private int numElevators;
	
	//Constructor
	private Building() {
		this.initialize();
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static Building getInstance() {
		if (thisBuilding == null) thisBuilding = new Building();
		return thisBuilding;
	}
	
	//Initializes class variables and all other classes necessary for the program
	private void initialize() {
		this.initializeBuildingVars();
		this.createElevatorFacade();
	}
	
	//Initialize class variables
	private void initializeBuildingVars() {
		// TODO error handling
		this.setNumFloors(UserInputData.getInstance().getNumFloors());
		this.setNumElevators(UserInputData.getInstance().getNumElevators());
	}
	
	//Initializes the Elevator Facade
	private void createElevatorFacade() {
		ElevatorFacade.getInstance();
	}
	
	//A function to set the number of floors in the building
	private void setNumFloors(int numFloors) {
		// TODO error handling
		this.numFloors = numFloors;
	}
	
	//A function to set the number of elevators in the building
	private void setNumElevators(int numElevators) {
		// TODO error handling
		this.numElevators = numElevators;
	}
	
	//A function to get the number of floors in the building
	public int getNumFloors() {
		return this.numFloors;
	}
	
	//A function to get the number of elevators in the building
	public int getNumElevators() {
		return this.numElevators;
	}
	
//	public static void main(String[] args) {
//		Building.getInstance();
//		//Building building1 = Building.getInstance();
//		System.out.printf("When accessed by class, there are %s floors\n",Building.getInstance().getNumFloors());
//		System.out.printf("When accessed by class, there are %s elevators\n",Building.getInstance().getNumElevators());
//		
//	}

}

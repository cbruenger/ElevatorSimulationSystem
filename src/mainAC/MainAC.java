package mainAC;

import building.Building;
import dataStore.DataStore;
import timeProcessor.TimeProcessor;

public class MainAC {

	public static void main(String[] args) {
		
		//Initialize Data Store to parse input file
		DataStore.getInstance();
		
		//Initialize creation of building and all components
		Building.getInstance();		
		
		//Run simulation
		TimeProcessor.getInstance().runSimulation();
	}
}

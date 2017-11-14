package mainAC;

import building.Building;
import dataStore.DataStore;
import timeProcessor.TimeProcessor;

public class MainAC {

	public static void main(String[] args) {
		
		DataStore.getInstance();
		
		Building.getInstance();		
		
		TimeProcessor.getInstance().runSimulation();
	}
}

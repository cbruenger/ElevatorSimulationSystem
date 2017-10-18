package Facades;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import DataStore.DataStore;
import Rider.Rider;
import Interfaces.FloorInterface;
import Interfaces.RiderInterface;

public final class RiderFacade {
	
	//Class attributes
	private static RiderFacade instance;
	private int idIncrementer;
	private int maxFloors;
	private HashMap<String, RiderInterface> riders;
		
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
		
	private RiderFacade() {
		this.initialize();
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static RiderFacade getInstance() {
		if (instance == null) instance = new RiderFacade();
		return instance;
	}
	
	//Initializes all things necessary for the class
	private void initialize() {
		
		//Creates the hash map for all riders to be stored
		this.createRidersHashMap();
		
		//Set floors for random generation
		this.setNumberOfFloors();
	}
	
	//A function to initialize the hash map for floors to be stored
	private void createRidersHashMap() {
		this.riders = new HashMap<String,RiderInterface>();
	}	
	
	private void setNumberOfFloors() {
		this.maxFloors = DataStore.getInstance().getNumFloors();
	}
	
	//A function to get the IDs of the all riders
	public ArrayList<String> getRiderIds() {
		ArrayList<String> ids = new ArrayList<String>();
		for (String id : this.riders.keySet()) {
			ids.add(id);
		}
		return ids;
	}
	
	public RiderInterface getRider(String riderId) {
		return riders.get(riderId);
	}
	
	public void generateRandomRider() {
		int randomStart = ThreadLocalRandom.current().nextInt(1, this.maxFloors+1);
		int randomDestination = ThreadLocalRandom.current().nextInt(1, this.maxFloors+1);
		
		// making sure start and destination are not same
		while (randomStart == randomDestination) {
			randomDestination = ThreadLocalRandom.current().nextInt(1, this.maxFloors+1);
		} 
		
		Rider newRider = new Rider(generateId(), randomStart, randomDestination);
		this.addToRiderMap(newRider);
		this.addRiderToFloor(randomStart, newRider.getId());
	}

	public void generateRider(int startFloor, int destinationFloor) {
		Rider newRider = new Rider(generateId(), startFloor, destinationFloor);
		this.addToRiderMap(newRider);
		this.addRiderToFloor(startFloor, newRider.getId());
	}
	
	private void addToRiderMap(RiderInterface rider) {
		this.riders.put(rider.getId(), rider);
	}
	
	private String generateId() {
		idIncrementer++;
		String id = "r" + idIncrementer;
		return id;
	}
	
	/////////////////////////////////
	//				               //
	//    Facade Communication     //
	//				               //
	/////////////////////////////////
	
	//Figure this out better because this shouldn't be public
	public HashMap<String, RiderInterface> getRiders() {
		return this.riders;
	}
	
	private void addRiderToFloor(int floorId, String riderId) {
		FloorFacade.getInstance().addRiderToFloor(Integer.toString(floorId), riderId);
		// Start time for rider...
	}  
	
	

}

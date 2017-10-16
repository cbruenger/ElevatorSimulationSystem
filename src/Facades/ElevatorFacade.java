package Facades;

import Elevator.Elevator;
import UserInputData.UserInputData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public final class ElevatorFacade {

	//Class variables
	private static ElevatorFacade instance;
	private int elevatorSpeed;
	private int doorsOpenTime;
	private int idleTime;
	private HashMap<String,Elevator> elevators;
	
	//Constructors
	private ElevatorFacade() {
		this.initialize();
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static ElevatorFacade getInstance() {
		if (instance == null) instance = new ElevatorFacade();
		return instance;
	}
	
	//Initializes all things necessary for the class
	private void initialize() {
		
		//Set class variables
		this.setElevatorSpeed(UserInputData.getInstance().getSpeed());
		this.setDoorsOpenTime(UserInputData.getInstance().getDoorsOpenTime());
		this.setIdleTime(UserInputData.getInstance().getIdleTime());
		
		//Creates the hash map for elevators to be stored
		this.createElevatorsHashMap();
		
		//Creates elevators
		this.createElevators();
	}
	
	//A function to initialize the hash map for elevators to be stored
	private void createElevatorsHashMap() {
		this.elevators = new HashMap<String,Elevator>();
	}
	
	//A function to create the chosen number of elevators
	private void createElevators() {
		for (int i = 0; i < UserInputData.getInstance().getNumElevators(); i++) {
			StringBuilder stringBuilder = new StringBuilder(String.valueOf("el"));
			stringBuilder.append(i);
			String id = stringBuilder.toString();
			this.elevators.put(id, new Elevator(id));
		}
	}
	
	//A function to set the speed at which an elevator moves between floors
	private void setElevatorSpeed(int speed) {
		// TODO error handling
		this.elevatorSpeed = speed * 1000;
	}
	
	//A function to set the amount of time an elevator's doors stay open
	private void setDoorsOpenTime(int doorsOpenTime) {
		// TODO error handling
		this.doorsOpenTime = doorsOpenTime * 1000;
	}

	//A function to set the amount of time an elevator stays idle until returning to floor 1
	private void setIdleTime(int idleTime) {
		// TODO error handling
		this.idleTime = idleTime * 1000;
	}
	
	//A function to get the speed at which an elevator moves between floors
	public int getElevatorSpeed() {
		return this.elevatorSpeed;
	}
	
	//A function to get the amount of time an elevator's doors stay open
	public int getDoorsOpenTime() {
		return this.doorsOpenTime;
	}
	
	//A function to get the amount of time an elevator stays idle until returning to floor 1
	public int getIdleTime() {
		return this.idleTime;
	}
	
	//A function to get the IDs of the elevators managed
	public String[] getElevatorIds() {
		String[] ids = printMap(this.elevators);
		return ids;
	}
	
	public String[] printMap(Map<String, Elevator> map) {
	    Iterator it = map.entrySet().iterator();
	    String[] ids = new String[this.elevators.size()];
	    int i = 0;
	    while (it.hasNext()) {
	    		Map.Entry<String, Elevator> pair = (Map.Entry<String, Elevator>)it.next();
	    		ids[i] = pair.getKey();
	        it.remove(); // avoids a ConcurrentModificationException
	        i++;
	    }
	    return ids;
	}
	
	
}

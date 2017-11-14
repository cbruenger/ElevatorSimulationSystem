package person;

import dataStore.DataStore;
import enumerators.MyDirection;
import static enumerators.MyDirection.*;
import errors.BadInputDataException;
import errors.InvalidArgumentException;
import interfaces.PersonInterface;
import timeProcessor.TimeProcessor;

/* The PersonImpl class is an implementation of a Person that is
 * given an id, start floor and destination floor upon creation.
 * Contains necessary getters, and setters for documenting and
 * calculating wait/ride times.
 */
public class PersonImpl implements PersonInterface {

	//Class variables
	private String id;
	private int startFloor;
	private int destinationFloor;
	private MyDirection direction;
	private long requestTime;
	private long enterTime;
	private long exitTime;
	private long waitTime;
	private long rideTime;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	//Constructor, initializes necessary components
	public PersonImpl(String id, int startFloor, int destinationFloor) {
		try {
			int numFloors = this.getNumFloors();
			this.setId(id);
			this.setStartFloor(startFloor, numFloors);
			this.setDestinationFloor(destinationFloor, numFloors);
			this.setDirection(startFloor, destinationFloor, numFloors);
			this.requestElevator();
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (BadInputDataException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Interface Methods 			*
	 * 										*
	 *////////////////////////////////////////
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public int getStartFloor() {
		return this.startFloor;
	}
	
	@Override
	public int getDestinationFloor() {
		return this.destinationFloor;
	}
	
	@Override
	public MyDirection getDirection() {
		return this.direction;
	}
	
	@Override
	public long getWaitTime() {
		return this.waitTime;
	}
	
	@Override
	public long getRideTime() {
		return this.rideTime;
	}
	
	@Override
	public void enterElevator() {
		this.setEnterTime();
		this.setWaitTime();
	}

	@Override
	public void exitElevator() {
		this.setExitTime();
		this.setRideTime();
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Methods called by the Constructor		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Assigns id variable
	private void setId(String id) throws  InvalidArgumentException {
		if (id == null || id.isEmpty()) {
			throw new InvalidArgumentException("PersonImpl cannot accept null or empty string for id\n");
		}
		this.id = id;
	}
	
	//Assigns startFloor variable
	private void setStartFloor(int startFloor, int numFloors) throws InvalidArgumentException {
		if (startFloor < 1 || startFloor > numFloors) {
			throw new InvalidArgumentException("PersonImpl cannot accept number less than 1 or greater than " + numFloors + " for startFloor assignment\n");
		}
		this.startFloor = startFloor;
	}
	
	//Assigns destinationFloor variable
	private void setDestinationFloor(int destinationFloor, int numFloors) throws InvalidArgumentException {
		if (destinationFloor < 1 || startFloor > numFloors) {
			throw new InvalidArgumentException("PersonImpl cannot accept number less than 1 or greater than " + numFloors + " for destinationFloor assignment\n");
		}
		this.destinationFloor = destinationFloor;
	}
	
	//Assigns the direction the person will travel
	private void setDirection(int startFloor, int destinationFloor, int numFloors) throws InvalidArgumentException {
		if (startFloor < 0 || startFloor > numFloors) {
			throw new InvalidArgumentException("PersonImpl cannot accept number less than 1 or greater than " + numFloors + " for direction assignment\n");
		}
		if (destinationFloor < 1 || startFloor > numFloors) {
			throw new InvalidArgumentException("PersonImpl cannot accept number less than 1 or greater than " + numFloors + " for direction assignment\n");
		}
		if (startFloor == destinationFloor) {
			throw new InvalidArgumentException("PersonImpl cannot accept startFloor and destinationFloor values that are equal for direction assignment\n");
		}
		if (startFloor - destinationFloor > 0) this.direction = DOWN;
		else this.direction = UP;
	}
	
	//Calls method to assign the time the person requested the elevator
	private void requestElevator() {
		this.setRequestTime();
	}
	
	/*////////////////////////////////////////////
	 * 											*
	 * 		Wait/Ride Time Handling Methods		*
	 * 											*
	 *////////////////////////////////////////////
	
	//Assigns the requestTime variable upon requesting an elevator
	private void setRequestTime() {
		this.requestTime = TimeProcessor.getInstance().getCurrentTimeMillis();
	}
    
	//Assigns the enterTime variable upon entering an elevator
    private void setEnterTime() {
		this.enterTime = TimeProcessor.getInstance().getCurrentTimeMillis();
	}
    
    //Assigns the waitTime variable upon entering an elevator
    private void setWaitTime() {
    	this.waitTime = this.enterTime - this.requestTime;
  	}
    
    //Assigns exitTime variable upon exiting an elevator
    private void setExitTime() {
		this.exitTime = TimeProcessor.getInstance().getCurrentTimeMillis();
	}
    
    //Assigns rideTime variable upon exiting an elevator
    private void setRideTime() {
		this.rideTime = this.exitTime - this.enterTime;
	}
    
    /*////////////////////////////////////////
	 * 										*
	 * 		DataStore Retrieval Method		*
	 * 										*
	 *////////////////////////////////////////
    
	//Accesses DataStore and parses the number of floors to an int, checks validity and returns it
    private int getNumFloors() throws BadInputDataException {
		try { 
			int numFloors = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (numFloors > 1)
				return numFloors;
			else
				throw new BadInputDataException("PersonImpl received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e1) { 
	        throw new BadInputDataException("PersonImpl could not parse DataStore's numFloors value to int during floorNumber assignment check\n");
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("PersonImpl received null from DataStore for numFloors value during floorNumber assignment check\n"); 
	    }	
	}
}

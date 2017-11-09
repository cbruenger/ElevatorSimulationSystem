package rider;

import dataStore.DataStore;
import enumerators.MyDirection;
import errors.BadInputDataException;
import errors.InvalidArgumentException;
import interfaces.RiderInterface;

public class RiderImpl implements RiderInterface {

	private String id;
	private int startFloor;
	private int destinationFloor;
	private MyDirection direction;
	private long requestTime;
	private long enterTime;
	private long exitTime;
	private long waitTime;
	private long rideTime;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public RiderImpl(String id, int startFloor, int destinationFloor) {
		
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
	
	
	////////////////////
	//				  //
	//    Setters     //
	//				  //
	////////////////////
	
	private void setId(String id) throws  InvalidArgumentException {
		if (id == null || id.isEmpty()) {
			throw new InvalidArgumentException("RiderImpl cannot accept null or empty string for id\n");
		}
		this.id = id;
	}
	
	private void setStartFloor(int startFloor, int numFloors) throws InvalidArgumentException {
		if (startFloor < 1 || startFloor > numFloors) {
			throw new InvalidArgumentException("RiderImpl cannot accept number less than 1 or greater than " + numFloors + " for startFloor assignment\n");
		}
		this.startFloor = startFloor;
	}
	
	private void setDestinationFloor(int destinationFloor, int numFloors) throws InvalidArgumentException {
		if (destinationFloor < 1 || startFloor > numFloors) {
			throw new InvalidArgumentException("RiderImpl cannot accept number less than 1 or greater than " + numFloors + " for destinationFloor assignment\n");
		}
		this.destinationFloor = destinationFloor;
	}
	
	private void setDirection(int startFloor, int destinationFloor, int numFloors) throws InvalidArgumentException {
		if (startFloor < 0 || startFloor > numFloors) {
			throw new InvalidArgumentException("RiderImpl cannot accept number less than 1 or greater than " + numFloors + " for direction assignment\n");
		}
		if (destinationFloor < 1 || startFloor > numFloors) {
			throw new InvalidArgumentException("RiderImpl cannot accept number less than 1 or greater than " + numFloors + " for direction assignment\n");
		}
		if (startFloor == destinationFloor) {
			throw new InvalidArgumentException("RiderImpl cannot accept startFloor and destinationFloor values that are equal for direction assignment\n");
		}
		if (startFloor - destinationFloor > 0) this.direction = MyDirection.DOWN;
		else this.direction = MyDirection.UP;
	}
		
	private void setRequestTime() {
		this.requestTime = System.currentTimeMillis();
	}
    
    private void setEnterTime() {
		this.enterTime = System.currentTimeMillis();
	}
    
    private void setWaitTime() {
    	this.waitTime = this.enterTime - this.requestTime;
  	}
    
    private void setExitTime() {
		this.exitTime = System.currentTimeMillis();
	}
    
    private void setRideTime() {
		this.rideTime = this.exitTime - this.enterTime;
	}
    
    private int getNumFloors() throws BadInputDataException {
		
		try { 
			int numFloors = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (numFloors > 1)
				return numFloors;
			else
				throw new BadInputDataException("FloorImpl received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e1) { 
	        throw new BadInputDataException("FloorImpl could not parse DataStore's numFloors value to int during floorNumber assignment check\n");
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("FloorImpl received null from DataStore for numFloors value during floorNumber assignment check\n"); 
	    }
		
	}

		////////////////////
		//				   //
		//    Getters      //
		//				   //
		/////////////////////
	
	
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
	
//	@Override
//	public long getRequestTime() {
//		return this.requestTime;
//	}
	
//	@Override
//	public long getEnterTime() {
//		return this.enterTime;
//	}
	
	@Override
	public long getWaitTime() {
		return this.waitTime;
	}
//
//	@Override
//	public long getExitTime() {
//		return this.exitTime;
//	}
	
	@Override
	public long getRideTime() {
		return this.rideTime;
	}
	
	
		//////////////////////////
		//				        //
		//    Other Methods     //
		//				        //
		//////////////////////////
    
    
	public void requestElevator() {
		this.setRequestTime();
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

}

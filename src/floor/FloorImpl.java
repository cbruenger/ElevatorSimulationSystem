package floor;

import java.util.ArrayList;

import dataStore.DataStore;
import enumerators.MyDirection;
import errors.*;
import interfaces.FloorInterface;
import interfaces.RiderInterface;
import timeProcessor.TimeProcessor;

public class FloorImpl implements FloorInterface{

	private int floorNumber;
	
	private ArrayList<RiderInterface> riders;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public FloorImpl(int floorNumber) {
		
		try {
			//Create necessary data structures
			this.createRidersArrayList();
			
			//Set initial variable values
			this.setFloorNumber(floorNumber);
			
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		} catch (InvalidArgumentException e3) {
			System.out.println(e3.getMessage());
			e3.printStackTrace();
			System.exit(-1);
		}
		
		
	}
	
	/////////////////////////////
	//						  //
	//    Setters/Private     //
	//				  		 //
	///////////////////////////
	
	private void createRidersArrayList() throws AlreadyExistsException {
		
		//Throw error if elevators have already been created
		if (this.riders != null) {
			throw new AlreadyExistsException("The riders ArrayList has already been created in FloorImpl\n");
		}
		
		this.riders = new ArrayList<RiderInterface>();
	}

	private void setFloorNumber(int floorNumber) throws InvalidArgumentException {
		
		try {
			int numFloors = this.getNumFloors();
			if (floorNumber < 1 || floorNumber > numFloors) {
				throw new InvalidArgumentException("FloorImpl cannot accept a number less than 1 or greater than " + numFloors + "\n");
			}
			this.floorNumber = floorNumber;
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	private int getNumFloors() throws BadInputDataException {
		
		try { 
			int numFloors = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (numFloors > 1)
				return numFloors;
			else
				throw new BadInputDataException("FloorImpl received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("FloorImpl could not parse DataStore's numFloors value to int during floorNumber assignment check\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("FloorImpl received null from DataStore for numFloors value during floorNumber assignment check\n"); 
	    }
		
	}
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////
	
	@Override
	public int getFloorNumber() {
		return this.floorNumber;
	}

//	//Only temporarily for testing in our main
//	@Override
//	public ArrayList<RiderInterface> getRiders() {
//		return this.riders;
//	}
	
	@Override
	public void addRider(RiderInterface rider) throws AlreadyExistsException {
		if (this.riders.contains(rider)) {
			throw new AlreadyExistsException("Rider " + rider.getId() + " is already on floor " + this.floorNumber);
		}
		System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " pressed " + rider.getDirection() + " on Floor " + rider.getStartFloor());
		this.riders.add(rider);
	}
	
	@Override
	public void removeRider(RiderInterface rider) throws CannotRemoveException {
		if (!this.riders.contains(rider)) {
			throw new CannotRemoveException("FloorImpl's removeRider method cannot remove rider " + rider.getId() + " from floor because the rider is not in rider ArrayList");
		}
		this.riders.remove(rider);
	}
	
	//Return a list of people who's direction is same as parameter
	//Also, delete them from floors list before returning
	@Override
	public ArrayList<RiderInterface> getRidersByDirection(MyDirection direction) {
		ArrayList<RiderInterface> ridersToDelete = new ArrayList<>();
		ArrayList<RiderInterface> ridersToTransfer = new ArrayList<>(this.riders);
		for (RiderInterface rider: this.riders) {
			if (rider.getDirection() == direction) {
				// PRINT STATEMENT
				ridersToDelete.add(rider);
			}
			else {
			ridersToTransfer.remove(rider);
			//System.out.print("");
			}
		}
		riders.removeAll(ridersToDelete);
		return ridersToTransfer;
	}

}

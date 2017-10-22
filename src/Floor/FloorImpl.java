package Floor;

import java.util.ArrayList;
import TimeProcessor.TimeProcessor;
import Interfaces.FloorInterface;
import Interfaces.RiderInterface;
import enumerators.Direction;

public class FloorImpl implements FloorInterface{

	private int floorNumber;
	
	private ArrayList<RiderInterface> riders;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public FloorImpl(int floorNumber) {
		
		//Create necessary data structures
		this.createRidersArrayList();
		
		//Set initial variable values
		this.setFloorNumber(floorNumber);
	}
	
	/////////////////////////////
	//						  //
	//    Setters/Private     //
	//				  		 //
	///////////////////////////
	
	private void createRidersArrayList() {
		this.riders = new ArrayList<RiderInterface>();
	}

	private void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
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

	//Only temporarily for testing in our main
	@Override
	public ArrayList<RiderInterface> getRiders() {
		return this.riders;
	}
	
	@Override
	public void addRider(RiderInterface riderId) {
		this.riders.add(riderId);
	}
	
	@Override
	public void removeRider(RiderInterface rider) {
		this.riders.remove(rider);
	}
	
	//Return a list of people who's direction is same as parameter
	//Also, delete them from floors list before returning
	@Override
	public ArrayList<RiderInterface> getRidersByDirection(Direction direction) {
		
		//Create an empty list, then add riders to it with the given direction and delete from floor
		ArrayList<RiderInterface> ridersToTransfer = new ArrayList<RiderInterface>();
		for (RiderInterface rider : this.riders) {
			if (rider.getDirection() == direction) {
				ridersToTransfer.add(rider);
				this.riders.remove(rider);
			}
		}
		return riders;
	}

}

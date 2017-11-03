package floor;

import java.util.ArrayList;

import enumerators.MyDirection;
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
	public void addRider(RiderInterface rider) {
		System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " pressed " + rider.getDirection() + " on Floor " + rider.getStartFloor());
		this.riders.add(rider);
	}
	
	@Override
	public void removeRider(RiderInterface rider) {
		this.riders.remove(rider);
	}
	
	//Return a list of people who's direction is same as parameter
	//Also, delete them from floors list before returning
	@Override
	public ArrayList<RiderInterface> getRidersByDirection(MyDirection direction) {
		ArrayList<RiderInterface> ridersToDelete = new ArrayList<>();
		ArrayList<RiderInterface> ridersToTransfer = new ArrayList<>(riders);
		for (RiderInterface rider: riders) {
			if (rider.getDirection() == direction) {
				// PRINT STATEMENT
				ridersToDelete.add(rider);
			}
			else {
			ridersToTransfer.remove(rider);
			System.out.print("");
			}
		}
		riders.removeAll(ridersToDelete);
		return ridersToTransfer;
	}

}

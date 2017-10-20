package Floor;

import java.util.ArrayList;

import Interfaces.FloorInterface;
import Interfaces.RiderInterface;

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
	
      /////////////////////
      //				 	 //
      //    Getters      //
      //				 	//
      /////////////////////
	
	public int getFloorNumber() {
		return this.floorNumber;
	}

	//Only temporarily for testing in our main
	public ArrayList<RiderInterface> getRiders() {
		return this.riders;
	}
	
	
	//////////////////////////
	//				        //
	//    Other Methods     //
	//				        //
	//////////////////////////
	
	public void addRider(RiderInterface riderId) {
		this.riders.add(riderId);
	}
	
	public void removeRider(RiderInterface rider) {
		this.riders.remove(rider);
	}

}

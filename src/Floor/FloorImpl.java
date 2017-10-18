package Floor;

import java.util.ArrayList;

import Interfaces.FloorInterface;
import Interfaces.RiderInterface;

public class FloorImpl implements FloorInterface{

	private String floorNumber;
	
	private ArrayList<RiderInterface> riders;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public FloorImpl(String floor_number) {
		
		//Create necessary data structures
		this.createRidersArrayList();
		
		//Set initial variable values
		this.setFloor(floor_number);
	}
	
	////////////////////
	//				  //
	//    Setters     //
	//				  //
	////////////////////
	
	private void createRidersArrayList() {
		this.riders = new ArrayList<RiderInterface>();
	}

	private void setFloor(String floorNumber) {
		this.floorNumber = floorNumber;
	}
	
	
      ////////////////////
      //				 //
      //    Getters      //
      //				 //
      /////////////////////
	
	public String getFloorNumber() {
		return this.floorNumber;
	}

	public ArrayList<RiderInterface> getRiders() {
		return this.riders;
	}
	
	
	//////////////////////////
	//				        //
	//    Other Methods     //
	//				        //
	//////////////////////////

	public void removeRider(RiderInterface rider) {
		this.riders.remove(rider);
	}
	
	public void addRider(RiderInterface riderId) {
		this.riders.add(riderId);
	}

}

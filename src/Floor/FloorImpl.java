package Floor;

import java.util.ArrayList;

import Interfaces.FloorInterface;

public class FloorImpl implements FloorInterface{

	private String floorNumber;
	
	private ArrayList<String> riderIds;
	
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
		this.riderIds = new ArrayList<String>();
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

	public ArrayList<String> getRiderIds() {
		return this.riderIds;
	}
	
	
	//////////////////////////
	//				        //
	//    Other Methods     //
	//				        //
	//////////////////////////

	public void removeRider(String riderId) {
		this.riderIds.remove(riderId);
	}
	
	public void addRider(String riderId) {
		this.riderIds.add(riderId);
	}
	

}

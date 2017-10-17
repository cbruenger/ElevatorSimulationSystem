package Floor;

import java.util.ArrayList;

import Interfaces.FloorInterface;

public class FloorImpl implements FloorInterface{

	private String floor_number;
	
	private ArrayList<String> riderIds;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public FloorImpl(String floor_number) {
		
		//Create necessary data structures
		this.createRidersArray();
		
		//Set initial variable values
		this.setFloor(floor_number);
	}
	
	////////////////////
	//				  //
	//    Setters     //
	//				  //
	////////////////////
	
	private void createRidersArray() {
		this.riderIds = new ArrayList<String>();
	}

	private void setFloor(String floor_number) {
		this.floor_number = floor_number;
	}
	
	
      ////////////////////
      //				 //
      //    Getters      //
      //				 //
      /////////////////////
	
	public String getFloorNumber() {
		return this.floor_number;
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

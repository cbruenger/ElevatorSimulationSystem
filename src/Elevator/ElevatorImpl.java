package Elevator;

import java.util.ArrayList;
import java.util.HashSet;

import Interfaces.ElevatorInterface;
import Rider.Rider;

/*////////////////////////////////////////
 * 										*
 * 				Notes TODO				*
 * 										*
 *////////////////////////////////////////
 
//  need to check for basement and top floor...
//  maybe a priority queue for pick_ups/drop_offs

public class ElevatorImpl implements ElevatorInterface{
	
	private String id;
	private String direction;
	private int current_floor;
	private boolean door_open;
	
	private HashSet<Rider> riders;
	private ArrayList<Integer> pickUps;
	private ArrayList<Integer> dropOffs;
	
	//Constructor
	public ElevatorImpl(String id) {
		
		//Create necessary data structures
		this.createRidersHashSet();
		this.createPickUpsArrayList();
		this.createDropOffsArrayList();
		
		
		//Set initial variable values
		this.setId(id);
		this.setDirection("IDLE");
		this.setCurrentFloor(1);
		this.setDoorStatus(false);
		
	}
	
	////////////////////////////////////////////////////////////
	//														//
	//     Private Setter methods called by constructor      //
	//														//
	///////////////////////////////////////////////////////////
	
	private void createRidersHashSet() {
		this.riders = new HashSet<Rider>();
	}
	
	private void createPickUpsArrayList() {
		this.pickUps = new ArrayList<Integer>();
	}
	
	private void createDropOffsArrayList() {
		this.dropOffs = new ArrayList<Integer>();
	}
	
	private void setId(String id) {
		this.id = id;
	}
	
	
	/////////////////////////////
	//						   //
	//     Public methods      //
	//						  //
	////////////////////////////
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public void setCurrentFloor(int floor) {
		this.current_floor = floor;
	}
	
	public void setDoorStatus(boolean status) {
		this.door_open = status;
	}
	
	public void moveUp() {
		this.setCurrentFloor(this.getCurrentFloor() + 1);
	}
	
	public void moveDown() {
		this.setCurrentFloor(this.getCurrentFloor() - 1);
	}
	
	public int getCurrentFloor() {
		return this.current_floor;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	public boolean getDoorOpenStatus() {
		return this.door_open;
	}
	
	public void openDoors() {
		this.door_open = true;
	}
	
	public void closeDoors() {
		this.door_open = false;
	}
	
	public String getId() {
		return this.id;
	}
	
	
	public ArrayList<String> getRiderIds() {
		ArrayList<String> riderIds = new ArrayList<String>();
		for (Rider i : this.riders) {
			riderIds.add(i.getId());
		}
		return riderIds;
	}
	
	public ArrayList<Integer> getPickUps() {
		return this.pickUps;
	}
	
	public ArrayList<Integer> getDropOffs() {
		//TODO
		return this.dropOffs;
	}
	
	
		
}

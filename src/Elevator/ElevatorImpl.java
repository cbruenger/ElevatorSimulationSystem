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
	private int capacity;
	private boolean door_open;
	
	private HashSet<Rider> riders;
	private ArrayList<Integer> pick_ups;
	private ArrayList<Integer> drop_offs;
	
	//Constructor
	public ElevatorImpl(String id, int capacity) {
		
		//Create necessary data structures
		this.createRidersHashSet();
		this.createPickUpsArrayList();
		this.createDropOffsArrayList();
		
		
		//Set initial variable values
		this.setId(id);
		this.setDirection("IDLE");
		this.setCurrentFloor(1);
		this.setCapacity(capacity);
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
		this.pick_ups = new ArrayList<Integer>();
	}
	
	private void createDropOffsArrayList() {
		this.drop_offs = new ArrayList<Integer>();
	}
	
	private void setId(String id) {
		this.id = id;
	}
	
	private void setCapacity(int capacity) {
		this.capacity = capacity;
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
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public String[] getRiderIds() {
		//TODO
		return new String[] {};
	}
	
	public ArrayList<Integer> getPickUps() {
		//TODO
		return new ArrayList<Integer>() {};
	}
	
	public ArrayList<Integer> getDropOffs() {
		//TODO
		return new ArrayList<Integer>() {};
	}
	
	
		
}

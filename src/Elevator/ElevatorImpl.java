package Elevator;

import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.ElevatorInterface;
import Interfaces.RiderInterface;
import enumerators.Direction;

/*////////////////////////////////////////
 * 										*
 * 				Notes TODO				*
 * 										*
 *////////////////////////////////////////
 
//  need to check for basement and top floor...
//  maybe a priority queue for pick_ups/drop_offs

public class ElevatorImpl implements ElevatorInterface{
	
	private String id;
	private Direction direction;
	private int currentFloor;
	private boolean doorOpen;
	
	private ArrayList<RiderInterface> riders;
	// HashMap of directions to floor #/IDs
	private HashMap<Direction, ArrayList<String>> pickUps;
	private HashMap<Direction, ArrayList<String>> dropOffs;
	
	//Constructor
	public ElevatorImpl(String id) {
		
		//Create necessary data structures
		this.createRidersArray();
		this.createPickUpsMap();
		this.createDropOffsMap();
		
		
		//Set initial variable values
		this.setId(id);
		this.setDirection(enumerators.Direction.IDLE);
		this.setCurrentFloor(1);
		this.setDoorStatus(false);
		
	}
	
	////////////////////////////////////////////////////////////
	//														//
	//     Private Setter methods called by constructor      //
	//														//
	///////////////////////////////////////////////////////////
	
	private void createRidersArray() {
		this.riders = new ArrayList<RiderInterface>();
	}
	
	private void createPickUpsMap() {
		this.pickUps = new HashMap<Direction, ArrayList<String>>();
	}
	
	private void createDropOffsMap() {
		this.dropOffs = new HashMap<Direction, ArrayList<String>>();
	}
	
	private void setId(String id) {
		this.id = id;
	}
	
	
	/////////////////////////////
	//						   //
	//     Public methods      //
	//						  //
	////////////////////////////
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setCurrentFloor(int floor) {
		this.currentFloor = floor;
	}
	
	public void setDoorStatus(boolean status) {
		this.doorOpen = status;
	}
	
	public void moveUp() {
		this.setCurrentFloor(this.getCurrentFloor() + 1);
	}
	
	public void moveDown() {
		this.setCurrentFloor(this.getCurrentFloor() - 1);
	}
	
	public void addRider(String riderId) {
		this.riderIds.add(riderId);
	}
	
	public void removeRider(String riderId) {
		this.riderIds.remove(riderId);
	}
	
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public boolean getDoorOpenStatus() {
		return this.doorOpen;
	}
	
	public void openDoors() {
		this.doorOpen = true;
	}
	
	public void closeDoors() {
		this.doorOpen = false;
	}
	
	public String getId() {
		return this.id;
	}
	
	
	public ArrayList<String> getRiderIds() {
		return this.riderIds;
	}
	
	public ArrayList<Integer> getPickUps() {
		return this.pickUps;
	}
	
	public ArrayList<Integer> getDropOffs() {
		//TODO
		return this.dropOffs;
	}
	
	
		
}

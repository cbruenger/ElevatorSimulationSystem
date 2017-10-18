package Elevator;

import java.util.ArrayList;
import java.util.HashMap;
import Rider.Rider;
import Interfaces.ElevatorInterface;
import Interfaces.RiderInterface;
import enumerators.Direction;
import Building.Building;

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
	
	///////////////////////////////
	//							//
	//     Private methods      //
	//						   //
	/////////////////////////////
	
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
	
	private void removeRider(Rider rider) {
		//Check if rider exists in riders and throw error if not
		this.riders.remove(rider);
		rider.exitElevator();
		//Tell building to decommission riders? May be multiple riders so send in ArrayList?
	}
	
	private void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	private void setCurrentFloor(int floor) {
		this.currentFloor = floor;
	}
	
	private void setDoorStatus(boolean status) {
		this.doorOpen = status;
	}
	
	private void moveUp() {
		this.setCurrentFloor(this.getCurrentFloor() + 1);
	}
	
	private void moveDown() {
		this.setCurrentFloor(this.getCurrentFloor() - 1);
	}
	
	
	
	
	
	
	
	
	public void addRider(Rider rider) {
		//Check if rider is already in elevator and throw error if needed
		//Then add
		this.riders.add(rider);
	}
	
	
	
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	private boolean getDoorOpenStatus() {
		return this.doorOpen;
	}
	
	private void openDoors() {
		this.doorOpen = true;
	}
	
	private void closeDoors() {
		this.doorOpen = false;
	}
	
	public String getId() {
		return this.id;
	}
	
	
	public ArrayList<String> getRiderIds() {
		ArrayList<String> ids = new ArrayList<String>();
		for (RiderInterface rider : this.riders)
			ids.add(rider.getId());
		return ids;
	}
	
	public ArrayList<Integer> getPickUps() {
		return this.pickUps;
	}
	
	public ArrayList<Integer> getDropOffs() {
		//TODO
		return this.dropOffs;
	}
	
	
		
}

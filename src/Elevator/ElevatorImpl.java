package Elevator;

import java.util.ArrayList;
import java.util.HashMap;

import Rider.Rider;
import enumerators.Direction;
import Interfaces.ElevatorInterface;
import Interfaces.RiderInterface;
import Building.Building;

/*////////////////////////////////////////
 * 										*
 * 				Notes TODO				*
 * 										*
 *////////////////////////////////////////
 
//  need to check for basement and top floor...
//  maybe a priority queue for pick_ups/drop_offs

public class ElevatorImpl implements ElevatorInterface{
	
	//Change id to an int representing floor number to avoid bs?
	private int elevatorNumber;
	private Direction direction;
	private Direction pendingDirection;
	private int currentFloor;
	private boolean doorOpen;
	private ArrayList<RiderInterface> riders;
	
	// HashMap of directions to floor #/IDs
	// Change HashMap to ArrayList because they need be able to be sorted and direction can be
	// retrieved through the getDirection() function?
	private HashMap<Direction, ArrayList<Integer>> pickUps;
	private HashMap<Direction, ArrayList<Integer>> dropOffs;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public ElevatorImpl(int elevatorNumber) {
		
		//Create necessary data structures
		this.createRidersArray();
		this.createPickUpsMap();
		this.createDropOffsMap();
		
		//Set initial variable values
		this.setElevatorNumber(elevatorNumber);
		this.setDirection(Direction.IDLE);
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
		this.pickUps = new HashMap<Direction, ArrayList<Integer>>();
	}
	
	private void createDropOffsMap() {
		this.dropOffs = new HashMap<Direction, ArrayList<Integer>>();
	}
	
	private void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}
	
	private void removeRider(RiderInterface rider) {
		//Check if rider exists in riders and throw error if not
		this.riders.remove(rider);
		rider.exitElevator();
		//Tell building to decommission riders? May be multiple riders so send in ArrayList?
	}
	
	private void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	private void setCurrentFloor(int floor) {
		// TODO error handling
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
	
	private boolean getDoorOpenStatus() {
		return this.doorOpen;
	}
	
	private void openDoors() {
		// TODO error handling
		this.doorOpen = true;
	}
	
	private void closeDoors() {
		// TODO error handling
		this.doorOpen = false;
	}
	
	private void move(long time) {
		
		if (this.getDirection() != Direction.IDLE) {
			
			
			
		}
//		if (this.getDirection() != Rider.Direction.IDLE) {
//			//determine how quickly to move...
//			//determine move UP
//			//or determine move DOWN
//			
//			//Once full time for movement has occured print out statement of elevator moving to floor X
//		}
		
	}
	
	private void processFloor() {
		//Check if riders need to exit
		//Tell building to decommission riders that exit
		//Remove exited riders from riders array
		//Check if this floor is also a pickup floor
		//Tell building to notify the floor if this is a pickup floor
		//If riders enter, add to riders array
	}
	
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////
	
	@Override
	public int getElevatorNumber() {
		return this.elevatorNumber;
	}
	
	@Override
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	@Override
	public Direction getDirection() {
		return this.direction;
	}
	
	/*
	 * Maybe instead of returning pickups and drop-offs just 
	 * return the current direction, destination, and whether its
	 * an up or down request?
	 */
	@Override
	public HashMap<Direction, ArrayList<Integer>> getPickUps() {
		return this.pickUps;
	}
	
	@Override
	public HashMap<Direction, ArrayList<Integer>> getDropOffs() {
		return this.dropOffs;
	}
	
	//Temporary for testing in our main
	@Override
	public ArrayList<String> getRiderIds() {
		ArrayList<String> ids = new ArrayList<String>();
		for (RiderInterface rider : this.riders)
			ids.add(rider.getId());
		return ids;
	}
	
	@Override
	public void update(long time) {
		this.move(time);
		//checkFloorStops();
			//will check if floorstops are empty... will make call in checkFloorStops to ReEvaluate if necessary...
		//checkRiderInElevator();
			//will check riders in elevator...will call to process floor if necessary and reEvalute...
		
	}
	
	@Override
	public void addRider(RiderInterface rider) {
		// TODO error handling
		this.riders.add(rider);
	}
	
	@Override
	public void addPickupRequest(Direction direction, int floor) {
		// TODO error handling
		//See lecture at 1hr 39min
		//this.pickUps.put(direction, floor);
	}

}

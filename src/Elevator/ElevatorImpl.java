package Elevator;

import java.util.ArrayList;
import java.util.HashMap;
import TimeProcessor.TimeProcessor;
import DataStore.DataStore;
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
	// retrieved through the getDirection() function???
	private HashMap<Direction, ArrayList<Integer>> pickUps;
	
	//  we don't really use drop offs
	private HashMap<Direction, ArrayList<Integer>> dropOffs;
	//
	
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
		this.setPendingDirection(Direction.IDLE);
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
	
	
	private void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	private void setPendingDirection(Direction direction ) {
		this.pendingDirection = direction;
	}
	
	private void setCurrentFloor(int floor) {
		// TODO error handling
		this.currentFloor = floor;
	}
	
	private void setDoorStatus(boolean status) {
		this.doorOpen = status;
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
		///TODO: throw errors if trying to go beyond top/bottom floor
		
		/*First check if the elevator is at a floor where it needs to stop. 
		 * If needs to stop, don't move. 
		 * Figure out where to handle changing the direction to the pending direction. 
		 * And probably setting the pending direction to idle once it has swapped over to the current direction
		 * */
		
		if (this.direction != Direction.IDLE) {
			double distancePerTravelSpeed = time/DataStore.getInstance().getSpeed();
			//Move it up if not on top floor!
			if (this.direction == Direction.UP) {
				if (this.currentFloor < DataStore.getInstance().getNumFloors())
					this.currentFloor += distancePerTravelSpeed;
				else 
					//Remove this print statement and replace with error
					System.out.println("Cannot go above Floor " + DataStore.getInstance().getNumFloors());
			}
			// Move it down if not on 1st floor!
			else {
				if (this.currentFloor > 1)
					this.currentFloor -= distancePerTravelSpeed;
				else
					//Remove this print statement and replace with error
					System.out.println("Cannot go below Floor 1");
			}
			
			//Check if at a floor
			if (this.currentFloor % 1.0 == 0) {
				//Print statement
			}
			else {
				//No print statement... maybe say - in movement...
			}
		}
	}
	
	private void removeRiders() {
		// TODO error handling. Check if rider exists in riders and throw error if not
		
		ArrayList<RiderInterface> exitingRiders = new ArrayList<RiderInterface>();
		for (RiderInterface rider : this.riders) {
			if (rider.getDestinationFloor() == this.currentFloor) {
				exitingRiders.add(rider);
				this.riders.remove(rider);
				rider.exitElevator();
				
				//Print info
				System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " has left Elevator " + this.elevatorNumber + " [Riders:");
				for (RiderInterface currentRider : this.riders) {
					System.out.print(" " + currentRider.getId());
				}
				System.out.print("]\n");
			}
		}
		Building.getInstance().decommissionRiders(exitingRiders);
	}
	
	private void pickUpRiders() {
		ArrayList<RiderInterface> incomingRiders = Building.getInstance().getWaitersFromFloor(this.currentFloor, this.direction);
		this.addRiders(incomingRiders);
		this.addNewRidersRequests(incomingRiders);
	}
	
	//Adds newly picked up rider's floor requests for when a rider enters and pushes a destination floor button
	private void addNewRidersRequests(ArrayList<RiderInterface> newRiders) {
		//TODO error handling?
		for (RiderInterface newRider : newRiders) {
			if (this.getDropOffs().get(newRider.getDirection()) == null) {
				ArrayList<Integer> dropOffList = new ArrayList<Integer>();
				dropOffList.add(newRider.getDestinationFloor());
				this.getDropOffs().put(newRider.getDirection(), dropOffList);
			} else {
				this.getDropOffs().get(newRider.getDirection()).add(newRider.getDestinationFloor());
			}
			
			//Print info
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Rider Request made for Floor " + newRider.getDestinationFloor() + " [Current Floor Requests:");
			for (int i : this.pickUps.get(this.direction)) {
				System.out.print(" " + i);
			}
			System.out.print("][Current Rider Requests:");
			for (int i : this.dropOffs.get(this.direction)) {
				System.out.print(" " + i);
			}
			System.out.print("]\n");
		}
	}
	
	@Override
	public void update(long time) {
		this.move(time);
		//need to drop/pickup off person
		if (!dropOffs.isEmpty() && !pickUps.isEmpty()) {
			//If we're at a floor, call process
			if (this.getCurrentFloor() % 1 == 0) {
				processFloor();
			}
			reevaluateDirection();
		}
	}
	
	private void processFloor() {
		this.openDoors();
		this.removeRiders();
		this.pickUpRiders();
		//this.reevaluateDirection();
		this.closeDoors();
	}
	
	private void reevaluateDirection() {
		// Did i finish a pending request?
		if (this.pendingDirection == Direction.DOWN) {
			if (currentFloor == pickUps.get(Direction.DOWN).get(0)) {
				this.pendingDirection = Direction.IDLE;
				this.direction = Direction.DOWN;
				//remove pickup since they are now picked up.  PRESUMES WE ADD TO BACK OF LIST
				pickUps.get(this.getDirection()).remove(0);
			}
		}
		if (this.pendingDirection == Direction.UP) {
			if (currentFloor == pickUps.get(Direction.UP).get(0)) {
				this.pendingDirection = Direction.IDLE;
				this.direction = Direction.UP;
				//remove pickup since they are now picked up.  PRESUMES WE ADD TO BACK OF LIST
				this.pickUps.get(this.getDirection()).remove(0);
			}
		}
		
		//Are there any riders in the car at the moment?
		if (this.riders.isEmpty()) {
			//Any pick up requests?
			if (this.pickUps.isEmpty()) {
				this.direction = Direction.IDLE;
			}
			// No riders, but we do have pickUps, then now we set elevator en route 
			else {
				
				//how to determine which direction to look into??
				//random logic right now... I choose DOWN requests first...
				
				if (!this.pickUps.get(Direction.DOWN).isEmpty()) { 
					this.pendingDirection = Direction.DOWN;
					if (this.currentFloor - this.pickUps.get(Direction.DOWN).get(0) >= 0) this.direction = Direction.DOWN;
					else this.direction = Direction.UP;
				}
				
				if (!this.pickUps.get(Direction.UP).isEmpty()) {
					this.pendingDirection = Direction.UP;
					if (this.currentFloor - this.pickUps.get(Direction.DOWN).get(0) >= 0) this.direction = Direction.DOWN;
					else this.direction = Direction.UP;
				}
				
			}
		}
		
		//  Any riders in the car are headed the same direction (presumption...), therefore we stay on the same direction...
		
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
	
	@Override
	public Direction getPendingDirection() {
		return this.pendingDirection;
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
	public void addRiders(ArrayList<RiderInterface> incomingRiders) {
		// TODO error handling
		
		//Add each incoming rider to elevator and call the enterElevator function on each rider
		for (RiderInterface rider : incomingRiders) {
			this.riders.add(rider);
			rider.enterElevator();
			
			//Print info
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " entered Elevator " + this.elevatorNumber + " [Riders:");
			for (RiderInterface currentRider : this.riders) {
				System.out.print(" " + currentRider.getId());
			}
			System.out.print("]\n");
		}
	}
	
	@Override
	public void addPickupRequest(Direction direction, int floor) {
		// TODO error handling
		
		/*If the direction doesn't exist in the pickups Array, create a list for the
		 * direction being requested, and add the direction with the list containing
		 * the given floor into the array.
		 * Otherwise, just add the floor to the direction's list in the array 
		 */
		
		if (this.pickUps.get(direction) == null) {
			ArrayList<Integer> floorsList = new ArrayList<Integer>();
			floorsList.add(floor);
			this.pickUps.put(direction, floorsList);
		} else {
			this.pickUps.get(direction).add(floor);
		}
	}

}

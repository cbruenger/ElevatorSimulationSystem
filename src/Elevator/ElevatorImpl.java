package Elevator;

import java.util.ArrayList;
import java.util.HashMap;
import TimeProcessor.TimeProcessor;
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
	// retrieved through the getDirection() function???
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
		
		if (this.direction == Direction.IDLE) {
			
			return;
			
		}
//		if (this.getDirection() != Rider.Direction.IDLE) {
//			//determine how quickly to move...
//			//determine move UP
//			//or determine move DOWN
//			
//			//Once full time for movement has occured print out statement of elevator moving to floor X
//		}
		
	}
	
	//Removes a number of riders from the elevator
	//Tells building to decommission them
	//Tells rider to calculate ride time
	//Prints info
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
	}
	
	private void processFloor() {
		
		this.openDoors();
		this.removeRiders();
		this.pickUpRiders();
		this.reevaluateDirection();
		this.closeDoors();
	}
	
	private void reevaluateDirection() {
		
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

package elevator;

import java.util.ArrayList;
import java.util.HashMap;

import building.Building;
import dataStore.DataStore;
import enumerators.MyDirection;
import errors.*;
import gui.ElevatorDisplay;
import gui.ElevatorDisplay.Direction;
import interfaces.ElevatorInterface;
import interfaces.RiderInterface;
import timeProcessor.TimeProcessor;

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
	private MyDirection direction;
	private MyDirection pendingDirection;
	private int currentFloor;
	private boolean doorOpen;
	private boolean returningTo1;
	private ArrayList<RiderInterface> riders;
	
	// HashMap of directions to floor #/IDs
	// Change HashMap to ArrayList because they need be able to be sorted and direction can be
	// retrieved through the getDirection() function???
	private HashMap<MyDirection, ArrayList<Integer>> pickUps;
	
	//  we don't really use drop offs
	private HashMap<MyDirection, ArrayList<Integer>> dropOffs;
	//
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public ElevatorImpl(int elevatorNumber) {
		
		try {
			//Create necessary data structures
			this.createRidersArray();
			this.createPickUpsMap();
			this.createDropOffsMap();
			
			//Set initial variable values
			this.setElevatorNumber(elevatorNumber);
			this.setDirection(MyDirection.IDLE);
			this.setPendingDirection(MyDirection.IDLE);
			this.setCurrentFloor(1);
			
			this.doorOpen = false;
			this.returningTo1 = false;
			
		} catch (AlreadyExistsException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (InvalidArgumentException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
		
		
	}
	
	///////////////////////////////
	//							//
	//     Private methods      //
	//						   //
	/////////////////////////////
	
	private void createRidersArray() throws AlreadyExistsException {
		if (this.riders != null) {
			throw new AlreadyExistsException("The riders ArrayList has already been created in ElevatorImpl\n");
		}
		this.riders = new ArrayList<RiderInterface>();
	}
	
	private void createPickUpsMap() throws AlreadyExistsException {
		if (this.pickUps != null) {
			throw new AlreadyExistsException("The pickUps HashMap has already been created in ElevatorImpl\n");
		}
		this.pickUps = new HashMap<MyDirection, ArrayList<Integer>>();
		ArrayList<Integer> pickUpsUpList = new ArrayList<Integer>();
		ArrayList<Integer> pickUpsDownList = new ArrayList<Integer>();
		ArrayList<Integer> pickUpsIdleList = new ArrayList<Integer>();
		this.pickUps.put(MyDirection.UP, pickUpsUpList);
		this.pickUps.put(MyDirection.DOWN, pickUpsDownList);
		this.pickUps.put(MyDirection.IDLE, pickUpsIdleList);
	}
	
	private void createDropOffsMap() throws AlreadyExistsException {
		if (this.dropOffs != null) {
			throw new AlreadyExistsException("The dropOffs HashMap has already been created in ElevatorImpl\n");
		}
		this.dropOffs = new HashMap<MyDirection, ArrayList<Integer>>();
		ArrayList<Integer> dropOffsUpList = new ArrayList<Integer>();
		ArrayList<Integer> dropOffsDownList = new ArrayList<Integer>();
		ArrayList<Integer> dropOffsIdleList = new ArrayList<Integer>();
		this.dropOffs.put(MyDirection.UP, dropOffsUpList);
		this.dropOffs.put(MyDirection.DOWN, dropOffsDownList);
		this.dropOffs.put(MyDirection.IDLE, dropOffsIdleList);
	}
	
	private void setElevatorNumber(int elevatorNumber) throws InvalidArgumentException {
		if (elevatorNumber < 1) {
			throw new InvalidArgumentException("ElevatorImpl can't accept a number less than 1 during elevatorNumber assignment\n");
		}
		this.elevatorNumber = elevatorNumber;
	}
	
	
	private void setDirection(MyDirection direction) throws InvalidArgumentException {
		if (direction == null) {
			throw new InvalidArgumentException("ElevatorImpl can't accept null during direction assignment\n");
		}
		this.direction = direction;
	}
	
	private void setPendingDirection(MyDirection direction ) throws InvalidArgumentException {
		if (pendingDirection == null) {
			throw new InvalidArgumentException("ElevatorImpl can't accept null during pendingDirection assignment\n");
		}
		this.pendingDirection = direction;
	}
	
	private void setCurrentFloor(int floor) throws InvalidArgumentException {
		
		try {
			
			int numFloors = this.getNumFloors();
			if (floor < 1 || floor > numFloors) {
				throw new InvalidArgumentException("ElevatorImpl cannot accept a number less than 1 or greater than " + numFloors + "\n");
			}
			this.currentFloor = floor;

		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private boolean getDoorOpenStatus() {
		return this.doorOpen;
	}
	
	private void openDoors() throws AlreadyExistsException {
		if (this.doorOpen) {
			throw new AlreadyExistsException("ElevatorImpl's door status is already open when openDoors method called\n");
		}
		this.doorOpen = true;
		ElevatorDisplay.getInstance().openDoors(this.elevatorNumber);
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " opens doors\n");
	}
	
	private void closeDoors() throws AlreadyExistsException {
		if (!this.doorOpen) {
			throw new AlreadyExistsException("ElevatorImpl's door status is already closed when closeDoors method called\n");
		}
		this.doorOpen = false;
		ElevatorDisplay.getInstance().closeDoors(this.elevatorNumber);
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " closes doors\n");
		//System.out.println("ELEVATORS CURRENT DIRECTION AFTER DOORS CLOSE is " + this.direction);
		//System.out.println("ELEVATORS PENDING DIRECTION AFTER DOORS CLOSE is " + this.pendingDirection);
	}
			
	private int getNumFloors() throws BadInputDataException {
		
		try { 
			int numFloors = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (numFloors > 1)
				return numFloors;
			else
				throw new BadInputDataException("ElevatorImpl received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("ElevatorImpl could not parse DataStore's numFloors value to int during currentFloor assignment check\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("ElevatorImpl received null from DataStore for numFloors value during currentFloor assignment check\n"); 
	    }
		
	}
	
	private long getSpeed() throws BadInputDataException {
		
		try {
			long speed = Integer.parseInt(DataStore.getInstance().getSpeed()) * 1000;
			if (speed > 0)
				return speed;
			else
				throw new BadInputDataException("ElevatorImpl received a value less than 1 for elevatorSpeed from DataStore\n");

		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("ElevatorImpl could not parse DataStore's elevatorSpeed value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("ElevatorImpl received null from DataStore for elevatorSpeed value\n"); 
	    }
		
	}
	
	private long getIdleTime() throws BadInputDataException {
		
		try {
			long idleTime = Integer.parseInt(DataStore.getInstance().getIdleTime()) * 1000;
			if (idleTime >= 0)
				return idleTime;
			else
				throw new BadInputDataException("ElevatorImpl cannot accept a negative value for idleTime from DataStore\n");

		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("ElevatorImpl could not parse DataStore's idleTime value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("ElevatorImpl received null from DataStore for idleTime value\n"); 
	    }
		
	}
	
	private void move(long time) throws InvalidArgumentException, ElevatorOutOfBoundsException {
		
		if (time < 0) {
			throw new InvalidArgumentException("ElevatorImpl's move method cannot accept negative value for time parameter\n");
		}
		
		try {
			
			long speed = this.getSpeed();
			int numFloors = this.getNumFloors();
			
			if (this.direction != MyDirection.IDLE) {
				double distancePerTravelSpeed = time/speed;
				//Move it up if not on top floor!
				if (this.direction == MyDirection.UP) {
					if (this.currentFloor < numFloors) {
						this.currentFloor += distancePerTravelSpeed;
						
						double previousFloor;
						double nextFloor;
						
						if (this.currentFloor % 1.0 == 0) {
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.UP);
							previousFloor = this.currentFloor - 1;
							nextFloor = this.currentFloor;
						} else {
							previousFloor = Math.floor(this.currentFloor);
							nextFloor = Math.ceil(this.currentFloor);
						}
						
						System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving from Floor " + (previousFloor) + " to Floor " + nextFloor + " [Current Floor Requests:");
						for (int i : this.pickUps.get(this.direction)) {
							System.out.print(" " + i);
						}
						System.out.print("][Current Rider Requests:");
						for (int i : this.dropOffs.get(this.direction)) {
							System.out.print(" " + i);
						}
						System.out.print("]\n");
						 				
					} else {
						throw new ElevatorOutOfBoundsException("Elevator cannot go above floor " + numFloors + "\n");	
					}	
				
				// Move it down if not on 1st floor!
				} else {
					if (this.currentFloor > 1) {
						this.currentFloor -= distancePerTravelSpeed;
						
						double previousFloor;
						double nextFloor;
						
						if (this.currentFloor % 1.0 == 0) {
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.DOWN);
							previousFloor = this.currentFloor + 1;
							nextFloor = this.currentFloor;
						} else {
							previousFloor = Math.ceil(this.currentFloor);
							nextFloor = Math.floor(this.currentFloor);
						}
						
						System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving from Floor " + (previousFloor) + " to Floor " + nextFloor + " [Current Floor Requests:");
						for (int i : this.pickUps.get(this.direction)) {
							System.out.print(" " + i);
						}
						System.out.print("][Current Rider Requests:");
						for (int i : this.dropOffs.get(this.direction)) {
							System.out.print(" " + i);
						}
						System.out.print("]\n");
		
					} else {						
						throw new ElevatorOutOfBoundsException("Elevator cannot go below floor 1\n");	
					}
				}
			}
			
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	
	private void removeRiders() {
		
		if (this.riders == null || this.riders.isEmpty()) 
			return;
		
		ArrayList<RiderInterface> exitingRiders = new ArrayList<>(riders);
		ArrayList<RiderInterface> ridersToDelete = new ArrayList<>();
		for (RiderInterface rider : riders) {
			if (rider.getDestinationFloor() == this.currentFloor) {
				ridersToDelete.add(rider);
				rider.exitElevator();
			}
			else exitingRiders.remove(rider);
		}
		this.riders.removeAll(ridersToDelete);
			
		for (RiderInterface exitingRider : exitingRiders) {
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + exitingRider.getId() + " has left Elevator " + this.elevatorNumber + " [Riders:");
			for (RiderInterface currentRider : this.riders) {
				System.out.print(" " + currentRider.getId());
			}
			System.out.print("]\n");
		}
		
		try {
			if (this.direction == MyDirection.DOWN) ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.DOWN);
			else ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.UP);
			Building.getInstance().decommissionRiders(exitingRiders);
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
		}
	}
	
	
	private void pickUpRiders() {
		
		try {
			
			ArrayList<RiderInterface> incomingRiders = Building.getInstance().getWaitersFromFloor(this.currentFloor, this.direction);
			this.addRiders(incomingRiders);
			this.addNewRidersRequests(incomingRiders);
			
		} catch (InvalidArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	
	
	
	
	@Override
	public void update(long time) {
		
		try {
			
			this.move(time);
			//If we're at a floor, call process
			
			//if (this.pickUps.get(this.pendingDirection) == null || this.pickUps.get(this.pendingDirection).isEmpty())
			
			if ((this.getCurrentFloor() % 1 == 0) 
					&& ((this.pickUps.get(this.pendingDirection).contains(this.currentFloor)) 
							|| (this.dropOffs.get(this.pendingDirection).contains(this.currentFloor))
							|| (this.pickUps.get(this.direction).contains(this.currentFloor)) 
							|| (this.dropOffs.get(this.direction).contains(this.currentFloor)))) { 
				checkDropOffs();
				checkPickUps();
				this.direction = this.pendingDirection;
				this.pendingDirection = this.direction;	
				processFloor();
			}
			reevaluateDirection();
			
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (ElevatorOutOfBoundsException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	private void processFloor() {
		
		try {
			
			this.openDoors();
			this.pickUpRiders();
			this.removeRiders();
			
			//close doors needs to happen X seconds later... 
			this.closeDoors();
			
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}
		
	}
	
	public void checkPickUps() {
		//Check if this stop is for pickup
		if (this.pickUps.get(this.pendingDirection).contains(this.currentFloor)) {
			
			//Print info
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " has arrived at " + this.currentFloor + " for Floor Request [Current Floor Requests:");
			for (int i : this.pickUps.get(this.direction)) {
				System.out.print(" " + i);
			}
			System.out.print("][Current Rider Requests:");
			for (int i : this.dropOffs.get(this.direction)) {
				System.out.print(" " + i);
			}
			System.out.print("]\n");
			
			//Delete this floor from pickups
			this.pickUps.get(this.pendingDirection).remove(new Integer((int) this.currentFloor));
		}
	}
	
	public void checkDropOffs() {
		//Check if this stop is for a dropoff
		if (this.dropOffs.get(this.pendingDirection).contains(this.currentFloor)) {
			
			//Print info
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " has arrived at " + this.currentFloor + " for Rider Request [Current Floor Requests:");
			for (int i : this.pickUps.get(this.direction)) {
				System.out.print(" " + i);
			}
			System.out.print("][Current Rider Requests:");
			for (int i : this.dropOffs.get(this.direction)) {
				System.out.print(" " + i);
			}
			System.out.print("]\n");
			
			//Delete this from drop offs
			this.dropOffs.get(this.pendingDirection).remove(new Integer((int) this.currentFloor));
		}
	} 
	
	private void reevaluateDirection() {	
		if (this.pendingDirection != MyDirection.IDLE) {
			if(this.riders.isEmpty() && this.pickUps.get(MyDirection.UP).isEmpty() && this.pickUps.get(MyDirection.DOWN).isEmpty() && this.dropOffs.get(MyDirection.UP).isEmpty() && this.dropOffs.get(MyDirection.DOWN).isEmpty()) {
				this.pendingDirection = MyDirection.IDLE;
				this.direction = this.pendingDirection;
				return;
			}
			// I have just gotten to pickup floor and now I will update my direction to drop off the new pickup (assumes no conflict)
			//REVERSED THE NEXT TWO CONDITIONALS.. they might have short circuited each other/ pickups would preclude drop offs i guess?
			if (this.dropOffs.get(this.direction).isEmpty()) {
				if (this.pickUps.get(this.direction).isEmpty()) {
					this.direction = this.pendingDirection;
					this.pendingDirection = MyDirection.IDLE;
				}
				// I added this to update for more people who are waiting for pick up request...
				///
				if (!this.pickUps.get(MyDirection.DOWN).isEmpty()) {
					//System.out.println("I AM CHECKING A NEW PICK UP for down");
					if (this.currentFloor - this.pickUps.get(MyDirection.DOWN).get(0) > 0) this.direction = MyDirection.DOWN;
					else this.direction = MyDirection.UP;	
					this.pendingDirection = MyDirection.DOWN;	
				}
				if (!this.pickUps.get(MyDirection.UP).isEmpty()) {
					//System.out.println("I AM CHECKING A NEW PICK UP for up");
					if (this.currentFloor - this.pickUps.get(MyDirection.UP).get(0) > 0) this.direction = MyDirection.DOWN;
					else this.direction = MyDirection.UP;	
					this.pendingDirection = MyDirection.UP;
				}
				///
			}
			
			//I currently don't have a pickup request
		} else {
			//If there are NO dropOffs in the direction I am moving
			if (this.dropOffs.get(this.direction).isEmpty()) {
				//returned to floor 1 check (base case)
				if (this.currentFloor == 1) {
					this.direction = MyDirection.IDLE;
					ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.IDLE);
					if (this.returningTo1) {
						this.returningTo1 = false;
					}
				} 
				/// I am not on floor 1 yet
				else {
					
					try {
						
						//Set to idle and update idleTime for the first time
						if (!this.returningTo1) {
							this.direction = MyDirection.IDLE;
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.IDLE);
							TimeProcessor.getInstance().updateIdleTime(this.elevatorNumber);
						} 
						// For all other checks - check if idle time reaches IdleTime
						long idleTime = this.getIdleTime();
						if (TimeProcessor.getInstance().getIdleTime(this.elevatorNumber) <= idleTime) {
							TimeProcessor.getInstance().updateIdleTime(this.elevatorNumber);
						} else {
							TimeProcessor.getInstance().resetIdleTime(this.elevatorNumber);
							this.direction = MyDirection.DOWN;
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.DOWN);
							this.returningTo1 = true;
						}
						
					} catch (BadInputDataException e1) {
						System.out.println(e1.getMessage());
						e1.printStackTrace();
						System.exit(-1);
					} catch (InvalidArgumentException e2) {
						System.out.println(e2.getMessage());
						e2.printStackTrace();
						System.exit(-1);
					}
					
					
				} 
			} 
		}
		//System.out.println("ELEVATORS CURRENT DIRECTION AFTER REEVALUTE is " + this.direction);
		//System.out.println("ELEVATORS PENDING DIRECTION AFTER REEVALUTE is " + this.pendingDirection);
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
	public MyDirection getDirection() {
		return this.direction;
	}
	
	@Override
	public MyDirection getPendingDirection() {
		return this.pendingDirection;
	}
	
	/*
	 * Maybe instead of returning pickups and drop-offs just 
	 * return the current direction, destination, and whether its
	 * an up or down request?
	 */
	@Override
	public HashMap<MyDirection, ArrayList<Integer>> getPickUps() {
		return this.pickUps;
	}
	
	@Override
	public HashMap<MyDirection, ArrayList<Integer>> getDropOffs() {
		return this.dropOffs;
	}
	
	//Temporary for testing in our main
//	@Override
//	public ArrayList<String> getRiderIds() {
//		ArrayList<String> ids = new ArrayList<String>();
//		for (RiderInterface rider : this.riders)
//			ids.add(rider.getId());
//		return ids;
//	}
	
	
	@Override
	public void addRiders(ArrayList<RiderInterface> incomingRiders) {
		// TODO error handling
		
		//Add each incoming rider to elevator and call the enterElevator function on each rider
		for (RiderInterface rider : incomingRiders) {
			this.riders.add(rider);
			rider.enterElevator();
			
			//this.addDropOffRequest(rider.getDirection() ,rider.getDestinationFloor());
			
			//Print info
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " entered Elevator " + this.elevatorNumber + " [Riders:");
			for (RiderInterface currentRider : this.riders) {
				System.out.print(" " + currentRider.getId());
			}
			System.out.print("]\n");
		}
	}
	

	
	@Override
	public void addPickupRequest(MyDirection direction, int floor) {
		// TODO error handling
		
		/*If the direction doesn't exist in the pickups Array, create a list for the
		 * direction being requested, and add the direction with the list containing
		 * the given floor into the array.
		 * Otherwise, just add the floor to the direction's list in the array 
		 */
		//changed this...
		if (this.pendingDirection == MyDirection.IDLE) this.pendingDirection = direction;
		this.pickUps.get(direction).add(floor);
		
		
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " is going to Floor " + floor + " for " + direction + " request  [Current Floor Requests:");
		
		for (int i : this.pickUps.get(this.direction)) {
			System.out.print(" " + i);
		}
		System.out.print("][Current Rider Requests:");
		for (int i : this.dropOffs.get(this.direction)) {
			System.out.print(" " + i);
		}
		System.out.print("]\n");
	}
	
	//Adds newly picked up rider's floor requests for when a rider enters and pushes a destination floor button
	private void addNewRidersRequests(ArrayList<RiderInterface> newRiders) {
		//TODO error handling?
		for (RiderInterface newRider : newRiders) {
			
			if (this.dropOffs.get(newRider.getDirection()) == null) {
				ArrayList<Integer> dropOffList = new ArrayList<Integer>();
				dropOffList.add(newRider.getDestinationFloor());
				this.dropOffs.put(newRider.getDirection(), dropOffList);
			} else {
				this.getDropOffs().get(newRider.getDirection()).add(newRider.getDestinationFloor());
			}
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Request made for Floor " + newRider.getDestinationFloor() + " [Current Floor Requests:");
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

}
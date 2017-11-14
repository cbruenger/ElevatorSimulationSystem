package elevator;

import java.util.ArrayList;
import java.util.HashMap;
import static enumerators.MyDirection.*;
import building.Building;
import dataStore.DataStore;
import enumerators.MyDirection;
import errors.*;
import gui.ElevatorDisplay;
import gui.ElevatorDisplay.Direction;
import interfaces.ElevatorInterface;
import interfaces.RiderInterface;
import timeProcessor.TimeProcessor;

public class ElevatorImpl implements ElevatorInterface{
	
	private int elevatorNumber;
	private MyDirection direction;
	private MyDirection pendingDirection;
	private int currentFloor;
	private int maxCapacity;
	private boolean doorOpen;
	private boolean returningTo1;
	private ArrayList<RiderInterface> riders;
	
	// HashMaps containing directions as keys and floor numbers as values
	private HashMap<MyDirection, ArrayList<Integer>> pickUps;
	private HashMap<MyDirection, ArrayList<Integer>> dropOffs;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	public ElevatorImpl(int elevatorNumber) {
		
		try {
			//Create necessary data structures
			this.createRidersArray();
			this.createPickUpsMap();
			this.createDropOffsMap();
			
			//Set initial variable values
			this.setElevatorNumber(elevatorNumber);
			this.setDirection(IDLE);
			this.setPendingDirection(IDLE);
			this.setCurrentFloor(1);
			this.setMaxCapacity();
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
		} catch (BadInputDataException e3) {
			System.out.println(e3.getMessage());
			e3.printStackTrace();
			System.exit(-1);
		}
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Interface Methods 			*
	 * 										*
	 *////////////////////////////////////////
	
	@Override
	public void update(long sleepTime) throws InvalidArgumentException {
		if (sleepTime <= 0) 
			throw new InvalidArgumentException("ElevatorImpl's update method cannot accept number less than or equal to 0 for time arg\n");
		try {
			long doorsOpenTimeRequired = getDoorsOpenTime();
			if (!this.doorOpen) {
				this.move(sleepTime);
				if ((this.currentFloor % 1 == 0) 
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
			} else if (TimeProcessor.getInstance().getDoorOpenTime(this.elevatorNumber) < doorsOpenTimeRequired) {
				TimeProcessor.getInstance().updateDoorOpenTime(this.elevatorNumber);
			} else if (TimeProcessor.getInstance().getDoorOpenTime(this.elevatorNumber) >= doorsOpenTimeRequired) {
				TimeProcessor.getInstance().resetDoorOpenTime(this.elevatorNumber);
				this.closeDoors();
			}
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (BadInputDataException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		} catch (AlreadyExistsException e3) {
			System.out.println(e3.getMessage());
			e3.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Override
	public void addPickupRequest(MyDirection direction, int floor) throws InvalidArgumentException {
		if (direction == null) {
			throw new InvalidArgumentException("ElevatorImpl's addPickupRequest method cannot accept null for direction\n");
		}
		try {
			int numFloors = this.getNumFloors();
			if (floor < 1 || floor > numFloors) {
				throw new InvalidArgumentException("ElevatorImpl's addPickupRequest method cannot accept floor number less than 1 or greater than " + numFloors + "\n");
			}
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
		if (this.pendingDirection == IDLE) {
			this.pendingDirection = direction;
		}

		if (!this.pickUps.get(direction).contains(floor)) {
			this.pickUps.get(direction).add(floor);
		}
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " going to Floor " + floor + " for " + direction + " request  ");
		this.printRequests();
	}
	
	@Override
	public ElevatorDTO getDTO() throws UnexpectedNullException {
		if (this.pickUps == null) {
			throw new UnexpectedNullException("ElevatorImpl's pickUps HashMap is null when trying to merge UP/DOWN for DTO\n");
		}
		if (this.pickUps.get(UP) == null) {
			throw new UnexpectedNullException("ElevatorImpl's pickUps UP ArrayList is null when trying to merge UP/DOWN for DTO\n");
		}
		if (this.pickUps.get(DOWN) == null) {
			throw new UnexpectedNullException("ElevatorImpl's pickUps DOWN ArrayList is null when trying to merge UP/DOWN for DTO\n");
		}
		try {
			ArrayList<Integer> mergedDropOffs = this.getMergedDropOffs();
			ArrayList<Integer> upPickUpsCopy = new ArrayList<Integer>(this.pickUps.get(UP));
			ArrayList<Integer> downPickUpsCopy = new ArrayList<Integer>(this.pickUps.get(DOWN));
			ElevatorDTO DTO = new ElevatorDTO(this.elevatorNumber, this.currentFloor, this.direction, this.pendingDirection, upPickUpsCopy, downPickUpsCopy, mergedDropOffs);
			return DTO;
		} catch (UnexpectedNullException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	/*////////////////////////////////////////////////////////
	 * 														*
	 * 		Setters and Data Structure Creating Methods 		*
	 * 		called by the Constructor						*
	 * 														*
	 *////////////////////////////////////////////////////////
	
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
		this.pickUps.put(UP, pickUpsUpList);
		this.pickUps.put(DOWN, pickUpsDownList);
		this.pickUps.put(IDLE, pickUpsIdleList);
	}
	
	private void createDropOffsMap() throws AlreadyExistsException {
		if (this.dropOffs != null) {
			throw new AlreadyExistsException("The dropOffs HashMap has already been created in ElevatorImpl\n");
		}
		this.dropOffs = new HashMap<MyDirection, ArrayList<Integer>>();
		ArrayList<Integer> dropOffsUpList = new ArrayList<Integer>();
		ArrayList<Integer> dropOffsDownList = new ArrayList<Integer>();
		ArrayList<Integer> dropOffsIdleList = new ArrayList<Integer>();
		this.dropOffs.put(UP, dropOffsUpList);
		this.dropOffs.put(DOWN, dropOffsDownList);
		this.dropOffs.put(IDLE, dropOffsIdleList);
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
		if (direction == null) {
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
	
	private void setMaxCapacity() throws BadInputDataException {
		try {
			int maxCapacityTemp = Integer.parseInt(DataStore.getInstance().getElevatorCapacity());
			if (maxCapacityTemp < 1) {
				throw new BadInputDataException("ElevatorImpl received value less than 1 from DataStore during maxCapacity assignment\n");
			}
			this.maxCapacity = maxCapacityTemp;
		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("ElevatorImpl could not parse DataStore's elevatorCapacity value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("ElevatorImpl received null from DataStore for elevatorCapacity value\n"); 
	    }
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		   Door Handling Methods			*
	 * 										*
	 *////////////////////////////////////////
	
	private void openDoors() throws AlreadyExistsException {
		if (this.doorOpen) {
			throw new AlreadyExistsException("ElevatorImpl's door status is already open when openDoors method called\n");
		}
		this.doorOpen = true;
		ElevatorDisplay.getInstance().openDoors(this.elevatorNumber);
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Doors Open\n");
	}
	
	private void closeDoors() throws AlreadyExistsException {
		if (!this.doorOpen) {
			throw new AlreadyExistsException("ElevatorImpl's door status is already closed when closeDoors method called\n");
		}
		this.doorOpen = false;
		ElevatorDisplay.getInstance().closeDoors(this.elevatorNumber);
		switch (this.direction) {
        case UP:  ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.UP);
            break;
        case DOWN: ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.DOWN);
        	break;
        default: ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.IDLE);
        	break;
		}
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Doors Close\n");
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		   Rider Handling Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	private void addRiders(ArrayList<RiderInterface> incomingRiders) throws InvalidArgumentException {
		if (incomingRiders == null) {
			throw new InvalidArgumentException("ElevatorImpl's addRiders method cannot accept null for incomingRiders arg\n");
		}
		
		//Add each incoming rider to elevator and call the enterElevator function on each rider
		for (RiderInterface rider : incomingRiders) {
			this.riders.add(rider);
			rider.enterElevator();
						
			//Print info
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " entered Elevator " + this.elevatorNumber + " ");
			this.printRiders();		
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
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + exitingRider.getId() + " has left Elevator " + this.elevatorNumber + " ");
			this.printRiders();
		}
		
		try {
			if (this.direction == DOWN) ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.DOWN);
			else ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.UP);
			Building.getInstance().decommissionRiders(exitingRiders);
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
		}
	}
	
	private void pickUpRiders() throws UnexpectedNullException, BeyondElevatorCapacityException {
		
		try {
			
			ArrayList<RiderInterface> incomingRiders = Building.getInstance().getWaitersFromFloor(this.currentFloor, this.direction, this.maxCapacity - this.riders.size());
			if (incomingRiders == null) {
				throw new UnexpectedNullException("ElevatorImpl received null when asking building for floor waiters\n");
			}
			if (incomingRiders.size() > this.maxCapacity - this.riders.size()) {
				throw new BeyondElevatorCapacityException("ElevatorImpl cannot receive " + incomingRiders.size() + " riders with maxCapacity " + this.maxCapacity + " and current capacity " + this.riders.size());
			}
			this.addRiders(incomingRiders);
			this.addNewRidersRequests(incomingRiders);
			
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(1);
		} catch (BadInputDataException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(1);
		} catch (UnexpectedNullException e3) {
			System.out.println(e3.getMessage());
			e3.printStackTrace();
			System.exit(1);
		}
	}
	
	//Adds rider's floor requests when a rider enters and pushes a destination floor button
	private void addNewRidersRequests(ArrayList<RiderInterface> newRiders) throws InvalidArgumentException {
		if (newRiders == null) {
			throw new InvalidArgumentException("ElevatorImpl's addNewRidersRequests method cannot accept null as newRiders arg\n");
		}
		for (RiderInterface newRider : newRiders) {
			
			if (this.dropOffs.get(newRider.getDirection()) == null) {
				ArrayList<Integer> dropOffList = new ArrayList<Integer>();
				dropOffList.add(newRider.getDestinationFloor());
				this.dropOffs.put(newRider.getDirection(), dropOffList);
			} else {
				if (!this.dropOffs.get(newRider.getDirection()).contains(newRider.getDestinationFloor()))
					this.dropOffs.get(newRider.getDirection()).add(newRider.getDestinationFloor());
			}
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Rider Request made for Floor " + newRider.getDestinationFloor() + " ");
			this.printRequests();
		}
	}

	/*////////////////////////////////////////////////////
	 * 													*
	 *		Movement and	 floor processing Methods		*
	 * 													*
	 *////////////////////////////////////////////////////
	
	
	private void move(long time) throws InvalidArgumentException {
		
		if (time < 0) {
			throw new InvalidArgumentException("ElevatorImpl's move method cannot accept negative value for time parameter\n");
		}
		
		try {
			
			long speed = this.getSpeed();
			int numFloors = this.getNumFloors();
			
			if (this.direction != IDLE) {
				
				//Move it up if not on top floor!
				if (this.direction == UP) {
					if (this.currentFloor < numFloors) {
						
						TimeProcessor.getInstance().updateFloorMovementTime(this.elevatorNumber);
						if (TimeProcessor.getInstance().getFloorMovementTime(this.elevatorNumber) >= speed) {
							System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving UP from Floor " + (this.currentFloor) + " to Floor " + (this.currentFloor+1) + " ");
							this.printRequests();
							this.currentFloor += 1;
							TimeProcessor.getInstance().resetFloorMovementTime(this.elevatorNumber);
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.UP);
							return;
						}
						System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving UP from Floor " + (this.currentFloor) + " to Floor " + (this.currentFloor+1) + " ");
						this.printRequests();
					}	
				
				// Move it down if not on 1st floor!
				} else {
					if (this.currentFloor > 1) {
						
						TimeProcessor.getInstance().updateFloorMovementTime(this.elevatorNumber);

						if (TimeProcessor.getInstance().getFloorMovementTime(this.elevatorNumber) >= speed) {
							System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving DOWN from Floor " + (this.currentFloor) + " to Floor " + (this.currentFloor-1) + " ");
							this.printRequests();
							this.currentFloor -= 1;
							TimeProcessor.getInstance().resetFloorMovementTime(this.elevatorNumber);
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.DOWN);
							return;
						}
						
						System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving DOWN from Floor " + (this.currentFloor) + " to Floor " + (this.currentFloor-1) + " ");
						this.printRequests();
					}
				}
			}
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void checkDropOffs() {
		if (this.dropOffs.get(this.pendingDirection).contains(this.currentFloor)) {
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " has arrived at " + this.currentFloor + " for Rider Request ");
			this.printRequests();
		}
	}
	
	public void checkPickUps() {
		if (this.pickUps.get(this.pendingDirection).contains(this.currentFloor)) {
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " has arrived at Floor " + this.currentFloor + " for Floor Request ");
			this.printRequests();
		}
	}
	
	private void removeFloorFromDropOffs() {
		this.dropOffs.get(this.pendingDirection).remove(new Integer((int) this.currentFloor));
	}
	
	private void removeFloorFromPickUps() {
		this.pickUps.get(this.pendingDirection).remove(new Integer((int) this.currentFloor));
	}
	
	private void processFloor() {
		
		try {
			if ((this.dropOffs.get(UP).contains(this.currentFloor) || this.dropOffs.get(DOWN).contains(this.currentFloor))
					|| (this.pickUps.get(this.direction).contains(this.currentFloor) 
					&& (this.pendingDirection == IDLE || this.pendingDirection == this.direction)))
			{
				this.openDoors();
				TimeProcessor.getInstance().updateDoorOpenTime(this.elevatorNumber);
				this.removeRiders();
				this.removeFloorFromDropOffs();
				this.removeFloorFromPickUps();
				this.pickUpRiders();
			}
		} catch (AlreadyExistsException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (UnexpectedNullException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		} catch (BeyondElevatorCapacityException e3) {
			System.out.println(e3.getMessage());
			e3.printStackTrace();
			System.exit(-1);
		} catch (InvalidArgumentException e4) {
			System.out.println(e4.getMessage());
			e4.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void reevaluateDirection() {	
		if (this.pendingDirection != IDLE) {
			if(this.riders.isEmpty() && this.pickUps.get(UP).isEmpty() && this.pickUps.get(DOWN).isEmpty() && this.dropOffs.get(UP).isEmpty() && this.dropOffs.get(DOWN).isEmpty()) {
				this.pendingDirection = IDLE;
				this.direction = this.pendingDirection;
				return;
			}
			// I have just gotten to pickup floor and now I will update my direction to drop off the new pickup (assumes no conflict)
			//REVERSED THE NEXT TWO CONDITIONALS.. they might have short circuited each other/ pickups would preclude drop offs i guess?
			if (this.dropOffs.get(this.direction).isEmpty()) {
				if (this.pickUps.get(this.direction).isEmpty()) {
					this.direction = this.pendingDirection;
					this.pendingDirection = IDLE;
				}
				// I added this to update for more people who are waiting for pick up request...
				///
				if (!this.pickUps.get(DOWN).isEmpty()) {
					//System.out.println("I AM CHECKING A NEW PICK UP for down");
					if (this.currentFloor - this.pickUps.get(DOWN).get(0) > 0) this.direction = DOWN;
					else this.direction = UP;	
					this.pendingDirection = DOWN;
					return;
				}
				if (!this.pickUps.get(UP).isEmpty()) {
					if (this.currentFloor - this.pickUps.get(UP).get(0) > 0) this.direction = DOWN;
					else this.direction = UP;	
					this.pendingDirection = UP;
					return;
				}
			}
			
			//I currently don't have a pickup request
		} else {
			//If there are NO dropOffs in the direction I am moving
			if (this.dropOffs.get(this.direction).isEmpty()) {
				//returned to floor 1 check (base case)
				if (this.currentFloor == 1) {
					this.direction = IDLE;
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
							this.direction = IDLE;
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.riders.size(), Direction.IDLE);
							TimeProcessor.getInstance().updateIdleTime(this.elevatorNumber);
						} 
						// For all other checks - check if idle time reaches IdleTime
						long idleTime = this.getIdleTime();
						if (TimeProcessor.getInstance().getIdleTime(this.elevatorNumber) <= idleTime) {
							TimeProcessor.getInstance().updateIdleTime(this.elevatorNumber);
						} else {
							TimeProcessor.getInstance().resetIdleTime(this.elevatorNumber);
							this.direction = DOWN;
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
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		   Printing Helper Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	private void printRequests() {
		System.out.print("[Current Floor Requests:");
		if (this.pickUps.get(UP).isEmpty() && this.pickUps.get(DOWN).isEmpty())
			System.out.print(" None");
		else {
			for (int i : this.pickUps.get(UP))
				System.out.print(" " + i);
			for (int i : this.pickUps.get(DOWN))
				System.out.print(" " + i);
		}
		System.out.print("][Current Rider Requests:");
		if (this.dropOffs.get(UP).isEmpty() && this.dropOffs.get(DOWN).isEmpty())
			System.out.print(" None");
		else {
			for (int i : this.dropOffs.get(UP))
				System.out.print(" " + i);
			for (int i : this.dropOffs.get(DOWN))
				System.out.print(" " + i);
		}
		System.out.print("]\n");
	}
	
	private void printRiders() {
		System.out.print("[Riders:");
		if (this.riders.isEmpty())
			System.out.print(" None");
		else {
			for (RiderInterface currentRider : this.riders)
				System.out.print(" " + currentRider.getId());
		}
		System.out.print("]\n");
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		DTO Builder Helper Method		*
	 * 										*
	 *////////////////////////////////////////
	
	private ArrayList<Integer> getMergedDropOffs() throws UnexpectedNullException {
		if (this.dropOffs == null)
			throw new UnexpectedNullException("ElevatorImpl's dropOffs HashMap is null when trying to merge UP/DOWN for DTO\n");
		if (this.dropOffs.get(UP) == null)
			throw new UnexpectedNullException("ElevatorImpl's dropOffs UP ArrayList is null when trying to merge UP/DOWN for DTO\n");
		if (this.dropOffs.get(DOWN) == null)
			throw new UnexpectedNullException("ElevatorImpl's dropOffs DOWN ArrayList is null when trying to merge UP/DOWN for DTO\n");
		ArrayList<Integer> dropOffsTotalCopy = new ArrayList<Integer>(this.dropOffs.get(UP));
		ArrayList<Integer> dropOffsDownCopy = new ArrayList<Integer>(this.dropOffs.get(DOWN));
		dropOffsTotalCopy.addAll(dropOffsDownCopy);
		return dropOffsTotalCopy;
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		DataStore Retrieval Methods		*
	 * 										*
	 *////////////////////////////////////////
	
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
	
	private long getDoorsOpenTime() throws BadInputDataException {
		try {
			long doorsOpenTime = Integer.parseInt(DataStore.getInstance().getDoorsOpenTime()) * 1000;
			if (doorsOpenTime >= 0)
				return doorsOpenTime;
			else
				throw new BadInputDataException("ElevatorImpl cannot accept a negative value for doorsOpenTime from DataStore\n");

		} catch (NumberFormatException e) { 
	        throw new BadInputDataException("ElevatorImpl could not parse DataStore's doorsOpenTime value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("ElevatorImpl received null from DataStore for doorsOpenTime value\n"); 
	    }
	}
}

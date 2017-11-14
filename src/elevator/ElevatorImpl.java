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
import interfaces.PersonInterface;
import timeProcessor.TimeProcessor;

/* The ElevatorImpl class is an implementation of an Elevator that is
 * created by the building. Responds to elevator requests at floors
 *  assigned by the controller and forwarded by the building. Responds 
 *  to requests made by people inside the elevator. Contains methods for
 * updating which is called by the building during every wake cycle. 
 * Contains methods for processing floors upon arrival for requests, which
 * drops people off and picks people up, and for re-evaluating the direction
 * and pending direction according to its state. Contains a DTO builder to 
 * send the building which forwards it to the controller upon request.
 */
public class ElevatorImpl implements ElevatorInterface{
	
	//Class variables and data structures
	private int elevatorNumber;
	private MyDirection direction;
	private MyDirection pendingDirection;
	private int currentFloor;
	private int maxCapacity;
	private boolean doorOpen;
	private boolean returningTo1;
	private ArrayList<PersonInterface> people;
	private HashMap<MyDirection, ArrayList<Integer>> pickUps;
	private HashMap<MyDirection, ArrayList<Integer>> dropOffs;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	//Constructor, initializes necessary components
	public ElevatorImpl(int elevatorNumber) {
		try {
			//Create necessary data structures
			this.createPeopleArrayList();
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
	
	/* Called by the building each wake cycle when activity can be updated.
	 * Handles door opening/closing, activities when a floor is reached such
	 * as dropOffs, pickUps, floor processing and direction evaluation.
	 */
	@Override
	public void update() {
		try {
			
			//If the doors are closed
			long doorsOpenTimeRequired = getDoorsOpenTime();
			if (!this.doorOpen) {
				
				//Move the elevator
				this.move();
				
				//If we've reached a floor where a pickUp or dropOff needs to take place
				if ((this.currentFloor % 1 == 0) 
						&& ((this.pickUps.get(this.pendingDirection).contains(this.currentFloor)) 
								|| (this.dropOffs.get(this.pendingDirection).contains(this.currentFloor))
								|| (this.pickUps.get(this.direction).contains(this.currentFloor)) 
								|| (this.dropOffs.get(this.direction).contains(this.currentFloor)))) { 
					
					//Process the floor
					checkDropOffs();
					checkPickUps();
					this.direction = this.pendingDirection;
					this.pendingDirection = this.direction;	
					processFloor();
				}
				
				//Evaluate the current/pending directions
				reevaluateDirection();
			
			//If the doors need to stay open longer, update the time they've been open
			} else if (TimeProcessor.getInstance().getDoorOpenTime(this.elevatorNumber) < doorsOpenTimeRequired) {
				TimeProcessor.getInstance().updateDoorOpenTime(this.elevatorNumber);
				
			//Otherwise, close the doors and reset the time they've been open to 0
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
	
	/* Called by the elevator controller when a pickUp request has been made at a given floor for a
	 * given direction. Adds the request to pickUps HashMap if it doesn't already contain the same
	 * request. Prints narration of elevator floor request assignment.
	 */
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
	
	/* Called by the building when the elevator controller needs to assign a new elevator request.
	 * Contains all necessary data for elevator controller decision making.
	 */
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
	
	//Creates the ArrayList for holding people
	private void createPeopleArrayList() throws AlreadyExistsException {
		if (this.people != null) {
			throw new AlreadyExistsException("The people ArrayList has already been created in ElevatorImpl\n");
		}
		this.people = new ArrayList<PersonInterface>();
	}
	
	//Creates a HashMap to contain the pickUps with keys of UP, DOWN, and IDLE which are mapped to Integer ArrayLists
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
	
	//Creates a HashMap to contain the dropOffs with keys of UP, DOWN, and IDLE which are mapped to Integer ArrayLists
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
	
	//Assigns elevatorNumber variable
	private void setElevatorNumber(int elevatorNumber) throws InvalidArgumentException {
		if (elevatorNumber < 1) {
			throw new InvalidArgumentException("ElevatorImpl can't accept a number less than 1 during elevatorNumber assignment\n");
		}
		this.elevatorNumber = elevatorNumber;
	}
	
	//Assigns direction variable
	private void setDirection(MyDirection direction) throws InvalidArgumentException {
		if (direction == null) {
			throw new InvalidArgumentException("ElevatorImpl can't accept null during direction assignment\n");
		}
		this.direction = direction;
	}
	
	//Assigns pendingDirection variable
	private void setPendingDirection(MyDirection direction ) throws InvalidArgumentException {
		if (direction == null) {
			throw new InvalidArgumentException("ElevatorImpl can't accept null during pendingDirection assignment\n");
		}
		this.pendingDirection = direction;
	}
	
	//Assigns currentFloor variable
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
	
	//Assigns maxCapacity variable
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
	
	//Re-assigns doorOpen variable to true, prints narration of event
	private void openDoors() throws AlreadyExistsException {
		if (this.doorOpen) {
			throw new AlreadyExistsException("ElevatorImpl's door status is already open when openDoors method called\n");
		}
		this.doorOpen = true;
		ElevatorDisplay.getInstance().openDoors(this.elevatorNumber);
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Doors Open\n");
	}
	
	//Re-assigns doorOpen variable to false, prints narration of event
	private void closeDoors() throws AlreadyExistsException {
		if (!this.doorOpen) {
			throw new AlreadyExistsException("ElevatorImpl's door status is already closed when closeDoors method called\n");
		}
		this.doorOpen = false;
		ElevatorDisplay.getInstance().closeDoors(this.elevatorNumber);
		switch (this.direction) {
        case UP:  ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.UP);
            break;
        case DOWN: ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.DOWN);
        		break;
        default: ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.IDLE);
        		break;
		}
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Doors Close\n");
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		   People Handling Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	/* Called by the pickUpPeople method when people are transferring from a floor into the elevator.
	 * The arg is an ArrayList of people. Prints narration of events.
	 */
	private void addPeople(ArrayList<PersonInterface> incomingPeople) throws InvalidArgumentException {
		if (incomingPeople == null) {
			throw new InvalidArgumentException("ElevatorImpl's addPeople method cannot accept null for incomingPeople arg\n");
		}
		
		//Add each incoming person to elevator and call the enterElevator function on each person
		for (PersonInterface person : incomingPeople) {
			this.people.add(person);
			person.enterElevator();
						
			//Print info
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + person.getId() + " entered Elevator " + this.elevatorNumber + " ");
			this.printRiders();		
		}
	}
	
	/* Called by the process floor method to remove people if the current floor is their
	 * destination floor. Builds an ArrayList of exiting people, removes them from the 
	 * people ArrayList, and passes the ArrayList to the building for decommissioning.
	 */
	private void removePeople() {
		
		//Only continue past this check if there are people exiting
		if (this.people == null || this.people.isEmpty()) 
			return;
		
		//Build ArrayList of exiting people, using a temp ArrayList for deleting during iteration
		ArrayList<PersonInterface> exitingPeople = new ArrayList<>(this.people);
		ArrayList<PersonInterface> peopleToDelete = new ArrayList<>();
		for (PersonInterface person : this.people) {
			if (person.getDestinationFloor() == this.currentFloor) {
				peopleToDelete.add(person);
				person.exitElevator();
			}
			else exitingPeople.remove(person);
		}
		this.people.removeAll(peopleToDelete);
		
		//Print narration for each person exiting
		for (PersonInterface exitingPerson : exitingPeople) {
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + exitingPerson.getId() + " has left Elevator " + this.elevatorNumber + " ");
			this.printRiders();
		}
		
		try {
			//Update GUI and pass exiting people to building for decommissioning
			if (this.direction == DOWN) 
				ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.DOWN);
			else 
				ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.UP);
			Building.getInstance().decommissionPeople(exitingPeople);
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
		}
	}
	
	//Called by the processFloor method to pickUp people from the current floor
	private void pickUpPeople() throws UnexpectedNullException, BeyondElevatorCapacityException {
		try {
			ArrayList<PersonInterface> incomingPeople = Building.getInstance().getWaitersFromFloor(this.currentFloor, this.direction, this.maxCapacity - this.people.size());
			if (incomingPeople == null) {
				throw new UnexpectedNullException("ElevatorImpl received null when asking building for floor waiters\n");
			}
			if (incomingPeople.size() > this.maxCapacity - this.people.size()) {
				throw new BeyondElevatorCapacityException("ElevatorImpl cannot receive " + incomingPeople.size() + " people with maxCapacity " + this.maxCapacity + " and current capacity " + this.people.size());
			}
			this.addPeople(incomingPeople);
			this.addNewRidersRequests(incomingPeople);
			
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
	
	//Called by the pickUpPeople method for adding new riders' floor requests when people enter and push a destination floor button
	private void addNewRidersRequests(ArrayList<PersonInterface> newRiders) throws InvalidArgumentException {
		if (newRiders == null)
			throw new InvalidArgumentException("ElevatorImpl's addNewRidersRequests method cannot accept null as newRiders arg\n");
		for (PersonInterface rider : newRiders) {
			if (this.dropOffs.get(rider.getDirection()) == null) {
				ArrayList<Integer> dropOffList = new ArrayList<Integer>();
				dropOffList.add(rider.getDestinationFloor());
				this.dropOffs.put(rider.getDirection(), dropOffList);
			} else {
				if (!this.dropOffs.get(rider.getDirection()).contains(rider.getDestinationFloor()))
					this.dropOffs.get(rider.getDirection()).add(rider.getDestinationFloor());
			}
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " Rider Request made for Floor " + rider.getDestinationFloor() + " ");
			this.printRequests();
		}
	}

	/*////////////////////////////////////////////////////
	 * 													*
	 *		Movement and	 floor processing Methods		*
	 * 													*
	 *////////////////////////////////////////////////////
	
	/* Called by the update method when the doors are closed. moves elevator in 
	 * it's current direction. Stores the time it has been moving between 2 floors
	 *  in the TimeProcessor. When the floor speed time has been met, current floor
	 *  is updated. Prints narration of events.
	 */
	private void move() throws InvalidArgumentException {
		try {
			
			//Only continue past this check if the current direction is UP or DOWN
			if (this.direction != IDLE) {
				
				long speed = this.getSpeed();
				int numFloors = this.getNumFloors();
				
				//Check if going UP
				if (this.direction == UP) {
					
					//Check if not on top floor
					if (this.currentFloor < numFloors) {
						
						//Update the time the elevator has been moving between floors
						TimeProcessor.getInstance().updateFloorMovementTime(this.elevatorNumber);
						
						//If movement time has been met, print narration, update current floor, reset movement time to 0, update GUI and return
						if (TimeProcessor.getInstance().getFloorMovementTime(this.elevatorNumber) >= speed) {
							System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving UP from Floor " + (this.currentFloor) + " to Floor " + (this.currentFloor+1) + " ");
							this.printRequests();
							this.currentFloor += 1;
							TimeProcessor.getInstance().resetFloorMovementTime(this.elevatorNumber);
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.UP);
							return;
						}
						
						//Print narration
						System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving UP from Floor " + (this.currentFloor) + " to Floor " + (this.currentFloor+1) + " ");
						this.printRequests();
					}	
				
				//Otherwise, elevator is going DOWN
				} else {
					
					//Check if not on bottom floor
					if (this.currentFloor > 1) {
						
						//Update the time the elevator has been moving between floors
						TimeProcessor.getInstance().updateFloorMovementTime(this.elevatorNumber);

						//If movement time has been met, print narration, update current floor, reset movement time to 0, update GUI and return
						if (TimeProcessor.getInstance().getFloorMovementTime(this.elevatorNumber) >= speed) {
							System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " moving DOWN from Floor " + (this.currentFloor) + " to Floor " + (this.currentFloor-1) + " ");
							this.printRequests();
							this.currentFloor -= 1;
							TimeProcessor.getInstance().resetFloorMovementTime(this.elevatorNumber);
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.DOWN);
							return;
						}
						
						//Print narration
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
	
	//Called by update method. Prints narration if arrived at a floor for a dropOff
	public void checkDropOffs() {
		if (this.dropOffs.get(this.pendingDirection).contains(this.currentFloor)) {
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " has arrived at " + this.currentFloor + " for Rider Request ");
			this.printRequests();
		}
	}
	
	//Called by update method. Prints narration if arrived at a floor for a pickUp
	public void checkPickUps() {
		if (this.pickUps.get(this.pendingDirection).contains(this.currentFloor)) {
			System.out.print(TimeProcessor.getInstance().getTimeString() + "Elevator " + this.elevatorNumber + " has arrived at Floor " + this.currentFloor + " for Floor Request ");
			this.printRequests();
		}
	}
	
	//Called by processFloor method for removing the current floor from the dropOffs ArraryList
	private void removeFloorFromDropOffs() {
		this.dropOffs.get(this.pendingDirection).remove(new Integer((int) this.currentFloor));
	}
	
	//Called by processFloor method for removing the current floor from the pickUps ArraryList
	private void removeFloorFromPickUps() {
		this.pickUps.get(this.pendingDirection).remove(new Integer((int) this.currentFloor));
	}
	
	/* Called by update method when arriving at a floor for a pickUp or dropOff.
	 * Opens doors and updates the doors open time in the TimeProcessor. Calls methods
	 * to drop any people off and remove this floor from the dropOffs ArrayList. 
	 * Calls methods to pick any people up and remove this floor from the pickUps ArrayList
	 */
	private void processFloor() {
		try {
			/*Only process the floor if there is a dropOff or pickUp and if the pending direction is IDLE or
			 * if the current/pending directions are the same*/
			if ((this.dropOffs.get(UP).contains(this.currentFloor) || this.dropOffs.get(DOWN).contains(this.currentFloor))
					|| (this.pickUps.get(this.direction).contains(this.currentFloor) 
							&& (this.pendingDirection == IDLE || this.pendingDirection == this.direction))) {
				
				//Call functions for floor processing
				this.openDoors();
				TimeProcessor.getInstance().updateDoorOpenTime(this.elevatorNumber);
				this.removePeople();
				this.removeFloorFromDropOffs();
				this.removeFloorFromPickUps();
				this.pickUpPeople();
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
	
	//Called by the update method to evaluate the current/pending directions.
	private void reevaluateDirection() {	
		
		//If the elevator currently has a pending direction
		if (this.pendingDirection != IDLE) {
			
			//If there are no pickUps or dropOffs, set current/pending directions to idle and return
			if(this.people.isEmpty() && this.pickUps.get(UP).isEmpty() && this.pickUps.get(DOWN).isEmpty() && this.dropOffs.get(UP).isEmpty() && this.dropOffs.get(DOWN).isEmpty()) {
				this.pendingDirection = IDLE;
				this.direction = this.pendingDirection;
				return;
			}
			
			//If there are no dropOffs in this direction
			if (this.dropOffs.get(this.direction).isEmpty()) {
				
				/* If there are also no more pickUps in this direction, assign the current direction to the pending
				 * and the pending direction to IDLE */
				if (this.pickUps.get(this.direction).isEmpty()) {
					this.direction = this.pendingDirection;
					this.pendingDirection = IDLE;
				}
				
				//If there is a down pickUp
				if (!this.pickUps.get(DOWN).isEmpty()) {
					
					/* Set pending direction to DOWN and if the down pickUp is at a lower floor,
					 * assign direction to down. If it's at a higher floor, assign direction 
					 * to UP. Return.
					 */
					if (this.currentFloor - this.pickUps.get(DOWN).get(0) > 0) 
						this.direction = DOWN;
					else
						this.direction = UP;	
					this.pendingDirection = DOWN;
					return;
				}
				
				//If there is an up pickUP
				if (!this.pickUps.get(UP).isEmpty()) {
					
					/* Set pending direction to UP and if the down pickUp is at a lower floor,
					 * assign direction to down. If it's at a higher floor, assign direction 
					 * to UP. Return.
					 */
					if (this.currentFloor - this.pickUps.get(UP).get(0) > 0) 
						this.direction = DOWN;
					else 
						this.direction = UP;	
					this.pendingDirection = UP;
					return;
				}
			}
			
		//Otherwise, the pending direction is IDLE (there are no pickUps)
		} else {
			
			//If there are no remaining dropOffs in the direction I am moving
			if (this.dropOffs.get(this.direction).isEmpty()) {
				
				
				/* *****************************************************
				 * Returning to floor 1 sudo-recursion base case check *
				 * *****************************************************/
				
				//If current floor is 1, update GUI, set direction to IDLE, set returningTo1 boolean to false
				if (this.currentFloor == 1) {
					this.direction = IDLE;
					ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.IDLE);
					if (this.returningTo1) {
						this.returningTo1 = false;
					}
					
					
				/* *****************************************
				 * Journey back to floor 1 sudo-recursion  *
				 * *****************************************/
					
				//Elevator is not at floor 1	
				} else {
					
					try {
						
						/* If the elevator is not on its journey back to floor 1,
						 * set direction to idle, update GUI, update idleTime in 
						 * the TimeProcessor for the first time */
						if (!this.returningTo1) {
							this.direction = IDLE;
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.IDLE);
							TimeProcessor.getInstance().updateIdleTime(this.elevatorNumber);
						} 
						
						//Get idleTime requirement before returning to floor 1
						long idleTime = this.getIdleTime();
						
						//If need to remain IDLE longer, update the idleTime in TimeProcessor
						if (TimeProcessor.getInstance().getIdleTime(this.elevatorNumber) <= idleTime) {
							TimeProcessor.getInstance().updateIdleTime(this.elevatorNumber);
							
						/* Otherwise, required idleTime has been met, set direction to down,
						 * Update the GUI, set returningTo1 boolean to true */
						} else {
							TimeProcessor.getInstance().resetIdleTime(this.elevatorNumber);
							this.direction = DOWN;
							ElevatorDisplay.getInstance().updateElevator(this.elevatorNumber, this.currentFloor, this.people.size(), Direction.DOWN);
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
	
	/* Called when narration of events is needed and prints the list of current floor
	 * requests in all directions, and the current peole in the elevator.
	 */ 
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
	
	/* Called when narration of events is needed and prints the list of people
	 * currently inside the elevator.
	 */
	private void printRiders() {
		System.out.print("[Riders:");
		if (this.people.isEmpty())
			System.out.print(" None");
		else {
			for (PersonInterface currentRider : this.people)
				System.out.print(" " + currentRider.getId());
		}
		System.out.print("]\n");
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		DTO Builder Helper Method		*
	 * 										*
	 *////////////////////////////////////////
	
	/* Called by getDTO method. Builds and returns a single ArrayList consisting
	 * of the UP dropOffs and DOWN dropOffs merged into one ArrayList.
	 */
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
	
	//Accesses DataStore and parses the number of floors to an int, checks validity and returns it
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
	
	//Accesses DataStore and parses the elevator floor speed to an int, checks validity and returns it
	private long getSpeed() throws BadInputDataException {
		try {
			long speed = Integer.parseInt(DataStore.getInstance().getElevatorFloorSpeed()) * 1000;
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
	
	//Accesses DataStore and parses the required IDLE time to an int, checks validity and returns it
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
	
	//Accesses DataStore and parses the required doors open time to an int, checks validity and returns it
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

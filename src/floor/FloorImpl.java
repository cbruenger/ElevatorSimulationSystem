package floor;

import java.util.ArrayList;

import dataStore.DataStore;
import enumerators.MyDirection;

import static enumerators.MyDirection.*;
import errors.*;
import interfaces.FloorInterface;
import interfaces.PersonInterface;
import timeProcessor.TimeProcessor;
import interfaces.ButtonInterface;
import button.Button;

/* The FloorImpl class is an implementation of a floor that is
 * created by the building. Receives people from the time processor
 * and manages all person activity while on the floor. Contains 
 * buttons which are pushed when people make requests for elevators.
 * Passes People from the floor to the building which forwards to the
 * elevator. Contains people IDs who have exited on this floor for
 * printing Floor People purposes.
 */
public class FloorImpl implements FloorInterface{

	//Class variables
	private int floorNumber;
	private ButtonInterface upButton;
	private ButtonInterface downButton;
	private ArrayList<PersonInterface> peopleWaiting;
	private ArrayList<String> peopleDecommissionedIds;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	//Constructor, initializes necessary components
	public FloorImpl(int floorNumber) {
		try {
			this.setFloorNumber(floorNumber);			
			this.createUpButton();
			this.createDownButton();
			this.createPeopleWaitingArrayList();
			this.createPeopleDecommissionedArrayList();	
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		} catch (InvalidArgumentException e3) {
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
	
	//Returns the floor number
	@Override
	public int getFloorNumber() {
		return this.floorNumber;
	}
	
	/* Called by the building when it receives a person from the TimeProcessor. 
	 * Accepts a person, adds it to the floor and pushes the button corresponding to
	 * the person's direction for requesting an elevator.
	 */
	@Override
	public void addWaitingPerson(PersonInterface person) throws AlreadyExistsException {
		if (this.peopleWaiting.contains(person)) {
			throw new AlreadyExistsException("Person " + person.getId() + " is already on floor " + this.floorNumber);
		}
		try {
			System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + person.getId() + " created on Floor " + person.getStartFloor() + ", wants to go " + person.getDirection() + " to Floor " + person.getDestinationFloor());
			System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + person.getId() + " presses " + person.getDirection() + " on Floor " + person.getStartFloor());
			this.pushButton(person.getDirection());
			this.peopleWaiting.add(person);
		} catch (InvalidArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	/* Called by the building when an elevator arrives for a pickUp. Args represent
	 * the direction of the pickUp and the available capacity in the elevator. 
	 * If some people cannot fit in the elevator, they make another request.
	 * Returns an ArrayList of people who will enter the elevator.
	 */
	@Override
	public ArrayList<PersonInterface> getWaitersByDirection(MyDirection direction, int availableCapacity) throws InvalidArgumentException {
		try {
			int maxCapacity = this.getMaxElevatorCapacity();
			if (availableCapacity < 0 || availableCapacity > maxCapacity)
				throw new InvalidArgumentException("FloorImpl cannot accept number less than 0 or greater than " + maxCapacity + " for availableCapacity arg\n");
			
			//Find out how any people are going the same direction, maintain a separate list for people to remove from floor
			ArrayList<PersonInterface> peopleToDeleteFromFloor = new ArrayList<>();
			ArrayList<PersonInterface> peopleAvailableForTransfer = new ArrayList<>(this.peopleWaiting);
			for (PersonInterface person : this.peopleWaiting) {
				if (person.getDirection() == direction)
					peopleToDeleteFromFloor.add(person);
				else
					peopleAvailableForTransfer.remove(person);
			}
			
			//Find out how many people need to wait for another elevator if the capacity is met
			int availableToTransferSize = peopleAvailableForTransfer.size();
			int numToLeaveBehind = 0;
			if (availableToTransferSize > availableCapacity)
				numToLeaveBehind = availableToTransferSize - availableCapacity;

			//Remove the people who will wait for another elevator from the transfer/delete lists
			int indexToDelete = peopleAvailableForTransfer.size() - 1;
			boolean printCapacityNotification = false;
			for (int i = 0; i < numToLeaveBehind; i++) {
				peopleToDeleteFromFloor.remove(indexToDelete);
				peopleAvailableForTransfer.remove(indexToDelete);
				indexToDelete--;
				printCapacityNotification = true;
			}
			
			//Remove the people who will transfer from the peopleWaiting list
			peopleWaiting.removeAll(peopleToDeleteFromFloor);
			
			//If there are people needing to request another elevator, reset the button so they can push it
			if (!peopleAvailableForTransfer.isEmpty() || printCapacityNotification) {
				this.resetButton(direction);
			}
			
			//If people are needing to request again, print capacity notification and have people make new requests
			if (printCapacityNotification) {
				System.out.println(TimeProcessor.getInstance().getTimeString() + "Elevator at capacity, " + peopleAvailableForTransfer.size() + " person(s) will enter, " + numToLeaveBehind + " person(s) will request again");
				for (PersonInterface person : this.peopleWaiting) {
					if (person.getDirection() == direction) {
						System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + person.getId() + " presses " + person.getDirection() + " on Floor " + person.getStartFloor());
						this.pushButton(direction);
					}
				}
			}
			
			/*Since peopleAvailableForTransfer are in a list and have been deleted from the floor, create a temporary list
			 * so we have a list to iterate through and a separate list to delete from when printing the current Floor People
			 */
			ArrayList<String> transfersTemp = new ArrayList<String>();
			for (PersonInterface person : peopleAvailableForTransfer) {
				transfersTemp.add(person.getId());
			}
			for (PersonInterface personLeaving : peopleAvailableForTransfer) {
				System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + personLeaving.getId()  + " has left Floor " + this.floorNumber + " ");
				transfersTemp.remove(personLeaving.getId());
				this.printFloorPeople(transfersTemp);
			}
			
			//Return the people transferring into the elevator
			return peopleAvailableForTransfer;
			
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	/* Called by the building when a person exits an elevator. The person is
	 * stored in the building, but their ID is passed here as an arg for 
	 * Floor People printing purposes.
	 */
	@Override
	public void addPersonToDecommissionedList(String id) throws InvalidArgumentException, AlreadyExistsException {
		if (id == null || id.isEmpty())
			throw new InvalidArgumentException("FloorImpl's addPersonToDecommissionedList method cannot accept null or empty string for id arg\n");
		if (this.peopleDecommissionedIds.contains(id))
			throw new AlreadyExistsException("FloorImpl's peopleDecommissioned arrayList already contains id " + id + "\n");
		this.peopleDecommissionedIds.add(id);
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + id  + " entered Floor " + this.floorNumber + " ");
		this.printFloorPeople(null);
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Methods called by the Constructor		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Assigns the floorNumber variable
	private void setFloorNumber(int floorNumber) throws InvalidArgumentException {
		try {
			int numFloors = this.getNumFloors();
			if (floorNumber < 1 || floorNumber > numFloors) {
				throw new InvalidArgumentException("FloorImpl cannot accept a number less than 1 or greater than " + numFloors + "\n");
			}
			this.floorNumber = floorNumber;
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Creates and assigns a Button with UP direction and current floor number
	private void createUpButton() {
		this.upButton = new Button(UP, this.floorNumber);
	}
	
	//Creates and assigns a Button with DOWN direction and current floor number
	private void createDownButton() {
		this.downButton = new Button(DOWN, this.floorNumber);
	}
	
	//Creates an ArrayList to contain the people on the floor waiting for elevators
	private void createPeopleWaitingArrayList() throws AlreadyExistsException {
		if (this.peopleWaiting != null)
			throw new AlreadyExistsException("The peopleWaiting ArrayList has already been created in FloorImpl\n");
		this.peopleWaiting = new ArrayList<PersonInterface>();
	}
	
	//Creates an ArrayList to contain the people on the floor waiting for elevators
	private void createPeopleDecommissionedArrayList() throws AlreadyExistsException {
		if (this.peopleDecommissionedIds != null)
			throw new AlreadyExistsException("The peopleDecommissioned ArrayList has already been created in FloorImpl\n");
		this.peopleDecommissionedIds = new ArrayList<String>();
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		   Button Handling Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	//Called when a person makes a request for an elevator for a given direction
	private void pushButton(MyDirection direction) throws InvalidArgumentException {
		if (direction == null) {
			throw new InvalidArgumentException("FloorImpl's pushButton method cannot accept null for direction arg\n");
		}
		if (direction == UP) {
			this.upButton.push();
		} else if (direction == DOWN) {
			this.downButton.push();
		}
	}
	
	//Called when the elevator arrives to the floor to make the button able to be pushed for another request
	private void resetButton(MyDirection direction) throws InvalidArgumentException {
		if (direction == null || direction == IDLE) {
			throw new InvalidArgumentException("FloorImpl's resetButton cannot accept null or IDLE for direction arg\n");
		}
		try {
			if (direction == UP) {
				this.upButton.reset();
			} else if (direction == DOWN) {
				this.downButton.reset();
			}
		} catch (AlreadyExistsException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		   Printing Helper Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	/* Called when narration of events is needed and prints the list of people on the floor as well
	 * as the people in the transferringPeople arg
	 */
	private void printFloorPeople(ArrayList<String> transferringPeople) {
		System.out.print("[Floor People:");
		if (!this.peopleWaiting.isEmpty() || !this.peopleDecommissionedIds.isEmpty()) {
			for (PersonInterface personWaiting : this.peopleWaiting)
				System.out.print(" " + personWaiting.getId());
			for (String personDecommissioned : this.peopleDecommissionedIds)
				System.out.print(" " + personDecommissioned);
		} else {
			if (transferringPeople != null) {
				if (!transferringPeople.isEmpty()) {
					for (String personId : transferringPeople)
						System.out.print(" " + personId);
				} else {
					System.out.print(" None");
				}
			} else {
				System.out.print(" None");
			}
		}
		System.out.print("]\n");
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
				throw new BadInputDataException("FloorImpl received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("FloorImpl could not parse DataStore's numFloors value to int during floorNumber assignment check\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("FloorImpl received null from DataStore for numFloors value during floorNumber assignment check\n"); 
	    }
	}
	
	//Accesses DataStore and parses the elevator capacity to an int, checks validity and returns it
	private int getMaxElevatorCapacity() throws BadInputDataException {
		try { 
			int maxElevatorCapacity = Integer.parseInt(DataStore.getInstance().getElevatorCapacity()); 
			if (maxElevatorCapacity > 0)
				return maxElevatorCapacity;
			else
				throw new BadInputDataException("FloorImpl received a value less than 1 for maxElevatorCapacity from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("FloorImpl could not parse DataStore's maxElevatorCapacity value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("FloorImpl received null from DataStore for maxElevatorCapacity value\n"); 
	    }
	}
}

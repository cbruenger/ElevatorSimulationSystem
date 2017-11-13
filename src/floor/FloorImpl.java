package floor;

import java.util.ArrayList;

import dataStore.DataStore;
import enumerators.MyDirection;

import static enumerators.MyDirection.*;
import errors.*;
import interfaces.FloorInterface;
import interfaces.RiderInterface;
import timeProcessor.TimeProcessor;
import interfaces.ButtonInterface;
import button.Button;

public class FloorImpl implements FloorInterface{

	private int floorNumber;
	private ButtonInterface upButton;
	private ButtonInterface downButton;
	private ArrayList<RiderInterface> peopleWaiting;
	private ArrayList<String> peopleDecommissioned;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	public FloorImpl(int floorNumber) {
		try {
			this.setFloorNumber(floorNumber);			
			this.createUpButton();
			this.createDownButton();
			this.createPeopleWaitingArrayList();
			this.peopleDecommissionedArrayList();	
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
	
	
	
	/////////////////////////////
	//						  //
	//    Setters/Private     //
	//				  		 //
	///////////////////////////
	
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
	
	private void createUpButton() {
		this.upButton = new Button(UP, this.floorNumber);
	}
	
	private void createDownButton() {
		this.downButton = new Button(DOWN, this.floorNumber);
	}
	
	private void createPeopleWaitingArrayList() throws AlreadyExistsException {
		
		//Throw error if elevators have already been created
		if (this.peopleWaiting != null) {
			throw new AlreadyExistsException("The peopleWaiting ArrayList has already been created in FloorImpl\n");
		}
		
		this.peopleWaiting = new ArrayList<RiderInterface>();
	}
	
	private void peopleDecommissionedArrayList() throws AlreadyExistsException {
		
		//Throw error if elevators have already been created
		if (this.peopleDecommissioned != null) {
			throw new AlreadyExistsException("The peopleDecommissioned ArrayList has already been created in FloorImpl\n");
		}
		
		this.peopleDecommissioned = new ArrayList<String>();
	}
	
	
	
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
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////
	
	@Override
	public int getFloorNumber() {
		return this.floorNumber;
	}
	
	@Override
	public void addWaitingPerson(RiderInterface rider) throws AlreadyExistsException {
		if (this.peopleWaiting.contains(rider)) {
			throw new AlreadyExistsException("Rider " + rider.getId() + " is already on floor " + this.floorNumber);
		}
		try {
			System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " created on Floor " + rider.getStartFloor() + ", wants to go " + rider.getDirection() + " to Floor " + rider.getDestinationFloor());
			System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " presses " + rider.getDirection() + " on Floor " + rider.getStartFloor());
			this.pushButton(rider.getDirection());
			this.peopleWaiting.add(rider);
		} catch (InvalidArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	@Override
	public ArrayList<RiderInterface> getWaitersByDirection(MyDirection direction, int availableCapacity) throws InvalidArgumentException {
		try {
			int maxCapacity = this.getMaxElevatorCapacity();
			if (availableCapacity < 0 || availableCapacity > maxCapacity)
				throw new InvalidArgumentException("FloorImpl cannot accept number less than 0 or greater than " + maxCapacity + " for availableCapacity arg\n");
			
			//Find out how any people are going the same direction, maintain a separate list for people to remove from floor
			ArrayList<RiderInterface> peopleToDeleteFromFloor = new ArrayList<>();
			ArrayList<RiderInterface> peopleAvailableForTransfer = new ArrayList<>(this.peopleWaiting);
			for (RiderInterface rider: this.peopleWaiting) {
				if (rider.getDirection() == direction)
					peopleToDeleteFromFloor.add(rider);
				else
					peopleAvailableForTransfer.remove(rider);
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
			
			//If people are needing to request again, print capacity notification and have riders make new requests
			if (printCapacityNotification) {
				System.out.println(TimeProcessor.getInstance().getTimeString() + "Elevator at capacity, " + peopleAvailableForTransfer.size() + " person(s) will enter, " + numToLeaveBehind + " person(s) will request again");
				for (RiderInterface rider : this.peopleWaiting) {
					if (rider.getDirection() == direction) {
						System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " presses " + rider.getDirection() + " on Floor " + rider.getStartFloor());
						this.pushButton(direction);
					}
				}
			}
			
			/*Since peopleAvailableForTransfer are in a list and have been deleted from the floor, create a temporary list
			 * so we have a list to iterate through and a separate list to delete from when printing the current Floor People
			 */
			ArrayList<String> transfersTemp = new ArrayList<String>();
			for (RiderInterface person : peopleAvailableForTransfer) {
				transfersTemp.add(person.getId());
			}
			for (RiderInterface personLeaving : peopleAvailableForTransfer) {
				System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + personLeaving.getId()  + " has left Floor " + this.floorNumber + " ");
				transfersTemp.remove(personLeaving.getId());
				this.printFloorPeople(transfersTemp);
			}
			
			//Return the people transfering into the elevator
			return peopleAvailableForTransfer;
			
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	@Override
	public void addPersonToDecommissionedList(String id) throws InvalidArgumentException, AlreadyExistsException {
		if (id == null || id.isEmpty())
			throw new InvalidArgumentException("FloorImpl's addRiderToDecommissionedList method cannot accept null or empty string for id arg\n");
		if (this.peopleDecommissioned.contains(id))
			throw new AlreadyExistsException("FloorImpl's peopleDecommissioned arrayList already contains id " + id + "\n");
		this.peopleDecommissioned.add(id);
		System.out.print(TimeProcessor.getInstance().getTimeString() + "Person " + id  + " entered Floor " + this.floorNumber + " ");
		this.printFloorPeople(null);
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 		   Printing Helper Methods		*
	 * 										*
	 *////////////////////////////////////////
	
	private void printFloorPeople(ArrayList<String> transferringPeople) {
		System.out.print("[Floor People:");
		if (!this.peopleWaiting.isEmpty() || !this.peopleDecommissioned.isEmpty()) {
			for (RiderInterface personWaiting : this.peopleWaiting)
				System.out.print(" " + personWaiting.getId());
			for (String personDecommissioned : this.peopleDecommissioned)
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
}

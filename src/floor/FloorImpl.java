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
	private ArrayList<RiderInterface> riders;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public FloorImpl(int floorNumber) {
		
		try {
			
			//Set initial variable values
			this.setFloorNumber(floorNumber);
			
			//Create buttons
			this.createUpButton();
			this.createDownButton();
			
			//Create necessary data structures
			this.createRidersArrayList();
			
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
	
	private void createRidersArrayList() throws AlreadyExistsException {
		
		//Throw error if elevators have already been created
		if (this.riders != null) {
			throw new AlreadyExistsException("The riders ArrayList has already been created in FloorImpl\n");
		}
		
		this.riders = new ArrayList<RiderInterface>();
	}
	
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

//	//Only temporarily for testing in our main
//	@Override
//	public ArrayList<RiderInterface> getRiders() {
//		return this.riders;
//	}
	
	@Override
	public void addRider(RiderInterface rider) throws AlreadyExistsException {
		if (this.riders.contains(rider)) {
			throw new AlreadyExistsException("Rider " + rider.getId() + " is already on floor " + this.floorNumber);
		}
		try {
			System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " created on Floor " + rider.getStartFloor() + ", wants to go " + rider.getDirection() + " to Floor " + rider.getDestinationFloor());
			System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " pressed " + rider.getDirection() + " on Floor " + rider.getStartFloor());
			this.pushButton(rider.getDirection());
			this.riders.add(rider);
		} catch (InvalidArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	@Override
	public void removeRider(RiderInterface rider) throws CannotRemoveException {
		if (!this.riders.contains(rider)) {
			throw new CannotRemoveException("FloorImpl's removeRider method cannot remove rider " + rider.getId() + " from floor because the rider is not in rider ArrayList");
		}
		this.riders.remove(rider);
	}
	
	//Return a list of people who's direction is same as parameter
	//Also, delete them from floors list before returning
	@Override
	public ArrayList<RiderInterface> getRidersByDirection(MyDirection direction, int availableCapacity) throws InvalidArgumentException {
		//TODO break this up into separate methods
		try {
			int maxCapacity = this.getMaxElevatorCapacity();
			if (availableCapacity < 0 || availableCapacity > maxCapacity) {
				throw new InvalidArgumentException("FloorImpl cannot accept number less than 0 or greater than " + maxCapacity + " for availableCapacity arg\n");
			}
			//System.out.println("Available capacity pre-loop " + availableCapacity);
			ArrayList<RiderInterface> ridersToDelete = new ArrayList<>();
			ArrayList<RiderInterface> ridersToTransfer = new ArrayList<>(this.riders);
			for (RiderInterface rider: this.riders) {
				if (rider.getDirection() == direction) {
					ridersToDelete.add(rider);
				} else {
				ridersToTransfer.remove(rider);
				}
			}
			//if (ridersToDelete.size() <= availableCapacity && availableCapacity != 0) {
			int ridersToTransferSize = ridersToTransfer.size();
			int numToLeaveBehind = 0;
			if (ridersToTransferSize > availableCapacity) {
				numToLeaveBehind = ridersToTransferSize - availableCapacity;
			}
			int indexToDelete = ridersToTransfer.size() - 1;
			boolean printCapacityNotification = false;
			for (int i = 0; i < numToLeaveBehind; i++) {
				ridersToDelete.remove(indexToDelete);
				ridersToTransfer.remove(indexToDelete);
				indexToDelete--;
				printCapacityNotification = true;
			}
			riders.removeAll(ridersToDelete);
			if (!ridersToTransfer.isEmpty() || printCapacityNotification) {
				this.resetButton(direction);
			}
			if (printCapacityNotification) {
				System.out.println(TimeProcessor.getInstance().getTimeString() + "Elevator at capacity, " + ridersToTransfer.size() + " person(s) will enter, " + numToLeaveBehind + " person(s) will request again");
				for (RiderInterface rider : this.riders) {
					if (rider.getDirection() == direction) {
						System.out.println(TimeProcessor.getInstance().getTimeString() + "Person " + rider.getId() + " pressed " + rider.getDirection() + " on Floor " + rider.getStartFloor());
						this.pushButton(direction);
					}
				}
			}
			//
			//System.out.println("Riders to delete post-loop " + ridersToDelete.size());
			//System.out.println("Riders being returned for transfer " + ridersToTransfer.size());
			
			
			return ridersToTransfer;
		} catch (BadInputDataException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	
	
	public boolean waitersLeftBehind(int floorNum, MyDirection direction) throws InvalidArgumentException {
		if (floorNum != this.floorNumber) {
			throw new InvalidArgumentException("FloorImpl's waitersLeftBehind method cannot accept floorNum arg which is different than own\n");
		}
		if (direction == null) {
			throw new InvalidArgumentException("FloorImpl's waitersLeftBehind method cannot accept null for direction arg\n");
		}
		for (RiderInterface rider : this.riders) {
			if (rider.getDirection() == direction) {
				return true;
			}
		}
		return false;
	}

}

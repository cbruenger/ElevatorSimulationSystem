package elevator;

import java.util.ArrayList;

import dataStore.DataStore;
import enumerators.MyDirection;
import errors.BadInputDataException;
import errors.InvalidArgumentException;


public class ElevatorDTO {
	private int elevatorNumber;
	private int currentFloor;
	private MyDirection direction;
	private MyDirection pendingDirection;
	private ArrayList<Integer> upPickups;
	private ArrayList<Integer> downPickups;
	private ArrayList<Integer> dropOffs;
	
	public ElevatorDTO(int elevatorNumber, int currentFloor, MyDirection direction, MyDirection pendingDirection, ArrayList<Integer> upPickups, ArrayList<Integer> downPickups, ArrayList<Integer> dropOffs){
		try {
			this.setElevatorNumber(elevatorNumber); 
			this.setCurrentFloor(currentFloor);
			this.setDirection(direction);
			this.setPendingDirection(pendingDirection);
			this.setUpPickups(upPickups);
			this.setDownPickups(downPickups);
			this.setDropOffs(dropOffs);
		} catch(InvalidArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	private void setElevatorNumber(int elevatorNumber) throws InvalidArgumentException {
		try {
			int numElevators = this.getNumElevators();
			if (elevatorNumber < 1 || elevatorNumber > numElevators) {
				throw new InvalidArgumentException("ElevatorDTO cannot accept number less than 1 or greater than " + numElevators + " for elevatorNumber arg\n");
			} else {
				this.elevatorNumber = elevatorNumber;

			}
		} catch (BadInputDataException e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	private void setCurrentFloor(int currentFloor) throws InvalidArgumentException {
		try {
			int numFloors = this.getNumFloors();
			if (currentFloor < 1 || currentFloor > numFloors) {
				throw new InvalidArgumentException("ElevatorDTO cannot accept number less than 1 or greater than " + numFloors + " for currentFloor arg\n");
			} else {
				this.currentFloor = currentFloor;

			}
		} catch (BadInputDataException e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public MyDirection getDirection() {
		return direction;
	}

	private void setDirection(MyDirection direction) throws InvalidArgumentException {
		if (direction == null) {
			throw new InvalidArgumentException("ElevatorDTO's setDirection method cannot accept null for direction arg\n");
		}
		this.direction = direction;
	}

	public MyDirection getPendingDirection() {
		return pendingDirection;
	}

	private void setPendingDirection(MyDirection pendingDirection) throws InvalidArgumentException {
		if (pendingDirection == null) {
			throw new InvalidArgumentException("ElevatorDTO's setPendingDirection method cannot accept null for pendingDirection arg\n");
		}
		this.pendingDirection = pendingDirection;
	}

	public ArrayList<Integer> getUpPickups() {
		return upPickups;
	}

	private void setUpPickups(ArrayList<Integer> upPickups) throws InvalidArgumentException {
		if (upPickups == null) {
			throw new InvalidArgumentException("ElevatorDTO's setUpPickups method cannot accept null for upPickups arg\n");
		}
		this.upPickups = upPickups;
	}

	public ArrayList<Integer> getDownPickups() throws InvalidArgumentException {
		return downPickups;
	}

	private void setDownPickups(ArrayList<Integer> downPickups) throws InvalidArgumentException {
		if (downPickups == null) {
			throw new InvalidArgumentException("ElevatorDTO's setDownPickups method cannot accept null for downPickups arg\n");
		}
		this.downPickups = downPickups;
	}

	public ArrayList<Integer> getDropOffs() {
		return dropOffs;
	}

	private void setDropOffs(ArrayList<Integer> dropOffs) throws InvalidArgumentException {
		if (dropOffs == null) {
			throw new InvalidArgumentException("ElevatorDTO's setDropOffs method cannot accept null for dropOffs arg\n");
		}
		this.dropOffs = dropOffs;
	}
	
	private int getNumFloors() throws BadInputDataException {
		
		try { 
			int numFloors = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (numFloors > 1)
				return numFloors;
			else
				throw new BadInputDataException("ElevatorDTO received a value less than 2 for numFloors from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("ElevatorDTO could not parse DataStore's numFloors value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("ElevatorDTO received null from DataStore for numFloors\n"); 
	    }
		
	}
	
	private int getNumElevators() throws BadInputDataException {
		
		try { 
			int numElevators = Integer.parseInt(DataStore.getInstance().getNumElevators()); 
			if (numElevators > 0)
				return numElevators;
			else
				throw new BadInputDataException("ElevatorDTO received a value less than 1 for numElevators from DataStore\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("ElevatorDTO could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("ElevatorDTO received null from DataStore for numElevators\n"); 
	    }
		
	}
	
	
 
}

package button;

import interfaces.ButtonInterface;
import dataStore.DataStore;
import enumerators.MyDirection;
import controller.ElevatorController;
import errors.BadInputDataException;
import errors.InvalidArgumentException;
import errors.UnexpectedNullException;
import errors.AlreadyExistsException;

public class ButtonImpl implements ButtonInterface {
	
	private MyDirection direction;
	private int floorNumber;
	private boolean currentlyPushed;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////

	public ButtonImpl(MyDirection direction, int floorNum) {
		try {
			this.assignDirection(direction);
			this.assignFloorNumber(floorNum);
			this.currentlyPushed = false;
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (BadInputDataException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Interface Methods 			*
	 * 										*
	 *////////////////////////////////////////
	
	@Override
	public void push() {
		try {
			if (!this.currentlyPushed) {
				ElevatorController.getInstance().pickupRequest(this.floorNumber, this.direction);
				this.currentlyPushed = true;
			}
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (UnexpectedNullException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Override
	public void reset() throws AlreadyExistsException {
		if (!this.currentlyPushed) {
			throw new AlreadyExistsException("Reset called on unpushed " + this.direction + " button on floor " + this.floorNumber + "\n");
		}
		this.currentlyPushed = false;
	}
	
	/*////////////////////////////////////////////////////
	 * 													*
	 * 		Setter Methods called by the Constructor		*
	 * 													*
	 *////////////////////////////////////////////////////
	
	private void assignDirection(MyDirection directionIn) throws InvalidArgumentException {
		if (directionIn == null) {
			throw new InvalidArgumentException("ButtonImpl cannot accept null for directionIn arg during initialization\n");
		}
		this.direction = directionIn;
	}
	
	private void assignFloorNumber(int floorNum) throws InvalidArgumentException, BadInputDataException {
		try {
			int numFloors = Integer.parseInt(DataStore.getInstance().getNumFloors());
			if (floorNum < 1 || floorNum > numFloors) {
				throw new InvalidArgumentException("ButtonImpl cannot accept number less than 0 or greater than " + numFloors + " for floorNum arg during initialization\n");
			}
			this.floorNumber = floorNum;
		} catch (NumberFormatException e1) { 
	        throw new BadInputDataException("ButtonImpl could not parse DataStore's numFloors value to int\n"); 
	    } catch(NullPointerException e2) {
	        throw new BadInputDataException("ButtonImpl received null from DataStore for numFloors value\n"); 
	    }
	}
}

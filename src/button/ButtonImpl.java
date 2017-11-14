package button;

import interfaces.ButtonInterface;
import dataStore.DataStore;
import enumerators.MyDirection;
import controller.ElevatorController;
import errors.BadInputDataException;
import errors.InvalidArgumentException;
import errors.UnexpectedNullException;
import errors.AlreadyExistsException;

/* The ButtonImpl is an implementation of a button that is given 
 * a direction and floor number upon creation. It forwards elevator
 * requests from the floor to the elevator controller and maintains
 * a boolean so the controller is only notified once for any number
 * of requests. It is reset when an elevator has arrived to the floor
 * and is then available to be pushed again.
 */
public class ButtonImpl implements ButtonInterface {
	
	//Class variables
	private MyDirection direction;
	private int floorNumber;
	private boolean currentlyPushed;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////

	//Constructor, initializes necessary components
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
	
	/* Called by the floor when a person makes an elevator request.
	 * Forwards the request to the controller and updates the boolean.
	 * When currentlyPushed is true, it can still be pushed for a request by 
	 * other people, but only the first will notify the controller of the request
	 * since only one elevator is needed.
	 */
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
	
	/* Called by the floor when people are being transfered to the 
	 * elevator when it arrives for the request, and also when people
	 *  are being left on the floor due to an elevator at capacity so
	 *  they can request again.
	 */
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
	
	//Assigns the direction variable
	private void assignDirection(MyDirection direction) throws InvalidArgumentException {
		if (direction == null) {
			throw new InvalidArgumentException("ButtonImpl cannot accept null for direction arg during initialization\n");
		}
		this.direction = direction;
	}
	
	//Assigns the floorNumber variable
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

package floor;

import errors.*;
import java.util.ArrayList;
import enumerators.MyDirection;
import factories.FloorFactory;
import interfaces.FloorInterface;
import interfaces.PersonInterface;

/* The implementation of this class is delegated to the
 * FloorImpl class. See FloorImpl for notes.
 */
public class Floor implements FloorInterface {
	
	private FloorInterface delegate; 
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	public Floor(int floorNumber){
		this.setDelegate(floorNumber);
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Interface Methods 			*
	 * 										*
	 *////////////////////////////////////////
	
	@Override
	public int getFloorNumber() {
		return delegate.getFloorNumber();
	}
	
	@Override
	public void addWaitingPerson(PersonInterface rider) throws AlreadyExistsException {
		delegate.addWaitingPerson(rider);
	}
	
	@Override
	public ArrayList<PersonInterface> getWaitersByDirection(MyDirection direction, int availableCapacity) throws InvalidArgumentException {
		return delegate.getWaitersByDirection(direction, availableCapacity);
	}
	
	@Override
	public void addPersonToDecommissionedList(String id) throws InvalidArgumentException, AlreadyExistsException {
		delegate.addPersonToDecommissionedList(id);
	}


	/*////////////////////////////////////////
	 * 										*
	 * 			Delegation Setter  			*
	 * 										*
	 *////////////////////////////////////////
	
	private void setDelegate(int floorNumber) {
		this.delegate = FloorFactory.build(floorNumber);
	}
	
}

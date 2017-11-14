package person;

import enumerators.MyDirection;
import factories.PersonFactory;
import interfaces.PersonInterface;

/* The implementation of this class is delegated to the
 * PersonImpl class. See PersonImpl for notes.
 */
public class Person implements PersonInterface {

	private PersonInterface delegate;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	public Person(String id, int startFloor, int destinationFloor) {
		this.setDelegate(id, startFloor, destinationFloor);
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Interface Methods 			*
	 * 										*
	 *////////////////////////////////////////
	
	@Override
	public String getId() {
		return this.delegate.getId();
	}
	
	@Override
	public int getStartFloor() {
		return this.delegate.getStartFloor();
	}

	@Override
	public int getDestinationFloor() {
		return this.delegate.getDestinationFloor();
	}
	
	@Override
	public MyDirection getDirection() {
		return this.delegate.getDirection();
	}

	@Override
	public long getWaitTime() {
		return this.delegate.getWaitTime();
	}

	@Override
	public long getRideTime() {
		return this.delegate.getRideTime();
	}

	@Override
	public void enterElevator() {
		this.delegate.enterElevator();
	}

	@Override
	public void exitElevator() {
		this.delegate.exitElevator();
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Delegation Setter  			*
	 * 										*
	 *////////////////////////////////////////
	
	private void setDelegate(String id, int startFloor, int destinationFloor) {
		this.delegate = PersonFactory.build(id, startFloor, destinationFloor);
	}

}

package elevator;

import errors.UnexpectedNullException;
import errors.InvalidArgumentException;
import enumerators.MyDirection;
import factories.ElevatorFactory;
import interfaces.ElevatorInterface;

public class Elevator implements ElevatorInterface{
	
	private ElevatorInterface delegate;
	
	/*////////////////////////////////////////
	 * 										*
	 * 				Constructor 				*
	 * 										*
	 *////////////////////////////////////////
	
	public Elevator(int elevatorNumber){
		this.setDelegate(elevatorNumber);
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Interface Methods 			*
	 * 										*
	 *////////////////////////////////////////
	
	@Override
	public void update() {
		this.delegate.update();
	}
	
	@Override
	public void addPickupRequest(MyDirection direction, int floor) throws InvalidArgumentException {
		this.delegate.addPickupRequest(direction, floor);
	}

	@Override
	public ElevatorDTO getDTO() throws UnexpectedNullException {
		return this.delegate.getDTO();
	}
	
	/*////////////////////////////////////////
	 * 										*
	 * 			Delegation Setter  			*
	 * 										*
	 *////////////////////////////////////////
	
	private void setDelegate(int elevatorNumber) {
		this.delegate = ElevatorFactory.build(elevatorNumber);
	}
	
}

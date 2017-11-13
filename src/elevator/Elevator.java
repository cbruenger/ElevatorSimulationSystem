package elevator;
import java.util.ArrayList;
import errors.*;
import enumerators.MyDirection;
import factories.ElevatorFactory;
import interfaces.ElevatorInterface;
import interfaces.RiderInterface;

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
	public void update(long time) {
		this.delegate.update(time);
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
	 * 			Private Methods  			*
	 * 										*
	 *////////////////////////////////////////
	
	private void setDelegate(int elevatorNumber) {
		this.delegate = ElevatorFactory.build(elevatorNumber);
	}
	
}

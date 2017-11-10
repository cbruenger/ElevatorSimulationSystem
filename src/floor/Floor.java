package floor;

import errors.*;
import java.util.ArrayList;
import enumerators.MyDirection;
import factories.FloorFactory;
import interfaces.FloorInterface;
import interfaces.RiderInterface;

public class Floor implements FloorInterface {
	
	private FloorInterface delegate; 
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public Floor(int floorNumber){
		this.setDelegate(floorNumber);
	}
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////
	
	@Override
	public int getFloorNumber() {
		return delegate.getFloorNumber();
	}

//	//Only for testing temporarily in our main
//	@Override
//	public ArrayList<RiderInterface> getRiders() {
//		return delegate.getRiders();
//	}
	
	@Override
	public void addRider(RiderInterface rider) throws AlreadyExistsException {
		delegate.addRider(rider);
	}
	
	@Override
	public void removeRider(RiderInterface rider) throws CannotRemoveException {
		delegate.removeRider(rider);
	}
	
	@Override
	public ArrayList<RiderInterface> getRidersByDirection(MyDirection direction, int availableCapacity) throws InvalidArgumentException {
		return delegate.getRidersByDirection(direction, availableCapacity);
	}
	
	@Override
	public boolean waitersLeftBehind(int floorNum, MyDirection direction) throws InvalidArgumentException {
		return delegate.waitersLeftBehind(floorNum, direction);
	}

	/////////////////////////////
	//				           //
	//    All other methods    //
	//				           //
	/////////////////////////////
	
	private void setDelegate(int floorNumber) {
		this.delegate = FloorFactory.build(floorNumber);
	}
	
}

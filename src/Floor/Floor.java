package Floor;

import java.util.ArrayList;
import enumerators.Direction;
import Factories.FloorFactory;
import Interfaces.FloorInterface;
import Interfaces.RiderInterface;

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

	//Only for testing temporarily in our main
	@Override
	public ArrayList<RiderInterface> getRiders() {
		return delegate.getRiders();
	}
	
	@Override
	public void addRider(RiderInterface rider) {
		delegate.addRider(rider);
	}
	
	@Override
	public void removeRider(RiderInterface rider) {
		delegate.addRider(rider);
	}
	
	@Override
	public ArrayList<RiderInterface> getRidersByDirection(Direction direction) {
		return delegate.getRidersByDirection(direction);
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

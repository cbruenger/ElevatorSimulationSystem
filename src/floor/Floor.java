package floor;

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
	public ArrayList<RiderInterface> getRidersByDirection(MyDirection direction) {
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

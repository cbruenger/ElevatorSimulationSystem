package Floor;

import java.util.ArrayList;

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
	
	public Floor(String floorNumber){
		this.setDelegate(floorNumber);
	}
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////
	
	@Override
	public String getFloorNumber() {
		return delegate.getFloorNumber();
	}

	@Override
	public ArrayList<RiderInterface> getRiders() {
		return delegate.getRiders();
	}

	@Override
	public void removeRider(RiderInterface rider) {
		delegate.removeRider(rider);
	}
	
	@Override
	public void addRider(RiderInterface rider) {
		delegate.addRider(rider);
	}

	/////////////////////////////
	//				           //
	//    All other methods    //
	//				           //
	/////////////////////////////
	
	private void setDelegate(String floor_number) {
		this.delegate = FloorFactory.build(floor_number);
	}
	
}

package Floor;

import java.util.ArrayList;

import Factories.FloorFactory;
import Interfaces.FloorInterface;

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
	public ArrayList<String> getRiderIds() {
		return delegate.getRiderIds();
	}

	@Override
	public void removeRider(String riderId) {
		delegate.removeRider(riderId);
	}
	
	@Override
	public void addRider(String riderId) {
		delegate.addRider(riderId);
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

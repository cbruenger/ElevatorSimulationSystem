package rider;

import enumerators.MyDirection;
import factories.RiderFactory;
import interfaces.RiderInterface;

public class Rider implements RiderInterface {

	private RiderInterface delegate;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public Rider(String id, int startFloor, int destinationFloor) {
		this.setDelegate(id, startFloor, destinationFloor);
	}
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////
	
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
	
	//////////////////////////
	//				        //
	//    Other Methods     //
	//				        //
	//////////////////////////
	
	private void setDelegate(String id, int startFloor, int destinationFloor) {
		this.delegate = RiderFactory.build(id, startFloor, destinationFloor);
	}

}

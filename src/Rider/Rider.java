package Rider;

import Factories.RiderFactory;
import Interfaces.RiderInterface;

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
	public String getStartFloor() {
		return this.delegate.getStartFloor();
	}

	@Override
	public String getDestinationFloor() {
		return this.delegate.getDestinationFloor();
	}
	
	@Override
	public String getDirection() {
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
	public void exitElavator() {
		this.delegate.exitElavator();
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

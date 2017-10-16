package Rider;

import Factories.RiderFactory;
import Interfaces.RiderInterface;

public class Rider implements RiderInterface {

	private RiderInterface delegate;
	
	//Maybe remove numFloors param and use static building data
	public Rider(String id) {
		this.setDelegate(id);
	}
	
	private void setDelegate(String id) {
		this.delegate = RiderFactory.build(id);
	}
	
	public String getId() {
		return this.delegate.getId();
	}
	
	@Override
	public void enterElevator() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitElavator() {
		// TODO Auto-generated method stub

	}

	@Override
	public float getWaitTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRideTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDestinationFloor() {
		// TODO Auto-generated method stub
		return 0;
	}

}

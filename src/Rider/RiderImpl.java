package Rider;

import Interfaces.RiderInterface;

public class RiderImpl implements RiderInterface {

	private String id;
	private int startFloor;
	private int currentFloor;
	private long requestTime;
	private long enterTime;
	private long exitTime;
	private long waitTime;
	private long rideTime;
	
	public RiderImpl(String id) {
		
		this.setId(id);
		this.setStartFloor();
		this.setDestinationFloor();
		this.requestElevator();
		
	}
	
	private void setId(String id) {
		// TODO
	}
	
	private void setStartFloor() {
		// TODO
	}
	
	private void setDestinationFloor() {
		// TODO
	}
	
	private void requestElevator() {
		this.setRequestTime();
		//ElevatorController.riderRequest(this.currentFloor);
		
	}
	
	private void setRequestTime() {
		
	}
	
	public String getId() {
		return this.id;
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

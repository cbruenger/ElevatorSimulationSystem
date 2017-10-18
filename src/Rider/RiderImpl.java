package Rider;

import Interfaces.RiderInterface;

public class RiderImpl implements RiderInterface {

	private String id;
	private String startFloor;
	private String destinationFloor;
	private String direction;
	private long requestTime;
	private long enterTime;
	private long exitTime;
	private long waitTime;
	private long rideTime;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public RiderImpl(String id, int startFloor, int destinationFloor) {	
		this.setId(id);
		this.setStartFloor(startFloor);
		this.setDestinationFloor(destinationFloor);
		this.setDirection(startFloor, destinationFloor);
		this.requestElevator();
		
	}
	
	
	////////////////////
	//				  //
	//    Setters     //
	//				  //
	////////////////////
	
	private void setId(String id) {
		this.id = id;
	}
	
	private void setStartFloor(int startFloor) {
		this.startFloor = Integer.toString(startFloor);
	}
	
	private void setDestinationFloor(int destinationFloor) {
		this.destinationFloor = Integer.toString(destinationFloor);
	}
	
	private void setDirection(int startFloor, int destinationFloor) {
		if (startFloor - destinationFloor > 0) this.direction = "DOWN";
		else this.direction = "UP";
	}
		
	private void setRequestTime() {
		this.requestTime = System.currentTimeMillis();
	}
    
    private void setEnterTime() {
		this.enterTime = System.currentTimeMillis();
	}
    
    private void setWaitTime() {
    	this.waitTime = this.enterTime - this.requestTime;
  	}
    
    private void setExitTime() {
		this.exitTime = System.currentTimeMillis();
	}
    
    private void setRideTime() {
		this.rideTime = this.exitTime - this.enterTime;
	}

		////////////////////
		//				   //
		//    Getters      //
		//				   //
		/////////////////////
	
	
	public String getId() {
		return this.id;
	}

	@Override
	public String getStartFloor() {
		return this.startFloor;
	}
	
	@Override
	public String getDestinationFloor() {
		return this.destinationFloor;
	}
	
	@Override
	public String getDirection() {
		return this.direction;
	}
	
//	@Override
//	public long getRequestTime() {
//		return this.requestTime;
//	}
	
//	@Override
//	public long getEnterTime() {
//		return this.enterTime;
//	}
	
	@Override
	public long getWaitTime() {
		return this.waitTime;
	}
//
//	@Override
//	public long getExitTime() {
//		return this.exitTime;
//	}
	
	@Override
	public long getRideTime() {
		return this.rideTime;
	}
	
	
		//////////////////////////
		//				        //
		//    Other Methods     //
		//				        //
		//////////////////////////
    
    
	public void requestElevator() {
		this.setRequestTime();
	}
	
	@Override
	public void enterElevator() {
		this.setEnterTime();
		this.setWaitTime();
	}

	@Override
	public void exitElevator() {
		this.setExitTime();
		this.setRideTime();
	}

}

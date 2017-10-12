package elevator;

public class RiderImpl implements RiderInterface {

	private String id;
	private int startFloor;
	private int destinationFloor;
	private long requestTime;
	private long enterTime;
	private long exitTime;
	private long waitTime;
	private long rideTime;
	
	public RiderImpl(String id, int numFloors) {
		
		this.setId(id);
		this.setStartFloor(numFloors);
		this.setDestinationFloor(numFloors);
		this.setRequest();
		
	}
	
	private void setId(String id) {
		// TODO
	}
	
	private void setStartFloor(int numFloors) {
		// TODO
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

package elevator;

public class Rider implements RiderInterface {

	private RiderInterface delegate;
	
	//Maybe remove numFloors param and use static building data
	public Rider(String id, int numFloors) {
		this.setDelegate(id, numFloors);
	}
	
	private void setDelegate(String id, int numFloors) {
		this.delegate = RiderFactory.build(id, numFloors);
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

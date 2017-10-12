package elevator;

public interface RiderInterface {
	
	public void enterElevator();
		// in impl...
		// ElevatorFacade.getInstance.addRider(el_ID, riderID)
	public void exitElavator();
//	public void getRequestTime();
//	public void getEnterTime();
//	public void getExitTime();
	public float getWaitTime();
	public float getRideTime();
//	public void getStartFloor();
	public int getDestinationFloor();

}

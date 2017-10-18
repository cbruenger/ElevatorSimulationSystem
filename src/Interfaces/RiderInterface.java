package Interfaces;

public interface RiderInterface {
	
	public String getId();
	public String getStartFloor();
	public String getDestinationFloor();
	public String getDirection();
	public long getWaitTime();
	public long getRideTime();
	public void enterElevator();
	public void exitElavator();
	//public long getRequestTime();
	//public long getEnterTime();
	//public long getExitTime();
}

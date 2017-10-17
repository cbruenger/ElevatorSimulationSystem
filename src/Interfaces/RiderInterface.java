package Interfaces;

public interface RiderInterface {
	
	public String getId();
	public int getStartFloor();
	public int getDestinationFloor();
	public String getDirection();
	public long getWaitTime();
	public long getRideTime();
	public void enterElevator();
	public void exitElavator();
	//public long getRequestTime();
	//public long getEnterTime();
	//public long getExitTime();
}

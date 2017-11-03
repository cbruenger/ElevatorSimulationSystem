package interfaces;

import enumerators.MyDirection;

public interface RiderInterface {
	
	public String getId();
	public int getStartFloor();
	public int getDestinationFloor();
	public MyDirection getDirection();
	public long getWaitTime();
	public long getRideTime();
	public void enterElevator();
	public void exitElevator();
	
	//public long getRequestTime();
	//public long getEnterTime();
	//public long getExitTime();
}

package Interfaces;

import enumerators.Direction;

public interface RiderInterface {
	
	public String getId();
	public int getStartFloor();
	public int getDestinationFloor();
	public Direction getDirection();
	public long getWaitTime();
	public long getRideTime();
	public void enterElevator();
	public void exitElevator();
	
	//public long getRequestTime();
	//public long getEnterTime();
	//public long getExitTime();
}

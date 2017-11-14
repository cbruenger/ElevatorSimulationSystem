package interfaces;

import enumerators.MyDirection;

public interface PersonInterface {
	public String getId();
	public int getStartFloor();
	public int getDestinationFloor();
	public MyDirection getDirection();
	public long getWaitTime();
	public long getRideTime();
	public void enterElevator();
	public void exitElevator();
}

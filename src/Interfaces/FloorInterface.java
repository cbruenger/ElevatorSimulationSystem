package Interfaces;

import java.util.ArrayList;
import enumerators.MyDirection;

public interface FloorInterface {
	
	public int getFloorNumber();
	public ArrayList<RiderInterface> getRiders(); //Only for testing temporarily in our main
	public void removeRider(RiderInterface rider);
	public void addRider(RiderInterface rider);
	public ArrayList<RiderInterface> getRidersByDirection(MyDirection direction);
}

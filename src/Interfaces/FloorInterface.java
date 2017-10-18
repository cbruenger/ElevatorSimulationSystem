package Interfaces;

import java.util.ArrayList;

public interface FloorInterface {
	public String getFloorNumber();
	public ArrayList<RiderInterface> getRiders();
	public void removeRider(RiderInterface rider);
	public void addRider(RiderInterface rider);
}

package Interfaces;

import java.util.ArrayList;

public interface FloorInterface {
	public String getFloorNumber();
	public ArrayList<String> getRiderIds();
	public void removeRider(String riderId);
	public void addRider(String riderId);
}

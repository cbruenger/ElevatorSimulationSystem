package Interfaces;

import java.util.ArrayList;

public interface ElevatorInterface {
	public void moveUp();
	public void moveDown();
	public void openDoors();
	public void closeDoors();
	public void addRider(String riderId);
	public void removeRider(String riderId);
	public String getId();
	public int getCurrentFloor();
	public String getDirection();
	public ArrayList<String> getRiderIds();
	public ArrayList<Integer> getPickUps();
	public ArrayList<Integer> getDropOffs();
}

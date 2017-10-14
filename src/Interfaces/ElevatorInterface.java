package Interfaces;

import java.util.ArrayList;

public interface ElevatorInterface {
	public void moveUp();
	public void moveDown();
	public void openDoors();
	public void closeDoors();
	public String getId();
	public int getCurrentFloor();
	public String getDirection();
	public String[] getRiderIds();
	public ArrayList<Integer> getPickUps();
	public ArrayList<Integer> getDropOffs();
}

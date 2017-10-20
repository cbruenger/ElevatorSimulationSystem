package Interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import enumerators.Direction;

public interface ElevatorInterface {
	
	//Getters
	public int getElevatorNumber();
	public int getCurrentFloor();
	public Direction getDirection();
	public HashMap<Direction, ArrayList<Integer>> getPickUps();
	public HashMap<Direction, ArrayList<Integer>> getDropOffs();
	public ArrayList<String> getRiderIds(); //Temporary for testing in our main
	
	//Useful functions
	public void update(long time);
	public void addRider(RiderInterface rider);
	public void addPickupRequest(Direction direction, int floor);
	
	//public void removeRider(String riderId);
	//public void moveUp();
	//public void moveDown();
	//public void openDoors();
	//public void closeDoors();
}

package interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import enumerators.MyDirection;

public interface ElevatorInterface {
	
	//Getters
	public int getElevatorNumber();
	public int getCurrentFloor();
	public MyDirection getDirection();
	public MyDirection getPendingDirection();
	public HashMap<MyDirection, ArrayList<Integer>> getPickUps();
	public HashMap<MyDirection, ArrayList<Integer>> getDropOffs();
//	public ArrayList<String> getRiderIds(); //Temporary for testing in our main
	
	//Useful functions
	public void update(long time);
	public void addRiders(ArrayList<RiderInterface> riders);
	public void addPickupRequest(MyDirection direction, int floor);
	
	//public void removeRider(String riderId);
	//public void moveUp();
	//public void moveDown();
	//public void openDoors();
	//public void closeDoors();
}

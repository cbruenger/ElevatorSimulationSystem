package Elevator;
import java.util.ArrayList;
import Factories.ElevatorFactory;
import Interfaces.ElevatorInterface;
import enumerators.Direction;

public class Elevator implements ElevatorInterface{
	
	private ElevatorInterface delegate;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public Elevator(String id){
		this.setDelegate(id);
	}
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////

	public void update(long time) {
		move(time);
		checkFloorStops();
			//will check if floorstops are empty... will make call in checkFloorStops to ReEvaluate if necessary...
		checkRiderInElevator():
			//will check riders in elevator...will call to process floor if necessary and reEvalute...
		
	}
	
	
	public void move(long time) {
		if(this.getDirection() != enumerators.Direction.IDLE) {
			//determine how quickly to move...
			//determine move UP
			//or determine move DOWN
			
			//Once full time for movement has occured print out statement of elevator moving to floor X
		}
		
	}
	
	public void openDoors() {
		this.delegate.openDoors();
	}
	
	public void closeDoors() {
		this.delegate.closeDoors();
	}
	
	public void addRider(String riderId) {
		this.delegate.addRider(riderId);
	}
	
	public void removeRider(String riderId) {
		this.delegate.removeRider(riderId);
	}
	
	
	//////
	// All other methods
	//////
	
	
	private void setDelegate(String id) {
		this.delegate = ElevatorFactory.build(id);
	}
	
	public String getId() {
		return this.delegate.getId();
	}
	
	public int getCurrentFloor() {
		return this.delegate.getCurrentFloor();
	}
	
	public Direction getDirection() {
		return this.delegate.getDirection();
	}

	public ArrayList<String> getRiderIds() {
		return this.delegate.getRiderIds();
	}
	
	public ArrayList<Integer> getPickUps() {
		return this.delegate.getPickUps();
	}
		
	public ArrayList<Integer> getDropOffs() {
		return this.delegate.getDropOffs();
	}
	
	
	}

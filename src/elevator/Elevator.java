package elevator;
import java.util.ArrayList;
import java.util.HashMap;
import errors.*;
import enumerators.MyDirection;
import factories.ElevatorFactory;
import interfaces.ElevatorInterface;
import interfaces.RiderInterface;

public class Elevator implements ElevatorInterface{
	
	private ElevatorInterface delegate;
	
	////////////////////////
	//				      //
	//    Constructor     //
	//				      //
	////////////////////////
	
	public Elevator(int elevatorNumber){
		this.setDelegate(elevatorNumber);
	}
	
	private void setDelegate(int elevatorNumber) {
		this.delegate = ElevatorFactory.build(elevatorNumber);
	}
	
	/////////////////////////////
	//				           //
	//    Interface Methods    //
	//				           //
	/////////////////////////////
	
	@Override
	public int getElevatorNumber() {
		return this.delegate.getElevatorNumber();
	}
	
	@Override
	public int getCurrentFloor() {
		return this.delegate.getCurrentFloor();
	}
	
	@Override
	public MyDirection getDirection() {
		return this.delegate.getDirection();
	}
	
	@Override
	public MyDirection getPendingDirection() {
		return delegate.getPendingDirection();
	}
	
	/*
	 * Maybe instead of returning pickups and drop-offs just 
	 * return the current direction, destination, and whether its
	 * an up or down request?
	 */
	@Override
	public HashMap<MyDirection, ArrayList<Integer>> getPickUps() {
		return this.delegate.getPickUps();
	}
	
	@Override
	public HashMap<MyDirection, ArrayList<Integer>> getDropOffs() {
		return this.delegate.getDropOffs();
	}

	//Temporary for testing in main
//	@Override
//	public ArrayList<String> getRiderIds() {
//		return this.delegate.getRiderIds();
//	}
	
	@Override
	public void update(long time) {
		this.delegate.update(time);
	}
	
	@Override
	public void addRiders(ArrayList<RiderInterface> riders) {
		this.delegate.addRiders(riders);
	}
	
	@Override
	public void addPickupRequest(MyDirection direction, int floor) throws InvalidArgumentException {
		this.delegate.addPickupRequest(direction, floor);
	}

	@Override
	public ElevatorDTO getDTO() {
		// TODO Auto-generated method stub
		return this.delegate.getDTO();
	}
	
}

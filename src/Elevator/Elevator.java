package Elevator;
import java.util.ArrayList;
import java.util.HashMap;

import Factories.ElevatorFactory;
import Interfaces.ElevatorInterface;
import Interfaces.RiderInterface;
import enumerators.Direction;

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
	public Direction getDirection() {
		return this.delegate.getDirection();
	}
	
	/*
	 * Maybe instead of returning pickups and drop-offs just 
	 * return the current direction, destination, and whether its
	 * an up or down request?
	 */
	@Override
	public HashMap<Direction, ArrayList<Integer>> getPickUps() {
		return this.delegate.getPickUps();
	}
	
	@Override
	public HashMap<Direction, ArrayList<Integer>> getDropOffs() {
		return this.delegate.getDropOffs();
	}

	//Temporary for testing in main
	@Override
	public ArrayList<String> getRiderIds() {
		return this.delegate.getRiderIds();
	}
	
	@Override
	public void update(long time) {
		this.delegate.update(time);
	}
	
	@Override
	public void addRider(RiderInterface rider) {
		this.delegate.addRider(rider);
	}
	
	@Override
	public void addPickupRequest(Direction direction, int floor) {
		this.delegate.addPickupRequest(direction, floor);
	}
	
}

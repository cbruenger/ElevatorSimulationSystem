package elevator;

import java.util.ArrayList;
import enumerators.MyDirection;


public class ElevatorDTO {
	private int elevatorNumber;
	private int currentFloor;
	private MyDirection direction;
	private MyDirection pendingDirection;
	private ArrayList<Integer> upPickups;
	private ArrayList<Integer> downPickups;
	private ArrayList<Integer> dropOffs;
	
	public ElevatorDTO(int elevatorNumber, int currentFloor, MyDirection direction, MyDirection pendingDirection, ArrayList<Integer> upPickups, ArrayList<Integer> downPickups, ArrayList<Integer> dropOffs){
		this.setElevatorNumber(elevatorNumber); 
		this.setCurrentFloor(currentFloor);
		this.setDirection(direction);
		this.setPendingDirection(pendingDirection);
		this.setUpPickups(upPickups);
		this.setDownPickups(downPickups);
		this.setDropOffs(dropOffs);
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	private void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	private void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public MyDirection getDirection() {
		return direction;
	}

	private void setDirection(MyDirection direction) {
		this.direction = direction;
	}

	public MyDirection getPendingDirection() {
		return pendingDirection;
	}

	private void setPendingDirection(MyDirection pendingDirection) {
		this.pendingDirection = pendingDirection;
	}

	public ArrayList<Integer> getUpPickups() {
		return upPickups;
	}

	private void setUpPickups(ArrayList<Integer> upPickups) {
		this.upPickups = upPickups;
	}

	public ArrayList<Integer> getDownPickups() {
		return downPickups;
	}

	private void setDownPickups(ArrayList<Integer> downPickups) {
		this.downPickups = downPickups;
	}

	public ArrayList<Integer> getDropOffs() {
		return dropOffs;
	}

	private void setDropOffs(ArrayList<Integer> dropOffs) {
		this.dropOffs = dropOffs;
	}
	
	
 
}

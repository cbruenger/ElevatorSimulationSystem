package elevator;


///  need to check for basement and top floor...

public class Elevator_Impl implements Elevator_Interface{
	
	private int curr_floor;
	private String direction;
	
	//Constructor
	public Elevator_Impl(int floor) {
		curr_floor = floor;
	}
	
	//this move elevator one space/floor...
	// request could represent a goal for passenger or picking up someone...
	
	public void moveUp() {
		direction = "UP";
		this.setFloor(this.getFloor() + 1);
		//sleep for a second then...
		// set direction idle...
	}
	
	public void moveDown() {
		direction = "DOWN";
		this.setFloor(this.getFloor() - 1);
		//sleep for a second then...
		// set direction idle...
	}
	
	public int getFloor() {
		return this.curr_floor;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	private void setFloor(int new_floor) {
		this.curr_floor = new_floor;
	}
	
	public void openDoors() {
		//sleep for a few seconds?
		
		//person update?
		//floor update?
	}
	public void closeDoors() {
		//sleep for a few seconds?
	}
		
}

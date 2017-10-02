package elevator;
import java.util.ArrayList;

public class Elevator1 implements Elevator_Interface{
	private int ID;
	private Elevator_Impl delegate;
	private String direction;
	private int curr_floor;
	//make persons class for persons[]
	private ArrayList<Integer> pick_ups;
	private ArrayList<Integer> drop_offs;
	private boolean doorOpen;
	
	//////
	//Constructor
	///////
	
	public Elevator1(int ID){
		setID(ID);
		setDel();
		//not sure about hard code
		direction = "IDLE";
		curr_floor = 1;
		// capacity of elevator = 10?
		pick_ups = new ArrayList<Integer>();
		drop_offs = new ArrayList<Integer>();
		doorOpen = false;
	}
	
	///////
	// Interface Methods
	///////

	/// should be called step?
	
	public void moveUp() {
		delegate.moveUp();
		this.setDirection(delegate.getDirection());
		this.setFloor(delegate.getFloor());
	}
	
	public void moveDown() {
		delegate.moveDown();
		this.setDirection(delegate.getDirection());
		this.setFloor(delegate.getFloor());
	}
	
	public void openDoors() {
		//This is delegate work
		// Thread.sleep()
		doorOpen = true;
	}
	
	public void closeDoors() {
		//This is delegate work
		// Thread.sleep()
		doorOpen = false;
	}
	
	
	//////
	// All other methods
	//////
	
	private void setID(int makeID) {
		ID = makeID;
	}
	
	private void setDel() {
		delegate = (Elevator_Impl) Elevator_Factory1.build(this.getCurrFloor());
	}
	
	public int getID() {
	// This "get" accessor is public - Strings and primitive types are 
	// safe to publically return.
		return ID;
	}
	
	private void setFloor(int new_floor) {
		curr_floor = new_floor;
	}
	
	private void setDirection(String dir) {
		direction = dir;
	}
	
	public int getCurrFloor() {
		return curr_floor;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public ArrayList<Integer> getPickUps() {
		return pick_ups;
	}
		

	public ArrayList<Integer> getDropOffs() {
		return drop_offs;
	}
	
	
	////
	// testing
	////
	public static void main(String[] args) {
		System.out.println("Start:\n");
		
		Elevator1 e1 = new Elevator1(1);
		Elevator1 e2 = new Elevator1(2);
		Elevator1 e3 = new Elevator1(3);
		System.out.printf("Elevator 1 Start floor: %s \n",e1.getCurrFloor());
		System.out.printf("Elevator 2 Start floor: %s \n",e2.getCurrFloor());
		System.out.printf("Elevator 3 Start floor: %s \n",e3.getCurrFloor());
		
		//move each floor up by 2
		e1.moveUp();
		e2.moveUp();
		e3.moveUp();
		e1.moveUp();
		e2.moveUp();
		e3.moveUp();
		
		System.out.printf("Elevator 1 floor: %s \n",e1.getCurrFloor());
		System.out.printf("Elevator 2 floor: %s \n",e2.getCurrFloor());
		System.out.printf("Elevator 3 floor: %s \n",e3.getCurrFloor());
		
		// new input...
		e1.moveDown();
		e2.moveUp();
		e2.moveUp();
		e3.moveUp();
		e3.moveDown();
		
		System.out.printf("Elevator 1 final floor: %s \n",e1.getCurrFloor());
		System.out.printf("Elevator 2 final floor: %s \n",e2.getCurrFloor());
		System.out.printf("Elevator 3 final floor: %s \n",e3.getCurrFloor());
		
		System.out.printf("Elevator 1 final direction: %s \n",e1.getDirection());
		System.out.printf("Elevator 2 final direction: %s \n",e2.getDirection());
		System.out.printf("Elevator 3 final direction: %s \n",e3.getDirection());
	}
	
	}

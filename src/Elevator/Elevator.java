package Elevator;
import java.util.ArrayList;

import Factories.ElevatorFactory;
import Interfaces.ElevatorInterface;

public class Elevator implements ElevatorInterface{
	
	private ElevatorInterface delegate;
	
	//////
	//Constructor
	///////
	
	public Elevator(String id){
		this.setDelegate(id);
	}
	
	///////
	// Interface Methods
	///////

	
	public void moveUp() {
		this.delegate.moveUp();
	}
	
	public void moveDown() {
		this.delegate.moveDown();
	}
	
	public void openDoors() {
		this.delegate.openDoors();
	}
	
	public void closeDoors() {
		this.delegate.closeDoors();
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
	
	public String getDirection() {
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
	
	
	////
	// testing
	////
//	public static void main(String[] args) {
//		System.out.println("Start:\n");
//		
//		Elevator e1 = new Elevator(1);
//		Elevator e2 = new Elevator(2);
//		Elevator e3 = new Elevator(3);
//		System.out.printf("Elevator 1 Start floor: %s \n",e1.getCurrFloor());
//		System.out.printf("Elevator 2 Start floor: %s \n",e2.getCurrFloor());
//		System.out.printf("Elevator 3 Start floor: %s \n",e3.getCurrFloor());
//		
//		//move each floor up by 2
//		e1.moveUp();
//		e2.moveUp();
//		e3.moveUp();
//		e1.moveUp();
//		e2.moveUp();
//		e3.moveUp();
//		
//		System.out.printf("Elevator 1 floor: %s \n",e1.getCurrFloor());
//		System.out.printf("Elevator 2 floor: %s \n",e2.getCurrFloor());
//		System.out.printf("Elevator 3 floor: %s \n",e3.getCurrFloor());
//		
//		// new input...
//		e1.moveDown();
//		e2.moveUp();
//		e2.moveUp();
//		e3.moveUp();
//		e3.moveDown();
//		
//		System.out.printf("Elevator 1 final floor: %s \n",e1.getCurrFloor());
//		System.out.printf("Elevator 2 final floor: %s \n",e2.getCurrFloor());
//		System.out.printf("Elevator 3 final floor: %s \n",e3.getCurrFloor());
//		
//		System.out.printf("Elevator 1 final direction: %s \n",e1.getDirection());
//		System.out.printf("Elevator 2 final direction: %s \n",e2.getDirection());
//		System.out.printf("Elevator 3 final direction: %s \n",e3.getDirection());
//	}
	
	}

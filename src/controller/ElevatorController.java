package controller;

import factories.ElevatorControllerFactory;
import interfaces.ElevatorControllerInterface;

public class ElevatorController implements ElevatorControllerInterface {

	private ElevatorControllerInterface delegate;
	
	public ElevatorController(String id) {
		this.setDelegate(id);
	}
	
	private void setDelegate(String id) {
		this.delegate = ElevatorControllerFactory.build(id);
	}
	
	public void riderRequest(int floor) {
		this.delegate.riderRequest(floor);
	}
	
}

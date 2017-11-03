package controller;

import interfaces.ElevatorControllerInterface;

public class ElevatorControllerImpl implements ElevatorControllerInterface {

	private String id;
	
	public ElevatorControllerImpl(String id) {
		
		this.setId(id);
		
	}
	
	@Override
	public void riderRequest(int floor) {
		// TODO Auto-generated method stub

	}
	
	private void setId(String id) {
		this.id = id;
	}

}

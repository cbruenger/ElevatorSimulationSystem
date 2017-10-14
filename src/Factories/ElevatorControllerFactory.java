package Factories;

import Controller.ElevatorControllerImpl;
import Interfaces.ElevatorControllerInterface;

public class ElevatorControllerFactory {

	public static ElevatorControllerInterface build(String id) {
		return new ElevatorControllerImpl(id);
	}
	
}

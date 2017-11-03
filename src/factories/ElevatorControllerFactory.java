package factories;

import controller.ElevatorControllerImpl;
import interfaces.ElevatorControllerInterface;

public class ElevatorControllerFactory {

	public static ElevatorControllerInterface build(String id) {
		return new ElevatorControllerImpl(id);
	}
	
}

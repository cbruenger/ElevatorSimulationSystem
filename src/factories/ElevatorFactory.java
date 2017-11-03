package factories;

import elevator.ElevatorImpl;
import interfaces.ElevatorInterface;

public class ElevatorFactory{
	
	public static ElevatorInterface build(int elevatorNumber){
		return new ElevatorImpl(elevatorNumber);
	}
}

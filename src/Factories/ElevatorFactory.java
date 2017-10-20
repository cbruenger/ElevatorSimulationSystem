package Factories;

import Elevator.ElevatorImpl;
import Interfaces.ElevatorInterface;

public class ElevatorFactory{
	
	public static ElevatorInterface build(int elevatorNumber){
		return new ElevatorImpl(elevatorNumber);
	}
}

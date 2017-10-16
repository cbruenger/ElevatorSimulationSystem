package Factories;

import Elevator.ElevatorImpl;
import Interfaces.ElevatorInterface;

public class ElevatorFactory{
	
	public static ElevatorInterface build(String id){
		return new ElevatorImpl(id);
	}
}

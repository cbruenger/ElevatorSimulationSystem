package Factories;

import Elevator.ElevatorImpl;
import Interfaces.ElevatorInterface;

public class ElevatorFactory{
	
	public static ElevatorInterface build(String id, int capacity){
		return new ElevatorImpl(id, capacity);
	}
}

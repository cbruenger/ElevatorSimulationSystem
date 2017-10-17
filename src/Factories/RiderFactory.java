package Factories;

import Interfaces.RiderInterface;
import Rider.RiderImpl;

public class RiderFactory {
	
	public static RiderInterface build(String id, int startFloor, int destinationFloor) {
		return new RiderImpl(id, startFloor, destinationFloor);
	}
}

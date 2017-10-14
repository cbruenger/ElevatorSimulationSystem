package Factories;

import Interfaces.RiderInterface;
import Rider.RiderImpl;

public class RiderFactory {
	
	public static RiderInterface build(String id, int numFloors) {
		return new RiderImpl(id, numFloors);
	}

}

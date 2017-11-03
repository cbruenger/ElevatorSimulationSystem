package factories;

import interfaces.RiderInterface;
import rider.RiderImpl;

public class RiderFactory {
	
	public static RiderInterface build(String id, int startFloor, int destinationFloor) {
		return new RiderImpl(id, startFloor, destinationFloor);
	}
}

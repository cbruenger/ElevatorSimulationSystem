package factories;

import floor.FloorImpl;
import interfaces.FloorInterface;

public class FloorFactory {
	
	public static FloorInterface build(int floorNumber){
		return new FloorImpl(floorNumber);
	}

}

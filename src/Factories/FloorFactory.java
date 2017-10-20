package Factories;

import Floor.FloorImpl;
import Interfaces.FloorInterface;

public class FloorFactory {
	
	public static FloorInterface build(int floorNumber){
		return new FloorImpl(floorNumber);
	}

}

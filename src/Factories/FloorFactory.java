package Factories;

import Floor.FloorImpl;
import Interfaces.FloorInterface;

public class FloorFactory {
	
	public static FloorInterface build(String id){
		return new FloorImpl(id);
	}

}

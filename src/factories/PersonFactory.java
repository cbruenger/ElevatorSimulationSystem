package factories;

import interfaces.PersonInterface;
import person.PersonImpl;

public class PersonFactory {
	
	public static PersonInterface build(String id, int startFloor, int destinationFloor) {
		return new PersonImpl(id, startFloor, destinationFloor);
	}
}

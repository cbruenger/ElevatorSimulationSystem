package elevator;

public class ElevatorFactory{
	
	public static ElevatorInterface build(String id, int capacity){
		return new ElevatorImpl(id, capacity);
	}
}

package elevator;

public class Elevator_Factory1{
	
	public static Elevator_Interface build(int floor){
		return new Elevator_Impl(floor);
	}
}

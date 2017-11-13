package interfaces;

import java.util.ArrayList;
import elevator.ElevatorDTO;
import errors.*;
import enumerators.MyDirection;

public interface ElevatorInterface {
	
	public void update(long time);
	public void addPickupRequest(MyDirection direction, int floor) throws InvalidArgumentException;
	public ElevatorDTO getDTO() throws UnexpectedNullException;
}

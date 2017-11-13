package interfaces;

import elevator.ElevatorDTO;
import errors.InvalidArgumentException;
import errors.UnexpectedNullException;
import enumerators.MyDirection;

public interface ElevatorInterface {
	public void update(long time) throws InvalidArgumentException;
	public void addPickupRequest(MyDirection direction, int floor) throws InvalidArgumentException;
	public ElevatorDTO getDTO() throws UnexpectedNullException;
}

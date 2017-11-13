package interfaces;

import java.util.ArrayList;
import enumerators.MyDirection;
import errors.*;

public interface FloorInterface {
	public int getFloorNumber();
	public void addWaitingPerson(RiderInterface rider) throws AlreadyExistsException;
	public ArrayList<RiderInterface> getWaitersByDirection(MyDirection direction, int availableCapacity) throws InvalidArgumentException;
	public void addPersonToDecommissionedList(String id) throws InvalidArgumentException, AlreadyExistsException;
}

package interfaces;

import java.util.ArrayList;
import enumerators.MyDirection;
import errors.*;

public interface FloorInterface {
	
	public int getFloorNumber();
//	public ArrayList<RiderInterface> getRiders(); //Only for testing temporarily in our main
	public void removeRider(RiderInterface rider) throws CannotRemoveException;
	public void addRider(RiderInterface rider) throws AlreadyExistsException;
	public ArrayList<RiderInterface> getRidersByDirection(MyDirection direction);
}

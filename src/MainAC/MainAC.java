package MainAC;
import UserInputData.UserInputData;
import Building.Building;
import Facades.ElevatorFacade;
import gui.ElevatorDisplay;

public class MainAC {

	public static void main(String[] args) {
		
		UserInputData.getInstance();
		
		Building.getInstance();
		
		System.out.println("Number of floors in the building class: " + Building.getInstance().getNumFloors());
		System.out.println("Number of elevators in the building class: " + Building.getInstance().getNumElevators());
		System.out.println("Elevator speed in ElevatorFacade: " + ElevatorFacade.getInstance().getElevatorSpeed() + "ms");
		System.out.println("Elevator doors open time in ElevatorFacade: " + ElevatorFacade.getInstance().getDoorsOpenTime() + "ms");
		System.out.println("Elevator idle time in ElevatorFacade: " + ElevatorFacade.getInstance().getIdleTime() + "ms");
		
		System.out.print("Elevator IDs managed by elevator facade: ");
		for (String id : ElevatorFacade.getInstance().getElevatorIds()) {
			System.out.print(id + " ");
		}
		System.out.println();

	}

}

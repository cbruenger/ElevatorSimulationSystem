package Building;
import java.util.Scanner;

public final class Building {
	
	private static Building thisBuilding;
	private int numFloors;
	private int numElevators;
	
	
	private Building() {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the number of floors:");
		int numFloors = scanner.nextInt();
		System.out.println("Enter the number of elevators:");
		int numElevators = scanner.nextInt();
		scanner.close();
		this.setNumFloors(numFloors);
		this.setNumElevators(numElevators);
		
	}
	
	private void setNumFloors(int numFloors) {
		this.numFloors = numFloors;
	}
	
	private void setNumElevators(int numElevators) {
		this.numElevators = numElevators;
	}
	
	public static Building getInstance() {
		if (thisBuilding == null) thisBuilding = new Building();
		return thisBuilding;
	}
	
	public int getFloors() {
		return this.numFloors;
	}
	
	public int getElevators() {
		return this.numElevators;
	}
	
	public static void main(String[] args) {
		Building.getInstance();
		//Building building1 = Building.getInstance();
		System.out.printf("When accessed by class, there are %s floors\n",Building.getInstance().getFloors());
		System.out.printf("When accessed by class, there are %s elevators\n",Building.getInstance().getElevators());
		
	}

}

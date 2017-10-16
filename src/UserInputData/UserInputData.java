package UserInputData;
import java.util.Scanner;

public final class UserInputData {

	//Class variables
	private static UserInputData instance;
	private Scanner scanner;
	private int numFloors;
	private int numElevators;
	private int speed;
	private int doorsOpenTime;
	private int idleTime;
	
	//Constructor
	private UserInputData() {
		this.initializeScanner();
		this.getData();
		this.closeScanner();
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static UserInputData getInstance() {
		if (instance == null) instance = new UserInputData();
		return instance;
	}
	
	//Creates scanner
	private void initializeScanner() {
		this.scanner = new Scanner(System.in);
	}
	
	public void closeScanner() {
		this.scanner.close();
	}
	
	//A function to get user input, parse it to integers, and call the setters
	public void getData() {
		
		//Get input
		System.out.println("Enter the number of floors:\n");
		String numFloorsString = this.scanner.nextLine();
		System.out.println();
		System.out.println("Enter the number of elevators:\n");
		String numElevatorsString = this.scanner.nextLine();
		System.out.println();
		System.out.println("Enter the elevator's travel speed per floor in seconds:\n");
		String speedString = this.scanner.nextLine();
		System.out.println();
		System.out.println("Enter how long the doors should stay open in seconds:\n");
		String doorsOpenTimeString = this.scanner.nextLine();
		System.out.println();
		System.out.println("Enter the idle time before returning to floor 1 in seconds:\n");
		String idleTimeString = this.scanner.nextLine();
		System.out.println();
		
		//Handle the parsing of the input with errors
		int numFloors = Integer.parseInt(numFloorsString);
		int numElevators = Integer.parseInt(numElevatorsString);
		int speed = Integer.parseInt(speedString);
		int doorsOpenTime = Integer.parseInt(doorsOpenTimeString);
		int idleTime = Integer.parseInt(idleTimeString);
		
		//Set data variables
		this.setNumFloors(numFloors);
		this.setNumElevators(numElevators);
		this.setSpeed(speed);
		this.setDoorsOpenTime(doorsOpenTime);
		this.setIdleTime(idleTime);
	}
	
	////////////////////
	//				  //
	//    Setters     //
	//				  //
	////////////////////
	
	//A function to set the number of floors in the building
	private void setNumFloors(int numFloors) {
		// TODO error handling
		this.numFloors = numFloors;
	}
	
	//A function to set the number of elevators in the building
	private void setNumElevators(int numElevators) {
		// TODO error handling
		this.numElevators = numElevators;
	}
	
	//A function to set the speed at which an elevator moves between floors
	private void setSpeed(int speed) {
		// TODO error handling
		this.speed = speed;
	}
	
	//A function to set the amount of time an elevator's doors stay open
	private void setDoorsOpenTime(int doorsOpenTime) {
		// TODO error handling
		this.doorsOpenTime = doorsOpenTime;
	}
		
	//A function to set the amount of time an elevator stays idle until returning to floor 1
	private void setIdleTime(int idleTime) {
		// TODO error handling
		this.idleTime = idleTime;
	}
	
	////////////////////
	//				  //
	//    Getters     //
	//				  //
	////////////////////
	
	//A function to return the number of floors in the building
	public int getNumFloors() {
		return this.numFloors;
	}
	
	//A function to return the number of elevators in the building
	public int getNumElevators() {
		return this.numElevators;
	}
	
	//A function to get the speed an elevator travels between floors
	public int getSpeed() {
		return this.speed;
	}
	
	//A function to get the amount of time an elevators doors stay open for
	public int getDoorsOpenTime() {
		return this.doorsOpenTime;
	}
	
	//A function to get the amount of time an elevator stays idle before returning to floor 1
	public int getIdleTime() {
		return this.idleTime;
	}
	
}

package DataStore;
import java.util.Scanner;

public final class DataStore {

	//Class variables
	private static DataStore instance;
	private Scanner scanner;
	private int testNumber;
	private int numFloors;
	private int numElevators;
	private int speed;
	private int doorsOpenTime;
	private int idleTime;
	private int sleepTime;
	private int riderGenerationTime;
	private int duration;
	
	//Constructor
	private DataStore() {
		this.initializeScanner();
		this.getData();
		this.closeScanner();
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static DataStore getInstance() {
		if (instance == null) instance = new DataStore();
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
		
		// TODO handle funky situations with time. Make sure sleep time <= floor speed
		
		//Get input
		System.out.println("Enter the Test number (1 - 4) described in the the project assignment that you want to test:\n");
		String testNumberString = this.scanner.nextLine();
		System.out.println();
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
		System.out.println("Enter the sleep time in seconds:\n");
		String sleepTimeString = this.scanner.nextLine();
		System.out.println();
		System.out.println("Enter the occurrence at which new Riders should be generated in seconds:\n");
		String riderGenerationTimeString = this.scanner.nextLine();
		System.out.println();
		System.out.println("Enter the duration of the simulation in seconds:\n");
		String durationTimeString = this.scanner.nextLine();
		System.out.println();
		
		//Handle the parsing of the input with errors
		int testNumber = Integer.parseInt(testNumberString);
		int numFloors = Integer.parseInt(numFloorsString);
		int numElevators = Integer.parseInt(numElevatorsString);
		int speed = Integer.parseInt(speedString);
		int doorsOpenTime = Integer.parseInt(doorsOpenTimeString);
		int idleTime = Integer.parseInt(idleTimeString);
		int sleepTime = Integer.parseInt(sleepTimeString);
		int riderGenerationTime = Integer.parseInt(riderGenerationTimeString);
		int duration = Integer.parseInt(durationTimeString);
		
		//Set data variables
		this.setTestNumber(testNumber);
		this.setNumFloors(numFloors);
		this.setNumElevators(numElevators);
		this.setSpeed(speed);
		this.setDoorsOpenTime(doorsOpenTime);
		this.setIdleTime(idleTime);
		this.setSleepTime(sleepTime);
		this.setRiderGenerationTime(riderGenerationTime);
		this.setDuration(duration);
	}
	
	////////////////////
	//				  //
	//    Setters     //
	//				  //
	////////////////////
	
	//A function to set the test number described in the project description that the user wants to test
	private void setTestNumber(int testNumber) {
		// TODO error handling
		this.testNumber = testNumber;
	}
	
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
		this.speed = speed * 1000;
	}
	
	//A function to set the amount of time an elevator's doors stay open
	private void setDoorsOpenTime(int doorsOpenTime) {
		// TODO error handling
		this.doorsOpenTime = doorsOpenTime * 1000;
	}
		
	//A function to set the amount of time an elevator stays idle until returning to floor 1
	private void setIdleTime(int idleTime) {
		// TODO error handling
		this.idleTime = idleTime * 1000;
	}
	
	//A function to set the sleep time 
	private void setSleepTime(int sleepTime) {
		// TODO error handling
		this.sleepTime = sleepTime * 1000;
	}
	
	//A function to set the rider generation time 
	private void setRiderGenerationTime(int riderGenerationTime) {
		// TODO error handling
		this.riderGenerationTime = riderGenerationTime * 1000;
	}
	
	//A function to set the duration of the simulation
	private void setDuration(int duration) {
		// TODO error handling
		this.duration = duration * 1000;
	}
	
	////////////////////
	//				  //
	//    Getters     //
	//				  //
	////////////////////
	
	//A function to set the test number described in the project description that the user wants to test
	public int getTestNumber() {
		return this.testNumber;
	}
	
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
	
	//A function to get the sleep time
	public int getSleepTime() {
		return this.sleepTime;
	}
	
	//A function to get the rider generation time
	public int getRiderGenerationTime() {
		return this.riderGenerationTime;
	}
	
	//A function to get duration of the simulation
	public int getDurationMillis() {
		return this.duration;
	}
	
}

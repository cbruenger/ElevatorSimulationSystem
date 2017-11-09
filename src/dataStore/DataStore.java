package dataStore;

import errors.*;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class DataStore {

	//Class variables
	private static DataStore instance;
	//private Scanner scanner;
	//private int testNumber;
	private String numFloors;
	private String numElevators;
	private String elevatorCapacity;
	private String speed;
	private String doorsOpenTime;
	private String idleTime;
	private String sleepTime;
	private String ridersPerMinute;
	private String duration;
	
	//Constructor
	private DataStore() {
//		this.initializeScanner();
//		this.getData();
//		this.closeScanner();
		
		//New stuff
		try {
			this.initializeVariables();
			this.parseData();
			this.sufficientDataCheck();
		} catch (InputParsingException e1) {
			System.out.println(e1.getMessage());
		} catch (InsufficientInputException e2) {
			System.out.println(e2.getMessage());
		}
		
	}
	
	//A function to get an instance of the class. Initializes instance if needed
	public static DataStore getInstance() {
		if (instance == null) instance = new DataStore();
		return instance;
	}
	
	public void initializeVariables() {
		this.numFloors = null;
		this.numElevators = null;
		this.elevatorCapacity = null;
		this.speed = null;
		this.doorsOpenTime = null;
		this.idleTime = null;
		this.sleepTime = null;
		this.ridersPerMinute = null;
		this.duration = null;
	}
	
	public void parseData() throws InputParsingException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("inputData.xml");
			NodeList list = doc.getElementsByTagName("data");
			for (int i = 0; i < list.getLength(); i++) {
				Node item = list.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					Element data = (Element) item;
					NodeList dataItems = data.getChildNodes();
					for (int j = 0; j < dataItems.getLength(); j++) {
						Node dataItem = dataItems.item(j);
						if (dataItem.getNodeType() == Node.ELEMENT_NODE) {
							Element dataName = (Element) dataItem;
							try {
								switch (dataName.getTagName()) {
									case "numFloors": this.setNumFloors(dataName.getTextContent());
										break;
									case "numElevators": this.setNumElevators(dataName.getTextContent());
										break;
									case "elevatorCapacity": this.setElevatorCapacity(dataName.getTextContent());
										break;
									case "floorSpeed": this.setSpeed(dataName.getTextContent());
										break;
									case "doorsOpenTime": this.setDoorsOpenTime(dataName.getTextContent());
										break;
									case "idleTime": this.setIdleTime(dataName.getTextContent());
										break;
									case "sleepTime": this.setSleepTime(dataName.getTextContent());
										break;
									case "ridersPerMinute": this.setRidersPerMinute(dataName.getTextContent());
										break;
									case "duration":  this.setDuration(dataName.getTextContent());
										break;
							
								} 
							} catch (InputParsingException e) {
								System.out.println(e.getMessage());
							}
						}
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new InputParsingException("Caught a system parsing error when parsing XML file\n");
		}
	}
	
	public void sufficientDataCheck() throws InsufficientInputException {
		if (this.numFloors == null) throw new InsufficientInputException("numFloors not acquired from parsing XML file\n");
		if (this.numElevators == null) throw new InsufficientInputException("numElevators not acquired from parsing XML file\n");
		if (this.elevatorCapacity == null) throw new InsufficientInputException("elevatorCapacity not acquired from parsing XML file\n");
		if (this.speed == null) throw new InsufficientInputException("speed not acquired from parsing XML file\n");
		if (this.doorsOpenTime == null) throw new InsufficientInputException("doorsOpenTime not acquired from parsing XML file\n");
		if (this.idleTime == null) throw new InsufficientInputException("idleTime not acquired from parsing XML file\n");
		if (this.sleepTime == null) throw new InsufficientInputException("sleepTime not acquired from parsing XML file\n");
		if (this.ridersPerMinute == null) throw new InsufficientInputException("ridersPerMinute not acquired from parsing XML file\n");
		if (this.duration == null) throw new InsufficientInputException("duration not acquired from parsing XML file\n");
	}
	
	//Creates scanner
//	private void initializeScanner() {
//		this.scanner = new Scanner(System.in);
//	}
//	
//	public void closeScanner() {
//		this.scanner.close();
//	}
	
	//A function to get user input, parse it to integers, and call the setters
//	public void getData() {
//		// Explanation of Hardcoding
//		System.out.println("For phase 1 we've hard coded the tests into the DataStore so\nyou only need to select which test (1-4) you wish to simulate.\nWe've not done any error handling up to this point, but plan on\ncombing over everything we've done so far and adding these exceptions\nbefore continuing to Phase 2.  -Aldo OG & Craig B\n\n");
//		
//		System.out.println("Hard coded values are as follows:\n\nNumber of Floors = 20\nNumber of Elevators = 4\nElevator Travel Speed = 1,000ms\nDoors Open Time = NA(Currently...)\nElevator Idle Time = 5,000ms\nSleep Time = 1,000ms\nDuration of Simulation = 100,000ms\n\n");
//		// TODO handle funky situations with time. Make sure sleep time <= floor speed
//		
//		//Get input
//		System.out.println("Enter the Test number (1 - 4) described in the the project assignment that you want to test:\n");
//		String testNumberString = this.scanner.nextLine();
//		System.out.println();
////		System.out.println("Enter the number of floors:\n");
////		String numFloorsString = this.scanner.nextLine();
////		System.out.println();
////		System.out.println("Enter the number of elevators:\n");
////		String numElevatorsString = this.scanner.nextLine();
////		System.out.println();
////		System.out.println("Enter the elevator's travel speed per floor in seconds:\n");
////		String speedString = this.scanner.nextLine();
////		System.out.println();
//////		System.out.println("Enter how long the doors should stay open in seconds:\n");
//////		String doorsOpenTimeString = this.scanner.nextLine();
//////		System.out.println();
////		System.out.println("Enter the idle time before returning to floor 1 in seconds:\n");
////		String idleTimeString = this.scanner.nextLine();
////		System.out.println();
////		System.out.println("Enter the sleep time in seconds:\n");
////		String sleepTimeString = this.scanner.nextLine();
////		System.out.println();
//////		System.out.println("Enter the occurrence at which new Riders should be generated in seconds:\n");
//////		String riderGenerationTimeString = this.scanner.nextLine();
//////		System.out.println();
////		System.out.println("Enter the duration of the simulation in seconds:\n");
////		String durationTimeString = this.scanner.nextLine();
////		System.out.println();
//		
//		//Handle the parsing of the input with errors
//		int testNumber = Integer.parseInt(testNumberString);
////		int numFloors = Integer.parseInt(numFloorsString);
////		int numElevators = Integer.parseInt(numElevatorsString);
////		int speed = Integer.parseInt(speedString);
//////		int doorsOpenTime = Integer.parseInt(doorsOpenTimeString);
////		int idleTime = Integer.parseInt(idleTimeString);
////		int sleepTime = Integer.parseInt(sleepTimeString);
//////		int riderGenerationTime = Integer.parseInt(riderGenerationTimeString);
////		int duration = Integer.parseInt(durationTimeString);
//		
//		//Set data variables
//		this.setTestNumber(testNumber);
////		this.setNumFloors(numFloors);
////		this.setNumElevators(numElevators);
////		this.setSpeed(speed);
//////		this.setDoorsOpenTime(doorsOpenTime);
////		this.setIdleTime(idleTime);
////		this.setSleepTime(sleepTime);
//////		this.setRiderGenerationTime(riderGenerationTime);
////		this.setDuration(duration);
//	}
	
	////////////////////
	//				  //
	//    Setters     //
	//				  //
	////////////////////
	
	//A function to set the test number described in the project description that the user wants to test
//	private void setTestNumber(int testNumber) {
//		// TODO error handling
//		this.testNumber = testNumber;
//	}
	
	//A function to set the number of floors in the building
	private void setNumFloors(String numFloors) throws InputParsingException {
		if (numFloors == null || numFloors.isEmpty()) {
			throw new InputParsingException("Could not set numFloors due to null or empty value in XML file\n");
		}
		this.numFloors = numFloors;
	}
	
	//A function to set the number of elevators in the building
	private void setNumElevators(String numElevators) throws InputParsingException {
		if (numElevators == null || numElevators.isEmpty()) {
			throw new InputParsingException("Could not set numElevators due to null or empty value in XML file\n");
		}
		this.numElevators = numElevators;
	}
	
	//A function to set the capacity of elevators
	private void setElevatorCapacity(String capacity) throws InputParsingException {
		if (capacity == null || capacity.isEmpty()) {
			throw new InputParsingException("Could not set capacity due to null or empty value in XML file\n");
		}
		this.elevatorCapacity = capacity;
	}
	
	//A function to set the speed at which an elevator moves between floors
	private void setSpeed(String speed) throws InputParsingException {
		if (speed == null || speed.isEmpty()) {
			throw new InputParsingException("Could not set speed due to null or empty value in XML file\n");
		}
		this.speed = speed;
	}
	
	//A function to set the amount of time an elevator's doors stay open
	private void setDoorsOpenTime(String doorsOpenTime) throws InputParsingException {
		if (doorsOpenTime == null || doorsOpenTime.isEmpty()) {
			throw new InputParsingException("Could not set doorsOpenTime due to null or empty value in XML file\n");
		}
		this.doorsOpenTime = doorsOpenTime;
	}
		
	//A function to set the amount of time an elevator stays idle until returning to floor 1
	private void setIdleTime(String idleTime) throws InputParsingException {
		if (idleTime == null || idleTime.isEmpty()) {
			throw new InputParsingException("Could not set idleTime due to null or empty value in XML file\n");
		}
		this.idleTime = idleTime;
	}
	
	//A function to set the sleep time 
	private void setSleepTime(String sleepTime) throws InputParsingException {
		if (sleepTime == null || sleepTime.isEmpty()) {
			throw new InputParsingException("Could not set sleepTime due to null or empty value in XML file\n");
		}
		this.sleepTime = sleepTime;
	}
	
	//A function to set the rider generation time 
	private void setRidersPerMinute(String ridersPerMinute) throws InputParsingException {
		if (ridersPerMinute == null || ridersPerMinute.isEmpty()) {
			throw new InputParsingException("Could not set ridersPerMinute due to null or empty value in XML file\n");
		}
		this.ridersPerMinute = ridersPerMinute;
	}
	
	//A function to set the duration of the simulation
	private void setDuration(String duration) throws InputParsingException {
		if (duration == null || duration.isEmpty()) {
			throw new InputParsingException("Could not set duration due to null or empty value in XML file\n");
		}
		this.duration = duration;
	}
	
	////////////////////
	//				  //
	//    Getters     //
	//				  //
	////////////////////
	
	//A function to set the test number described in the project description that the user wants to test
//	public int getTestNumber() {
//		return this.testNumber;
//	}
	
	//A function to return the number of floors in the building
	public String getNumFloors() {
		return this.numFloors;
	}
	
	//A function to return the number of elevators in the building
	public String getNumElevators() {
		return this.numElevators;
	}
	
	//A function to return the max capacity of elevators
	public String getElevatorCapacity() {
		return this.elevatorCapacity;
	}
	
	//A function to get the speed an elevator travels between floors
	public String getSpeed() {
		return this.speed;
	}
	
	//A function to get the amount of time an elevators doors stay open for
	public String getDoorsOpenTime() {
		return this.doorsOpenTime;
	}
	
	//A function to get the amount of time an elevator stays idle before returning to floor 1
	public String getIdleTime() {
		return this.idleTime;
	}
	
	//A function to get the sleep time
	public String getSleepTime() {
		return this.sleepTime;
	}
	
	//A function to get the rider generation time
	public String getRidersPerMinute() {
		return this.ridersPerMinute;
	}
	
	//A function to get duration of the simulation
	public String getDuration() {
		return this.duration;
	}
	
}

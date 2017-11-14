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

/* The DataStore class retrieves data from an XML input file and
 * saves the parsed data as strings in the class variables. Sets 
 * and verifies that all necessary data was retrieved. Contains
 * getters for other classes to retrieve the data.
 */
public final class DataStore {

	//Class variables
	private static DataStore instance;
	private String numFloors;
	private String numElevators;
	private String elevatorCapacity;
	private String elevatorFloorSpeed;
	private String doorsOpenTime;
	private String idleTime;
	private String sleepTime;
	private String peoplePerMinute;
	private String duration;
	
	/*////////////////////////////////////////////////////
	 * 													*
	 * 		Constructor and Singleton Instance Getter	*
	 * 													*
	 *////////////////////////////////////////////////////
	
	//Constructor, initializes necessary components
	private DataStore() {
		try {
			this.initializeVariables();
			this.parseInputFile();
			this.sufficientDataCheck();
		} catch (InputParsingException e1) {
			System.out.println(e1.getMessage());
		} catch (InsufficientInputException e2) {
			System.out.println(e2.getMessage());
		}
		
	}
	
	//Returns the instance of this class, initializes if 1st time called
	public static DataStore getInstance() {
		if (instance == null) instance = new DataStore();
		return instance;
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Methods called by the Constructor		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Initializes all class variables to null
	public void initializeVariables() {
		this.numFloors = null;
		this.numElevators = null;
		this.elevatorCapacity = null;
		this.elevatorFloorSpeed = null;
		this.doorsOpenTime = null;
		this.idleTime = null;
		this.sleepTime = null;
		this.peoplePerMinute = null;
		this.duration = null;
	}
	
	//Parses the input file and calls setters for assigning class variables
	public void parseInputFile() throws InputParsingException {
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
									case "elevatorFloorSpeed": this.setElevatorFloorSpeed(dataName.getTextContent());
										break;
									case "doorsOpenTime": this.setDoorsOpenTime(dataName.getTextContent());
										break;
									case "idleTime": this.setIdleTime(dataName.getTextContent());
										break;
									case "sleepTime": this.setSleepTime(dataName.getTextContent());
										break;
									case "peoplePerMinute": this.setPeoplePerMinute(dataName.getTextContent());
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
	
	//Verifies that all necessary data was retrieved from input file
	public void sufficientDataCheck() throws InsufficientInputException {
		if (this.numFloors == null) throw new InsufficientInputException("numFloors not acquired from parsing XML file\n");
		if (this.numElevators == null) throw new InsufficientInputException("numElevators not acquired from parsing XML file\n");
		if (this.elevatorCapacity == null) throw new InsufficientInputException("elevatorCapacity not acquired from parsing XML file\n");
		if (this.elevatorFloorSpeed == null) throw new InsufficientInputException("elevatorFloorSpeed not acquired from parsing XML file\n");
		if (this.doorsOpenTime == null) throw new InsufficientInputException("doorsOpenTime not acquired from parsing XML file\n");
		if (this.idleTime == null) throw new InsufficientInputException("idleTime not acquired from parsing XML file\n");
		if (this.sleepTime == null) throw new InsufficientInputException("sleepTime not acquired from parsing XML file\n");
		if (this.peoplePerMinute == null) throw new InsufficientInputException("peoplePerMinute not acquired from parsing XML file\n");
		if (this.duration == null) throw new InsufficientInputException("duration not acquired from parsing XML file\n");
	}
	
	/*////////////////////////////////////////////////////////////
	 * 															*
	 * 		Methods for assigning parsed data into variables		*
	 * 															*
	 *////////////////////////////////////////////////////////////
	
	//Assigns the numFloors variable
	private void setNumFloors(String numFloors) throws InputParsingException {
		if (numFloors == null || numFloors.isEmpty()) {
			throw new InputParsingException("Could not set numFloors due to null or empty value in XML file\n");
		}
		this.numFloors = numFloors;
	}
	
	//Assigns the numElevators variable
	private void setNumElevators(String numElevators) throws InputParsingException {
		if (numElevators == null || numElevators.isEmpty()) {
			throw new InputParsingException("Could not set numElevators due to null or empty value in XML file\n");
		}
		this.numElevators = numElevators;
	}
	
	//Assigns the elevatorCapacity variable
	private void setElevatorCapacity(String capacity) throws InputParsingException {
		if (capacity == null || capacity.isEmpty()) {
			throw new InputParsingException("Could not set capacity due to null or empty value in XML file\n");
		}
		this.elevatorCapacity = capacity;
	}
	
	//Assigns the elevatorFloorSpeed variable
	private void setElevatorFloorSpeed(String speed) throws InputParsingException {
		if (speed == null || speed.isEmpty()) {
			throw new InputParsingException("Could not set elevatorFloorSpeed due to null or empty value in XML file\n");
		}
		this.elevatorFloorSpeed = speed;
	}
	
	//Assigns the doorsOpenTime variable
	private void setDoorsOpenTime(String doorsOpenTime) throws InputParsingException {
		if (doorsOpenTime == null || doorsOpenTime.isEmpty()) {
			throw new InputParsingException("Could not set doorsOpenTime due to null or empty value in XML file\n");
		}
		this.doorsOpenTime = doorsOpenTime;
	}
		
	//Assigns the idleTime variable
	private void setIdleTime(String idleTime) throws InputParsingException {
		if (idleTime == null || idleTime.isEmpty()) {
			throw new InputParsingException("Could not set idleTime due to null or empty value in XML file\n");
		}
		this.idleTime = idleTime;
	}
	
	//Assigns the sleepTime variable
	private void setSleepTime(String sleepTime) throws InputParsingException {
		if (sleepTime == null || sleepTime.isEmpty()) {
			throw new InputParsingException("Could not set sleepTime due to null or empty value in XML file\n");
		}
		this.sleepTime = sleepTime;
	}
	
	//Assigns the peoplePerMinute variable
	private void setPeoplePerMinute(String peoplePerMinute) throws InputParsingException {
		if (peoplePerMinute == null || peoplePerMinute.isEmpty()) {
			throw new InputParsingException("Could not set peoplePerMinute due to null or empty value in XML file\n");
		}
		this.peoplePerMinute = peoplePerMinute;
	}
	
	//Assigns the duration variable
	private void setDuration(String duration) throws InputParsingException {
		if (duration == null || duration.isEmpty()) {
			throw new InputParsingException("Could not set duration due to null or empty value in XML file\n");
		}
		this.duration = duration;
	}
	
	/*////////////////////////////
	 * 							*
	 * 		Getter Methods		*
	 * 							*
	 *////////////////////////////
	
	//Returns the number of floors in the building
	public String getNumFloors() {
		return this.numFloors;
	}
	
	//Returns the number of elevators in the building
	public String getNumElevators() {
		return this.numElevators;
	}
	
	//Returns the max capacity of elevators
	public String getElevatorCapacity() {
		return this.elevatorCapacity;
	}
	
	//Returns the speed an elevator travels between floors
	public String getElevatorFloorSpeed() {
		return this.elevatorFloorSpeed;
	}
	
	//Returns the amount of time an elevators doors stay open for
	public String getDoorsOpenTime() {
		return this.doorsOpenTime;
	}
	
	//Returns the amount of time an elevator stays idle before returning to floor 1
	public String getIdleTime() {
		return this.idleTime;
	}
	
	//Returns the sleep time
	public String getSleepTime() {
		return this.sleepTime;
	}
	
	//Returns the number of people to generate per minute
	public String getPeoplePerMinute() {
		return this.peoplePerMinute;
	}
	
	//Returns duration of the simulation
	public String getDuration() {
		return this.duration;
	}
}

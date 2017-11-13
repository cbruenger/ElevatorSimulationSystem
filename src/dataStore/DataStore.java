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

	private static DataStore instance;
	private String numFloors;
	private String numElevators;
	private String elevatorCapacity;
	private String speed;
	private String doorsOpenTime;
	private String idleTime;
	private String sleepTime;
	private String ridersPerMinute;
	private String duration;
	
	/*////////////////////////////////////////////////////
	 * 													*
	 * 		Constructor and Singleton Instance Getter	*
	 * 													*
	 *////////////////////////////////////////////////////
	
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
	
	public static DataStore getInstance() {
		if (instance == null) instance = new DataStore();
		return instance;
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Methods called by the Constructor		*
	 * 												*
	 *////////////////////////////////////////////////
	
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
	
	/*////////////////////////////////////////////////////////////
	 * 															*
	 * 		Methods for assigning parsed data into vaiables		*
	 * 															*
	 *////////////////////////////////////////////////////////////
	
	//Sets the number of floors in the building
	private void setNumFloors(String numFloors) throws InputParsingException {
		if (numFloors == null || numFloors.isEmpty()) {
			throw new InputParsingException("Could not set numFloors due to null or empty value in XML file\n");
		}
		this.numFloors = numFloors;
	}
	
	//Sets the number of elevators in the building
	private void setNumElevators(String numElevators) throws InputParsingException {
		if (numElevators == null || numElevators.isEmpty()) {
			throw new InputParsingException("Could not set numElevators due to null or empty value in XML file\n");
		}
		this.numElevators = numElevators;
	}
	
	//Sets the capacity of elevators
	private void setElevatorCapacity(String capacity) throws InputParsingException {
		if (capacity == null || capacity.isEmpty()) {
			throw new InputParsingException("Could not set capacity due to null or empty value in XML file\n");
		}
		this.elevatorCapacity = capacity;
	}
	
	//Sets the speed at which an elevator moves between floors
	private void setSpeed(String speed) throws InputParsingException {
		if (speed == null || speed.isEmpty()) {
			throw new InputParsingException("Could not set speed due to null or empty value in XML file\n");
		}
		this.speed = speed;
	}
	
	//Sets the amount of time an elevator's doors stay open
	private void setDoorsOpenTime(String doorsOpenTime) throws InputParsingException {
		if (doorsOpenTime == null || doorsOpenTime.isEmpty()) {
			throw new InputParsingException("Could not set doorsOpenTime due to null or empty value in XML file\n");
		}
		this.doorsOpenTime = doorsOpenTime;
	}
		
	//Sets the amount of time an elevator stays idle until returning to floor 1
	private void setIdleTime(String idleTime) throws InputParsingException {
		if (idleTime == null || idleTime.isEmpty()) {
			throw new InputParsingException("Could not set idleTime due to null or empty value in XML file\n");
		}
		this.idleTime = idleTime;
	}
	
	//Sets the sleep time 
	private void setSleepTime(String sleepTime) throws InputParsingException {
		if (sleepTime == null || sleepTime.isEmpty()) {
			throw new InputParsingException("Could not set sleepTime due to null or empty value in XML file\n");
		}
		this.sleepTime = sleepTime;
	}
	
	//Sets the rider generation time 
	private void setRidersPerMinute(String ridersPerMinute) throws InputParsingException {
		if (ridersPerMinute == null || ridersPerMinute.isEmpty()) {
			throw new InputParsingException("Could not set ridersPerMinute due to null or empty value in XML file\n");
		}
		this.ridersPerMinute = ridersPerMinute;
	}
	
	//Sets the duration of the simulation
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
	public String getSpeed() {
		return this.speed;
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
	
	//Returns the rider generations per minute
	public String getRidersPerMinute() {
		return this.ridersPerMinute;
	}
	
	//Returns duration of the simulation
	public String getDuration() {
		return this.duration;
	}
	
}

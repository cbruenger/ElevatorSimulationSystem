package mainAC;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import building.Building;
import dataStore.DataStore;
import errors.InputParsingException;
import timeProcessor.TimeProcessor;

public class MainAC {

	public static void main(String[] args) {
		
		DataStore.getInstance();
		
		Building.getInstance();
				
		
		TimeProcessor.getInstance().begin();
		
		
		
		
		
		
		
//		Building.getInstance();
//		
//		FloorFacade.getInstance();
//		
//		RiderFacade.getInstance();
//		
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		RiderFacade.getInstance().generateRandomRider();
//		
//		
//		System.out.println("Number of floors in the Data Store: " + DataStore.getInstance().getNumFloors());
//		System.out.println("Number of elevators in the Data Store: " + DataStore.getInstance().getNumElevators());
//		System.out.println();
//		System.out.println("Elevator speed in ElevatorFacade: " + ElevatorFacade.getInstance().getElevatorSpeed());
//		System.out.println("Elevator doors open time in ElevatorFacade: " + ElevatorFacade.getInstance().getDoorsOpenTime());
//		System.out.println("Elevator idle time in ElevatorFacade: " + ElevatorFacade.getInstance().getIdleTime());
//		System.out.println();
//		
//		System.out.print("Elevator IDs managed by elevator facade: ");
//		for (String id : ElevatorFacade.getInstance().getElevatorIds()) {
//			System.out.print(id + " ");
//		}
//		System.out.println();
//		
//		System.out.print("Rider IDs managed by rider facade: ");
//		for (String id : RiderFacade.getInstance().getRiderIds()) {
//			System.out.print(id + " ");
//		}
//		System.out.println();
//		
//		System.out.print("Floor IDs managed by floor facade: ");
//		for (String id : FloorFacade.getInstance().getFloorIds()) {
//			System.out.print(id + " ");
//		}
//		System.out.println("\n");
//		
//		
//		//Prints floors with riders 
//		for (String fId : FloorFacade.getInstance().getFloorIds()) {
//			if (!FloorFacade.getInstance().getFloors().get(fId).getRiderIds().isEmpty()) {
//				System.out.print("f" + fId + " contains ");
//				for (String rId : FloorFacade.getInstance().getFloors().get(fId).getRiderIds()) {
//					System.out.print(rId + " ");
//
//				}
//				System.out.println();
//			}
//		}
//		System.out.println();
//		
//		//Prints riders and the floors they're on
//		for (String rId : RiderFacade.getInstance().getRiderIds()) {
//			System.out.println("Rider " + rId + "'s start floor is f" + RiderFacade.getInstance().getRiders().get(rId).getStartFloor() + " and destination floor is " + RiderFacade.getInstance().getRiders().get(rId).getDestinationFloor());
//		}
//		System.out.println();

	}

}

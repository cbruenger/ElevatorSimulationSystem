package controller;

import building.Building;
import elevator.ElevatorDTO;
import dataStore.DataStore;
import enumerators.MyDirection;
import static enumerators.MyDirection.*;

import java.util.ArrayList;

import errors.BadInputDataException;
import errors.InvalidArgumentException;

public class ElevatorController {
	private int numFloors;
	private int numElevators;
	private static ElevatorController instance;
	
	private ElevatorController(){
		try {getData();}
		catch(BadInputDataException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static ElevatorController getInstance() {
		if (instance == null) instance = new ElevatorController();
		return instance;
	}
	
	private void getData() throws BadInputDataException {
		this.setNumFloors();
		this.setNumElevators();
	}
	
	private void setNumFloors() throws BadInputDataException {
		try { 
			int temp = Integer.parseInt(DataStore.getInstance().getNumFloors()); 
			if (temp > 1)
				this.numFloors = temp;
			else
				throw new BadInputDataException("Elevator Controller cannot accept a value less than 2 for numFloors\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("Elevator Controller could not parse DataStore's numFloors value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("Elevator Controller received null from DataStore for numFloors value\n"); 
	    }
	}
	
	private void setNumElevators() throws BadInputDataException {
		try { 
			int temp = Integer.parseInt(DataStore.getInstance().getNumElevators());
			if (temp > 0)
				this.numElevators = temp;
			else
				throw new BadInputDataException("Elevator Controller cannot accept a value less than 1 for numElevators\n");
	    } catch (NumberFormatException e) { 
	        throw new BadInputDataException("Elevator Controller could not parse DataStore's numElevators value to int\n"); 
	    } catch(NullPointerException e) {
	        throw new BadInputDataException("Elevator Controller received null from DataStore for numElevators value\n"); 
	    }
	}
	
	
	public void pickupRequest(int floor, MyDirection direction) throws InvalidArgumentException {
			
		//Throw error if floor is invalid
		if (floor < 1 || floor > this.numFloors) {
			throw new InvalidArgumentException("ElevatorController's pickupRequest method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
		}
		
		//Throw error if direction is null or IDLE
		if (direction == null || direction == IDLE) {
			throw new InvalidArgumentException("ElevatorController's pickupRequest method cannot accept null or IDLE for direction parameter\n");
		}
		
		ArrayList<ElevatorDTO> elevatorDTOs = this.getElevatorDTOs();
		
		//check if any elevator is idle first
		for (int i = 0; i < this.numElevators; i++) {
			if (elevatorDTOs.get(i).getDirection() == IDLE && elevatorDTOs.get(i).getPendingDirection() == IDLE) {
				Building.getInstance().assignElevatorForPickup(floor, direction, elevatorDTOs.get(i).getElevatorNumber());
				return;
			}
		}
		
		Building.getInstance().assignElevatorForPickup(floor, direction, 1);
	}
	
	private ArrayList<ElevatorDTO> getElevatorDTOs() {
		ArrayList<ElevatorDTO> elevatorDTOs = new ArrayList<ElevatorDTO>();
		//Get the DTO's
		for (int i = 1; i <= this.numElevators; i++) {
			elevatorDTOs.add(Building.getInstance().getElevatorDTO(i));
		}
		return elevatorDTOs;
	}
	
	
}

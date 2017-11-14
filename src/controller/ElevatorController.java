package controller;

import building.Building;
import java.util.HashMap;
import elevator.ElevatorDTO;
import dataStore.DataStore;
import enumerators.MyDirection;
import static enumerators.MyDirection.*;
import errors.*;
import java.util.ArrayList;
import errors.BadInputDataException;
import errors.InvalidArgumentException;

/* The ElevatorController class assigns elevator requests made on
 * the floors of the building. Retrieves DTOs for the elevators
 * from the building and attempts to assign the request. Keeps 
 * a list of pending requests from previous cycles. Also called
 * by the TimeProcessor during every cycle to attempt assigning
 * pending requests.
 */
public class ElevatorController {
	
	//Class variables, pending requests data structure, static instance
	private int numFloors;
	private int numElevators;
	private ArrayList<HashMap<Integer, MyDirection>> pendingRequests;
	private static ElevatorController instance;
	
	/*////////////////////////////////////////////////////
	 * 													*
	 * 		Constructor and Singleton Instance Getter	*
	 * 													*
	 *////////////////////////////////////////////////////
	
	//Constructor, initializes necessary components
	private ElevatorController(){
		try {
			this.setNumFloors();
			this.setNumElevators();
			this.createPendingRequestsArrayList();
		} catch (BadInputDataException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (AlreadyExistsException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
	
	//Returns the instance of this class, initializes if 1st time called
	public static ElevatorController getInstance() {
		if (instance == null) instance = new ElevatorController();
		return instance;
	}
	
	/*////////////////////////////////////////////////
	 * 												*
	 * 		Methods called by the Constructor		*
	 * 												*
	 *////////////////////////////////////////////////
	
	//Accesses DataStore and parses the number of floors to an int, checks validity and assigns class variable
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
	
	//Accesses DataStore and parses the number of elevators to an int, checks validity and assigns class variable
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
	
	//Creates the ArrayList to contain pending elevator requests that weren't assigned in the previous cycle(s)
	private void createPendingRequestsArrayList() throws AlreadyExistsException {
		if (this.pendingRequests != null) {
			throw new AlreadyExistsException("The pendingRequests ArrayList has already been created in ElevatorController\n");
		}
		this.pendingRequests = new ArrayList<HashMap<Integer, MyDirection>>();
	}
	
	/*////////////////////////////////////////////////////////////
	 * 															*
	 * 		Elevator Request and Assignment Handler Methods		*
	 * 															*
	 *////////////////////////////////////////////////////////////
	
	//Builds and returns an ArrayList of ElevatorDTO objects retrieved from the building
	private ArrayList<ElevatorDTO> getElevatorDTOs() throws UnexpectedNullException {
		try {
			ArrayList<ElevatorDTO> elevatorDTOs = new ArrayList<ElevatorDTO>();
			//Get the DTO's
			for (int i = 1; i <= this.numElevators; i++) {
				ElevatorDTO elevatorDTO = Building.getInstance().getElevatorDTO(i);
				if (elevatorDTO == null) {
					throw new UnexpectedNullException("ElevatorController's getElevatorDTOs method received null for elevator " + i + "\n"); 
				}
				elevatorDTOs.add(elevatorDTO);
			}
			return elevatorDTOs;
		} catch (InvalidArgumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	/* Called when a button is pushed from a given floor for a given direction
	 * Also called every wake cycle for every pending request that wasn't assigned
	 * during the previous attempt */
	public void pickupRequest(int floor, MyDirection direction) throws InvalidArgumentException, UnexpectedNullException {
			
		//Throw error if floor is invalid
		if (floor < 1 || floor > this.numFloors) {
			throw new InvalidArgumentException("ElevatorController's pickupRequest method cannot accept floor numbers less than 1 or greater than " + this.numFloors + "\n");
		}
		
		//Throw error if direction is null or IDLE
		if (direction == null || direction == IDLE) {
			throw new InvalidArgumentException("ElevatorController's pickupRequest method cannot accept null or IDLE for direction parameter\n");
		}
		
		//Throw error if failed to retrieve elevatorDTOs
		ArrayList<ElevatorDTO> elevatorDTOs = this.getElevatorDTOs();
		if (elevatorDTOs == null) {
			throw new UnexpectedNullException("ElevatorController's pickupRequest cannot use null elevatorDTOs object\n");
		}
		
		/* Check If any elevator is traveling the same direction or if elevator is empty with no pending direction,
		 *  assign the closest one that isn't already on the current floor */
		int elevatorToAssignIndex = -1;
		for (int i = 0; i < this.numElevators; i++) {
			
			//Boolean true if elevator's direction and pending direction are the same as passed direction arg
			boolean directionsMatch = (elevatorDTOs.get(i).getDirection() == direction) && (elevatorDTOs.get(i).getPendingDirection() == direction);
			
			//Boolean true if the elevator has no pickUps or dropOffs in any direction
			boolean nothingToDo = elevatorDTOs.get(i).getUpPickups().isEmpty() 
									&& elevatorDTOs.get(i).getDownPickups().isEmpty()
									&& elevatorDTOs.get(i).getDropOffs().isEmpty();
			
			//Boolean true if elevator's current direction and pending direction are both IDLE
			boolean idleIdle = (elevatorDTOs.get(i).getDirection() == IDLE) && (elevatorDTOs.get(i).getPendingDirection() == IDLE);
			
			//If elevator is either traveling in the same direction or has no pickUps/DropOffs
			if (directionsMatch || nothingToDo || idleIdle) {
				
				//Check UP
				if (direction == UP) {
					
					//Check if elevator's floor is less than current floor
					if (elevatorDTOs.get(i).getCurrentFloor() < floor) { 
						
						//Check if no satisfying elevator has been found yet and update the elevatorToAssignIndex
						if (elevatorToAssignIndex == -1) {
							elevatorToAssignIndex = i;
							
						//Otherwise check if this elevator is closer than the elevatorToAssignIndex
						} else {
							if (floor - elevatorDTOs.get(i).getCurrentFloor() < floor - elevatorDTOs.get(elevatorToAssignIndex).getCurrentFloor()) {
								
								//If the elevator is on the current floor and has no requests or has current/pending directions IDLE update the elevatorToAssignIndex
								if (elevatorDTOs.get(i).getCurrentFloor() == floor) {
									if (nothingToDo || idleIdle) {
										elevatorToAssignIndex = i;
									}
									
								//Otherwise it's not on the current floor, update the elevatorToAssignIndex
								} else if (elevatorDTOs.get(i).getCurrentFloor() != floor) {
									elevatorToAssignIndex = i;
								}
							}
						}
					}
					
				//Check DOWN
				} else if (direction == DOWN) {
					
					//Check if elevator's floor is greater than current floor
					if (elevatorDTOs.get(i).getCurrentFloor() > floor) { 
						
						//Check if no satisfying elevator has been found yet and update the elevatorToAssignIndex
						if (elevatorToAssignIndex == -1) {
							elevatorToAssignIndex = i;
						
						//Otherwise check if this elevator is closer than the elevatorToAssignIndex
						} else {
							if (elevatorDTOs.get(i).getCurrentFloor() - floor < elevatorDTOs.get(elevatorToAssignIndex).getCurrentFloor() - floor) {

								//If the elevator is on the current floor and has not requests or has current/pending directions IDLE update the elevatorToAssignIndex
								if (elevatorDTOs.get(i).getCurrentFloor() == floor) {
									if (nothingToDo || idleIdle) {
										elevatorToAssignIndex = i;
									}
									
								//Otherwise it's not on the current floor, update the elevatorToAssignIndex
								} else if (elevatorDTOs.get(i).getCurrentFloor() != floor) {
									elevatorToAssignIndex = i;
								} 
							}
						}	
					}
				}
			}
		}
		
		//If an elevator was found that fit the previous description assign it the request
		if (elevatorToAssignIndex != -1) {
			Building.getInstance().assignElevatorForPickup(floor, direction, elevatorDTOs.get(elevatorToAssignIndex).getElevatorNumber());
			return;
		}
		
		//If no elevator was found during the previous method, check if any elevators' current and pending directions are idle
		for (int i = 0; i < this.numElevators; i++) {
			if (elevatorDTOs.get(i).getDirection() == IDLE && elevatorDTOs.get(i).getPendingDirection() == IDLE) {
				Building.getInstance().assignElevatorForPickup(floor, direction, elevatorDTOs.get(i).getElevatorNumber());
				return;
			}
		}
		
		//Otherwise add this request to the pending requests
		HashMap<Integer, MyDirection> pendingRequest = new HashMap<Integer, MyDirection>();
		pendingRequest.put(floor, direction);
		this.pendingRequests.add(pendingRequest);
		
	}
	
	/* Called by the TimeProcessor during every wake cycle to try assigning pending requests
	 * Makes a copy of the pendingRequests ArrayList to iterate through and deletes them
	 * from the original pendingRequests list (at index 0) before trying the request again */
	public void tryPendingRequests() throws UnexpectedNullException {
		if (this.pendingRequests == null)
			throw new UnexpectedNullException("ElevatorController's pendingRequests ArrayList is null when tryPendingRequests method called\n");
		try {
			ArrayList<HashMap<Integer, MyDirection>> pendingRequestsCopy = new ArrayList<HashMap<Integer, MyDirection>>(this.pendingRequests);
			for (int i = 0; i < pendingRequestsCopy.size(); i++) {
				for (int elevatorNumber : pendingRequestsCopy.get(i).keySet()) {
					this.pendingRequests.remove(0);
					this.pickupRequest(elevatorNumber, pendingRequestsCopy.get(i).get(elevatorNumber));
				}
			}
		} catch (InvalidArgumentException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.exit(-1);
		} catch (UnexpectedNullException e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
			System.exit(-1);
		}
	}
}

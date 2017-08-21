package com.tingco.codechallenge.elevator.api;

/**
 * Interface for an elevator object.
 * 
 * @author Sven Wesley, Mats Wetterlund
 */
public interface Elevator {

	/**
	 * Enumeration for describing elevator's direction.
	 */
	enum Direction {
		UP, DOWN, NONE
	}

	/**
	 * Get the Id of this elevator.
	 * 
	 * @return primitive integer representing the elevator.
	 */
	int getId();

	/**
	 * Tells which direction is the elevator going in.
	 * 
	 * @return Direction Enumeration value describing the direction.
	 */
	Direction getDirection();

	/**
	 * Check if the elevator is occupied at the moment.
	 * 
	 * @return true if busy.
	 */
	boolean isBusy();

	/**
	 * Reports which floor the elevator is at right now.
	 * 
	 * @return int actual floor at the moment.
	 */
	int currentFloor();

	/**
	 * If the elevator is moving. This is the target floor.
	 * 
	 * @return primitive integer number of floor
	 */
	int getAddressedFloor();

	/**
	 * Command to move the elevator to the given floor.
	 * 
	 * @param toFloor
	 *            int where to go.
	 */
	void moveElevator(int toFloor);

	/**
	 * Command for the elevator to execute the current procedure.
	 */
	void executeProcedure();

}

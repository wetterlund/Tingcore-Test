package com.tingco.codechallenge.elevator.impl;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.Elevator.Direction;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.config.ApplicationContextProvider;
import com.tingco.codechallenge.elevator.config.ElevatorApplication;

/**
 * Implementation of the ElevatorController interface.
 * 
 * @author Mats Wetterlund
 */
public class SimpleElevatorController implements ElevatorController {
	/**
	 * Using a singleton approach as there should only be one object.
	 */
	private static SimpleElevatorController	instance			= null;
	private static ApplicationContext		applicationContext;
	private static EventBus					eventBus;
	private List<Elevator>					elevators			= new Vector<Elevator>();
	private List<Elevator>					availableElevators	= new Vector<Elevator>();

	/**
	 * Private constructor for using a singleton approach as there should only be one object.
	 */
	private SimpleElevatorController() {
	}

	/**
	 * Method for retrieving the object.
	 * 
	 * @return The ElevatorControllerImpl instance for this singleton class.
	 */
	public static synchronized SimpleElevatorController getElevatorController() {
		if (instance == null) {
			instance = new SimpleElevatorController();

			applicationContext = ApplicationContextProvider.getApplicationContext();
			eventBus = applicationContext.getBean(AsyncEventBus.class);

			ElevatorEventsSubscriber.getElevatorEventsSubscriber();
		}

		return instance;
	}

	@Override
	public synchronized Elevator requestElevator(int toFloor) throws RejectedExecutionException {
		eventBus.post(new ElevatorEvent("Elevator requested to floor " + toFloor + "."));

		Elevator selectedElevator = null;
		int selectedElevatorDifferenceFloors = ElevatorApplication.getFloorsTotal();

		if (toFloor < ElevatorApplication.getFloorBottom() || toFloor > ElevatorApplication.getFloorTop()) {
			eventBus.post(new ElevatorEvent("Can't add a floor lower than bottom floor (" + ElevatorApplication.getFloorBottom()
					+ ") or higher than top floor (" + ElevatorApplication.getFloorTop() + ")."));

			throw new RejectedExecutionException("Can't add a floor lower than bottom floor (" + ElevatorApplication.getFloorBottom()
					+ ") or higher than top floor (" + ElevatorApplication.getFloorTop() + ").");
		}

		if (availableElevators.size() > 0) {
			for (Elevator elevator : availableElevators) {
				if (Math.abs(elevator.currentFloor() - toFloor) < selectedElevatorDifferenceFloors || selectedElevator == null) {
					selectedElevator = elevator;
					selectedElevatorDifferenceFloors = Math.abs(elevator.currentFloor() - toFloor);
				}
			}

			availableElevators.remove(selectedElevator);
		}
		else {
			for (Elevator elevator : elevators) {
				if (toFloor > elevator.currentFloor() && elevator.getDirection() == Direction.UP) {
					if (Math.abs(elevator.currentFloor() - toFloor) < selectedElevatorDifferenceFloors || selectedElevator == null) {
						selectedElevator = elevator;
						selectedElevatorDifferenceFloors = Math.abs(elevator.currentFloor() - toFloor);
					}
				}
				else if (toFloor < elevator.currentFloor() && elevator.getDirection() == Direction.DOWN) {
					if (Math.abs(elevator.currentFloor() - toFloor) < selectedElevatorDifferenceFloors || selectedElevator == null) {
						selectedElevator = elevator;
						selectedElevatorDifferenceFloors = Math.abs(elevator.currentFloor() - toFloor);
					}
				}
				else if (elevator.getDirection() == Direction.NONE) {
					if (Math.abs(elevator.currentFloor() - toFloor) < selectedElevatorDifferenceFloors || selectedElevator == null) {
						selectedElevator = elevator;
						selectedElevatorDifferenceFloors = Math.abs(elevator.currentFloor() - toFloor);
					}
				}
			}
		}

		if (selectedElevator != null) {
			selectedElevator.moveElevator(toFloor);

			eventBus.post(new ElevatorEvent("Elevator with id " + selectedElevator.getId() + " sent to floor " + toFloor + "."));
		}
		else {
			eventBus.post(new ElevatorEvent("No elevator was available to be sent to floor " + toFloor + "."));
		}

		return selectedElevator;
	}

	@Override
	public List<Elevator> getElevators() {
		return elevators;
	}

	@Override
	public List<Elevator> getAvailableElevators() {
		return availableElevators;
	}

	@Override
	public synchronized void releaseElevator(Elevator elevator) {
		availableElevators.add(elevator);
	}

	/**
	 * Register an elevator to the controller.
	 * 
	 * @param elevator
	 *            the elevator that shall be registered.
	 */
	public void registerElevator(Elevator elevator) {
		elevators.add(elevator);
		availableElevators.add(elevator);
		
		ScheduledExecutorService scheduledExecutor = applicationContext.getBean(ScheduledExecutorService.class);

		Runnable task = () -> elevator.executeProcedure();
		scheduledExecutor.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);
	}
}

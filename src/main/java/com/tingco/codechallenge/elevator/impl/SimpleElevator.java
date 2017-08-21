package com.tingco.codechallenge.elevator.impl;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.RejectedExecutionException;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.config.ApplicationContextProvider;
import com.tingco.codechallenge.elevator.config.ElevatorApplication;
import org.springframework.context.ApplicationContext;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.base.MoreObjects;

/**
 * Implementation of the Elevator interface.
 * 
 * @author Mats Wetterlund
 */
public class SimpleElevator implements Elevator {
	private Integer						id;
	private Direction					direction;
	private boolean						busy;
	private Integer						currentFloor;
	private Integer						addressedFloor;
	private List<Integer>				queuedFloors;
	private Integer						timer;

	private static final int			timeOnFloor			= 10;
	private static final int			timeBetweenFloors	= 5;

	private static ApplicationContext	applicationContext;
	private static EventBus				eventBus;

	public SimpleElevator(int id) {
		this.id = id;
		this.direction = Direction.NONE;
		this.busy = false;
		this.currentFloor = ElevatorApplication.getFloorBottom();
		this.addressedFloor = ElevatorApplication.getFloorBottom();
		this.queuedFloors = new Vector<Integer>();
		this.timer = 0;

		if (SimpleElevator.applicationContext == null) {
			applicationContext = ApplicationContextProvider.getApplicationContext();
		}
		if (SimpleElevator.eventBus == null) {
			eventBus = applicationContext.getBean(AsyncEventBus.class);
		}

		SimpleElevatorController.getElevatorController().registerElevator(this);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public boolean isBusy() {
		return busy;
	}

	@Override
	public int currentFloor() {
		return currentFloor;
	}

	@Override
	public int getAddressedFloor() {
		return addressedFloor;
	}

	@Override
	public synchronized void moveElevator(int toFloor) throws RejectedExecutionException {
		if (busy == false || direction == Direction.NONE) {
			busy = true;
			queuedFloors.add(toFloor);
			addressedFloor = toFloor;
			if (toFloor > currentFloor) {
				direction = Direction.UP;
				timer += timeBetweenFloors;
			}
			else if (toFloor < currentFloor) {
				direction = Direction.DOWN;
				timer += timeBetweenFloors;
			}
			else {
				timer = timeOnFloor;
			}
		}
		else if (toFloor > currentFloor && direction == Direction.UP) {
			if (toFloor >= addressedFloor) {
				if (toFloor > addressedFloor) {
					queuedFloors.add(toFloor);
					addressedFloor = toFloor;
				}
			}
			else {
				for (int i = 0; i < queuedFloors.size(); i++) {
					Integer floor = queuedFloors.get(i);
					if (toFloor == floor) {
						break;
					}
					else if (toFloor < floor) {
						queuedFloors.add(i, toFloor);
						break;
					}
				}
			}
		}
		else if (toFloor < currentFloor && direction == Direction.DOWN) {
			if (toFloor <= addressedFloor) {
				if (toFloor < addressedFloor) {
					queuedFloors.add(toFloor);
					addressedFloor = toFloor;
				}
			}
			else {
				for (int i = 0; i < queuedFloors.size(); i++) {
					Integer floor = queuedFloors.get(i);
					if (toFloor == floor) {
						break;
					}
					else if (toFloor > floor) {
						queuedFloors.add(i, toFloor);
						break;
					}
				}
			}
		}
		else {
			throw new RejectedExecutionException("Can't add a floor in the oposite direction of what the elevator is going.");
		}
	}

	@Override
	public synchronized void executeProcedure() {
		if (busy == true) {
			if (timer > 0) {
				timer--;
			}
			else {
				if (direction == Direction.UP) {
					currentFloor++;
				}
				else if (direction == Direction.DOWN) {
					currentFloor--;
				}

				if (queuedFloors.size() > 0) {
					try {
						Integer queuedFloor = queuedFloors.get(0);

						if (currentFloor == queuedFloor) {
							queuedFloors.remove(0);

							if (currentFloor == addressedFloor) {
								direction = Direction.NONE;
								timer = timeOnFloor;
							}
							else {
								timer = timeOnFloor + timeBetweenFloors;
							}

							SimpleElevator.eventBus.post(new ElevatorEvent("Elevator with id " + id + " arrived to floor " + currentFloor + "."));
						}
						else {
							timer = timeBetweenFloors;
						}
					}
					catch (Exception e) {
						// In case of errors reset the elevator.
						direction = Direction.NONE;
						busy = true;
						addressedFloor = currentFloor;
						queuedFloors = new Vector<Integer>();
						queuedFloors.add(currentFloor);
						timer = timeBetweenFloors;

						e.printStackTrace();
					}
				}
				else {
					SimpleElevator.eventBus.post(new ElevatorEvent("Elevator with id " + id + " is now available."));

					busy = false;
					SimpleElevatorController.getElevatorController().releaseElevator(this);
				}
			}
		}
	}

	/**
	 * toString method to get a textual representation of the object.
	 * 
	 * @return A string with all parameters and their value of the object.
	 */
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("direction", direction).add("busy", busy).add("currentFloor", currentFloor)
				.add("addressedFloor", addressedFloor).add("queuedFloors", queuedFloors).add("timer", timer).toString();
	}
}

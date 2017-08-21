package com.tingco.codechallenge.elevator.resources;

import java.util.concurrent.ScheduledExecutorService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.impl.SimpleElevatorController;

/**
 * Rest Resource.
 * 
 * @author Sven Wesley
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {

	/**
	 * Ping service to test if we are alive.
	 * 
	 * @return String pong
	 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping() {

		return "pong";
	}

	/**
	 * Request an elevator to the specified floor.
	 * 
	 * @return ElevatorImpl The Elevator that is going to the floor, if there is one to move.
	 */
	@RequestMapping(value = "/requestElevator", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Elevator requestElevator(@RequestParam("toFloor") int toFloor) {
		return SimpleElevatorController.getElevatorController().requestElevator(toFloor);
	}
}

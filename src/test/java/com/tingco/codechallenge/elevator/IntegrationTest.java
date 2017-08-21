package com.tingco.codechallenge.elevator;

import java.util.Vector;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.config.ElevatorApplication;
import com.tingco.codechallenge.elevator.impl.SimpleElevatorController;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Boiler plate test class to get up and running with a test faster.
 * 
 * @author Sven Wesley
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ElevatorApplication.class)
public class IntegrationTest {

	@Test
	public void simulateAnElevatorShaft() {
		ElevatorApplication elevatorApplication = new ElevatorApplication();
		elevatorApplication.setNumberOfElevators(6);
		elevatorApplication.setFloorBottom(0);
		elevatorApplication.setFloorTop(20);

		ElevatorApplication.main(new String[] {});

		try {
			Thread.sleep(5000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(20);

		try {
			Thread.sleep(3000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(12);
		SimpleElevatorController.getElevatorController().requestElevator(2);

		try {
			Thread.sleep(3000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(7);
		
		try {
			Thread.sleep(500);
		}
		catch (Exception e) {
		}
		
		SimpleElevatorController.getElevatorController().requestElevator(14);
		
		try {
			Thread.sleep(500);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(15);

		try {
			Thread.sleep(5000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(17);

		try {
			Thread.sleep(10000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(16);

		try {
			Thread.sleep(2000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(15);

		try {
			Thread.sleep(2000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(14);

		try {
			Thread.sleep(3000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(13);

		try {
			Thread.sleep(25000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(3);

		try {
			Thread.sleep(4000);
		}
		catch (Exception e) {
		}

		SimpleElevatorController.getElevatorController().requestElevator(5);

		while (SimpleElevatorController.getElevatorController().getAvailableElevators().size() != 6) {
			try {
				Thread.sleep(1000);
			}
			catch (Exception e) {
			}
		}

		Vector<Elevator> elevators = (Vector<Elevator>) SimpleElevatorController.getElevatorController().getElevators();

		assertEquals("Elevator with id 0 should be on floor 20", 20, elevators.get(0).currentFloor());
		assertEquals("Elevator with id 1 should be on floor 12", 12, elevators.get(1).currentFloor());
		assertEquals("Elevator with id 2 should be on floor 14", 14, elevators.get(2).currentFloor());
		assertEquals("Elevator with id 3 should be on floor 3", 3, elevators.get(3).currentFloor());
		assertEquals("Elevator with id 4 should be on floor 14", 14, elevators.get(4).currentFloor());
		assertEquals("Elevator with id 5 should be on floor 15", 15, elevators.get(5).currentFloor());
	}
}

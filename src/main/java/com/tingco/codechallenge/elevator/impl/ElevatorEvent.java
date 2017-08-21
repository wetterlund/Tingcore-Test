package com.tingco.codechallenge.elevator.impl;

/**
 * Class for elevator events.
 * 
 * @author Mats Wetterlund
 */
public class ElevatorEvent {
	String	text;

	public ElevatorEvent(String text) {
		System.out.println("ElevatorEvent: " + text);
		this.text = text;
	}

	public String getText() {
		return text;
	}
}

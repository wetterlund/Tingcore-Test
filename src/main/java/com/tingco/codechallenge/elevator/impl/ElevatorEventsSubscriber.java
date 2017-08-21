package com.tingco.codechallenge.elevator.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tingco.codechallenge.elevator.config.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;

/**
 * Class for handling subscribing to elevator events.
 * Writes all events to a file in event/ sub folder.
 * 
 * @author Mats Wetterlund
 */
public class ElevatorEventsSubscriber {

	/**
	 * Using a singleton approach as there should only be one object.
	 */
	private static ElevatorEventsSubscriber	instance	= null;
	private static SimpleDateFormat			sdfDate;
	private static File						file;

	private ElevatorEventsSubscriber() {
	}

	/**
	 * Method for retrieving the object.
	 * 
	 * @return The ElevatorEventsSubscriber instance for this singleton class.
	 */
	public static synchronized ElevatorEventsSubscriber getElevatorEventsSubscriber() {
		if (instance == null) {
			instance = new ElevatorEventsSubscriber();

			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			AsyncEventBus eventBus = applicationContext.getBean(AsyncEventBus.class);

			eventBus.register(instance);

			try {
				sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				file = new File("events/ElevatorEvents_" + (new SimpleDateFormat("yyyyMMdd-HHmmss")).format(new Date()) + ".txt");
				file.getParentFile().mkdirs();
				System.out.println("Event file created at " + file.getAbsoluteFile());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	@Subscribe
	public void handleElevatorEvents(ElevatorEvent event) {
		try {
			Files.append(sdfDate.format(new Date()) + ": " + event.getText() + System.lineSeparator(), file, Charset.forName("UTF-8"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

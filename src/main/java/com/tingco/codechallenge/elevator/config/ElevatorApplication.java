package com.tingco.codechallenge.elevator.config;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import com.tingco.codechallenge.elevator.impl.SimpleElevator;
import com.tingco.codechallenge.elevator.impl.SimpleElevatorController;

/**
 * Preconfigured Spring Application boot class.
 */
@Configuration
@ComponentScan(basePackages = { "com.tingco.codechallenge.elevator" })
@EnableAutoConfiguration
@PropertySources({ @PropertySource("classpath:application.properties") })
public class ElevatorApplication {

	private static int	numberOfElevators;
	private static int	floorBottom;
	private static int	floorTop;

	@Value("${com.tingco.elevator.numberofelevators}")
	public void setNumberOfElevators(int numberOfElevators) {
		ElevatorApplication.numberOfElevators = numberOfElevators;
	}

	@Value("${com.tingco.elevator.floorBottom}")
	public void setFloorBottom(int floorBottom) {
		ElevatorApplication.floorBottom = floorBottom;
	}

	@Value("${com.tingco.elevator.floorTop}")
	public void setFloorTop(int floorTop) {
		ElevatorApplication.floorTop = floorTop;
	}
    
	/**
	 * Start method that will be invoked when starting the Spring context.
	 * 
	 * @param args
	 *            Not in use
	 */
	public static void main(final String[] args) {
		SpringApplication.run(ElevatorApplication.class, args);

		for (int i = 0; i < numberOfElevators; i++) {
			new SimpleElevator(i);
		}
	}

	/**
	 * Create a default thread pool for your convenience.
	 * 
	 * @return Executor thread pool
	 */
	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(numberOfElevators);
	}

	/**
	 * Create an event bus for your convenience.
	 * 
	 * @return EventBus for async task execution
	 */
	@Bean
	public EventBus eventBus() {
		return new AsyncEventBus(Executors.newCachedThreadPool());
	}

	public static int getFloorBottom() {
		return floorBottom;
	}

	public static int getFloorTop() {
		return floorTop;
	}

	public static int getFloorsTotal() {
		return floorTop - floorBottom;
	}
}

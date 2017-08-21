package com.tingco.codechallenge.elevator.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Service for providing application context.
 * 
 * @author Mats Wetterlund
 */
@Service
public class ApplicationContextProvider {

    private static ApplicationContext applicationContext;

    @Autowired
    public ApplicationContextProvider(ApplicationContext providedApplicationContext) {
    	applicationContext = providedApplicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}

package com.dinclab.eventbus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * EventBus implementation
 * Sample 
 * 
 * @author Muhammet
 * muhammetdinc@gmail.com
 *
 */
public class EventBus implements Serializable {
	private static final long serialVersionUID = 846977931469261400L;

	/**
	 * Default bus name
	 */
	public static final String DEFAULT_BUS_NAME = "default_bus_1q2w3eKksAwrsgftwE";
	
	/**
	 * Named event bussed, DEFAULT_BUS_NAME and user defined busses
	 */
	private static Map<String, EventBusInstance> eventBuses;

	/**
	 * Get default EventBusInstance
	 * 
	 * @return {@link EventBusInstance}
	 */
	public static EventBusInstance getDefault() {
		if (eventBuses == null) {
			synchronized (DEFAULT_BUS_NAME) {
				eventBuses = new HashMap<String, EventBusInstance>();
				eventBuses.put(DEFAULT_BUS_NAME, new EventBusInstance());
			}

			return eventBuses.get(DEFAULT_BUS_NAME);
		} else {
			return eventBuses.get(DEFAULT_BUS_NAME);
		}
	}

	/**
	 * Get named EventBus instances, if not exists creates one and return it
	 * 
	 * @param busName : named busName
	 * @return {@link EventBusInstance}
	 */
	public static EventBusInstance getBus(String busName) {
		if (eventBuses == null) {
			synchronized (DEFAULT_BUS_NAME) {
				eventBuses = new HashMap<String, EventBusInstance>();
				eventBuses.put(DEFAULT_BUS_NAME, new EventBusInstance());
			}
		}

		synchronized (eventBuses) {
			if (eventBuses.containsKey(busName)) {
				return eventBuses.get(busName);
			} else {
				EventBusInstance ebi = new EventBusInstance();
				eventBuses.put(busName, ebi);
				return ebi;
			}
		}
	}
	
	/**
	 * Post all events in all bus
	 * @param eventArg
	 */
	public static void postAll(Object eventArg) {
		for(EventBusInstance eventBus: eventBuses.values()) {
			eventBus.post(eventArg);
		}
	}
}

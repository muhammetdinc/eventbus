package com.dinclab.eventbus;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An EventBusInstance used by {@link EventBus}
 * 
 * @author Muhammet
 * muhammetdinc@gmail.com
 *
 */
public class EventBusInstance implements Serializable {

	private static final long serialVersionUID = -3559322817109625L;

	private static String METHOD_NAME = "onEvent";
	private static int MAX_THREAD_COUNT = 10;

	/**
	 * EventInstanceMap
	 * 
	 * Key: EventObject's className
	 * Value: registered instances to the event
	 */
	private Map<Class<? extends Object>, List<Object>> eventInstanceMap;

	/**
	 * ExecutorService that executes the event functions
	 */
	private ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);

	public EventBusInstance() {
		eventInstanceMap = new HashMap<Class<? extends Object>, List<Object>>();
	}

	/**
	 * Register new event
	 * 
	 * @param instance
	 * @param eventParamType
	 */
	private void registerNewEvent(Object instance, Class<?> eventParamType) {
		List<Object> registeredInstanceList = eventInstanceMap.get(eventParamType);

		if (registeredInstanceList == null) {
			registeredInstanceList = new ArrayList<Object>();
			registeredInstanceList.add(instance);
			eventInstanceMap.put(eventParamType, registeredInstanceList);
		} else {
			registeredInstanceList.add(instance);
		}
	}

	/**
	 * Register an instance
	 * 
	 * This instance's <b>onEvent</b> methods will be registered,
	 * if somebody post an event that matches this instance's onEvent signature
	 * instance.onEvent(...) will be fired
	 * 
	 * @param instance 
	 */
	public void register(Object instance) {
		Method[] methods = instance.getClass().getMethods();

		for (Method m : methods) {
			if (m.getName().equals(METHOD_NAME) && m.getParameterTypes().length == 1) {
				registerNewEvent(instance, m.getParameterTypes()[0]);
			}
		}
	}

	/**
	 * Post this event: invokes all onEvent methods inside registered instances
	 * 
	 * @param eventArg:
	 */
	public void post(Object eventArg) {
		List<Object> registeredInstanceList = eventInstanceMap.get(eventArg.getClass());

		if (registeredInstanceList != null) {
			for (Object registeredInstance : registeredInstanceList) {
				executorService.execute(new EventMehtodInvoker(registeredInstance, eventArg));
			}
		}
	}

	class EventMehtodInvoker implements Runnable {
		private Object instance;
		private Object param;

		public EventMehtodInvoker(Object inctance, Object param) {
			this.instance = inctance;
			this.param = param;
		}

		public void run() {
			try {
				Method m = instance.getClass().getMethod(METHOD_NAME, param.getClass());
				try {
					m.invoke(instance, param);
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (InvocationTargetException e) {

					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {

				e.printStackTrace();
			} catch (SecurityException e) {

				e.printStackTrace();
			}
		}
	}
}

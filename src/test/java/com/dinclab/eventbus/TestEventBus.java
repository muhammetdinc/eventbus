package com.dinclab.eventbus;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 
 * 
 * 
 * @author Muhammet
 * muhammetdinc@gmail.com
 *
 */
public class TestEventBus {
	private static String instanceName;
	private static final Logger LOG = Logger.getLogger("TestEventBus");
	
	/**
	 * 
	 * @param instanceName : instance name to distinguish objects 
	 */
	public TestEventBus(String instanceName) {
		this.instanceName = instanceName;
	}
	
	public static void main(String[] args) throws InterruptedException{
		/// create 3 instances for testing
		TestEventBus t1 = new TestEventBus("instance1");
		TestEventBus t2 = new TestEventBus("instance2");
		TestEventBus t3 = new TestEventBus("instance3");
		
		// register t1 instance to the default bus
		EventBus.getDefault().register(t1);
		
		// register t2 instance to the busA
		EventBus.getBus("busA").register(t2);
		
		// register t2 instance to the busB
		EventBus.getBus("busB").register(t3);
		
		// post string event to default bus
		EventBus.getDefault().post("Hello registered default bus instances");
		
		// post EventTestObject event to default bus
		EventBus.getDefault().post(new EventTestObject("To the default bus"));
		
		// post EventTestObject event to busB
		EventBus.getBus("busB").post(new EventTestObject("To the busB"));
				
		// post EventTestObject event to busA
		EventBus.getBus("busA").post(new EventTestObject("To the busA"));
		
		
		// wait for complete (for testing purpose)
		for(int i=0;i<10;i++)
			Thread.sleep(100);

	}
	
	
	/**
	 * For event testing posted events
	 * @param stringEvent
	 */
	public void onEvent(String stringEvent) {
		LOG.log(Level.INFO, "Instance:"+instanceName+", String event received: "+stringEvent);
	}
	
	/**
	 * For event testing posted events
	 * @param stringEvent
	 */
	public void onEvent(EventTestObject testEvent) {
		LOG.log(Level.INFO, "Instance:"+instanceName+", Test event received: "+testEvent.getTitle());
	}
}

class EventTestObject {
	String title;
	
	public EventTestObject(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
}


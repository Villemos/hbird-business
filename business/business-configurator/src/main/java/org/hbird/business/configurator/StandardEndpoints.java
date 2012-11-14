package org.hbird.business.configurator;

public class StandardEndpoints {

	/** Monitoring data. */
	public static String monitoring = "activemq:topic:monitoring";

	/** Tasks scheduled for execution. */
	public static String tasks = "activemq:queue:tasks";
	
	/** Command requests that are scheduled for verification. */
	public static String requests = "activemq:queue:requests";	

	/** Commands which have been verified and released. */
	public static String commands = "activemq:topic:commands";
	
	/** A topic for everybody monitoring what occurs in the system. */
	public static String notification = "activemq:topic:notification";
}

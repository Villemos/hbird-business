package org.hbird.exchange.configurator;

public class StartQueueManagerComponent extends StartComponent {

	private static final long serialVersionUID = -1157145181641481338L;

	public StartQueueManagerComponent(String componentname) {
		super(componentname, "StartQueueManager", "A request to start a queue manager for a specific queue.");
	}

}

package org.hbird.exchange.configurator;

public class StartQueueManagerComponent extends StartComponent {

    private static final String DESCRIPTION = "A request to start a queue manager for a specific queue.";

    private static final long serialVersionUID = 6547352120012880268L;

    public StartQueueManagerComponent(String componentname) {
        super(componentname, StartQueueManagerComponent.class.getSimpleName(), DESCRIPTION);
    }
}

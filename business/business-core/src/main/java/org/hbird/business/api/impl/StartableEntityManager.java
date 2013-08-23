package org.hbird.business.api.impl;

import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IStartableEntityManager;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartableEntityManager implements IStartableEntityManager {

    private static final String DEFAULT_DESTINATION = "Configurator";

    private final Logger LOG = LoggerFactory.getLogger(StartableEntityManager.class);

    protected IPublisher publisher;

    protected String issuedBy;
    protected String destination;

    public StartableEntityManager(String issuedBy, IPublisher publisher) {
        this(issuedBy, DEFAULT_DESTINATION, publisher);
    }

    public StartableEntityManager(String issuedBy, String destination, IPublisher publisher) {
        this.issuedBy = issuedBy;
        this.destination = destination;
        this.publisher = publisher;
    }

    @Override
    public void start(IStartableEntity part) {
        StartComponent request = new StartComponent(issuedBy);
        request.setIssuedBy(issuedBy);
        request.setEntity(part);
        request.setDestination(destination);

        try {
            publisher.publish(request);
        }
        catch (Exception e) {
            LOG.error("Error sending start command for StartableEntity '{}'", part.getID(), e);
        }
    }

    @Override
    public void stop(String entityId) {
        StopComponent request = new StopComponent(issuedBy);
        request.setIssuedBy(issuedBy);
        request.setEntityID(entityId);
        request.setDestination(destination);

        try {
            publisher.publish(request);
        }
        catch (Exception e) {
            LOG.error("Error sending stop command for StartableEntity '{}'", entityId, e);
        }
    }
}

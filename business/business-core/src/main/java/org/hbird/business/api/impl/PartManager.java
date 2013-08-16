package org.hbird.business.api.impl;

import org.hbird.business.api.IPartManager;
import org.hbird.business.api.IPublisher;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartManager extends AbstractHbirdApi implements IPartManager {

    private static final String DEFAULT_DESTINATION = "Configurator";

    private final Logger LOG = LoggerFactory.getLogger(PartManager.class);

    protected IPublisher publisher;

    public PartManager(String issuedBy, IPublisher publisher) {
        this(issuedBy, DEFAULT_DESTINATION, publisher);
    }

    public PartManager(String issuedBy, String destination, IPublisher publisher) {
        super(issuedBy, destination);
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

    @Override
    public Part resolveParent(Part child) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getQualifiedName(Part part) {
        return getQualifiedName(part, "/");
    }

    @Override
    public String getQualifiedName(Part part, String separator) {
        // TODO Auto-generated method stub
        return null;
    }

}

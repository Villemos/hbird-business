package org.hbird.business.api.impl;

import org.apache.camel.Handler;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.EntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoPublisher implements IPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(DaoPublisher.class);

    private final IDataAccess dao;

    public DaoPublisher(IDataAccess dao) {
        this.dao = dao;
    }

    @Handler
    @Override
    public <T extends EntityInstance> T publish(T object) throws Exception {

        if (object == null) {
            // E.g. limit checker can send null
            return null;
        }

        if (object.getIssuedBy() == null) {
            LOG.warn("Storing EntityInstance '{}' without issuedBy value", object.getInstanceID());
        }

        if (object instanceof Command) {
            Command command = (Command) object;
            if (command.getDestination() == null) {
                LOG.warn("Storing Command '{}' without destination value", command.getInstanceID());
            }
        }

        dao.save(object);
        return object;
    }
}

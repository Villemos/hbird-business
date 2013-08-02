package org.hbird.business.api.impl;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.EntityInstance;

public class Publisher extends AbstractHbirdApi implements IPublisher {
    private IDataAccess dao;

    public Publisher(String issuedBy, String destination, IDataAccess dao) {
        super(issuedBy, destination);

        this.dao = dao;
    }

    @Override
    public EntityInstance publish(EntityInstance object) throws Exception {
        if (object.getIssuedBy() == null) {
            object.setIssuedBy(getIssuedBy());
        }

        if (object instanceof Command) {
            Command comm = (Command) object;
            if (comm.getDestination() == null) {
                comm.setDestination(getDestination());
            }
        }

        dao.save(object);

        return object;
    }
}

package org.hbird.business.navigation.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Handler;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.exceptions.ArchiveException;
import org.hbird.exchange.navigation.Satellite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestSatellites {

    protected static final Logger LOG = LoggerFactory.getLogger(RequestSatellites.class);

    private IDataAccess dao;

    public RequestSatellites(IDataAccess dao) {
        this.dao = dao;
    }

    @Handler
    public List<Satellite> request() {
        try {
            return dao.getAll(Satellite.class);
        }
        catch (ArchiveException e) {
            LOG.error("Failed to request the satellites", e);
            return new ArrayList<Satellite>();
        }
    }

}

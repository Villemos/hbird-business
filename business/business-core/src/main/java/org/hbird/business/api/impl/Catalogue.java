package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.core.State;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.navigation.Satellite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Catalogue extends AbstractHbirdApi implements ICatalogue {
    private final static Logger LOG = LoggerFactory.getLogger(Catalogue.class);

    private IDataAccess dao;

    public Catalogue(String issuedBy, String destination, IDataAccess dao) {
        super(issuedBy, destination);

        this.dao = dao;
    }

    protected <T extends IEntityInstance> List<T> getLatest(Class<T> clazz) throws Exception {
        List<T> allInstances = dao.getAll(clazz);

        Set<String> ids = new TreeSet<String>();

        for (T instance : allInstances) {
            ids.add(instance.getID());
        }

        List<T> latest = new ArrayList<T>(ids.size());

        for (String id : ids) {
            latest.add(dao.getById(id, clazz));
        }

        return latest;
    }

    protected <T extends IEntityInstance> List<T> getLatestOrEmpty(Class<T> clazz) {
        try {
            // return getLatest(clazz);
            return dao.getAll(clazz);
        }
        catch (Exception e) {
            LOG.error("Exception while fetching latest instances of " + clazz.getSimpleName(), e);

            return Collections.emptyList();
        }
    }

    @Override
    public List<Parameter> getParameters() {
        return getLatestOrEmpty(Parameter.class);
    }

    @Override
    public List<State> getStates() {
        return getLatestOrEmpty(State.class);
    }

    @Override
    public List<GroundStation> getGroundStations() {
        return getLatestOrEmpty(GroundStation.class);
    }

    @Override
    public List<Satellite> getSatellites() {
        return getLatestOrEmpty(Satellite.class);
    }

    protected <T extends IEntityInstance> T getByName(String name, Class<T> clazz) {
        List<T> latest = getLatestOrEmpty(clazz);

        for (T entity : latest) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }

        LOG.error("Failed to retrieve " + clazz.getSimpleName() + " with name " + name);

        return null;
    }

    @Override
    public GroundStation getGroundStationByName(String name) {
        return getByName(name, GroundStation.class);
    }

    @Override
    public Satellite getSatelliteByName(String name) {
        return getByName(name, Satellite.class);
    }

    @Override
    public List<GroundStation> getGroundStationsByName(List<String> names) {
        // TODO: Can do with less requests
        List<GroundStation> groundStations = new ArrayList<GroundStation>();

        for (String name : names) {
            groundStations.add(getGroundStationByName(name));
        }

        return groundStations;
    }

    @Override
    public List<Part> getParts() {
        return getLatestOrEmpty(Part.class);
    }

    @Override
    public Part getPart(String name) {
        return getByName(name, Part.class);
    }

    @Override
    public List<Part> getPartChildren(String parentName) {
        // TODO: Implement when clear about how it should work
        return null;
    }
}

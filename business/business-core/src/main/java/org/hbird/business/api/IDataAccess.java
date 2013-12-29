package org.hbird.business.api;

import java.util.List;

import org.hbird.business.api.exceptions.ArchiveException;
import org.hbird.exchange.core.State;
import org.hbird.exchange.interfaces.IApplicableTo;
import org.hbird.exchange.interfaces.IDerivedFrom;
import org.hbird.exchange.interfaces.IEntityInstance;

// TODO - 23.08.2013, kimmell - needs refactoring
public interface IDataAccess {
    public <T extends IEntityInstance> T getById(String id, Class<T> clazz) throws ArchiveException;

    public <T extends IEntityInstance> List<T> getById(String id, long from, long to, Class<T> clazz) throws ArchiveException;

    // Returns last VERSION of an entity with a given id
    public <T extends IEntityInstance> T getByInstanceId(String id, Class<T> clazz) throws ArchiveException;

    // XXX: Do we need it now?
    public <T extends IEntityInstance> List<T> getAllInstancesById(String id, Class<T> clazz) throws ArchiveException;

    // Returns all entity instances of a class <clazz>
    public <T extends IEntityInstance> List<T> getAll(Class<T> clazz) throws ArchiveException;

    public <T extends IEntityInstance> List<T> getAllBySupertype(Class<T> superclass) throws ArchiveException;

    public Object save(Object o) throws ArchiveException;

    /**
     * @return A list of most recent versions of states with given names
     * 
     */
    public List<State> getStates(List<String> names) throws ArchiveException; // TODO: Remove

    public <T extends IEntityInstance & IApplicableTo> List<T> getApplicableTo(String id, Class<T> clazz) throws ArchiveException;

    // FIXME: getById uses different argument order (id/from/to/clazz). This one is probably better
    public <T extends IEntityInstance & IApplicableTo> List<T> getApplicableTo(String id, Class<T> clazz, long from, long to) throws ArchiveException;

    public <T extends IEntityInstance & IDerivedFrom> List<T> getDerivedFrom(String instanceID, Class<T> clazz) throws ArchiveException;

    public <T extends IEntityInstance & IDerivedFrom> List<T> getDerivedFrom(String instanceID, Class<T> clazz, long from, long to) throws ArchiveException;

    public <T extends IEntityInstance> List<T> getIssuedBy(String instanceID, Class<T> clazz) throws ArchiveException;

    public <T extends IEntityInstance> List<T> getIssuedBy(String instanceID, Class<T> clazz, long from, long to) throws ArchiveException;
}

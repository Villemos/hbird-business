package org.hbird.business.api;

import java.util.List;

import org.hbird.business.api.exceptions.ArchiveException;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public interface IDataAccess {
    public <T extends IEntityInstance> T getById(String id, Class<T> clazz) throws ArchiveException;

    // Returns last VERSION of an entity with a given id
    public <T extends IEntityInstance> T getByInstanceId(String id, Class<T> clazz) throws ArchiveException;

    public <T extends IEntityInstance> List<T> getAllInstancesById(String id, Class<T> clazz) throws ArchiveException;

    // Returns all entity instances of a class <clazz>
    public <T extends IEntityInstance> List<T> getAll(Class<T> clazz) throws ArchiveException;

    public <T extends IEntityInstance> List<T> getAllBySupertype(Class<T> superclass) throws ArchiveException;

    public Object save(Object o) throws ArchiveException;

    /**
     * Method to retrieve the last sample of a single parameter. The method will return <li>One parameter sample</li>
     * <li>With the highest possible version</li>
     * 
     * @param name The name of the parameter
     * @return The parameter sample.
     * 
     * @throws ArchiveException if no samples found or on data access error
     */
    public Parameter getParameter(String name) throws ArchiveException;

    /**
     * Method to retrieve the samples of a single parameter. The retrieval will be <li>All samples</li> <li>With a
     * sample timestamp between 'from' and 'to'</li> <li>and sorted on timestamp in ASCENDING order</li>
     * 
     * @param name The name of the parameter
     * @param from The earliest time (Java time).
     * @param to The latest time (Java time)
     * @return List<Parameter> of Parameter samples. May be empty if no samples matching the criteria were found.
     */
    public List<Parameter> getParameter(String name, long from, long to) throws ArchiveException;

    /**
     * Method to retrieve all states applicable to a Named object (such as a Parameter). <li>A maximum of 1000 entries
     * (parameter samples and applicable states)</li> <li>Starting from the last received sample of the parameter</li>
     * <li>and sorted on timestamp in DECENDING order</li>
     * 
     * @param applicableTo The ID of Entity that the state must be applicable
     * @return A list of all states applicable to the named object
     */
    public List<State> getState(String applicableTo) throws ArchiveException;

    /**
     * Method to retrieve all states applicable to a Named object (such as a Parameter). <li>All state samples</li> <li>
     * With a sample timestamp between 'from' and 'to'</li> <li>and sorted on timestamp in ASCENDING order</li>
     * 
     * @param applicableTo The ID of Entity that the state must be applicable
     * @param from The earliest time (Java time).
     * @param to The latest time (Java time)
     * @return A list of all states applicable to the Named object
     */
    public List<State> getState(String applicableTo, long from, long to) throws ArchiveException;

    /**
     * @return A list of most recent versions of states with given names
     * 
     */
    public List<State> getStates(List<String> names) throws ArchiveException;

    /**
     * Method to retrieve the last orbital state of a satellite as derived from the last TLE. The method will return <li>
     * The last orbital state sample applicable to the satellite, derived from the last TLE parameters.</li>
     * 
     * @param satelliteID The ID of the satellite
     */
    public OrbitalState getOrbitalStateFor(String satelliteID) throws ArchiveException;

    /**
     * Method to retrieve the orbital state of a satellite, identified throughs its name, within
     * a given interval. The method will return <li>All orbital state samples applicable to the satellite</li> <li>With
     * a sample timestamp between 'from' and 'to'</li> <li>and sorted on timestamp in ASCENDING order</li>
     * 
     * @param satelliteID ID of the satellite
     * @param from The earliest time (Java time).
     * @param to The latest time (Java time)
     * @return A list of all orbital states applicable to the satellites
     */
    public List<OrbitalState> getOrbitalStatesFor(String satelliteID, long from, long to) throws ArchiveException;

    /**
     * @return Last version of a TLE for a given satellite
     */
    public TleOrbitalParameters getTleFor(String satelliteID) throws ArchiveException;

    /**
     * Method to retrieve the orbital state of a satellite, identified throughs its name, within
     * a given interval. The method will return <li>All orbital state samples applicable to the satellite</li> <li>With
     * a sample timestamp between 'from' and 'to'</li> <li>and sorted on timestamp in ASCENDING order</li>
     * 
     * @param satelliteID ID of the satellite
     * @param from The earliest time (Java time).
     * @param to The latest time (Java time)
     * @return A list of all orbital states applicable to the satellites
     */
    public List<TleOrbitalParameters> getTleFor(String satelliteID, long from, long to) throws ArchiveException;

    /**
     * Method to retrieve the next location contact event.
     * 
     * @param groundStationID ID of the ground station
     * @return
     */
    public LocationContactEvent getNextLocationContactEventForGroundStation(String groundStationID) throws ArchiveException;

    /**
     * Method to retrieve the next location contact event.
     * 
     * @param groundStationID ID of the ground station
     * @return
     */
    public LocationContactEvent getNextLocationContactEventForGroundStation(String groundStationID, long from) throws ArchiveException;

    /**
     * Method to retrieve the next location contact event.
     * 
     * @param groundStationID ID of the ground station
     * @param satelliteID ID of the satellite
     * @return
     */
    public LocationContactEvent getNextLocationContactEventFor(String groundStationID, String satelliteID) throws ArchiveException;

    /**
     * Method to retrieve the next location contact event.
     * 
     * @param groundStationID ID of the ground station
     * @param satelliteID ID of the satellite
     * @return
     */
    public LocationContactEvent getNextLocationContactEventFor(String groundStationID, String satelliteID, long from) throws ArchiveException;

    /**
     * Method to retrieve the metadata of an object.
     * 
     * @param subject The Named object that the metadata must be applicable to.
     * @return A list with zero or more metadata objects applicable to the subject
     */
    public List<Metadata> getMetadata(String applicableTo) throws ArchiveException;
}

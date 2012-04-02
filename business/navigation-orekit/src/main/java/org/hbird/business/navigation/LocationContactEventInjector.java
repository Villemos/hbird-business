package org.hbird.business.navigation;

import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satelitte;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.time.TimeScalesFactory;

/**
 * This camel route processor will receive callbacks from the orekit library 
 * notifying of events such as the establishment / loss of contact. The processor
 * will create the corresponding OrbitalEvent and send it to the consumer.
 */
public class LocationContactEventInjector extends ElevationDetector {

	/** The unique UID */
	private static final long serialVersionUID = 801203905525890103L;

	/** The location that comes into contact. */
	protected Location location = null;
	
	/** The satellite. */
	protected Satelitte satellite = null;
	
	/** Unique identifier assigned to all orbital data events that generated
	 *  as part of this run. Will be used to identify a complete orbit, and replace
	 *  it with new series when available. */
	protected long datasetidentifier = 0;

	protected OrbitPredictor predictor = null;
	
	/**
	 * COnstructor of an injector of location contact events.
	 * 
	 * @param maxCheck Sorry, I dont know... TODO
	 * @param elevation The degrees above the horizon that the satellite must be to be visible from this location.
	 * @param topo The topocentric framework used.
	 * @param satellite The satellite whose orbit we are predicting.
	 * @param location The location to which contact has been established / lost if this event occurs.
	 */
	public LocationContactEventInjector(double maxCheck, double elevation, TopocentricFrame topo, Satelitte satellite, Location location, long datasetidentifier, OrbitPredictor predictor) {
		super(maxCheck, elevation, topo);
		this.satellite = satellite;
		this.location = location;
		this.datasetidentifier = datasetidentifier;
		this.predictor = predictor;
	}
	
	/* (non-Javadoc)
	 * @see org.orekit.propagation.events.ElevationDetector#eventOccurred(org.orekit.propagation.SpacecraftState, boolean)
	 */
	public int eventOccurred(final SpacecraftState s, final boolean increasing) throws OrekitException {
		
		/** Create orbital event and send it on the response stream. */
		predictor.addResult(new LocationContactEvent("", "Visibility", "", s.getDate().toDate(TimeScalesFactory.getUTC()).getTime(), datasetidentifier, location, satellite, increasing));

		/** Continue listening for events. */
		return CONTINUE;
	}
}
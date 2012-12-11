package org.hbird.business.navigation;

import java.util.Date;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.commons.math.geometry.Vector3D;
import org.apache.log4j.Logger;
import org.hbird.exchange.navigation.KeplianOrbitalState;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitPredictionRequest;
import org.hbird.exchange.navigation.OrbitalState;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.events.EventDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;

/** 
 * Processor of 'OrbitalPredictionRequests', implemented based on the OREKIT open source
 * tool. 
 * 
 * For an example of how to use, see https://www.orekit.org/forge/projects/orekit/repository/revisions/4decf40db88b02ce82ec9cac7629536c701ac535/entry/src/tutorials/fr/cs/examples/propagation/VisibilityCheck.java
 * 
 * NOTE NOTE NOTE NOTE
 * 
 * The following system property should be set orekit.data.path=[path to UTC-TAI.history file]
 * 
 * 
 */
public class KeplianOrbitPredictor extends OrbitPredictor {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(KeplianOrbitPredictor.class);

	
	/** The exchange must contain a OrbitalState object as the in-body. 
	 * @throws OrekitException */
	@Handler
	public List<Object> predictOrbit(@Body OrbitPredictionRequest request) throws OrekitException {

		results.clear();

		KeplianOrbitalState initialState = null;

		/** Check if the request contains the initial state. Else use the last known state of the satellite. */
		if (request.getArguments().containsKey("initialstate") == false) {
			// TODO
		}
		else {
			initialState = (KeplianOrbitalState) request.getArguments().get("initialstate");
		}

		Vector3D position = new Vector3D(initialState.getPosition().p1, initialState.getPosition().p2,initialState.getPosition().p3);
		Vector3D velocity = new Vector3D(initialState.getVelocity().p1, initialState.getVelocity().p2, initialState.getVelocity().p3);
		PVCoordinates pvCoordinates = new PVCoordinates(position, velocity);

		AbsoluteDate initialDate = new AbsoluteDate(new Date(request.getStarttime()), TimeScalesFactory.getUTC());

		Frame inertialFrame = FramesFactory.getEME2000();

		// Initial date
		Orbit initialOrbit = new KeplerianOrbit(pvCoordinates, inertialFrame, initialDate, mu);

		Propagator propagator = new KeplerianPropagator(initialOrbit);

		/** Register the visibility events for the requested locations. */
		for (Location location : request.getLocations()) {
			GeodeticPoint point = new GeodeticPoint((Double) location.p1, (Double) location.p2, (Double) location.p3);				
			BodyShape earth = new OneAxisEllipsoid(ae, f, FramesFactory.getITRF2005());
			TopocentricFrame sta1Frame = new TopocentricFrame(earth, point, location.getName());

			/** Register the injector that will send the detected events, for this location, to the propagator. */
			EventDetector sta1Visi = new LocationContactEventInjector(maxcheck, location.getThresholdElevation(), sta1Frame, request.getSatellite(), location, request.getDatasetidentifier(), this);
			propagator.addEventDetector(sta1Visi);				
		}

		OrbitalStateInjector injector = new OrbitalStateInjector(request.getDatasetidentifier(), this, request.getName(), request.getDescription(), request.getSatellite());
		injector.setDatasetidentifier(request.getSatellite().getName() + "/" + (new Date()).toGMTString());			

		propagator.setMasterMode(request.getStepSize(), injector);			
		propagator.propagate(new AbsoluteDate(initialDate, request.getDeltaPropagation()));

		/** Create dataset identifier based on the time. */
		String datasetidentifier = request.getDatasetidentifier();
		if (datasetidentifier != null) {
			(new Date()).toString();
		}

		return results;
	}
}

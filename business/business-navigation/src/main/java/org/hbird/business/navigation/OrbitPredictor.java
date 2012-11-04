package org.hbird.business.navigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.commons.math.geometry.Vector3D;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitPredictionRequest;
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
 */
public class OrbitPredictor {

	/** FIXME I don't know what this does but OREKIT needs it...*/
	protected double maxcheck = 1.;

	/** gravitation coefficient */
	protected Double mu = 3.986004415e+14; 

	/** flattening */
	protected Double f  =  1.0 / 298.257223563; 

	/** equatorial radius in meter */
	protected Double ae = 6378137.0; 

	protected List<Object> results = new ArrayList<Object>();

	public OrbitPredictor() {
		System.setProperty("orekit.data.path", "src/main/resources");	
	}	

	public OrbitPredictor(String datapath) {
		System.setProperty("orekit.data.path", datapath);		
	}

	/** The exchange must contain a OrbitalState object as the in-body. 
	 * @throws OrekitException */
	@Handler
	public List<Object> predictOrbit(@Body OrbitPredictionRequest request) throws OrekitException {
		// Inertial frame			
		Vector3D position = new Vector3D((Double) request.position.p1, (Double) request.position.p2, (Double) request.position.p3);
		Vector3D velocity = new Vector3D((Double) request.velocity.p1, (Double) request.velocity.p2, (Double) request.velocity.p3);
		PVCoordinates pvCoordinates = new PVCoordinates(position, velocity);

		AbsoluteDate initialDate = new AbsoluteDate(new Date(request.starttime), TimeScalesFactory.getUTC());

		Frame inertialFrame = FramesFactory.getEME2000();

		// Initial date
		Orbit initialOrbit = new KeplerianOrbit(pvCoordinates, inertialFrame, initialDate, mu);

		// NumericalPropagator propagator = new NumericalPropagator(integrator);
		Propagator propagator = new KeplerianPropagator(initialOrbit);

		/** Create dataset identifier based on the time. */
		String datasetidentifier = (new Date()).toString();

		/** Register the visibility events for the requested locations. */
		for (Location location : request.locations) {
			GeodeticPoint point = new GeodeticPoint((Double) location.p1, (Double) location.p2, (Double) location.p3);				
			BodyShape earth = new OneAxisEllipsoid(ae, f, FramesFactory.getITRF2005());
			TopocentricFrame sta1Frame = new TopocentricFrame(earth, point, location.getName());

			/** Register the injector that will send the detected events, for this location, to the propagator. */
			EventDetector sta1Visi = new LocationContactEventInjector(maxcheck, location.getThresholdElevation(), sta1Frame, request.satellite, location, datasetidentifier, this);
			propagator.addEventDetector(sta1Visi);				
		}

		OrbitalStateInjector injector = new OrbitalStateInjector(datasetidentifier, this, request.getName(), request.getDescription(), request.satellite);
				
		propagator.setMasterMode(request.stepSize, injector);			
		propagator.propagate(new AbsoluteDate(initialDate, request.deltaPropagation));
		
		return results;
	}
	
	public void addResult(Object result) {
		this.results.add(result);
	}
}

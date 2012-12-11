package org.hbird.business.navigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.navigation.KeplianOrbitalState;
import org.hbird.exchange.navigation.OrbitPredictionRequest;
import org.hbird.exchange.navigation.TleOrbitalState;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.tle.TLEPropagator;

public class TleOrbitalPredictor extends OrbitPredictor {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(OrbitPredictor.class);

	/** The exchange must contain a OrbitalState object as the in-body. 
	 * @throws OrekitException */
	@Handler
	public List<Object> predictOrbit(@Body OrbitPredictionRequest request) throws OrekitException {

		results.clear();

		TleOrbitalState initialState = null;

		/** Check if the request contains the initial state. Else use the last known state of the satellite. */
		if (request.getArguments().containsKey("initialstate") == false) {
			// TODO
		}
		else {
			initialState = (TleOrbitalState) request.getArguments().get("initialstate");
		}

		// TODO

		return results;
	}
}

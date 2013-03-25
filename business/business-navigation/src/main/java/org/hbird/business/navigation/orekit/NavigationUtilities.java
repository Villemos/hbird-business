package org.hbird.business.navigation.orekit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math.geometry.Vector3D;
import org.apache.log4j.Logger;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.LocalOrbitalFrame.LOFType;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;
import org.orekit.utils.PVCoordinates;

public class NavigationUtilities {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(NavigationUtilities.class);
	
    protected static UTCScale scale = null;
    
    static {
        try {
            scale = TimeScalesFactory.getUTC();
        }
        catch (OrekitException e) {
            e.printStackTrace();
        }
    }

    public static PVCoordinates toPVCoordinates(D3Vector pos, D3Vector vel) {

        Vector3D position = new Vector3D(pos.p1, pos.p2, pos.p3);
        Vector3D velocity = new Vector3D(vel.p1, vel.p2, vel.p3);

        return new PVCoordinates(position, velocity);
    }

    public static OrbitalState toOrbitalState(SpacecraftState state, String satellite, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {

        /** Create position vector. */
        D3Vector position = new D3Vector("", "Position", "Position", "The orbital position of the satellite at the given time.",
                state.getOrbit().getPVCoordinates().getPosition().getX(),
                state.getOrbit().getPVCoordinates().getPosition().getY(),
                state.getOrbit().getPVCoordinates().getPosition().getZ());

        /** Create velocity vector. */
        D3Vector velocity = new D3Vector("", "Velocity", "Velocity", "The orbital velocity of the satellite at the given time",
                state.getOrbit().getPVCoordinates().getVelocity().getX(),
                state.getOrbit().getPVCoordinates().getVelocity().getY(),
                state.getOrbit().getPVCoordinates().getVelocity().getZ());

        /** Create momentum vector. */
        D3Vector momentum = new D3Vector("", "Velocity", "Velocity", "The orbital velocity of the satellite at the given time",
                state.getOrbit().getPVCoordinates().getMomentum().getX(),
                state.getOrbit().getPVCoordinates().getMomentum().getY(),
                state.getOrbit().getPVCoordinates().getMomentum().getZ());

        return new OrbitalState(StandardComponents.ORBIT_PREDICTOR, "OrbitalState", "Predicted", "Orbital state of satellite", state.getDate().toDate(scale).getTime(),
                satellite, position,
                velocity, momentum, derivedFromName, derivedFromTimestamp, derivedFromType);
    }

    public static List<PointingData> calculateContactData(LocationContactEvent startContactEvent, LocationContactEvent endContactEvent,
            GroundStation groundStation, Satellite satellite, long contactDataStepSize) throws OrekitException {
        List<PointingData> data = new ArrayList<PointingData>();

        long startTime = startContactEvent.getTimestamp();
        long endTime = endContactEvent.getTimestamp();

        D3Vector location = groundStation.getGeoLocation();
        GeodeticPoint point = new GeodeticPoint(location.p1, location.p2, location.p3);
        TopocentricFrame locationOnEarth = new TopocentricFrame(Constants.earth, point, "");

        PVCoordinates coord = toPVCoordinates(startContactEvent.getSatelliteState().position, startContactEvent.getSatelliteState().velocity);

        AbsoluteDate date = new AbsoluteDate(new Date(startTime), TimeScalesFactory.getUTC());            
		Orbit initialOrbit = new KeplerianOrbit(coord, Constants.frame, date, Constants.MU);
		Propagator propagator = new KeplerianPropagator(initialOrbit);

		/** Register initial point */
    	double azimuth = calculateAzimuth(coord, locationOnEarth, date);
        double elevation = calculateElevation(coord, locationOnEarth, date);
        double doppler = calculateDoppler(coord, locationOnEarth, date);
        double dopplerShift = calculateDopplerShift(doppler, satellite.getFrequency());

        PointingData entry = new PointingData(startTime, azimuth, elevation, doppler, dopplerShift, startContactEvent.getSatelliteName(), location.getName());

        LOG.debug(entry.prettyPrint());
        data.add(entry);

        /** Calculate contact data. */		
        for (int i = 1; startTime + contactDataStepSize * i < endTime; i++) {

        	/** New target date */
            AbsoluteDate target = new AbsoluteDate(new Date(startTime + contactDataStepSize * i), TimeScalesFactory.getUTC());            
    		SpacecraftState newState = propagator.propagate(target);
        	coord = newState.getPVCoordinates();

        	azimuth = calculateAzimuth(coord, locationOnEarth, date);
            elevation = calculateElevation(coord, locationOnEarth, date);
            doppler = calculateDoppler(coord, locationOnEarth, date);
            dopplerShift = calculateDopplerShift(doppler, satellite.getFrequency());

            long time = startTime + contactDataStepSize * i;
            
            entry = new PointingData(time, azimuth, elevation, doppler, dopplerShift, startContactEvent.getSatelliteName(), location.getName());

            LOG.debug(entry.prettyPrint());
            data.add(entry);            
        }

        return data;
    }

    protected static double calculateAzimuth(PVCoordinates state, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {
        return Math.toDegrees(locationOnEarth.getAzimuth(state.getPosition(), Constants.frame, absoluteDate));
    }

    protected static double calculateElevation(PVCoordinates satellite, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {
        return Math.toDegrees(locationOnEarth.getElevation(satellite.getPosition(), Constants.frame, absoluteDate));
    }

    protected static double calculateDoppler(PVCoordinates satellite, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {
        Orbit initialOrbit = new CartesianOrbit(satellite, Constants.frame, absoluteDate, Constants.MU); // an orbit
                                                                                                         // defined by
                                                                                                         // the position
                                                                                                         // and the
                                                                                                         // velocity of
                                                                                                         // the
                                                                                                         // satellite in
                                                                                                         // the inertial
                                                                                                         // frame at the
                                                                                                         // date.
        Propagator propagator = new KeplerianPropagator(initialOrbit);// as a propagator, we consider a simple
                                                                      // KeplerianPropagator.
        LocalOrbitalFrame lof = new LocalOrbitalFrame(Constants.frame, LOFType.QSW, propagator, "QSW"); // local orbital
                                                                                                        // frame.
        PVCoordinates pv = locationOnEarth.getTransformTo(lof, absoluteDate).transformPVCoordinates(PVCoordinates.ZERO);
        return Vector3D.dotProduct(pv.getPosition(), pv.getVelocity()) / pv.getPosition().getNorm();
    }

    public static double calculateDopplerShift(double doppler, double frequency) {
        return ((1 - (doppler / Constants.SPEED_OF_LIGHT)) * frequency) - frequency;
    }
}

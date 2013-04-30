package org.hbird.business.navigation.orekit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math.geometry.Vector3D;
import org.apache.log4j.Logger;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
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
import org.orekit.tle.TLE;
import org.orekit.utils.PVCoordinates;

public class NavigationUtilities {

    // constants for orbit number calculation from TLE

    public static final int MINUTES_PER_DAY = 60 * 24;
    public static final int SECONDS_PER_DAY = 60 * MINUTES_PER_DAY;

    public static final double NORMALIZED_EQUATORIAL_RADIUS = 1.0;
    public static final double XKE = 0.0743669161331734132;
    public static final double XJ2 = 1.082616e-3;
    public static final double CK2 = 0.5 * XJ2 * NORMALIZED_EQUATORIAL_RADIUS * NORMALIZED_EQUATORIAL_RADIUS;

    private static org.apache.log4j.Logger LOG = Logger.getLogger(NavigationUtilities.class);

    /** UTC scale to use. Don't access directly use {@link #getScale()} instead. */
    protected static UTCScale scale = null;

    protected static UTCScale getScale() {
        if (scale == null) {
            try {
                scale = TimeScalesFactory.getUTC();
            }
            catch (OrekitException e) {
                LOG.error("Failed to load UTC Scale", e);
            }
        }
        return scale;
    }

    public static PVCoordinates toPVCoordinates(D3Vector pos, D3Vector vel) {

        Vector3D position = new Vector3D(pos.p1, pos.p2, pos.p3);
        Vector3D velocity = new Vector3D(vel.p1, vel.p2, vel.p3);

        return new PVCoordinates(position, velocity);
    }

    public static OrbitalState toOrbitalState(SpacecraftState state, TleOrbitalParameters parameters) {

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

        return new OrbitalState(NavigationComponent.ORBIT_PROPAGATOR_NAME, "OrbitalState", "Orbital state of satellite",
                state.getDate().toDate(getScale()).getTime(),
                parameters.getSatelliteId(), position,
                velocity, momentum, parameters);
    }

    public static List<PointingData> calculateContactData(LocationContactEvent locationContactEvent,
            GroundStation groundStation, Satellite satellite, long contactDataStepSize) throws OrekitException {
        List<PointingData> data = new ArrayList<PointingData>();

        long startTime = locationContactEvent.getStartTime();
        long endTime = locationContactEvent.getEndTime();

        D3Vector location = groundStation.getGeoLocation();
        GeodeticPoint point = new GeodeticPoint(location.p1, location.p2, location.p3);
        TopocentricFrame locationOnEarth = new TopocentricFrame(Constants.earth, point, "");

        OrbitalState startState = locationContactEvent.getSatelliteStateAtStart();
        PVCoordinates coord = toPVCoordinates(startState.getPosition(), startState.getVelocity());

        AbsoluteDate date = new AbsoluteDate(new Date(startTime), TimeScalesFactory.getUTC());
        Orbit initialOrbit = new KeplerianOrbit(coord, Constants.frame, date, Constants.MU);
        Propagator propagator = new KeplerianPropagator(initialOrbit);

        /** Register initial point */
        double azimuth = calculateAzimuth(coord, locationOnEarth, date);
        double elevation = calculateElevation(coord, locationOnEarth, date);
        double doppler = calculateDoppler(coord, locationOnEarth, date);

        PointingData entry = new PointingData(startTime, azimuth, elevation, doppler, locationContactEvent.getSatelliteId(), location.getName());

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

            long time = startTime + contactDataStepSize * i;

            entry = new PointingData(time, azimuth, elevation, doppler, locationContactEvent.getSatelliteId(), location.getName());

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

        // an orbit defined by the position and the velocity of the satellite in the inertial frame at the date.
        Orbit initialOrbit = new CartesianOrbit(satellite, Constants.frame, absoluteDate, Constants.MU);

        // as a propagator, we consider a simple KeplerianPropagator.
        Propagator propagator = new KeplerianPropagator(initialOrbit);

        // local orbital frame.
        LocalOrbitalFrame lof = new LocalOrbitalFrame(Constants.frame, LOFType.QSW, propagator, "QSW");

        PVCoordinates pv = locationOnEarth.getTransformTo(lof, absoluteDate).transformPVCoordinates(PVCoordinates.ZERO);
        return Vector3D.dotProduct(pv.getPosition(), pv.getVelocity()) / pv.getPosition().getNorm();
    }

    public static double calculateDopplerShift(double doppler, double frequency) {
        return ((1 - (doppler / Constants.SPEED_OF_LIGHT)) * frequency) - frequency;
    }

    public static int calculateOrbitNumber(TLE tle, AbsoluteDate date) throws OrekitException {
        // calculations for original recovered mean motion.
        final double a1 = Math.pow(XKE / (tle.getMeanMotion() * 60.0), 2.0 / 3.0);
        final double cosi0 = Math.cos(tle.getI());
        final double theta2 = cosi0 * cosi0;
        final double x3thm1 = 3.0 * theta2 - 1.0;
        final double e0sq = tle.getE() * tle.getE();
        final double beta02 = 1.0 - e0sq;
        final double beta0 = Math.sqrt(beta02);
        final double tval = CK2 * 1.5 * x3thm1 / (beta0 * beta02);
        final double delta1 = tval / (a1 * a1);
        final double a0 = a1 * (1.0 - delta1 * (1.0 / 3.0 + delta1 * (1.0 + 134.0 / 81.0 * delta1)));
        final double delta0 = tval / (a0 * a0);

        // recover original mean motion :
        final double xn0dp = tle.getMeanMotion() * 60.0 / (delta0 + 1.0);

        double age = date.durationFrom(tle.getDate()) / SECONDS_PER_DAY;

        int orbitNum = (int) (Math.floor((xn0dp * MINUTES_PER_DAY / (Math.PI * 2) + age * tle.getBStar() * 1.0) * age
                + tle.getMeanAnomaly() / (Math.PI * 2))
                + tle.getRevolutionNumberAtEpoch() - 1);
        return orbitNum;
    }
}

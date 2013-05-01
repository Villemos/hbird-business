package org.hbird.business.navigation.orekit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math.geometry.Vector3D;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.GeoLocation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.PointingData;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.LocalOrbitalFrame.LOFType;
import org.orekit.frames.TopocentricFrame;
import org.orekit.frames.Transform;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationUtilities {

    private static Logger LOG = LoggerFactory.getLogger(NavigationUtilities.class);

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

    public static OrbitalState toOrbitalState(SpacecraftState state, String satelliteId, String derivedFrom) {

        Vector3D pvcPosition = state.getOrbit().getPVCoordinates().getPosition();
        Vector3D pvcVelocity = state.getOrbit().getPVCoordinates().getVelocity();
        Vector3D pvcMomoentum = state.getOrbit().getPVCoordinates().getMomentum();

        /* Create position vector. */
        D3Vector position = new D3Vector("", "Position", "Position", "The orbital position of the satellite at the given time.",
                pvcPosition.getX(), pvcPosition.getY(), pvcPosition.getZ());

        /* Create velocity vector. */
        D3Vector velocity = new D3Vector("", "Velocity", "Velocity", "The orbital velocity of the satellite at the given time",
                pvcVelocity.getX(), pvcVelocity.getY(), pvcVelocity.getZ());

        /* Create momentum vector. */
        D3Vector momentum = new D3Vector("", "Velocity", "Velocity", "The orbital velocity of the satellite at the given time",
                pvcMomoentum.getX(), pvcMomoentum.getY(), pvcMomoentum.getZ());

        OrbitalState result = new OrbitalState(NavigationComponent.ORBIT_PROPAGATOR_NAME, OrbitalState.class.getSimpleName(), "Orbital state of satellite",
                state.getDate().toDate(getScale()).getTime(), satelliteId, position, velocity, momentum, derivedFrom);

        try {
            GeoLocation geoLocation = toGeoLocation(state);
            result.setGeoLocation(geoLocation);
        }
        catch (OrekitException e) {
            LOG.error("Failed to calculate GeoLocation from the SpaceCraftState", e);
        }
        return result;
    }

    public static GeoLocation toGeoLocation(SpacecraftState state) throws OrekitException {
        AbsoluteDate date = state.getDate();
        PVCoordinates pvInert = state.getPVCoordinates();
        Transform t = state.getFrame().getTransformTo(Constants.earth.getBodyFrame(), date);
        Vector3D p = t.transformPosition(pvInert.getPosition());
        GeodeticPoint center = Constants.earth.transform(p, Constants.earth.getBodyFrame(), date);
        GeoLocation geoLocation = new GeoLocation(Math.toDegrees(center.getLatitude()), Math.toDegrees(center.getLongitude()), center.getAltitude());
        return geoLocation;
    }

    public static List<PointingData> calculateContactData(LocationContactEvent locationContactEvent,
            GroundStation groundStation, long contactDataStepSize) throws OrekitException {
        List<PointingData> data = new ArrayList<PointingData>();

        long startTime = locationContactEvent.getStartTime();
        long endTime = locationContactEvent.getEndTime();

        D3Vector location = groundStation.getGeoLocation();
        GeodeticPoint point = new GeodeticPoint(location.p1, location.p2, location.p3);
        TopocentricFrame locationOnEarth = new TopocentricFrame(Constants.earth, point, groundStation.getName());

        OrbitalState startState = locationContactEvent.getSatelliteStateAtStart();
        PVCoordinates coord = toPVCoordinates(startState.getPosition(), startState.getVelocity());

        AbsoluteDate date = new AbsoluteDate(new Date(startTime), TimeScalesFactory.getUTC());
        // TODO - 01.05.2013, kimmell - use Cartesian instead of Keplerian here?
        // TODO - 01.05.2013, kimmell - which frame to use here?
        Orbit initialOrbit = new KeplerianOrbit(coord, Constants.FRAME, date, Constants.MU);
        Propagator propagator = new KeplerianPropagator(initialOrbit);
        String satelliteId = locationContactEvent.getSatelliteId();
        String gsId = groundStation.getGroundStationId();
        double timeSift = contactDataStepSize / 1000D; // shift has to be in seconds

        /** Calculate contact data. */
        for (int i = 0; startTime + contactDataStepSize * i < endTime; i++) {
            SpacecraftState newState = propagator.propagate(date);
            coord = newState.getPVCoordinates();
            double azimuth = calculateAzimuth(coord, locationOnEarth, date);
            double elevation = calculateElevation(coord, locationOnEarth, date);
            double doppler = calculateDoppler(coord, locationOnEarth, date);
            long time = date.toDate(TimeScalesFactory.getUTC()).getTime();
            PointingData entry = new PointingData(time, azimuth, elevation, doppler, satelliteId, gsId);
            LOG.debug(entry.prettyPrint());
            data.add(entry);
            /* New target date */
            date = date.shiftedBy(timeSift);
        }

        return data;
    }

    protected static double calculateAzimuth(PVCoordinates state, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {
        return Math.toDegrees(locationOnEarth.getAzimuth(state.getPosition(), Constants.FRAME, absoluteDate));
    }

    protected static double calculateElevation(PVCoordinates state, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {
        return Math.toDegrees(locationOnEarth.getElevation(state.getPosition(), Constants.FRAME, absoluteDate));
    }

    protected static double calculateDoppler(PVCoordinates state, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {

        // an orbit defined by the position and the velocity of the satellite in the inertial FRAME at the date.
        // TODO - 01.05.2013, kimmell - which Orbit and Frame to use here?
        Orbit initialOrbit = new CartesianOrbit(state, Constants.FRAME, absoluteDate, Constants.MU);

        // as a propagator, we consider a simple KeplerianPropagator.
        Propagator propagator = new KeplerianPropagator(initialOrbit);

        // local orbital FRAME.
        LocalOrbitalFrame lof = new LocalOrbitalFrame(Constants.FRAME, LOFType.QSW, propagator, "QSW");

        PVCoordinates pv = locationOnEarth.getTransformTo(lof, absoluteDate).transformPVCoordinates(PVCoordinates.ZERO);
        return Vector3D.dotProduct(pv.getPosition(), pv.getVelocity()) / pv.getPosition().getNorm();
    }

    public static double calculateDopplerShift(double doppler, double frequency) {
        return ((1 - (doppler / Constants.SPEED_OF_LIGHT)) * frequency) - frequency;
    }

    public static int calculateOrbitNumber(TLE tle, AbsoluteDate date) throws OrekitException {
        // calculations for original recovered mean motion.
        final double a1 = Math.pow(Constants.XKE / (tle.getMeanMotion() * 60.0), 2.0 / 3.0);
        final double cosi0 = Math.cos(tle.getI());
        final double theta2 = cosi0 * cosi0;
        final double x3thm1 = 3.0 * theta2 - 1.0;
        final double e0sq = tle.getE() * tle.getE();
        final double beta02 = 1.0 - e0sq;
        final double beta0 = Math.sqrt(beta02);
        final double tval = Constants.CK2 * 1.5 * x3thm1 / (beta0 * beta02);
        final double delta1 = tval / (a1 * a1);
        final double a0 = a1 * (1.0 - delta1 * (1.0 / 3.0 + delta1 * (1.0 + 134.0 / 81.0 * delta1)));
        final double delta0 = tval / (a0 * a0);

        // recover original mean motion :
        final double xn0dp = tle.getMeanMotion() * 60.0 / (delta0 + 1.0);

        double age = date.durationFrom(tle.getDate()) / Constants.SECONDS_PER_DAY;

        int orbitNum = (int) (Math.floor((xn0dp * (Constants.MINUTES_PER_DAY / (Math.PI * 2)) + age * tle.getBStar() * 1.0) * age
                + tle.getMeanAnomaly() / (Math.PI * 2))
                + tle.getRevolutionNumberAtEpoch() - 1);
        return orbitNum;
    }
}

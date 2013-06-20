package org.hbird.business.navigation.orekit;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.hbird.business.navigation.processors.orekit.EclipseCalculator;
import org.hbird.exchange.navigation.GeoLocation;
import org.hbird.exchange.navigation.OrbitalState;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Transform;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;
import org.orekit.utils.PVCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gert Villemos
 * 
 */
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

    public static PVCoordinates toPVCoordinates(Vector3D position, Vector3D velocity) {
        return new PVCoordinates(position, velocity);
    }

    public static synchronized OrbitalState toOrbitalState(SpacecraftState state, String satelliteId, String derivedFrom) {

        PVCoordinates pvCoordinates = state.getOrbit().getPVCoordinates();
        Vector3D pvcPosition = pvCoordinates.getPosition();
        Vector3D pvcVelocity = pvCoordinates.getVelocity();
        Vector3D pvcMomentum = pvCoordinates.getMomentum();

        // TODO - 20.05.2013, kimmell - use IdBuilder here!
        OrbitalState result = new OrbitalState(satelliteId + "/OrbitalState", OrbitalState.class.getSimpleName());
        result.setDescription("Orbital state of satellite");
        result.setTimestamp(state.getDate().toDate(getScale()).getTime());
        result.setSatelliteId(satelliteId);
        result.setPosition(pvcPosition);
        result.setVelocity(pvcVelocity);
        result.setMomentum(pvcMomentum);
        result.setDerivedFromId(derivedFrom);

        try {
            result.setInSunLight(EclipseCalculator.inSunLight(state));
        }
        catch (OrekitException e) {
            LOG.error("Failed to calculate inSunLight from the SpaceCraftState", e);
        }

        try {
            GeoLocation geoLocation = toGeoLocation(state);
            result.setGeoLocation(geoLocation);
        }
        catch (OrekitException e) {
            LOG.error("Failed to calculate GeoLocation from the SpaceCraftState", e);
        }

        // TODO - 16.05.2013, kimmell - add range
        // result.setRange(range);

        return result;
    }

    public static GeoLocation toGeoLocation(SpacecraftState state) throws OrekitException {
        AbsoluteDate date = state.getDate();
        PVCoordinates pvInert = state.getPVCoordinates();
        Transform t = state.getFrame().getTransformTo(Constants.earth.getBodyFrame(), date);
        Vector3D p = t.transformPosition(pvInert.getPosition());
        GeodeticPoint center = Constants.earth.transform(p, Constants.earth.getBodyFrame(), date);
        return toGeoLocation(center);
    }

    public static GeoLocation toGeoLocation(GeodeticPoint gp) {
        return new GeoLocation(Math.toDegrees(gp.getLatitude()), Math.toDegrees(gp.getLongitude()), gp.getAltitude());
    }

    public static GeodeticPoint toGeodeticPoint(GeoLocation gl) {
        return new GeodeticPoint(Math.toRadians(gl.getLatitude()), Math.toRadians(gl.getLongitude()), gl.getAltitude());
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

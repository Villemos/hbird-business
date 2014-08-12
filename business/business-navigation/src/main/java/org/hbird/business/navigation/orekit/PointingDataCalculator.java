/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.navigation.orekit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hbird.business.navigation.processors.orekit.AzimuthCalculator;
import org.hbird.business.navigation.processors.orekit.DopplerCalculator;
import org.hbird.business.navigation.processors.orekit.EclipseCalculator;
import org.hbird.business.navigation.processors.orekit.ElevationCalculator;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.GeoLocation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.PointingData;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class PointingDataCalculator {

    public static final Logger LOG = LoggerFactory.getLogger(PointingDataCalculator.class);

    public List<PointingData> calculateContactData(LocationContactEvent locationContactEvent,
            GroundStation groundStation, boolean calculateEclipse, long contactDataStepSize) throws OrekitException {
        List<PointingData> data = new ArrayList<PointingData>();
        long startTime = locationContactEvent.getStartTime();
        long endTime = locationContactEvent.getEndTime();

        GeoLocation location = groundStation.getGeoLocation();
        GeodeticPoint point = NavigationUtilities.toGeodeticPoint(location);
        TopocentricFrame locationOnEarth = new TopocentricFrame(Constants.earth, point, groundStation.getName());

        OrbitalState startState = locationContactEvent.getSatelliteStateAtStart();
        PVCoordinates coord = NavigationUtilities.toPVCoordinates(startState.getPosition(), startState.getVelocity());

        AbsoluteDate date = new AbsoluteDate(new Date(startTime), TimeScalesFactory.getUTC());
        // TODO - 01.05.2013, kimmell - use Cartesian instead of Keplerian here?
        // TODO - 01.05.2013, kimmell - which frame to use here?
        Frame inertialFrame = Constants.FRAME; // TODO - 19.05.2013, kimmell -
                                               // check this!
        Orbit initialOrbit = new KeplerianOrbit(coord, inertialFrame, date, Constants.MU);
        Propagator propagator = new KeplerianPropagator(initialOrbit);
        String satelliteId = locationContactEvent.getSatelliteID();
        String gsId = groundStation.getGroundStationID();
        double timeSift = contactDataStepSize / 1000D; // shift has to be in seconds

        /* Calculate contact data. */

        // Used to ignore negative elevations
        long negativeElevationSkip = Math.max(1, contactDataStepSize / 100);
        double negativeElevationSkipShift = negativeElevationSkip / 1000D;

        long lastAddedTime = startTime - 1;
        double lastElevation = -91D;

        boolean addingEnd = false;
        for (long time = startTime; time <= endTime;) {
            if (time <= lastAddedTime)
                break;
            SpacecraftState newState = propagator.propagate(date);
            double elevation = ElevationCalculator.calculateElevation(newState, locationOnEarth, inertialFrame);
            if (elevation >= 0) {
                lastElevation = elevation;
                elevation = Math.toDegrees(elevation);
                double azimuth = Math.toDegrees(AzimuthCalculator.calculateAzimuth(newState, locationOnEarth, inertialFrame));
                double doppler = DopplerCalculator.calculateDoppler(newState, locationOnEarth, inertialFrame);
                PointingData entry = new PointingData(time, azimuth, elevation, doppler, satelliteId, gsId);
                if (calculateEclipse) {
                    entry.setEclipse(EclipseCalculator.calculateEclipse(newState));
                }
                LOG.debug(entry.toString());
                data.add(entry);
                if (addingEnd)
                    break;
                lastAddedTime = time;
                time += contactDataStepSize;
                /* New target date */
                if (time > endTime) { // To include end time
                    addingEnd = true;
                    time = endTime;
                    date = new AbsoluteDate(new Date(endTime), TimeScalesFactory.getUTC());
                } else {
                    date = date.shiftedBy(timeSift);
                }
            } else if (addingEnd || lastElevation >= elevation) {
                lastElevation = elevation;
                time -= negativeElevationSkip;
                date = date.shiftedBy(-negativeElevationSkipShift);
                addingEnd = true;
            } else {
                lastElevation = elevation;
                time += negativeElevationSkip;
                date = date.shiftedBy(negativeElevationSkipShift);
            }
        }
        return data;
    }
}

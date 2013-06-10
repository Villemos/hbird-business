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
package org.hbird.business.navigation.processors.orekit;

import org.apache.commons.math.geometry.Vector3D;
import org.hbird.business.navigation.orekit.Constants;
import org.hbird.exchange.navigation.ContactParameterRange;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.LocalOrbitalFrame.LOFType;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DopplerCalculator extends AbstractContactDetailCalculator {

    /**
     * @see org.hbird.business.navigation.processors.orekit.IContactDetailCalculator#calculate(org.orekit.propagation.SpacecraftState,
     *      org.orekit.propagation.SpacecraftState, org.orekit.frames.TopocentricFrame, org.orekit.frames.Frame,
     *      org.hbird.exchange.navigation.LocationContactEvent)
     */
    @Override
    public void calculate(SpacecraftState startState, SpacecraftState endState, TopocentricFrame locationOnEarth, Frame inertialFrame,
            LocationContactEvent event) throws OrekitException {

        double startDoppler = calculateDoppler(startState, locationOnEarth, inertialFrame);
        double endDoppler = calculateDoppler(startState, locationOnEarth, inertialFrame);
        ContactParameterRange doppler = new ContactParameterRange(startDoppler, endDoppler);
        event.setDoppler(doppler);
    }

    /**
     * Calculates Doppler.
     * 
     * @param state
     * @param locationOnEarth
     * @param inertialFrame
     * @return
     * @throws OrekitException
     * @see {@link #calculateDoppler(PVCoordinates, AbsoluteDate, TopocentricFrame, Frame)}
     */
    public static double calculateDoppler(SpacecraftState state, TopocentricFrame locationOnEarth, Frame inertialFrame) throws OrekitException {
        return calculateDoppler(state.getPVCoordinates(), state.getDate(), locationOnEarth, inertialFrame);
    }

    /**
     * Calculates Doppler using {@link KeplerianPropagator} and {@link CartesianOrbit}.
     * 
     * @param coordinates
     * @param date
     * @param locationOnEarth
     * @param inertialFrame
     * @return
     * @throws OrekitException
     */
    public static double calculateDoppler(PVCoordinates coordinates, AbsoluteDate date, TopocentricFrame locationOnEarth, Frame inertialFrame)
            throws OrekitException {
        Orbit initialOrbit = new CartesianOrbit(coordinates, inertialFrame, date, Constants.MU);
        Propagator propagator = new KeplerianPropagator(initialOrbit);
        LocalOrbitalFrame lof = new LocalOrbitalFrame(inertialFrame, LOFType.QSW, propagator, "QSW");
        PVCoordinates pv = locationOnEarth.getTransformTo(lof, date).transformPVCoordinates(PVCoordinates.ZERO);
        return Vector3D.dotProduct(pv.getPosition(), pv.getVelocity()) / pv.getPosition().getNorm();
    }
}

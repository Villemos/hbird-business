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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.hbird.exchange.navigation.ContactParameterRange;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class AzimuthCalculator extends AbstractContactDetailCalculator {

    @Override
    public void calculate(SpacecraftState startState, SpacecraftState endState, TopocentricFrame locationOnEarth, Frame inertialFrame,
            LocationContactEvent event) throws OrekitException {
        double startAzimuth = calculateAzimuth(startState, locationOnEarth, inertialFrame);
        double endAzimuth = calculateAzimuth(endState, locationOnEarth, inertialFrame);
        ContactParameterRange result = new ContactParameterRange(Math.toDegrees(startAzimuth), Math.toDegrees(endAzimuth));
        event.setAzimuth(result);
    }

    /**
     * Calculates azimuth in radians.
     * 
     * @param state
     * @param locationOnEarth
     * @param inertialFrame
     * @return
     * @throws OrekitException
     */
    public static double calculateAzimuth(SpacecraftState state, TopocentricFrame locationOnEarth, Frame inertialFrame) throws OrekitException {
        return calculateAzimuth(state.getPVCoordinates().getPosition(), state.getDate(), locationOnEarth, inertialFrame);
    }

    /**
     * Calculates azimuth in radians.
     * 
     * @param position
     * @param date
     * @param locationOnEarth
     * @param inertialFrame
     * @return
     * @throws OrekitException
     */
    public static double calculateAzimuth(Vector3D position, AbsoluteDate date, TopocentricFrame locationOnEarth, Frame inertialFrame) throws OrekitException {
        return locationOnEarth.getAzimuth(position, inertialFrame, date);
    }
}

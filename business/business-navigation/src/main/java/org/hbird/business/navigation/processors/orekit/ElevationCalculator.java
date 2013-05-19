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
import org.hbird.exchange.navigation.ExtendedContactParameterRange;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;

/**
 *
 */
public class ElevationCalculator implements IContactDetailCalculator {

    private final double calculationStep;

    public ElevationCalculator(double calculationStep) {
        this.calculationStep = calculationStep;
    }

    /**
     * @see org.hbird.business.navigation.processors.orekit.IContactDetailCalculator#calculate(org.orekit.propagation.SpacecraftState,
     *      org.orekit.propagation.SpacecraftState, org.orekit.frames.TopocentricFrame, org.orekit.frames.Frame,
     *      org.hbird.exchange.navigation.LocationContactEvent)
     */
    @Override
    public void calculate(SpacecraftState startState, SpacecraftState endState, TopocentricFrame locationOnEarth, Frame inertialFrame,
            LocationContactEvent event) throws OrekitException {

        double startElevation = calculateElevation(startState, locationOnEarth, inertialFrame);
        double endElevation = calculateElevation(startState, locationOnEarth, inertialFrame);
        double maxElevation = calculateMaxElevation(startState, endState.getDate(), locationOnEarth, inertialFrame, calculationStep);
        double minElevation = Math.min(startElevation, endElevation);
        ExtendedContactParameterRange range = new ExtendedContactParameterRange(Math.toDegrees(startElevation), Math.toDegrees(endElevation));
        range.setMax(Math.toDegrees(maxElevation));
        range.setMin(Math.toDegrees(minElevation));
        event.setElevation(range);
    }

    double calculateMaxElevation(SpacecraftState startState, AbsoluteDate endDate, TopocentricFrame locationOnEarth, Frame inertialFrame, double calculationStep)
            throws OrekitException {
        double maxElevation = 0D;
        SpacecraftState current = startState.shiftedBy(0); // make a copy
        while (current.getDate().compareTo(endDate) < 0) {
            double elevation = calculateElevation(current, locationOnEarth, inertialFrame);
            // TODO - 19.05.2013, kimmell - optimize: brake the loop when elevation is no more increasing
            maxElevation = Math.max(maxElevation, elevation);
            current = current.shiftedBy(calculationStep);
        }
        return maxElevation;
    }

    /**
     * Calculates elevation in radians
     * 
     * @param state
     * @param locationOnEarth
     * @param inertialFrame
     * @return
     * @throws OrekitException
     */
    public static double calculateElevation(SpacecraftState state, TopocentricFrame locationOnEarth, Frame inertialFrame) throws OrekitException {
        return calculateElevation(state.getPVCoordinates().getPosition(), state.getDate(), locationOnEarth, inertialFrame);
    }

    /**
     * Caclulates elevation in radians.
     * 
     * @param position
     * @param date
     * @param locationOnEarth
     * @param inertialFrame
     * @return
     * @throws OrekitException
     */
    public static double calculateElevation(Vector3D position, AbsoluteDate date, TopocentricFrame locationOnEarth, Frame inertialFrame) throws OrekitException {
        return locationOnEarth.getElevation(position, inertialFrame, date);
    }
}

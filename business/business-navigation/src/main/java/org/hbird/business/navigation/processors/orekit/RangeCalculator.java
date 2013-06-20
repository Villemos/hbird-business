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
import org.hbird.exchange.navigation.ExtendedContactParameterRange;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class RangeCalculator extends AbstractContactDetailCalculator {

    private final double calculationStep;

    public RangeCalculator(double calculationStep) {
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

        double startRange = calculateRang(startState, locationOnEarth, inertialFrame);
        double endRange = calculateRang(endState, locationOnEarth, inertialFrame);
        ExtendedContactParameterRange range = new ExtendedContactParameterRange(startRange, endRange);
        calculateMinAndMax(startState, endState.getDate(), locationOnEarth, inertialFrame, calculationStep, range);
        event.setRange(range);
    }

    void calculateMinAndMax(SpacecraftState startState, AbsoluteDate endDate, TopocentricFrame locationOnEarth, Frame inertialFrame, double calculationStep,
            ExtendedContactParameterRange range) throws OrekitException {
        double minRange = Double.MAX_VALUE;
        double maxRange = 0;
        SpacecraftState current = startState.shiftedBy(0.0D); // make a copy
        while (current.getDate().compareTo(endDate) < 0) {
            double currentRange = calculateRang(current, locationOnEarth, inertialFrame);
            maxRange = Math.max(maxRange, currentRange);
            minRange = Math.min(minRange, currentRange);
            current = current.shiftedBy(calculationStep);
        }
        range.setMax(maxRange);
        range.setMin(minRange);
    }

    public static double calculateRang(SpacecraftState state, TopocentricFrame locationOnEarth, Frame inertialFrame) throws OrekitException {
        return calculateRang(state.getPVCoordinates().getPosition(), state.getDate(), locationOnEarth, inertialFrame);
    }

    public static double calculateRang(Vector3D position, AbsoluteDate date, TopocentricFrame locationOnEarth, Frame inertialFrame) throws OrekitException {
        return locationOnEarth.getRange(position, inertialFrame, date);
    }
}

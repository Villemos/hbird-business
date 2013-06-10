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

import org.hbird.business.navigation.orekit.Constants;
import org.hbird.exchange.navigation.ExtendedContactParameterRange;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class SignalDelayCalculator extends AbstractContactDetailCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(SignalDelayCalculator.class);

    /**
     * @see org.hbird.business.navigation.processors.orekit.IContactDetailCalculator#calculate(org.orekit.propagation.SpacecraftState,
     *      org.orekit.propagation.SpacecraftState, org.orekit.frames.TopocentricFrame, org.orekit.frames.Frame,
     *      org.hbird.exchange.navigation.LocationContactEvent)
     */
    @Override
    public void calculate(SpacecraftState startState, SpacecraftState endState, TopocentricFrame locationOnEarth, Frame inertialFrame,
            LocationContactEvent event) throws OrekitException {

        ExtendedContactParameterRange range = event.getRange();
        if (range == null) {
            LOG.warn("Unable to calculate signal delay; make sure range calulation is done before calling the {}", getClass().getSimpleName());
            return;
        }

        double startDelay = calculateSignalDelay(range.getStart());
        double endDelay = calculateSignalDelay(range.getEnd());
        double minDelay = calculateSignalDelay(range.getMin());
        double maxDelay = calculateSignalDelay(range.getMax());

        ExtendedContactParameterRange signalDelay = new ExtendedContactParameterRange(startDelay, endDelay);
        signalDelay.setMin(minDelay);
        signalDelay.setMax(maxDelay);
        event.setSignalDelay(signalDelay);
    }

    public static double calculateSignalDelay(double range) {
        return range / Constants.SPEED_OF_LIGHT;
    }
}

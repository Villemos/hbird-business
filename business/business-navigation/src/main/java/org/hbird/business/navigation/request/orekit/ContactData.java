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
package org.hbird.business.navigation.request.orekit;

import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;

/**
 *
 */
public class ContactData {

    private final SpacecraftState startState;
    private final SpacecraftState endState;
    private final TopocentricFrame locationOnEarth;
    private final Frame inertialFrame;
    private final LocationContactEvent event;

    /**
     * @param startState
     * @param endState
     * @param locationOnEarth
     * @param inertialFrame
     * @param event
     */
    public ContactData(SpacecraftState startState, SpacecraftState endState, TopocentricFrame locationOnEarth, Frame inertialFrame, LocationContactEvent event) {
        this.startState = startState;
        this.endState = endState;
        this.locationOnEarth = locationOnEarth;
        this.inertialFrame = inertialFrame;
        this.event = event;
    }

    /**
     * @return the endState
     */
    public SpacecraftState getStartState() {
        return startState;
    }

    /**
     * @return the endState
     */
    public SpacecraftState getEndState() {
        return endState;
    }

    /**
     * @return the locationOnEarth
     */
    public TopocentricFrame getLocationOnEarth() {
        return locationOnEarth;
    }

    /**
     * @return the inertialFrame
     */
    public Frame getInertialFrame() {
        return inertialFrame;
    }

    /**
     * @return the event
     */
    public LocationContactEvent getEvent() {
        return event;
    }
}

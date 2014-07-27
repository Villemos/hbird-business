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
import org.hbird.business.navigation.orekit.Constants;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class EclipseCalculator extends AbstractContactDetailCalculator {

    /**
     * @see org.hbird.business.navigation.processors.orekit.IContactDetailCalculator#calculate(org.orekit.propagation.SpacecraftState,
     *      org.orekit.propagation.SpacecraftState, org.orekit.frames.TopocentricFrame, org.orekit.frames.Frame,
     *      org.hbird.exchange.navigation.LocationContactEvent)
     */
    @Override
    public void calculate(SpacecraftState startState, SpacecraftState endState, TopocentricFrame locationOnEarth, Frame inertialFrame,
            LocationContactEvent event) throws OrekitException {

        boolean startInSunligth = inSunLight(startState);
        boolean endInSunligth = inSunLight(endState);
        boolean contactInSunlight = startInSunligth && endInSunligth;
        event.setInSunLigth(contactInSunlight);
    }

    public static boolean inSunLight(SpacecraftState s) throws OrekitException {
        return calculateEclipse(s) > 0.0D;
    }

    public static double calculateEclipse(SpacecraftState s) throws OrekitException {
        double occultedRadius = Constants.EQUATORIAL_RADIUS_OF_SUN;
        double occultingRadius = Constants.EQUATORIAL_RADIUS_OF_THE_EARTH;
        final Vector3D pted = CelestialBodyFactory.getSun().getPVCoordinates(s.getDate(), s.getFrame()).getPosition();
        final Vector3D ping = CelestialBodyFactory.getEarth().getPVCoordinates(s.getDate(), s.getFrame()).getPosition();
        final Vector3D psat = s.getPVCoordinates().getPosition();
        final Vector3D ps = pted.subtract(psat);
        final Vector3D po = ping.subtract(psat);
        final double angle = Vector3D.angle(ps, po);
        final double rs = Math.asin(occultedRadius / ps.getNorm());
        final double ro = Math.asin(occultingRadius / po.getNorm());
        return angle - ro - rs;
    }
}

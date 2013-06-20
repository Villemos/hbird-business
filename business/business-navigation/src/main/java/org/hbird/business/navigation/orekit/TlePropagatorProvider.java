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

import org.hbird.business.navigation.request.PredictionRequest;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.OrekitException;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;

/**
 *
 */
public class TlePropagatorProvider implements IPropagatorProvider {

    /**
     * @throws OrekitException
     * @see org.hbird.business.navigation.orekit.IPropagatorProvider#getPropagator(org.hbird.business.navigation.request.PredictionRequest)
     */
    @Override
    public Propagator getPropagator(PredictionRequest<?> request) throws OrekitException {

        TleOrbitalParameters tleParameters = request.getTleParameters();
        Satellite satellite = request.getSatellite();

        /* Create propagator. */
        TLE tle = new TLE(tleParameters.getTleLine1(), tleParameters.getTleLine2());
        // form grams to kilograms
        double spaceCraftMass = satellite.getSatelliteMass() / 1000.0D;
        // TODO - 20.06.2013, kimmell - is the default law good enough?
        TLEPropagator tlePropagator = TLEPropagator.selectExtrapolator(tle, TLEPropagator.DEFAULT_LAW, spaceCraftMass);
        return tlePropagator;
    }
}

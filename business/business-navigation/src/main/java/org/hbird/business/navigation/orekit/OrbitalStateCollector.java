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
import java.util.List;

import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.navigation.OrbitalState;
import org.orekit.errors.PropagationException;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback class of the orekit propagator. The propagator will call the
 * 'handleStep' method on this object at intervals, providing the next orbital state.
 * This class transforms the orbital state provided by orekit into a generic
 * type, and parses it to the 'seda:OrbitPredictions' route. This route must be
 * configured as part of the system.
 * 
 * @author Gert Villemos
 */
public class OrbitalStateCollector implements OrekitFixedStepHandler {
    private static final Logger LOG = LoggerFactory.getLogger(OrbitalStateCollector.class);

    public static final String ORBITAL_STATE = "orbitalstate";

    protected final String satelliteId;

    protected final String derivedFrom;

    protected final String issuer;

    protected List<OrbitalState> states = new ArrayList<OrbitalState>();

    protected IPublisher publisher;

    protected final IdBuilder idBuilder;

    public OrbitalStateCollector(String satelliteId, String derivedFrom, IPublisher publisher, IdBuilder idBuilder, String issuer) {
        this.satelliteId = satelliteId;
        this.derivedFrom = derivedFrom;
        this.publisher = publisher;
        this.idBuilder = idBuilder;
        this.issuer = issuer;
    }

    /**
     * @see org.orekit.propagation.sampling.OrekitFixedStepHandler#handleStep(org.orekit.propagation.SpacecraftState,
     *      boolean)
     */
    @Override
    public void handleStep(SpacecraftState currentState, boolean isLast) throws PropagationException {
        long generationTime = System.currentTimeMillis();
        OrbitalState state = NavigationUtilities.toOrbitalState(currentState, satelliteId, derivedFrom);
        String id = idBuilder.buildID(satelliteId, ORBITAL_STATE);
        state.setID(id);
        state.setGenerationTime(generationTime);
        state.setIssuedBy(issuer);

        states.add(state);
        if (publisher != null) {
            try {
                publisher.publish(state);
            }
            catch (Exception e) {
                LOG.error("Failed to publish current state", e);
            }
        }
    }

    public List<OrbitalState> getDataSet() {
        return states;
    }

    public OrbitalState getLatestState() {
        return states.isEmpty() ? null : states.get(states.size() - 1);
    }

    public void clearDataSet() {
        states.clear();
    }

    /**
     * @return the publisher
     */
    public IPublisher getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(IPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @see org.orekit.propagation.sampling.OrekitFixedStepHandler#init(org.orekit.propagation.SpacecraftState,
     *      org.orekit.time.AbsoluteDate)
     */
    @Override
    public void init(SpacecraftState s0, AbsoluteDate t) throws PropagationException {
        // TODO - 20.06.2013, kimmell - just ignore ?
    }
}

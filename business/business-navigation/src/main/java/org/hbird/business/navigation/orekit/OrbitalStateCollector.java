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

import org.hbird.business.api.IPublish;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.PropagationException;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;

/**
 * Callback class of the orekit propagator. The propagator will call the
 * 'handleStep' method on this object at intervals, providing the next orbital state.
 * This class transforms the orbital state provided by orekit into a generic
 * type, and parses it to the 'seda:OrbitPredictions' route. This route must be
 * configured as part of the system.
 */
public class OrbitalStateCollector implements OrekitFixedStepHandler {

    private static final long serialVersionUID = 7292599962670447718L;

    protected final String satelliteId;

    protected TleOrbitalParameters parameters;

    protected final IPublish publisher;

    protected List<EntityInstance> states = new ArrayList<EntityInstance>();

    protected String orbitalStateEntityId = null;

    public OrbitalStateCollector(String satelliteId, TleOrbitalParameters parameters, IPublish publisher) {
        this.satelliteId = satelliteId;
        this.parameters = parameters;
        this.publisher = publisher;
        this.orbitalStateEntityId = satelliteId + "/OrbitalState";
    }

    /**
     * @see org.orekit.propagation.sampling.OrekitFixedStepHandler#handleStep(org.orekit.propagation.SpacecraftState,
     *      boolean)
     */
    @Override
    public void handleStep(SpacecraftState currentState, boolean isLast) throws PropagationException {

        OrbitalState state = NavigationUtilities.toOrbitalState(currentState, satelliteId, parameters.getInstanceID());
        state.setEntityID(orbitalStateEntityId);
        states.add(state);

        /* If stream mode, then deliver the data as a stream. */
        if (publisher != null) {
            publisher.publish(state);
        }
    }

    public List<EntityInstance> getDataSet() {
        return states;
    }
}

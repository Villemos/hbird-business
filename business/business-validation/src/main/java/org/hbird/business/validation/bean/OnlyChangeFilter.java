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
package org.hbird.business.validation.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.State;

/**
 * This class implements a filter, ensuring that only changes to a state will be issued.
 * 
 * */
public class OnlyChangeFilter {

    /** The class logger. */
    private static org.apache.log4j.Logger logger = Logger.getLogger(OnlyChangeFilter.class);

    /** Map of the current states. Used to detect changes in state. */
    protected Map<String, Boolean> currentState = new HashMap<String, Boolean>();

    /**
     * Method to detect whether a change has occurred. If no change has occurred, then
     * the route is stopped.
     * 
     * @param exchange The exchange containing the new state.
     */
    public void process(Exchange exchange, @Body State state) throws Exception {

        /** If we know this state already... */
        if (currentState.containsKey(state.getName()) == true) {

            /** If the state has changed. */
            if (currentState.get(state.getName()) != state.getValue()) {
                logger.debug("Setting state " + state.getName() + " to " + state + " as it has changed.");
                currentState.put(state.getName(), state.getValue());
            }
            /** If the state hasnt changed, then stop the route. */
            else {
                logger.info("State change filtered out as not delta change.");
                exchange.setProperty(Exchange.ROUTE_STOP, true);
            }
        }
        /** New state. Register it. */
        else {
            logger.debug("Setting state " + state.getName() + " to " + state + " as it is new.");
            currentState.put(state.getName(), state.getValue());
        }
    }
}

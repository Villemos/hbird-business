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
package org.hbird.business.systemtest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.State;

public class StateArchivalTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(StateArchivalTester.class);

    public void process(CamelContext context) throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();

        Thread.sleep(2000);

        /** Store states. */
        publishState("STATE1", "STATE1", "A test description", "COMMAND1:Command:*", true);
        Thread.sleep(1);
        publishState("STATE2", "STATE2", "A test description", "COMMAND1:Command:*", true);
        Thread.sleep(1);
        publishState("STATE3", "STATE3", "A test description", "COMMAND1:Command:*", false);
        Thread.sleep(1);
        publishState("STATE4", "STATE4", "A test description", "COMMAND1:Command:*", true);
        Thread.sleep(1);
        publishState("STATE5", "STATE5", "A test description", "COMMAND1:Command:*", true);
        Thread.sleep(1);
        publishState("STATE6", "STATE6", "A test description", "COMMAND1:Command:*", false);
        Thread.sleep(1);
        publishState("STATE7", "STATE7", "A test description", "COMMAND1:Command:*", true);
        Thread.sleep(1);
        publishState("STATE1", "STATE1", "A test description", "COMMAND2:Command:*", true);
        Thread.sleep(1);
        publishState("STATE2", "STATE2", "A test description", "COMMAND2:Command:*", true);
        Thread.sleep(1);
        publishState("STATE3", "STATE3", "A test description", "COMMAND2:Command:*", false);
        Thread.sleep(1);
        publishState("STATE4", "STATE4", "A test description", "COMMAND2:Command:*", true);
        Thread.sleep(1);
        publishState("STATE5", "STATE5", "A test description", "COMMAND2:Command:*", true);
        Thread.sleep(1);
        publishState("STATE1", "STATE1", "A test description", "COMMAND3:Command:*", true);
        Thread.sleep(1);
        publishState("STATE2", "STATE2", "A test description", "COMMAND3:Command:*", true);
        Thread.sleep(1);
        publishState("STATE3", "STATE3", "A test description", "COMMAND3:Command:*", true);
        Thread.sleep(1);
        publishState("STATE4", "STATE4", "A test description", "COMMAND3:Command:*", true);

        Thread.sleep(2000);

        /** Test retrieval. */
        Object respond = accessApi.getApplicableTo("COMMAND1:Command:*", State.class);
        azzert(respond != null, "Received a response.");

        Map<String, State> states = new HashMap<String, State>();
        for (State state : (List<State>) respond) {
            states.put(state.getName(), state);
        }

        azzert(states.get("STATE1").getValue() == true, "STATE1 should be 'true'");
        azzert(states.get("STATE2").getValue() == true, "STATE2 should be 'true'");
        azzert(states.get("STATE3").getValue() == false, "STATE3 should be 'false'");
        azzert(states.get("STATE4").getValue() == true, "STATE4 should be 'true'");
        azzert(states.get("STATE5").getValue() == true, "STATE5 should be 'true'");
        azzert(states.get("STATE6").getValue() == false, "STATE6 should be 'false'");
        azzert(states.get("STATE7").getValue() == true, "STATE7 should be 'true'");

        /** Store a new set of states. Notice that STATE1 and STATE3 doesnt change. */
        publishState("STATE2", "STATE2", "A test description", "COMMAND1:Command:*", true);
        publishState("STATE4", "STATE4", "A test description", "COMMAND1:Command:*", true);
        publishState("STATE5", "STATE5", "A test description", "COMMAND1:Command:*", false);
        publishState("STATE6", "STATE6", "A test description", "COMMAND1:Command:*", true);
        publishState("STATE7", "STATE7", "A test description", "COMMAND1:Command:*", true);

        Thread.sleep(2000);

        /** Test retrieval. */
        respond = accessApi.getApplicableTo("COMMAND1:Command:*", State.class);
        azzert(respond != null, "Received a response #2.");

        states = new HashMap<String, State>();
        for (State state : (List<State>) respond) {
            states.put(state.getName(), state);
        }
        azzert(states.get("STATE1").getValue() == true, "STATE1 should be 'true'");
        azzert(states.get("STATE2").getValue() == true, "STATE2 should be 'true'");
        azzert(states.get("STATE3").getValue() == false, "STATE3 should be 'false'");
        azzert(states.get("STATE4").getValue() == true, "STATE4 should be 'true'");
        azzert(states.get("STATE5").getValue() == false, "STATE5 should be 'false'");
        azzert(states.get("STATE6").getValue() == true, "STATE6 should be 'true'");
        azzert(states.get("STATE7").getValue() == true, "STATE7 should be 'true'");

        LOG.info("Finished");
    }
}

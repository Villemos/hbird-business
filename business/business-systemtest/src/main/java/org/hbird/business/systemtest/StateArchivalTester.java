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
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.StateRequest;

public class StateArchivalTester extends SystemTest {

    public void process(CamelContext context) throws InterruptedException {
    	        
    	startMonitoringArchive();
		
		Thread.sleep(2000);

    	/** Store states. */
        injection.sendBody(new State("SystemTestSuite", "STATE1", "A test description,", "COMMAND1", true));
        injection.sendBody(new State("SystemTestSuite", "STATE2", "A test description,", "COMMAND1", true));
        injection.sendBody(new State("SystemTestSuite", "STATE3", "A test description,", "COMMAND1", false));
        injection.sendBody(new State("SystemTestSuite", "STATE4", "A test description,", "COMMAND1", true));
        injection.sendBody(new State("SystemTestSuite", "STATE5", "A test description,", "COMMAND1", true));
        injection.sendBody(new State("SystemTestSuite", "STATE6", "A test description,", "COMMAND1", false));
        injection.sendBody(new State("SystemTestSuite", "STATE7", "A test description,", "COMMAND1", true));
        
        injection.sendBody(new State("SystemTestSuite", "STATE8", "A test description,", "COMMAND2", true));
        injection.sendBody(new State("SystemTestSuite", "STATE9", "A test description,", "COMMAND2", true));
        injection.sendBody(new State("SystemTestSuite", "STATE10", "A test description,", "COMMAND2", false));
        injection.sendBody(new State("SystemTestSuite", "STATE11", "A test description,", "COMMAND2", true));
        injection.sendBody(new State("SystemTestSuite", "STATE12", "A test description,", "COMMAND2", true));
        
        injection.sendBody(new State("SystemTestSuite", "STATE13", "A test description,", "COMMAND3", true));
        injection.sendBody(new State("SystemTestSuite", "STATE14", "A test description,", "COMMAND3", true));
        injection.sendBody(new State("SystemTestSuite", "STATE15", "A test description,", "COMMAND3", true));
        injection.sendBody(new State("SystemTestSuite", "STATE16", "A test description,", "COMMAND3", true));

		/** Send command to commit all changes. */
		injection.sendBody(new CommitRequest("SystemTest", "ParameterArchive"));	

        /** Test retrieval. */
		Object respond = injection.requestBody(new StateRequest("SystemTest", "ParameterArchive", "COMMAND1"));
		azzert(respond != null, "Received a response.");
        
        Map<String, State> states = new HashMap<String, State>();
        for (State state : (List<State>) respond) {
        	states.put(state.getName(), state);
        }
        
        azzert(states.get("STATE1").getValue() == true);
        azzert(states.get("STATE2").getValue() == true);
        azzert(states.get("STATE3").getValue() == false);
        azzert(states.get("STATE4").getValue() == true);
        azzert(states.get("STATE5").getValue() == true);
        azzert(states.get("STATE6").getValue() == false);
        azzert(states.get("STATE7").getValue() == true);

    	/** Store a new set of states. Notice that STATE1 and STATE3 doesnt change. */
        injection.sendBody(new State("SystemTestSuite", "STATE2", "A test description,", "COMMAND1", true));
        injection.sendBody(new State("SystemTestSuite", "STATE4", "A test description,", "COMMAND1", true));
        injection.sendBody(new State("SystemTestSuite", "STATE5", "A test description,", "COMMAND1", false));
        injection.sendBody(new State("SystemTestSuite", "STATE6", "A test description,", "COMMAND1", true));
        injection.sendBody(new State("SystemTestSuite", "STATE7", "A test description,", "COMMAND1", true));

		/** Send command to commit all changes. */
		injection.sendBody(new CommitRequest("SystemTest", "ParameterArchive"));	

        /** Test retrieval. */
		respond = injection.requestBody(new StateRequest("SystemTest", "ParameterArchive", "COMMAND1"));
		azzert(respond != null, "Received a response.");

        states = new HashMap<String, State>();
        for (State state : (List<State>) respond) {
        	states.put(state.getName(), state);
        }
        azzert(states.get("STATE1").getValue() == true);
        azzert(states.get("STATE2").getValue() == true);
        azzert(states.get("STATE3").getValue() == false);
        azzert(states.get("STATE4").getValue() == true);
        azzert(states.get("STATE5").getValue() == false);
        azzert(states.get("STATE6").getValue() == true);
        azzert(states.get("STATE7").getValue() == true);

		/** Send command to commit all changes. */
		injection.sendBody(new CommitRequest("SystemTest", "ParameterArchive"));	

        /** Test retrieval. */
		StateRequest stateRequest = new StateRequest("SystemTest", "ParameterArchive", "COMMAND1");
        stateRequest.addName("STATE11");
        stateRequest.addName("STATE15");
		
		respond = injection.requestBody(stateRequest);
		azzert(respond != null, "Received a response.");

        states = new HashMap<String, State>();
        for (State state : (List<State>) respond) {
        	states.put(state.getName(), state);
        }

        azzert(states.get("STATE1").getValue() == true);
        azzert(states.get("STATE2").getValue() == true);
        azzert(states.get("STATE3").getValue() == false);
        azzert(states.get("STATE4").getValue() == true);
        azzert(states.get("STATE5").getValue() == false);
        azzert(states.get("STATE6").getValue() == true);
        azzert(states.get("STATE7").getValue() == true);
        azzert(states.get("STATE11").getValue() == true);
        azzert(states.get("STATE15").getValue() == true);
    }
}

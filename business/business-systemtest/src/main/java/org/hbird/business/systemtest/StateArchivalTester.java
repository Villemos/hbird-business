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
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.State;

public class StateArchivalTester extends SystemTest {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(StateArchivalTester.class);
	
    public void process(CamelContext context) throws InterruptedException {
    	        
		LOG.info("------------------------------------------------------------------------------------------------------------");
    	LOG.info("Starting");
    	
    	IDataAccess api = ApiFactory.getDataAccessApi("SystemTest");
    	
    	startMonitoringArchive();
		
		Thread.sleep(2000);

    	/** Store states. */
        publishApi.publishState("STATE1", "A test description", estcube1.getID() + "/COMMAND1", true);
		Thread.sleep(1);
        publishApi.publishState("STATE2", "A test description", estcube1.getID() + "/COMMAND1", true);
		Thread.sleep(1);
        publishApi.publishState("STATE3", "A test description", estcube1.getID() + "/COMMAND1", false);
		Thread.sleep(1);
        publishApi.publishState("STATE4", "A test description", estcube1.getID() + "/COMMAND1", true);
		Thread.sleep(1);
        publishApi.publishState("STATE5", "A test description", estcube1.getID() + "/COMMAND1", true);
		Thread.sleep(1);
        publishApi.publishState("STATE6", "A test description", estcube1.getID() + "/COMMAND1", false);
		Thread.sleep(1);
        publishApi.publishState("STATE7", "A test description", estcube1.getID() + "/COMMAND1", true);
		Thread.sleep(1);
        publishApi.publishState("STATE1", "A test description", estcube1.getID() + "/COMMAND2", true);
		Thread.sleep(1);
        publishApi.publishState("STATE2", "A test description", estcube1.getID() + "/COMMAND2", true);
		Thread.sleep(1);
        publishApi.publishState("STATE3", "A test description", estcube1.getID() + "/COMMAND2", false);
		Thread.sleep(1);
        publishApi.publishState("STATE4", "A test description", estcube1.getID() + "/COMMAND2", true);
		Thread.sleep(1);
        publishApi.publishState("STATE5", "A test description", estcube1.getID() + "/COMMAND2", true);
		Thread.sleep(1);
        publishApi.publishState("STATE1", "A test description", estcube1.getID() + "/COMMAND3", true);
		Thread.sleep(1);
        publishApi.publishState("STATE2", "A test description", estcube1.getID() + "/COMMAND3", true);
		Thread.sleep(1);
        publishApi.publishState("STATE3", "A test description", estcube1.getID() + "/COMMAND3", true);
		Thread.sleep(1);
        publishApi.publishState("STATE4", "A test description", estcube1.getID() + "/COMMAND3", true);

        Thread.sleep(2000);
        
        forceCommit();
		
        /** Test retrieval. */
		//Object respond = injection.requestBody(new StateRequest("SystemTest", StandardComponents.PARAMETER_ARCHIVE, "COMMAND1"));
		Object respond = api.retrieveState(estcube1.getID() + "/COMMAND1");
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
        publishApi.publishState("STATE2", "A test description", estcube1.getID() + "/COMMAND1", true);
        publishApi.publishState("STATE4", "A test description", estcube1.getID() + "/COMMAND1", true);
        publishApi.publishState("STATE5", "A test description", estcube1.getID() + "/COMMAND1", false);
        publishApi.publishState("STATE6", "A test description", estcube1.getID() + "/COMMAND1", true);
        publishApi.publishState("STATE7", "A test description", estcube1.getID() + "/COMMAND1", true);

        Thread.sleep(2000);
        
		/** Send command to commit all changes. */
		forceCommit();
		
        /** Test retrieval. */
		// respond = injection.requestBody(new StateRequest("SystemTest", StandardComponents.PARAMETER_ARCHIVE, "COMMAND1"));
		respond = api.retrieveState(estcube1.getID() + "/COMMAND1");
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

		LOG.info("Finished");
    }
}

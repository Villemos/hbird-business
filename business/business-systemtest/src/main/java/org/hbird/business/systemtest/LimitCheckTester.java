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

import org.apache.camel.Handler;
import org.hbird.business.validation.LimitCheckComponent;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimitCheckTester extends SystemTest {

    private static Logger LOG = LoggerFactory.getLogger(LimitCheckTester.class);

    @Handler
    public void process() throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();

        /** Start a limit checker. */
        LOG.info("Issuing commands for starting two lower limit limitcheckers.");

        LimitCheckComponent com = createLimitCheckComponent("LIMIT_CHECKER_1", "PARA1_LOWER_SOFTLIMIT", eLimitType.Lower, "PARA1", 2d,
                "The first lower limit of PARA1", "PARA1_LOWER_SOFTLIMIT");
        startableEntityManager.start(com);

        com = createLimitCheckComponent("LIMIT_CHECKER_2", "PARA1_LOWER_HARDLIMIT", eLimitType.Lower, "PARA1", 0d, "The second lower limit of PARA1",
                "PARA1_LOWER_HARDLIMIT");
        startableEntityManager.start(com);

        Thread.sleep(2000);

        send(5d, "PARA1_LOWER_SOFTLIMIT", true, "PARA1_LOWER_HARDLIMIT", true);
        send(3.1d, "PARA1_LOWER_SOFTLIMIT", true, "PARA1_LOWER_HARDLIMIT", true);
        send(2.1d, "PARA1_LOWER_SOFTLIMIT", true, "PARA1_LOWER_HARDLIMIT", true);
        send(2d, "PARA1_LOWER_SOFTLIMIT", true, "PARA1_LOWER_HARDLIMIT", true);
        send(1.9, "PARA1_LOWER_SOFTLIMIT", false, "PARA1_LOWER_HARDLIMIT", true);
        send(0.1d, "PARA1_LOWER_SOFTLIMIT", false, "PARA1_LOWER_HARDLIMIT", true);
        send(-0.1d, "PARA1_LOWER_SOFTLIMIT", false, "PARA1_LOWER_HARDLIMIT", false);
        send(2.1d, "PARA1_LOWER_SOFTLIMIT", true, "PARA1_LOWER_HARDLIMIT", true);
        send(-0.1d, "PARA1_LOWER_SOFTLIMIT", false, "PARA1_LOWER_HARDLIMIT", false);

        /** Start a limit checker. */
        LOG.info("Issuing commands for starting two upper limit limitcheckers.");

        com = createLimitCheckComponent("LIMIT_CHECKER_3", "PARA1_UPPER_SOFTLIMIT", eLimitType.Upper, "PARA1", 10.5d, "The first upper limit of PARA1",
                "PARA1_UPPER_SOFTLIMIT");
        startableEntityManager.start(com);

        com = createLimitCheckComponent("LIMIT_CHECKER_4", "PARA1_UPPER_HARDLIMIT", eLimitType.Upper, "PARA1", 15d, "The second upper limit of PARA1",
                "PARA1_UPPER_HARDLIMIT");
        startableEntityManager.start(com);

        Thread.sleep(2000);

        send(9d, "PARA1_UPPER_SOFTLIMIT", true, "PARA1_UPPER_HARDLIMIT", true);
        send(10d, "PARA1_UPPER_SOFTLIMIT", true, "PARA1_UPPER_HARDLIMIT", true);
        send(10.5d, "PARA1_UPPER_SOFTLIMIT", true, "PARA1_UPPER_HARDLIMIT", true);
        send(11d, "PARA1_UPPER_SOFTLIMIT", false, "PARA1_UPPER_HARDLIMIT", true);
        send(14d, "PARA1_UPPER_SOFTLIMIT", false, "PARA1_UPPER_HARDLIMIT", true);
        send(16d, "PARA1_UPPER_SOFTLIMIT", false, "PARA1_UPPER_HARDLIMIT", false);
        send(11d, "PARA1_UPPER_SOFTLIMIT", false, "PARA1_UPPER_HARDLIMIT", true);
        send(9d, "PARA1_UPPER_SOFTLIMIT", true, "PARA1_UPPER_HARDLIMIT", true);

        /** Disable limit. */
        LOG.info("Disabling limit component 'LIMIT_CHECKER_3'.");
        // publishApi.publishState("PARA1_UPPER_SOFTLIMIT_SWITCH", "PARA1_UPPER_SOFTLIMIT_SWITCH", "A test description",
        // "LIMIT_CHECKER_3", false);

        State state = new State("PARA1_UPPER_SOFTLIMIT_SWITCH", "PARA1_UPPER_SOFTLIMIT_SWITCH");
        state.setDescription("A test description");
        state.setApplicableTo("LIMIT_CHECKER_3");
        state.setValue(false);

        publishApi.publish(state);

        Thread.sleep(2000);

        /** Would normally violate SOFT limit, but the limit should be disabled. */
        azzert(stateListener.lastReceived.getName().equals("PARA1_UPPER_SOFTLIMIT_SWITCH") == true, "Disabling state distributed.");
        send(11d, "PARA1_UPPER_SOFTLIMIT", true, "PARA1_UPPER_HARDLIMIT", true);

        Thread.sleep(2000);

        azzert(stateListener.lastReceived.getName().equals("PARA1_UPPER_SOFTLIMIT") == false, "Disabled state was not distributed.");

        LOG.info("Finished");
    }

    protected LimitCheckComponent createLimitCheckComponent(String componentName, String limitName, eLimitType type, String parameterId, Double value,
            String description, String stateName) {

        LimitCheckComponent component = new LimitCheckComponent(componentName, componentName);

        Limit limit = new Limit(limitName, limitName);
        limit.setLimitOfParameter(parameterId);
        limit.setValue(value);
        limit.setDescription(description);
        limit.setType(type);
        limit.setStateName(stateName);

        component.setLimit(limit);

        return component;
    }

    protected void send(Double value, String soft, boolean expectedSoft, String hard, boolean expectedHard) throws Exception {
        LOG.info("Publishing parameters.");
        // publishApi.publishParameter("PARA1", "PARA1", "A test description,", value, "Volt");

        Parameter param = new Parameter("PARA1", "PARA1");
        param.setDescription("A test description");
        param.setValue(value);
        param.setUnit("Volt");

        publishApi.publish(param);

        /** Give the limit checkers a bit of time. */
        Thread.sleep(2000);

        /** Check that the states were calculated and distributed. */

        LOG.warn("soft: {}, hard: {}", soft, hard);
        for (String key : stateListener.states.keySet()) {
            LOG.warn(" found: '{}'", key);
        }

        azzert(stateListener.states.get(soft).getValue() == expectedSoft, "Received soft limit with value " + expectedSoft);
        azzert(stateListener.states.get(hard).getValue() == expectedHard, "Received hard limit with value " + expectedHard);
    }
}

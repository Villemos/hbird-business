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
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.StartLimitComponent;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.validation.Limit.eLimitType;

public class LimitCheckTester extends Tester {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(LimitCheckTester.class);

	@Handler
	public void process() throws InterruptedException {
			
		/** Start a limit checker. */
		LOG.info("Issuing commands for starting two lower limit limitcheckers.");

		StartLimitComponent request = new StartLimitComponent("PARA1_LowerSoftLimit", eLimitType.Lower, "PARA1", "2d", "PARA1_LOWER_SOFTLIMIT", "The first lower limit of PARA1");
		injection.sendBody(request);

		request = new StartLimitComponent("PARA1_LowerHardLimit", eLimitType.Lower, "PARA1", "0d", "PARA1_LOWER_HARDLIMIT", "The second lower limit of PARA1");
		injection.sendBody(request);

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

		request = new StartLimitComponent("PARA1_UpperSoftLimit", eLimitType.Upper, "PARA1", "10.5d", "PARA1_UPPER_SOFTLIMIT", "The first upper limit of PARA1");
		injection.sendBody(request);

		request = new StartLimitComponent("PARA1_UpperHardLimit", eLimitType.Upper, "PARA1", "15d", "PARA1_UPPER_HARDLIMIT", "The second upper limit of PARA1");
		injection.sendBody(request);

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
		LOG.info("Disabling limit 'PARA1_UpperSoftLimit'.");
		injection.sendBody(new State("SystemTestSuite", "PARA1_UpperSoftLimit_SWITCH", "A test description", "PARA1_UpperSoftLimit", false));

		Thread.sleep(2000);		
		
		/** Would normally violate SOFT limit, but the limit should be disabled. */
		azzert(stateListener.lastReceived.getName().equals("PARA1_UpperSoftLimit_SWITCH") == true, "Disabling state distributed.");
		send(11d, "PARA1_UPPER_SOFTLIMIT", true, "PARA1_UPPER_HARDLIMIT", true);
		
		Thread.sleep(2000);
		
		azzert(stateListener.lastReceived.getName().equals("PARA1_UpperSoftLimit") == false, "Disabled state was not distributed.");
	}	
	
	protected void send(Double value, String soft, boolean expectedSoft, String hard, boolean expectedHard) throws InterruptedException {
		LOG.info("Publishing parameters.");
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", value, "Volt"));

		/** Give the limit checkers a bit of time. */
		Thread.sleep(2000);
		
		/** Check that the states were calculated and distributed. */
		azzert(stateListener.states.get(soft).getValue() == expectedSoft);
		azzert(stateListener.states.get(hard).getValue() == expectedHard);		
	}
}

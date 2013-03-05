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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Parameter;

public class ParameterArchivalTester extends SystemTest {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(ParameterArchivalTester.class);

	@Handler
	public void process(CamelContext context) throws InterruptedException {

		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");
		
		startMonitoringArchive();
		
		Thread.sleep(2000);

		/** Publish parameters. */
		LOG.info("Publishing parameters.");
		publishApi.publishParameter("PARA1", "", "A test description,", 2d, "Volt");
		Thread.sleep(1);
		publishApi.publishParameter("PARA1", "", "A test description,", 2.1d, "Volt");
		Thread.sleep(1);
		publishApi.publishParameter("PARA1", "", "A test description,", 2.2d, "Volt");
		Thread.sleep(1);
		publishApi.publishParameter("PARA1", "", "A test description,", 2.3d, "Volt");
		Thread.sleep(1);
		publishApi.publishParameter("PARA1", "", "A test description,", 2.4d, "Volt");
		Thread.sleep(1);
		publishApi.publishParameter("PARA1", "", "A test description,", 2.5d, "Volt");
		Thread.sleep(1);
		publishApi.publishParameter("PARA1", "", "A test description,", 2.6d, "Volt");
		Thread.sleep(1);
		publishApi.publishParameter("PARA2", "", "A test description,", 2l, "Meter");

		/** Make sure we have different timestamps. */
		Thread.sleep(1);
		Date start = new Date();
		Thread.sleep(1);
		
		publishApi.publishParameter("PARA2", "", "A test description,", 3l, "Meter");
		Thread.sleep(1);
		publishApi.publishParameter("PARA2", "", "A test description,", 4l, "Meter");
		Thread.sleep(1);
		publishApi.publishParameter("PARA3", "", "A test description,", 10f, "Seconds");
		Thread.sleep(1);
		publishApi.publishParameter("PARA3", "", "A test description,", 15f, "Seconds");
		Thread.sleep(1);
		publishApi.publishParameter("PARA3", "", "A test description,", 20f, "Seconds");
		
		Thread.sleep(1);
		Date end = new Date();
		Thread.sleep(1);
		
		publishApi.publishParameter("PARA2", "", "A test description,", 5l, "Meter");
		Thread.sleep(1);
		publishApi.publishParameter("PARA3", "", "A test description,", 35f, "Seconds");

        Thread.sleep(2000);
		
		/** Send command to commit all changes. */
		forceCommit();

		/** Check whether they were published on ActiveMQ to the listener. */
		azzert(parameterListener.elements.size() == 15, "Expect to receive 15 parameters. Received " + parameterListener.elements.size());

		/** Test retrieval. */
		
		// Test retrieval of only the last value of a parameter.
		try {
			LOG.info("Retrieveing last value of PARA1");

			Parameter respond = accessApi.getParameter("PARA1");
			azzert(respond != null, "Received a response.");
			
			azzert(respond.asDouble() == 2.6d, "Last value should be 2.6. Received " + respond.asDouble());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// Test retrieval of a lower bound time range for one parameter.
		try {
			List<Parameter> respond = accessApi.retrieveParameter("PARA2", start.getTime(), (new Date()).getTime());
			azzert(respond != null, "Received a response.");			
			azzert(respond.size() == 3, "Expect 3 entries. Received " + respond.size());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Test retrieval of a lower bound time range for one parameter.
		try {
			List<Parameter> respond = accessApi.retrieveParameter("PARA2", start.getTime(), end.getTime());
			azzert(respond != null, "Received a response.");			
			azzert(respond.size() == 2, "Expect 2 entries. Received " + respond.size());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Test retrieval of a lower bound time range for one parameter.
		try {
			List<Parameter> respond = accessApi.retrieveParameters(Arrays.asList("PARA1", "PARA2", "PARA3"), start.getTime(), end.getTime());
			azzert(respond != null, "Received a response.");			
			azzert(respond.size() == 5, "Expect 5 entries. Received " + respond.size());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		LOG.info("Finished");
	}
}

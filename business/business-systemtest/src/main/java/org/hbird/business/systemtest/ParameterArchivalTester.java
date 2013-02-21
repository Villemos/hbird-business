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
import org.hbird.exchange.dataaccess.ParameterRequest;

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
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2d, "Volt"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.1d, "Volt"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.2d, "Volt"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.3d, "Volt"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.4d, "Volt"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.5d, "Volt"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA1", "", "A test description,", 2.6d, "Volt"));

		injection.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 2l, "Meter"));

		/** Make sure we have different timestamps. */
		Thread.sleep(1000);
		Date start = new Date();
		
		injection.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 3l, "Meter"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 4l, "Meter"));

		injection.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 10f, "Seconds"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 15f, "Seconds"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 20f, "Seconds"));

		Date end = new Date();
		Thread.sleep(1000);
		
		injection.sendBody(new Parameter("SystemTestSuite", "PARA2", "", "A test description,", 5l, "Meter"));
		injection.sendBody(new Parameter("SystemTestSuite", "PARA3", "", "A test description,", 35f, "Seconds"));

		/** Send command to commit all changes. */
		forceCommit();

		/** Check whether they were published on ActiveMQ to the listener. */
		azzert(parameterListener.elements.size() == 15, "Expect to receive 15 parameters. Received " + parameterListener.elements.size());

		/** Test retrieval. */
		
		// Test retrieval of only the last value of a parameter.
		try {
			LOG.info("Retrieveing last value of PARA1");

			Object respond = injection.requestBody(new ParameterRequest("PARA1", 1));
			azzert(respond != null, "Received a response.");
			azzert(((List) respond).isEmpty() == false, "Expected to receive at least one element.");
			
			Parameter parameter = (Parameter) ((List) respond).get(0);
			azzert(parameter.getValue().doubleValue() == 2.6d, "Last value should be 2.6. Received " + parameter.getValue().doubleValue());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// Test retrieval of a lower bound time range for one parameter.
		try {
			Object respond = injection.requestBody(new ParameterRequest("PARA2", start.getTime(), null));
			azzert(respond != null, "Received a response.");			
			azzert(((List) respond).size() == 3, "Expect 3 entries. Received " + ((List) respond).size());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Test retrieval of a lower bound time range for one parameter.
		try {
			Object respond = injection.requestBody(new ParameterRequest("PARA2", start.getTime(), end.getTime()));
			azzert(respond != null, "Received a response.");			
			azzert(((List) respond).size() == 2, "Expect 2 entries. Received " + ((List) respond).size());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Test retrieval of a lower bound time range for one parameter.
		try {
			Object respond = injection.requestBody(new ParameterRequest(Arrays.asList("PARA1", "PARA2", "PARA3"), start.getTime(), end.getTime()));
			azzert(respond != null, "Received a response.");			
			azzert(((List) respond).size() == 5, "Expect 5 entries. Received " + ((List) respond).size());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		LOG.info("Finished");
	}
}

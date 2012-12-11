package org.hbird.business.systemtest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.StartMonitoringDataArchiveComponent;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;

public class ParameterArchivalTester extends Tester {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(ParameterArchivalTester.class);

	@Handler
	public void process(CamelContext context) throws InterruptedException {

		/** Start the Parameter Archive. */
		LOG.info("Issuing command for start of a parameter archive.");
		StartMonitoringDataArchiveComponent request = new StartMonitoringDataArchiveComponent("ParameterArchive");
		injection.sendBody(request);
		Thread.sleep(2000);

		/** Check that the command was distributed. */
		azzert(commandingListener.elements.size() == 1, "Command was distributed.");

		/** TODO Send command to the archive to delete all data. */
		injection.sendBody(new DeletionRequest("SystemTest", "ParameterArchive", "*:*"));	

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
		injection.sendBody(new CommitRequest("SystemTest", "ParameterArchive"));	

		/** Check whether they were published on ActiveMQ to the listener. */
		azzert(monitoringListener.elements.size() == 15, "Expect to receive 15 parameters. Received " + monitoringListener.elements.size());

		/** Test retrieval. */
		
		// Test retrieval of only the last value of a parameter.
		try {
			LOG.info("Retrieveing last value of PARA1");

			Object respond = injection.requestBody(new ParameterRequest("PARA1", 1));
			azzert(respond != null, "Received a response.");
			
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
	}
}

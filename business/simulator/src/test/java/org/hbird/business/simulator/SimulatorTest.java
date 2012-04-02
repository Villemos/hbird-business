package org.hbird.business.simulator;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.hbird.business.simulator.waveforms.FlatWaveform;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/** Will read the Spring configuration of the route from the local file '[filename]-context.xml' */
@ContextConfiguration(locations = { "/SimulatorTest-context.xml" })
public class SimulatorTest extends AbstractJUnit38SpringContextTests {

	/** End point injected when the context XML file is read. */
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint = null;

	@Autowired
	protected Simulator simulator = null;

	@Test
	public void testSimulator() throws Exception {
		resultEndpoint.reset();

		resultEndpoint.setExpectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();

		simulator.sendMessage(0);

		resultEndpoint.setExpectedMessageCount(1);
		resultEndpoint.assertIsSatisfied();

		simulator.sendMessage(0);

		resultEndpoint.setExpectedMessageCount(2);
		resultEndpoint.assertIsSatisfied();

		simulator.sendMessage(0);
		simulator.sendMessage(0);
		simulator.sendMessage(0);

		resultEndpoint.setExpectedMessageCount(5);
		resultEndpoint.assertIsSatisfied();
	}

	@Test
	public void testRunningSimulator() throws Exception {
		resultEndpoint.reset();

		simulator.addWaveform(new FlatWaveform(2));

		Thread simulatorThread = new Thread(simulator);
		simulatorThread.start();
		Thread.sleep(2500);
		simulator.stopSimulator();

		resultEndpoint.setMinimumExpectedMessageCount(2);
		resultEndpoint.assertIsSatisfied();
	}

}

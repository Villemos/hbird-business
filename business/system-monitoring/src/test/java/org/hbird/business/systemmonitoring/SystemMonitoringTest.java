package org.hbird.business.systemmonitoring;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;

import org.hbird.exchange.core.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration (locations={"/SystemMonitoringTest-context.xml"})
public class SystemMonitoringTest extends AbstractJUnit38SpringContextTests {

	@Autowired
    protected CamelContext context;
	
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;
	
	@Produce(uri = "direct:memory")
    protected ProducerTemplate memory;

	@Produce(uri = "direct:threads")
    protected ProducerTemplate threads;

	
	public void testEjectionMessage() throws Exception {		
		assertTrue(resultEndpoint.getReceivedCounter() == 0);
		Exchange exchange = new DefaultExchange(context);
		
		memory.send(exchange);
		assertTrue(resultEndpoint.getReceivedCounter() == 1);
		assertTrue(((Parameter)resultEndpoint.getReceivedExchanges().get(0).getIn().getBody()).getName(), ((Parameter)resultEndpoint.getReceivedExchanges().get(0).getIn().getBody()).getName().equals("Heap Memory Usage"));

		exchange = new DefaultExchange(context);
		threads.send(exchange);
		assertTrue(resultEndpoint.getReceivedCounter() == 2);
		assertTrue(((Parameter)resultEndpoint.getReceivedExchanges().get(1).getIn().getBody()).getName().equals("Thread Count"));

	}
 }

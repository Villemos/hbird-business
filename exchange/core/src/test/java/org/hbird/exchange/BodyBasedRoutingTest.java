package org.hbird.exchange;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.hbird.exchange.core.Parameter;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration (locations={"/BodyBasedRoutingTest-context.xml"})
public class BodyBasedRoutingTest extends AbstractJUnit38SpringContextTests  {

	@Produce(uri = "direct:Start")
    protected ProducerTemplate template;

	@EndpointInject(uri = "mock:Parameter")
	protected MockEndpoint parameterQueue;

	@EndpointInject(uri = "mock:Other")
	protected MockEndpoint otherQueue;
	
	@Test
	public void testWithParameter() throws InterruptedException {
		parameterQueue.reset();
		otherQueue.reset();
		
		template.sendBody(new Parameter("", "parameter1", "test type", "test description", 2, "unit"));
		
		parameterQueue.setExpectedMessageCount(1);
		parameterQueue.assertIsSatisfied();
		
		otherQueue.setExpectedMessageCount(0);
		otherQueue.assertIsSatisfied();
	}	
	
	@Test
	public void testWithOther() throws InterruptedException {
		parameterQueue.reset();
		otherQueue.reset();
		
		template.sendBody(new byte[1000]);
		
		parameterQueue.setExpectedMessageCount(0);
		parameterQueue.assertIsSatisfied();
		
		otherQueue.setExpectedMessageCount(1);
		otherQueue.assertIsSatisfied();
	}
	
	@Test
	public void testBoth() throws InterruptedException {
		parameterQueue.reset();
		otherQueue.reset();
		
		template.sendBody(new Parameter("", "parameter1", "test type", "test description", 2, "unit"));
		template.sendBody(new byte[1000]);
		
		parameterQueue.setExpectedMessageCount(1);
		parameterQueue.assertIsSatisfied();
		
		otherQueue.setExpectedMessageCount(1);
		otherQueue.assertIsSatisfied();
	}	
	
}

package org.hbird.business.simpleparametersimulator;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration (locations={"/SimpleParameterSimulatorTest-context.xml"})
public class SimpleParameterSimulatorTest extends AbstractJUnit38SpringContextTests  {

	@Produce(uri = "direct:Commands")
    protected ProducerTemplate template;

	@Autowired
    protected CamelContext context;
	
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@DirtiesContext
	@Test
	public void testCommanding() throws Exception {
	
		Map<String,Object> headers = new HashMap<String, Object>();
		headers.put("Bean", "Parameter1");
		headers.put("Attribute", "Value");
		
		template.sendBodyAndHeaders(false, headers);
		assertFalse( (Boolean) ((BooleanParameter) context.getRegistry().lookup("Parameter1")).getValue());
		
		template.sendBodyAndHeaders(true, headers);
		assertTrue( (Boolean) ((BooleanParameter) context.getRegistry().lookup("Parameter1")).getValue());
	}
	
}

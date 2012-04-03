/**
 * Licensed under the Apache License, Version 2.0. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0 or at this project's root.
 */

package org.hbird.business.parameterstorage;

import static org.junit.Assert.*;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.hbird.exchange.core.StateParameter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "/InMemoryParameterBufferTest-context.xml" })
public class InMemoryParameterBufferTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	protected InMemoryParameterBuffer provider = null;
	
	@Produce(uri = "direct:ParameterRequest")
	protected ProducerTemplate request = null;

	@Produce(uri = "direct:Parameter")
	protected ProducerTemplate parameter = null;
	
	@Autowired
	protected CamelContext context = null;
	
	@Autowired
	protected ProducerTemplate producer = null;
	
	@Test
	public void testRetrieval() {
		
		/** Store a few state parameters. */
		/** Send the request. Respond is expected to be a single StateParameter. */
		Exchange exchange = new DefaultExchange(context);
		exchange.getIn().setBody(new StateParameter("", "TestParameter1", "Description of test parameter 1", null, new Boolean(true)));
		parameter.send(exchange);
		assertTrue(provider.getLatestParameterValue().size() == 1);
		assertTrue((Boolean) provider.getLatestParameterValue().get("TestParameter1").getValue() == true);

		exchange = new DefaultExchange(context);
		exchange.getIn().setBody(new StateParameter("", "TestParameter2", "Description of test parameter 2", null, new Boolean(true)));
		parameter.send(exchange);
		assertTrue(provider.getLatestParameterValue().size() == 2);
		assertTrue((Boolean) provider.getLatestParameterValue().get("TestParameter2").getValue() == true);
		
		exchange = new DefaultExchange(context);
		exchange.getIn().setBody(new StateParameter("", "TestParameter3", "Description of test parameter 3", null, new Boolean(true)));
		parameter.send(exchange);
		assertTrue(provider.getLatestParameterValue().size() == 3);
		assertTrue((Boolean) provider.getLatestParameterValue().get("TestParameter3").getValue() == true);

		/** Override a parameter.*/
		exchange = new DefaultExchange(context);
		exchange.getIn().setBody(new StateParameter("", "TestParameter3", "Description of test parameter 3", null, new Boolean(false)));
		parameter.send(exchange);
		assertTrue(provider.getLatestParameterValue().size() == 3);
		assertTrue((Boolean) provider.getLatestParameterValue().get("TestParameter3").getValue() == false);

		/** Ask for a parameter. */
		exchange = new DefaultExchange(context, ExchangePattern.InOut);
		exchange.getIn().setBody("name=TestParameter1");
		request.send(exchange);
		assertTrue(exchange.getOut() != null);
		assertTrue(exchange.getOut().getBody() != null);
		assertTrue(((StateParameter) exchange.getOut().getBody()).getStateValue() == true);
		
		exchange = new DefaultExchange(context, ExchangePattern.InOut);
		exchange.getIn().setBody("name=TestParameter3");
		request.send(exchange);
		assertTrue(exchange.getOut() != null);
		assertTrue(exchange.getOut().getBody() != null);
		assertTrue(((StateParameter) exchange.getOut().getBody()).getStateValue() == false);

	}
	
}

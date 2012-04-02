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
package org.hbird.business.calibration;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.hbird.exchange.core.StateParameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration (locations={"/ScriptExecutorTest-context.xml"})
public class ScriptExecutorTest extends AbstractJUnit38SpringContextTests  {

	@Produce(uri = "direct:Start")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:End")
	protected MockEndpoint releaseQueue;

	@Autowired
	protected CamelContext context;

	protected String script = "print(request.getName()); " +
			                  "result = true;"; 
	
	@Test
	public void testScriptExecution() {
		ScriptExecutionRequest request = new ScriptExecutionRequest("", "Test Calibration", 
				                                            "Script used for test calibrations",
				                                            script);
		
		Exchange exchange = new DefaultExchange(context);
		exchange.getIn().setBody(request);
		template.send(exchange);
		
		while (releaseQueue.getExchanges().size() == 0) {
			try {
				// Wait until 1 exchange is received, but max 10ms + 20ms + 40ms + 80ms + 160ms + 320ms + 640ms + 1280ms = 2550ms.
				for(int i = 10; releaseQueue.getExchanges().size() == 1 && i <= 2550; i *= 2) {
					Thread.sleep( i );
				}	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		assertTrue(releaseQueue.getExchanges().size() == 1);
		assertTrue(releaseQueue.getExchanges().get(0).getIn().getBody() != null);
		assertTrue( (Boolean) ((StateParameter) releaseQueue.getExchanges().get(0).getIn().getBody()).getValue() == true);
	}
}

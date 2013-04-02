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

import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;

/**
 * @author Admin
 *
 */
public class JmsSelectorTester extends SystemTest {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(ParseControlTester.class);
	
	class TestRouteBuilder extends RouteBuilder {

		public Listener listener = new Listener();
		
		/* (non-Javadoc)
		 * @see org.apache.camel.builder.RouteBuilder#configure()
		 */
		@Override
		public void configure() throws Exception {
			from(StandardEndpoints.MONITORING + "?selector=class='Parameter' OR class='Label' AND destination='JmsTest' OR destination='any'").bean(listener);			
		}		
	}
	
	@Handler
	public void process(CamelContext context) throws Exception {
		
		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");
		
		TestRouteBuilder builder = new TestRouteBuilder();
		context.addRoutes(builder);
		
		Thread.sleep(2000);
		
		injection.sendBody(new Parameter("SystemTest", "PARA1", "A description", "unit")); // Should be received
		injection.sendBody(new Label("SystemTest", "LABEL1", "A description", "unit")); // Should be received
		injection.sendBody(new State("SystemTest", "STATE1", "A description", "PARA1", false));  // Should NOT be received
		injection.sendBody(new Parameter("SystemTest", "PARA2", "A description", "unit"));   // Should be received

		Thread.sleep(2000);

		azzert(builder.listener.elements.get(0).getName().equals("Obj3"));
		azzert(builder.listener.elements.get(0).getName().equals("Obj3"));
		azzert(builder.listener.elements.get(0).getName().equals("Obj4"));
		

		Thread.sleep(2000);
		
		LOG.info("Finished");
	}	
}

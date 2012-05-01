/**
 * Created by Villemos Solutions (www.villemos.com), 2012.
 * 
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
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
package org.hbird.business.cfdp;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.hbird.exchange.cfdp.requests.Put;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration (locations={"/SenderTest-context.xml"})
public class SenderTest extends AbstractJUnit38SpringContextTests  {

	@Autowired
    protected CamelContext context;
	
	@Produce(uri = "direct:start")
    protected ProducerTemplate sender;
	
	@Test
	public void testSend() {
		Put put = new Put();
		put.destination = "TestReceiver";
		put.destinationFileName = "Test destination filename";
		put.faultHandlerOverrides = "Test fault handler overrides";
		put.filestoreRequest = "Test filestore request";
		put.flowLabel = "Test flow label";
		put.messagesToUser = "Test message";
		put.segmentationControl = "Test segmentation control";
		put.sourceFileName = "src/test/resources/CFDP Test Data.eap";
		put.transmissionMode = "Test transmission mode";
		
		Exchange exchange = new DefaultExchange(context);
		exchange.getIn().setBody(put);
		sender.send(exchange);
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}

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
package org.hbird.business.celestrack;

import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.villemos.ispace.httpcrawler.HttpCrawlerConsumer;

public class CelestrackConsumer extends HttpCrawlerConsumer {

	private static final Log Logger = LogFactory.getLog(CelestrackConsumer.class);
	
	protected CelestrackAccessor crawler = null;
	
	public CelestrackConsumer(DefaultEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		crawler = new CelestrackAccessor(endpoint, null, getAsyncProcessor());
	}

	public CelestrackConsumer(Endpoint endpoint, Processor processor, ScheduledExecutorService executor) {
		super(endpoint, processor, executor);
		crawler = new CelestrackAccessor(endpoint, this, getAsyncProcessor());
	}

	@Override
	protected int poll() throws Exception {
		crawler.doPoll();	
		
		return 0;
	}
}

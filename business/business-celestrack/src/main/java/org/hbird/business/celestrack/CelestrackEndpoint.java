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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.villemos.ispace.httpcrawler.HttpCrawlerEndpoint;

/**
 * Represents a direct endpoint that synchronously invokes the consumers of the
 * endpoint when a producer sends a message to it.
 * 
 * @version 
 */
public class CelestrackEndpoint extends HttpCrawlerEndpoint {

	private static final Log LOG = LogFactory.getLog(CelestrackEndpoint.class);

	boolean allowMultipleConsumers = true;

	public CelestrackEndpoint(String uri, CelestrackComponent component) {
		super(uri, component);
	}

	public Producer createProducer() throws Exception {
		throw new UnsupportedOperationException("Producer not supported for HttpCrawler endpoint. Sorry!");
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new CelestrackConsumer(this, processor);
	}

	public boolean isAllowMultipleConsumers() {
		return allowMultipleConsumers;
	}

	public void setAllowMultipleConsumers(boolean allowMutlipleConsumers) {
		this.allowMultipleConsumers = allowMutlipleConsumers;
	}

	public boolean isSingleton() {
		return true;
	}
}

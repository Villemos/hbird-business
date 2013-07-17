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
package org.hbird.business.simpleparametersimulator;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.core.EntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gert Villemos
 * 
 */
public class SenderBean {

    protected static Logger LOG = LoggerFactory.getLogger(SenderBean.class);

    protected IPublish pApi = ApiFactory.getPublishApi("Simulator");

    @Handler
    public synchronized void send(@Body EntityInstance entry) {
        LOG.debug("Publishing object '{}'", entry.toString());
        try {
        	pApi.publish(entry);
        } catch(Exception e) {
        	LOG.error("Failed to publish object " + entry, e);
        }
    }
}

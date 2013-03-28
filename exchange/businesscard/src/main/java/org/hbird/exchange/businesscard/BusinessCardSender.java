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
package org.hbird.exchange.businesscard;

import org.apache.camel.Handler;
import org.hbird.exchange.core.BusinessCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A BusinessCard presents a component to its environment. It defines the name
 * of the component, the host the component runs on and the Commands that the component
 * will accept. The component that the BusinessCard described is defined through the
 * 'issuedBy' attribute.
 * 
 * The business cards can be used to discover which components exist in the system and
 * which commands can be send.
 * 
 * @author Gert Villemos
 * 
 */
public class BusinessCardSender {

    /** Default value for the period. */
    public static final long DEFAULT_PERIOD = 10000L;

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(BusinessCardSender.class);

    /** The period between heartbeats. The time to-wait for the next expected beat, */
    protected long period = DEFAULT_PERIOD;

    protected BusinessCard card = null;

    /**
     * @param issuedBy The name of the component that issues this BusinessCard
     * @param period delay before next BusinessCard is published
     */
    public BusinessCardSender(BusinessCard card, long period) {
        this.period = period;
        this.card = card;
    }

    /**
     * Method to add this business card to a route.
     * 
     * @return The BusinessCard, i.e. this object,
     */
    @Handler
    public BusinessCard send() {
        LOG.debug("Issuing business card for '{}'.", card.getIssuedBy());
        card.setTimestamp(System.currentTimeMillis());
        return card;
    }

    /**
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(long period) {
        this.period = period;
    }
}

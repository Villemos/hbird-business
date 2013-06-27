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
package org.hbird.business.tracking.quartz;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ScheduleDeltaCheck implements Predicate {

    private final TrackingDriverConfiguration config;

    public ScheduleDeltaCheck(TrackingDriverConfiguration config) {
        this.config = config;
    }

    boolean canSchedule(long now, long contactStart, long delta) {
        return now + delta < contactStart;
    }

    /**
     * @see org.apache.camel.Predicate#matches(org.apache.camel.Exchange)
     */
    @Override
    public boolean matches(Exchange exchange) {
        Message in = exchange.getIn();
        LocationContactEvent event = in.getBody(LocationContactEvent.class);
        long now = System.currentTimeMillis();
        long contactStart = event.getStartTime();
        long delta = config.getScheduleDelta();
        return canSchedule(now, contactStart, delta);
    }
}

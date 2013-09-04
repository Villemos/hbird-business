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
import org.hbird.exchange.util.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ScheduleDeltaCheck implements Predicate {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleDeltaCheck.class);

    private final TrackingDriverConfiguration config;
    private final SchedulingSupport support;

    public ScheduleDeltaCheck(TrackingDriverConfiguration config, SchedulingSupport support) {
        this.config = config;
        this.support = support;
    }

    /**
     * @see org.apache.camel.Predicate#matches(org.apache.camel.Exchange)
     */
    @Override
    public boolean matches(Exchange exchange) {
        long now = System.currentTimeMillis();
        Message in = exchange.getIn();
        LocationContactEvent event = in.getBody(LocationContactEvent.class);
        long scheduleTime = support.getScheduleTime(event, config, now);
        boolean canSchedule = scheduleTime != SchedulingSupport.TOO_LATE;
        if (!canSchedule) {
            LOG.debug("Too late to issue Track command for {}; Current time {}; configured schedule delta {}",
                    new Object[] { event.toString(), Dates.toDefaultDateFormat(now), config.getScheduleDelta() });
        }
        return canSchedule;
    }
}

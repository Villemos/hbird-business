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
import org.apache.camel.Processor;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TrackCommandUnscheduler implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(TrackCommandUnscheduler.class);

    private final Scheduler scheduler;
    private final SchedulingSupport support;

    /**
     * @param config
     * @param scheduler
     */
    public TrackCommandUnscheduler(Scheduler scheduler, SchedulingSupport support) {
        this.scheduler = scheduler;
        this.support = support;
    }

    /**
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        Message out = exchange.getOut();
        out.copyFrom(in);

        LocationContactEvent event = in.getBody(LocationContactEvent.class);
        String groupName = support.createGroupName(event);
        String jobName = support.createJobName(JobType.TRACK, event);
        JobKey jobKey = new JobKey(jobName, groupName);
        try {
            boolean result = scheduler.deleteJob(jobKey);
            if (result) {
                LOG.info("Removed outdated job for {}", event);
            }
            else {
                LOG.warn("No outdated job found for {}. This should not happen. Needs investigation", event);
            }
        }
        catch (SchedulerException e) {
            LOG.error("Failed to reschedule {}; stop processing the message", event, e);
            // failed to delete the job; no point to continue; drop the exchange
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        }
    }
}

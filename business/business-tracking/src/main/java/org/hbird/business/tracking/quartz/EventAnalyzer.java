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
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class EventAnalyzer extends SchedulingBase implements Processor {

    public static final String HEADER_KEY_EVENT_TYPE = "EventType";
    public static final String HEADER_VALUE_NEW_EVENT = "NewEvent";
    public static final String HEADER_VALUE_KNOWN_EVENT = "KnownEvent";
    public static final String HEADER_VALUE_UPDATED_EVENT = "UpdatedEvent";

    private static final Logger LOG = LoggerFactory.getLogger(EventAnalyzer.class);

    /**
     * @param config
     * @param scheduler
     */
    public EventAnalyzer(TrackingDriverConfiguration config, Scheduler scheduler) {
        super(config, scheduler);
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
        String triggerName = createTriggerName(event);
        String groupName = createGroupName(event);
        TriggerKey triggerKey = new TriggerKey(triggerName, groupName);
        LOG.trace("Looking for trigger {}", triggerKey);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            LOG.trace("Trigger not found for the event {}; result: {}", event.getInstanceID(), HEADER_VALUE_NEW_EVENT);
            out.setHeader(HEADER_KEY_EVENT_TYPE, HEADER_VALUE_NEW_EVENT);
        }
        else {
            LOG.trace("Trigger FOUND for the key {}", triggerKey);
            String jobName = createJobName(event);
            JobKey jobKey = new JobKey(jobName, groupName);
            JobDetail job = scheduler.getJobDetail(jobKey);
            JobDataMap map = job.getJobDataMap();
            long startTimeFromJob = map.getLong(TrackCommandCreationJob.JOB_DATA_START_TIME);
            String type = startTimeFromJob == event.getStartTime() ? HEADER_VALUE_KNOWN_EVENT
                    : HEADER_VALUE_UPDATED_EVENT;
            LOG.trace("Comparing {} and {}; result: {}", new Object[] { startTimeFromJob, event.getStartTime(), type });
            out.setHeader(HEADER_KEY_EVENT_TYPE, type);
        }
    }
}

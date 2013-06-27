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

import java.util.Date;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.util.Dates;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ContactScheduler extends SchedulingBase {

    private static final Logger LOG = LoggerFactory.getLogger(ContactScheduler.class);

    public ContactScheduler(TrackingDriverConfiguration config, Scheduler scheduler) {
        super(config, scheduler);
    }

    @Handler
    public void handle(@Body LocationContactEvent event) {
        long now = System.currentTimeMillis();
        long jobStartTime = getScheduleTime(event, config, now);
        if (jobStartTime == TOO_LATE) {
            LOG.warn("Too late to issue Track command for {}; Current time {}; configured schedule delta {}",
                    new Object[] { event.toString(), Dates.toDefaultDateFormat(now), config.getScheduleDelta() });
            return;
        }

        String groupName = createGroupName(event);
        Date startTime = new Date(jobStartTime);
        JobDetail job = createJob(event, groupName);
        Trigger trigger = createTrigger(event, groupName, startTime);

        try {
            scheduler.scheduleJob(job, trigger);
            LOG.info("{} - Scheduled {}", Dates.toDefaultDateFormat(jobStartTime), event);
        }
        catch (SchedulerException e) {
            LOG.error("Failed to schedule Track command creation for the {}", event.toString(), e);
        }
    }
}

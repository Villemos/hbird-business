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

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ContactRescheduler extends ContactScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ContactRescheduler.class);

    /**
     * @param config
     * @param scheduler
     */
    public ContactRescheduler(TrackingDriverConfiguration config, Scheduler scheduler) {
        super(config, scheduler);
    }

    /**
     * @see org.hbird.business.tracking.quartz.ContactScheduler#handle(org.hbird.exchange.navigation.LocationContactEvent)
     */
    @Override
    @Handler
    public void handle(@Body LocationContactEvent event) {
        // TODO - remove old job & trigger
        String groupName = createGroupName(event);
        String jobName = createJobName(event);
        JobKey jobKey = new JobKey(jobName, groupName);
        try {
            boolean result = scheduler.deleteJob(jobKey);
            if (result) {
                LOG.info("Removed outdated job for {}", event);
            }
            else {
                LOG.warn("No outdated job found for {}. This should not happen. Needs investigation", event);
            }
            super.handle(event);
        }
        catch (SchedulerException e) {
            LOG.error("Failed to reschedule {}", event, e);
        }
    }
}

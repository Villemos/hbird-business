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
import org.hbird.exchange.util.Dates;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TrackCommandScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(TrackCommandScheduler.class);

    protected final TrackingDriverConfiguration config;
    protected final Scheduler scheduler;
    protected final SchedulingSupport support;

    public TrackCommandScheduler(TrackingDriverConfiguration config, Scheduler scheduler, SchedulingSupport support) {
        this.config = config;
        this.scheduler = scheduler;
        this.support = support;
    }

    @Handler
    public void handle(@Body LocationContactEvent event) {
        long now = System.currentTimeMillis();
        long jobStartTime = support.getScheduleTime(event, config, now);
        if (jobStartTime == SchedulingSupport.TOO_LATE) {
            throw new IllegalStateException("Too late to issue Track command for " + event.toString());
        }

        String groupName = support.createGroupName(event);
        String triggerName = support.createTriggerName(JobType.TRACK, event);
        String jobName = support.createJobName(JobType.TRACK, event);
        JobDetail job = createJob(groupName, jobName, event);
        Trigger trigger = support.createTrigger(groupName, triggerName, jobStartTime);

        try {
            scheduler.scheduleJob(job, trigger);
            LOG.info("{} - Scheduled {}", Dates.toDefaultDateFormat(jobStartTime), event);
        }
        catch (SchedulerException e) {
            LOG.error("Failed to schedule Track command creation for the {}", event.toString(), e);
        }
    }

    JobDetail createJob(String groupName, String jobName, LocationContactEvent event) {
        JobDetail job = JobBuilder
                .newJob(TrackCommandCreationJob.class)
                .withIdentity(jobName, groupName)
                .usingJobData(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID, event.getInstanceID())
                .usingJobData(TrackCommandCreationJob.JOB_DATA_EVENT_ID, event.getID())
                .usingJobData(TrackCommandCreationJob.JOB_DATA_TLE_ID, event.getDerivedFromId())
                .usingJobData(TrackCommandCreationJob.JOB_DATA_START_TIME, event.getStartTime())
                .build();
        return job;
    }

}

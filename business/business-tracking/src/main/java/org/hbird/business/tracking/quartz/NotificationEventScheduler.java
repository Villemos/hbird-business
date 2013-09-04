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
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.util.Dates;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class NotificationEventScheduler extends SchedulingSupport {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventScheduler.class);

    private final Scheduler scheduler;
    private final SchedulingSupport support;

    /**
     * @param config
     * @param scheduler
     */
    public NotificationEventScheduler(Scheduler scheduler, SchedulingSupport support) {
        this.scheduler = scheduler;
        this.support = support;
    }

    @Handler
    public void handle(@Body Track command) {
        LocationContactEvent event = command.getLocationContactEvent();
        long startTime = event.getStartTime();
        long endTime = event.getEndTime();
        scheduleJob(scheduler, support, event, JobType.AOS, startTime);
        scheduleJob(scheduler, support, event, JobType.LOS, endTime);
    }

    void scheduleJob(Scheduler scheduler, SchedulingSupport support, LocationContactEvent event, JobType type, long timestamp) {
        String groupName = support.createGroupName(event);
        String triggerName = support.createTriggerName(type, event);
        String jobName = support.createJobName(type, event);
        Trigger trigger = support.createTrigger(groupName, triggerName, timestamp);
        JobDataMap jobData = createJobDataMap(event, type, timestamp);
        JobDetail jobDetail = createJob(groupName, jobName, jobData);
        try {
            Date date = scheduler.scheduleJob(jobDetail, trigger);
            LOG.debug("{} - Scheduled {} event notification for {}", new Object[] { Dates.toDefaultDateFormat(date.getTime()), type, event });
        }
        catch (Exception e) {
            LOG.error("Failed to schedule {} event notification for {}", new Object[] { type, event, e });
        }
    }

    JobDetail createJob(String groupName, String jobName, JobDataMap jobData) {
        JobDetail job = JobBuilder
                .newJob(NotificationEventCreationJob.class)
                .withIdentity(jobName, groupName)
                .usingJobData(jobData)
                .build();
        return job;
    }

    JobDataMap createJobDataMap(LocationContactEvent event, JobType type, long timestamp) {
        JobDataMap map = new JobDataMap();
        map.put(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID, event.getInstanceID());
        map.put(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE, type.toString());
        map.put(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME, timestamp);
        // TODO - 04.09.2013, kimmell - resolve and use GS name here
        map.put(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME, event.getGroundStationID());
        // TODO - 04.09.2013, kimmell - resolve and use Satellite name here
        map.put(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME, event.getSatelliteID());
        return map;
    }
}

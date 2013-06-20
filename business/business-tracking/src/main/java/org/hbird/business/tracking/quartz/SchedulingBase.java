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

import org.hbird.exchange.navigation.LocationContactEvent;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 *
 */
public abstract class SchedulingBase {

    public static final String PREFIX_JOB = "Job";
    public static final String PREFIX_TRIGGER = "Trigger";
    public static final long TOO_LATE = -1;
    protected final TrackingDriverConfiguration config;
    protected final Scheduler scheduler;

    public SchedulingBase(TrackingDriverConfiguration config, Scheduler scheduler) {
        this.config = config;
        this.scheduler = scheduler;
    }

    protected Trigger createTrigger(LocationContactEvent event, String groupName, Date startTime) {
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(createTriggerName(event), groupName)
                .startAt(startTime)
                .build();
        return trigger;
    }

    protected JobDetail createJob(LocationContactEvent event, String groupName) {
        JobDetail job = JobBuilder
                .newJob(TrackCommandCreationJob.class)
                .withIdentity(createJobName(event), groupName)
                .usingJobData(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID, event.getInstanceID())
                .usingJobData(TrackCommandCreationJob.JOB_DATA_EVENT_ID, event.getID())
                .usingJobData(TrackCommandCreationJob.JOB_DATA_TLE_ID, event.getDerivedFromId())
                .usingJobData(TrackCommandCreationJob.JOB_DATA_START_TIME, event.getStartTime())
                .build();
        return job;
    }

    String createJobName(LocationContactEvent event) {
        return String.format("%s-%s", PREFIX_JOB, event.getInstanceID());
    }

    String createTriggerName(LocationContactEvent event) {
        return String.format("%s-%s", PREFIX_TRIGGER, event.getInstanceID());
    }

    protected String createGroupName(LocationContactEvent event) {
        return String.format("%s/%s", event.getGroundStationID(), event.getSatelliteID());
    }

    protected long getScheduleTime(LocationContactEvent event, TrackingDriverConfiguration config, long now) {
        long start = event.getStartTime() - config.getScheduleDelta();
        return now < start ? start : TOO_LATE;
    }
}

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
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 *
 */
public class SchedulingSupport {

    public static final String JOB = "Job";
    public static final String TRIGGER = "Trigger";

    public static final long TOO_LATE = -1;

    public Trigger createTrigger(String groupName, String triggerName, long startTime) {
        Date startDate = new Date(startTime);
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, groupName)
                .startAt(startDate)
                .build();
        return trigger;
    }

    public String createJobName(JobType type, LocationContactEvent event) {
        return String.format("%s%s-%s", type.toString(), JOB, event.getInstanceID());
    }

    public String createTriggerName(JobType type, LocationContactEvent event) {
        return String.format("%s%s-%s", type.toString(), TRIGGER, event.getInstanceID());
    }

    public String createGroupName(LocationContactEvent event) {
        return String.format("%s/%s", event.getGroundStationID(), event.getSatelliteID());
    }

    public long getScheduleTime(LocationContactEvent event, TrackingDriverConfiguration config, long now) {
        long start = event.getStartTime() - config.getScheduleDelta();
        return now < start ? start : TOO_LATE;
    }
}

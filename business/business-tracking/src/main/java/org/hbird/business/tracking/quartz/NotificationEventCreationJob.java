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

import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.groundstation.Aos;
import org.hbird.exchange.groundstation.ContactEventBase;
import org.hbird.exchange.groundstation.Los;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.hbird.exchange.util.Dates;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class NotificationEventCreationJob implements Job {

    public static final String JOB_DATA_KEY_CONTACT_INSTANCE_ID = "CONTACT_INSTANCE_ID";
    public static final String JOB_DATA_KEY_GROUND_STATION_ID = "GS_ID";
    public static final String JOB_DATA_KEY_GROUND_STATION_NAME = "GS_NAME";
    public static final String JOB_DATA_KEY_SATELLITE_ID = "SAT_ID";
    public static final String JOB_DATA_KEY_SATELLITE_NAME = "SAT_NAME";
    public static final String JOB_DATA_KEY_EVENT_TIME = "EVENT_TIME";
    public static final String JOB_DATA_KEY_EVENT_TYPE = "EVENT_TYPE";

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventScheduler.class);

    private String endPoint;
    private ProducerTemplate producer;
    private IdBuilder idBuilder;
    private IStartableEntity issuer;
    private TrackingDriverConfiguration config;

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * @param producer the producer to set
     */
    public void setProducer(ProducerTemplate producer) {
        this.producer = producer;
    }

    /**
     * @param idBuilder the idBuilder to set
     */
    public void setIdBuilder(IdBuilder idBuilder) {
        this.idBuilder = idBuilder;
    }

    public void setIssuer(IStartableEntity issuer) {
        this.issuer = issuer;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(TrackingDriverConfiguration config) {
        this.config = config;
    }

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        String locationContactEventInstanceId = data.getString(JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        String gsId = data.getString(JOB_DATA_KEY_GROUND_STATION_ID);
        String gsName = data.getString(JOB_DATA_KEY_GROUND_STATION_NAME);
        String satId = data.getString(JOB_DATA_KEY_SATELLITE_ID);
        String satName = data.getString(JOB_DATA_KEY_SATELLITE_NAME);
        String type = data.getString(JOB_DATA_KEY_EVENT_TYPE);
        long time = data.getLong(JOB_DATA_KEY_EVENT_TIME);
        String message = String.format("%s - %s %s at %s", Dates.toDefaultDateFormat(time), satName, type, gsName);
        String eventId = idBuilder.buildID(config.getEventNameSpace(), type);

        ContactEventBase event;
        if (JobType.AOS.toString().equals(type)) {
            event = new Aos(eventId);
        }
        else if (JobType.LOS.toString().equals(type)) {
            event = new Los(eventId);
        }
        else {
            throw new IllegalArgumentException("Invalid event type for notification event: " + type);
        }
        event.setGroundStationId(gsId);
        event.setSatelliteId(satId);
        event.setApplicableTo(locationContactEventInstanceId);
        event.setDescription(message);
        event.setTimestamp(time);
        event.setIssuedBy(issuer.getID());

        LOG.debug("Publishing {} notification event {}", type, event);
        try {
            producer.asyncSendBody(endPoint, event);
        }
        catch (Exception e) {
            LOG.error("Failed to publish notification event {}", event, e);
        }
    }
}

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

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.groundstation.GroundStationConfigurationBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TrackingDriverConfiguration extends GroundStationConfigurationBase {

    private static final long serialVersionUID = -9010929452553708972L;

    public static final long DEFAULT_SCHEDULE_DELTA = 1000L * 60 * 5;

    public static final long DEFAULT_ARCHIVE_POLL_INTERVAL = 1000L * 60;

    public static final String DEFAULT_EVENT_NAMESPACE = "/Events";

    @Value("${satellites}")
    protected List<String> satelliteIds = new ArrayList<String>(0);

    @Value("${schedule.delta:300000}")
    protected long scheduleDelta = DEFAULT_SCHEDULE_DELTA;

    @Value("${archive.poll.interval:60000}")
    protected long archivePollInterval = DEFAULT_ARCHIVE_POLL_INTERVAL;

    @Value("${namespace.event:/Events}")
    protected String eventNameSpace = DEFAULT_EVENT_NAMESPACE;

    public TrackingDriverConfiguration() {
        super();
    }

    /**
     * @return the satelliteIds
     */
    public List<String> getSatelliteIds() {
        return satelliteIds;
    }

    /**
     * @param satelliteIds the satelliteIds to set
     */
    public void setSatelliteIds(List<String> satelliteIds) {
        this.satelliteIds = satelliteIds;
    }

    /**
     * @return the scheduleDelta
     */
    public long getScheduleDelta() {
        return scheduleDelta;
    }

    /**
     * @param scheduleDelta the scheduleDelta to set
     */
    public void setScheduleDelta(long scheduleDelta) {
        this.scheduleDelta = scheduleDelta;
    }

    /**
     * @return the archivePollIntervall
     */
    public long getArchivePollInterval() {
        return archivePollInterval;
    }

    /**
     * @param archivePollIntervall the archivePollIntervall to set
     */
    public void setArchivePollInterval(long archivePollInterval) {
        this.archivePollInterval = archivePollInterval;
    }

    /**
     * @return the eventNameSpace
     */
    public String getEventNameSpace() {
        return eventNameSpace;
    }

    /**
     * @param eventNameSpace the eventNameSpace to set
     */
    public void setEventNameSpace(String eventNameSpace) {
        this.eventNameSpace = eventNameSpace;
    }
}

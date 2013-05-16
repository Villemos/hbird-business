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
package org.hbird.business.navigation;

import org.hbird.business.core.StartableEntity;

/**
 * Abstract class, acting as a base for all navigation components.
 * 
 * @author Gert Villemos
 * 
 */
public abstract class NavigationComponent extends StartableEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2451309271151513203L;

    /** The Entity ID of the satellite that this navigation component manages. */
    protected String satelliteId;

    /**
     * The interval (ms) for which propagation should be available. The controller will ensure that there
     * is always orbital data available for the period <li>[NOW] to [NOW] + lead time + execution delay.</li>
     */
    protected long leadTime = 6 * 60 * 60 * 1000L;

    /** The interval between propagations */
    protected long executionDelay = 5 * 60 * 1000L;

    /** The size of the steps in calculating the orbital state, measured in Seconds */
    protected long stepSize = 60L;

    protected long from = 0L;

    protected long to = 0L;

    /**
     * @param ID
     * @param name
     */
    public NavigationComponent(String ID, String name) {
        super(ID, name);
    }

    /**
     * @return the satellite
     */
    public String getSatelliteId() {
        return satelliteId;
    }

    /**
     * @param satelliteId the satellite to set
     */
    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    /**
     * @return the leadTime
     */
    public long getLeadTime() {
        return leadTime;
    }

    /**
     * @param leadTime the leadTime to set
     */
    public void setLeadTime(long leadTime) {
        this.leadTime = leadTime;
    }

    /**
     * @return the executionDelay
     */
    public long getExecutionDelay() {
        return executionDelay;
    }

    /**
     * @param executionDelay the executionDelay to set
     */
    public void setExecutionDelay(long executionDelay) {
        this.executionDelay = executionDelay;
    }

    /**
     * @return the stepSize
     */
    public long getStepSize() {
        return stepSize;
    }

    /**
     * @param stepSize the stepSize to set
     */
    public void setStepSize(long stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * @return the from
     */
    public long getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(long from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public long getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(long to) {
        this.to = to;
    }
}

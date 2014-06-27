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
package org.hbird.business.navigation.configuration;

import org.hbird.exchange.core.ConfigurationBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Holds configurable data for TLE Updater
 */
@Component
public class TleUpdaterConfiguration extends ConfigurationBase {

    private static final long serialVersionUID = 3783672950051045639L;

    /** Space-Track query interval in milliseconds */
    @Value("${update.interval:43200000}")
    protected long updateInterval;

    /** Space-Track username for authenticating */
    @Value("${auth.username:}")
    private String userName;

    /** Space-Track password for authenticating */
    @Value("${auth.password:}")
    private String password;

    /**
     * returns Space-Track query interval in milliseconds
     * 
     * @return the updateInterval
     */
    public long getUpdateInterval() {
        return updateInterval;
    }

    /**
     * 
     * @param updateInterval the updateInterval to set
     */
    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    /**
     * returns Space-Track username for authenticating
     * 
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * returns Space-Track password for authenticating
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

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
/**
 * 
 */
package org.hbird.exchange.core;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;

/**
 * Base class for configuration objects.
 * 
 * Component assemblies are responsible for loading property files to fill
 * values in configuration objects.
 * 
 * Example snippet from Spring/Camel context file: <code>
 * <pre>
 *    &lt;bean id="propertyPlaceholder" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"&gt;
 *       &lt;property name="systemPropertiesModeName" value="SYSTEM_PROPERTIES_MODE_OVERRIDE"/&gt;
 *       &lt;property name="ignoreResourceNotFound" value="true"/&gt;
 *       &lt;property name="locations"&gt;
 *           &lt;list&gt;
 *               &lt;value&gt;classpath:service.properties&lt;/value&gt;
 *               &lt;value&gt;file:service.properties&lt;/value&gt;
 *           &lt;/list&gt;
 *       &lt;/property&gt;
 *   &lt;/bean&gt;
 * </pre>
 * </code>
 * 
 * In this snippet properties are loaded from <tt>service.properties</tt> file
 * from classpath and if found from file <tt>service.properties</tt>.
 * Values from the file override values in the classpath. No properties mixing
 * is taking place - all values has to be present in both files to work
 * properly.
 */
public class ConfigurationBase implements Serializable {

    private static final long serialVersionUID = -1020014261438984730L;

    /** Default value for the heart beat interval. */
    public static final long DEFAULT_HEART_BEAT_INTERVAL = 5000L;

    @Value("${service.id}")
    protected String serviceId;

    @Value("${service.version}")
    protected String serviceVersion;

    @Value("${heart.beat.interval:5000}")
    protected long heartBeatInterval = DEFAULT_HEART_BEAT_INTERVAL;

    public ConfigurationBase() {
    }

    public ConfigurationBase(String serviceId, String serviceVersion, long heartBeatInterval) {
        this.serviceId = serviceId;
        this.serviceVersion = serviceVersion;
        this.heartBeatInterval = heartBeatInterval;
    }

    /**
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the heartBeatInterval
     */
    public long getHeartBeatInterval() {
        return heartBeatInterval;
    }

    /**
     * @param heartBeatInterval the heartBeatInterval to set
     */
    public void setHeartBeatInterval(long heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    /**
     * @return the serviceVersion
     */
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * @param serviceVersion the serviceVersion to set
     */
    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

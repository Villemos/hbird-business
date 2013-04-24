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
public class ConfigurationBase {

    public ConfigurationBase() {
    };

    public ConfigurationBase(String serviceId, String serviceVersion, int heartBeatInterval) {
        super();
        this.serviceId = serviceId;
        this.serviceVersion = serviceVersion;
        this.heartBeatInterval = heartBeatInterval;
    }

    protected String serviceId;

    protected String serviceVersion;

    protected int heartBeatInterval;

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getServiceId() {
        return serviceId;
    }

    public int getHeartBeatInterval() {
        return heartBeatInterval;
    }
}

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
package org.hbird.business.systemmonitoring.bean;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.systemmonitoring.SystemMonitorComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component builder to create a system monitoring component.
 * 
 * @author Gert Villemos
 * 
 */
public class SystemMonitorComponentDriver extends SoftwareComponentDriver<SystemMonitorComponent> {

    private static final Logger LOG = LoggerFactory.getLogger(SystemMonitorComponentDriver.class);

    @Override
    public void doConfigure() {

        SystemMonitorComponent component = entity;
        IdBuilder idBuilder = ApiFactory.getIdBuilder();
        String entityId = component.getID();
        String baseName = idBuilder.buildID(entityId, HostInfo.getHostName());
        long interval = component.getMonitoringInterval();
        LOG.info("Starting Monitoring for '{}' with interval {}", baseName, interval);

        from(addTimer("systemmonitor", component.getMonitoringInterval())).multicast().to("seda:heap", "seda:thread",
                "seda:cpu", "seda:harddisk", "seda:os", "seda:uptime");

        from("seda:heap").bean(new HeapMemoryUsageMonitor(baseName, idBuilder)).split(body()).to("seda:out");
        from("seda:thread").bean(new ThreadCountMonitor(baseName, idBuilder)).to("seda:out");
        from("seda:cpu").bean(new CpuMonitor(baseName, idBuilder)).to("seda:out");
        from("seda:harddisk").bean(new HarddiskMonitor(baseName, idBuilder)).split(body()).to("seda:out");
        from("seda:os").bean(new OsMonitor(baseName, idBuilder)).to("seda:out");
        from("seda:uptime").bean(new UptimeMonitor(baseName, idBuilder)).to("seda:out");

        ProcessorDefinition<?> route = from("seda:out");
        addInjectionRoute(route);

        addCommandHandler();
    }
}

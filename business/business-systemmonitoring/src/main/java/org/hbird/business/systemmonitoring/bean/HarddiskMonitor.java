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

import java.io.File;
import java.net.UnknownHostException;

import org.apache.camel.Handler;
import org.hbird.business.core.naming.Base;
import org.hbird.exchange.core.Parameter;

public class HarddiskMonitor extends Monitor {

    public HarddiskMonitor(String componentId) {
        super(componentId);
    }

    /**
     * Method to create a new instance of the memory parameter. The body of the
     * exchange will be updated.
     * 
     * @param exchange The exchange to hold the new value.
     * @throws UnknownHostException
     */
    @Handler
    public Parameter[] check() {

        File[] roots = File.listRoots();
        Parameter[] list = new Parameter[roots.length * 3];

        for (int i = 0; i < roots.length; i++) {
            File root = roots[i];
            String name = root.getPath();
            long total = root.getTotalSpace();
            long free = root.getFreeSpace();
            double used = total == 0 ? 0 : (100D - ((100 * free) / total));

            list[i * 3 + 0] = new Parameter(componentId, naming.createAbsoluteName(Base.HOST.toString(),
                    HostInfo.getHostName(),
                    name, "Available Disk Space"), "The available harddisk space.",
                    total, "Byte");
            list[i * 3 + 1] = new Parameter(componentId, naming.createAbsoluteName(Base.HOST.toString(),
                    HostInfo.getHostName(),
                    name, "Free Disk Space"), "The free harddisk space.", free, "Byte");
            list[i * 3 + 2] = new Parameter(componentId, naming.createAbsoluteName(Base.HOST.toString(),
                    HostInfo.getHostName(),
                    name, "Used Disk Space"), "The used harddisk space.", used, "%");
        }
        return list;
    }
}

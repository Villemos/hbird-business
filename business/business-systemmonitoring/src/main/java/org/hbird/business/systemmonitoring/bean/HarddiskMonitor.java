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
import org.hbird.exchange.core.Parameter;

public class HarddiskMonitor extends Monitor {

    public static final String ESCAPED_SHLASH = "&#47;";

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
            String hddName = root.getPath();
            long total = root.getTotalSpace();
            long free = root.getFreeSpace();
            double used = total == 0 ? 0 : (100D - ((100 * free) / total));
            String baseId = naming.buildId(componentId, hddName);

            // TODO Not sure how to set ID and name...
            list[i * 3 + 0] = createParameter(baseId, "Available Disk Space", "The available harddisk space.", "Byte", total);
            list[i * 3 + 1] = createParameter(baseId, "Free Disk Space", "The free harddisk space.", "Byte", free);
            list[i * 3 + 2] = createParameter(baseId, "Used Disk Space", "The used harddisk space.", "%", used);
        }
        return list;
    }

    Parameter createParameter(String baseId, String name, String description, String unit, Number value) {
        Parameter p = new Parameter(naming.buildId(baseId, name), name);
        p.setDescription(description);
        p.setUnit(unit);
        p.setValue(value);
        return p;
    }

}

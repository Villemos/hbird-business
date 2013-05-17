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

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;

import org.apache.camel.Handler;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.core.Parameter;

/**
 * A parameter reporting the current memory usage (bytes). Each call will lead
 * to
 * the creation of a Parameter instance, holding the memory usage in bytes as a
 * long.
 * 
 * To create a Parameter instance every 60 seconds and inject this into a
 * Activemq parameter topic,
 * configure the following;
 * 
 * <bean id="memory"
 * class="org.hbird.business.systemmonitoring.MemoryParameter">
 * <constructor-arg index="0" value="Memory"/>
 * <constructor-arg index="1"
 * value="The memory (bytes) currently used by the system."/>
 * </bean>
 * <camelContext id="context" xmlns="http://camel.apache.org/schema/spring">
 * <route>
 * <from uri="timer://memory?fixedRate=true&amp;period=60000" />
 * <to uri="bean:memory"/>
 * <to uri="activemq:topic:Parameters"/>
 * </route>
 * </camelContext>
 * 
 */
public class HeapMemoryUsageMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME_HEAP_MEMORY = "Heap Memory";
    public static final String PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY = "Non Heap Memory";
    public static final String DESCRIPTION_NON_HEAP_MEMORY_USAGE = "The non heap memory usage";
    public static final String DESCRIPTION_HEAP_MEMORY_USAGE = "The heap memory usage";

    public HeapMemoryUsageMonitor(String componentId, IdBuilder idBuilder) {
        super(componentId, idBuilder);
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

        Parameter parameter = new Parameter(idBuilder.buildID(componentId, PARAMETER_RELATIVE_NAME_HEAP_MEMORY), PARAMETER_RELATIVE_NAME_HEAP_MEMORY);
        parameter.setDescription(DESCRIPTION_HEAP_MEMORY_USAGE);
        parameter.setValue(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed());
        parameter.setUnit(UNIT_BYTE);
        parameter.setIssuedBy(componentId);
        parameter.setApplicableTo(componentId);

        Parameter nonHeap = new Parameter(idBuilder.buildID(componentId, PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY), PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY);
        nonHeap.setDescription(DESCRIPTION_NON_HEAP_MEMORY_USAGE);
        nonHeap.setValue(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed());
        nonHeap.setUnit(UNIT_BYTE);
        nonHeap.setIssuedBy(componentId);
        nonHeap.setApplicableTo(componentId);

        return new Parameter[] { parameter, nonHeap };
    }
}

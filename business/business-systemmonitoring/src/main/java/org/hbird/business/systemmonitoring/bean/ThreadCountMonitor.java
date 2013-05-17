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

import org.apache.camel.Handler;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.core.Parameter;

/**
 * A parameter reporting the current thread usage (count). Each call will lead
 * to
 * the creation of a Parameter instance, holding the the number of threads used.
 * 
 * To create a Parameter instance every 60 seconds and inject this into a
 * Activemq parameter topic,
 * configure the following;
 * 
 * <bean id="threads"
 * class="org.hbird.business.systemmonitoring.ThreadParameter">
 * <constructor-arg index="0" value="Threads"/>
 * <constructor-arg index="1"
 * value="The threads (count) currently used by the system."/>
 * </bean>
 * <camelContext id="context" xmlns="http://camel.apache.org/schema/spring">
 * <route>
 * <from uri="timer://threads?fixedRate=true&amp;period=60000" />
 * <to uri="bean:threads"/>
 * <to uri="activemq:topic:Parameters"/>
 * </route>
 * </camelContext>
 * 
 */
public class ThreadCountMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME = "Thread Count";
    public static final String DESCRIPTION_THREAD_COUNT = "The number of threads used.";

    public ThreadCountMonitor(String componentId, IdBuilder idBuilder) {
        super(componentId, idBuilder);
    }

    /**
     * Method to create a new instance of the threads parameter. The body of the
     * exchange will be updated.
     * 
     * @param exchange The exchange to hold the new value.
     */
    @Handler
    public Parameter check() {
        Parameter parameter = new Parameter(idBuilder.buildID(componentId, PARAMETER_RELATIVE_NAME), PARAMETER_RELATIVE_NAME);
        parameter.setDescription(DESCRIPTION_THREAD_COUNT);
        parameter.setValue(ManagementFactory.getThreadMXBean().getThreadCount());
        parameter.setUnit(UNIT_COUNT);
        parameter.setIssuedBy(componentId);
        parameter.setApplicableTo(componentId);
        return parameter;
    }
}

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
package org.hbird.business.systemtest;

import org.apache.camel.Handler;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.constants.StandardMissionEvents;
import org.hbird.exchange.core.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class EventTester extends SystemTest {

    private static Logger LOG = LoggerFactory.getLogger(EventTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        azzert(eventListener.elements.size() == 0, "There should be no events published");
        startCommandingChain();

        azzert(eventListener.elements.size() == 1, "There should be 1 event published");
        Event event = (Event) eventListener.elements.get(0);
        azzert(StandardMissionEvents.COMPONENT_START.getName().equals(event.getName()), "startEvent.getName()");
        azzert(parts.get(StandardComponents.COMMAND_RELEASER_NAME).getQualifiedName().equals(event.getIssuedBy()), "startEvent.getIssuedBy()");
        long diff = System.currentTimeMillis() - event.getTimestamp();
        azzert(diff >= 0L && diff <= 30000L, "startEvent.getTimestamp()");
        azzert(StandardMissionEvents.COMPONENT_START.getQualifiedName().equals(event.getQualifiedName()), "startEvent.getQualifiedName()");
        azzert(event.getID() != null, "startEvent.getID()");
        azzert(StandardMissionEvents.COMPONENT_START.getDescription().equals(event.getDescription()), "startEvent.getDescription()");

        stopCommandingChain();

        azzert(eventListener.elements.size() == 2, "There should be 2 events published");
        event = (Event) eventListener.elements.get(1);
        azzert(StandardMissionEvents.COMPONENT_STOP.getName().equals(event.getName()), "stopEvent.getName()");
        azzert(parts.get(StandardComponents.COMMAND_RELEASER_NAME).getQualifiedName().equals(event.getIssuedBy()), "stopEvent.getIssuedBy()");
        diff = System.currentTimeMillis() - event.getTimestamp();
        azzert(diff >= 0L && diff <= 30000L, "stopEvent.getTimestamp()");
        azzert(StandardMissionEvents.COMPONENT_STOP.getQualifiedName().equals(event.getQualifiedName()), "stopEvent.getQualifiedName()");
        azzert(event.getID() != null, "stopEvent.getUuid()");
        azzert(StandardMissionEvents.COMPONENT_STOP.getDescription().equals(event.getDescription()), "stopEvent.getDescription()");

        LOG.info("Finished");
    }

}

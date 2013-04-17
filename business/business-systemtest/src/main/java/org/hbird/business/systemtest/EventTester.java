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

import java.util.List;

import org.apache.camel.Handler;
import org.hbird.business.commanding.CommandingComponent;
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

        eventListener.elements.clear();
        azzert(eventListener.elements.size() == 0, "There should be no events published");
        startMonitoringArchive();
        startCommandingChain();

        azzert(eventListener.elements.size() == 1, "There should be 1 events published");
        Event event = (Event) eventListener.elements.get(0);
        azzert(StandardMissionEvents.COMPONENT_START.getName().equals(event.getName()), "startEvent.getName()");
        azzert(parts.get(CommandingComponent.COMMAND_RELEASER_NAME).getName().equals(event.getIssuedBy()), "startEvent.getIssuedBy()");
        long diff = System.currentTimeMillis() - event.getTimestamp();
        azzert(diff >= 0L && diff <= 30000L, "startEvent.getTimestamp()");
        azzert(event.getIssuedBy().equals(CommandingComponent.COMMAND_RELEASER_NAME), "startEvent.getName()");
        azzert(event.getID() != null, "startEvent.getID()");
        azzert(StandardMissionEvents.COMPONENT_START.getDescription().equals(event.getDescription()), "startEvent.getDescription()");

        stopCommandingChain();

        azzert(eventListener.elements.size() == 2, "There should be 2 events published");
        event = (Event) eventListener.elements.get(1);
        azzert(StandardMissionEvents.COMPONENT_STOP.getName().equals(event.getName()), "stopEvent.getName()");
        azzert(parts.get(CommandingComponent.COMMAND_RELEASER_NAME).getName().equals(event.getIssuedBy()), "stopEvent.getIssuedBy()");
        diff = System.currentTimeMillis() - event.getTimestamp();
        azzert(diff >= 0L && diff <= 30000L, "stopEvent.getTimestamp()");
        azzert(event.getIssuedBy().equals(CommandingComponent.COMMAND_RELEASER_NAME), "stopEvent.getName()");
        azzert(event.getID() != null, "stopEvent.getUuid()");
        azzert(StandardMissionEvents.COMPONENT_STOP.getDescription().equals(event.getDescription()), "stopEvent.getDescription()");

        forceCommit();
        
        List<Event> events = accessApi.getEvents(null, null);
        azzert(events.size() == 2, "Expected to receive 2 events.");
        
        LOG.info("Finished");
    }

}

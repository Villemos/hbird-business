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
package org.hbird.business.systemtest;

import java.util.Date;

import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.core.InMemoryScheduler;
import org.hbird.exchange.core.Named;

/**
 * @author Admin
 *
 */
public class InMemorySchedulerTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(InMemorySchedulerTester.class);

    @Handler
    public void process(CamelContext context) throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        InMemoryScheduler scheduler = new InMemoryScheduler(context.createProducerTemplate(), "direct:inmemorytest");
        
        long now = (new Date()).getTime();
        
        
        scheduler.add(new DummyTask("1", "1", "", now + 5000));
        scheduler.add(new DummyTask("2", "2", "", now + 5100));
        scheduler.add(new DummyTask("3", "3", "", now + 5200));
        scheduler.add(new DummyTask("4", "4", "", now + 5300));
        scheduler.add(new DummyTask("5", "5", "", now + 5300));
        scheduler.add(new DummyTask("6", "6", "", now + 5400));

        scheduler.add(new DummyTask("8", "8", "", now + 10000));
        scheduler.add(new DummyTask("9", "9", "", now + 10100));
        scheduler.add(new DummyTask("10", "10", "", now + 10200));
        scheduler.add(new DummyTask("11", "11", "", now + 10300));
        scheduler.add(new DummyTask("12", "12", "", now + 10300));
        scheduler.add(new DummyTask("13", "13", "", now + 10400));

        scheduler.add(new DummyTask("7", "7", "", now - 1000));

        Thread.sleep(1000);
        azzert(inMemoryTestListener.elements.size() == 1, "Expect 1 entry, received " + inMemoryTestListener.elements.size());
        azzert(inMemoryTestListener.elements.get(0).getName().equals("7"), "Expected to get entry '7'");
        
        Thread.sleep(8000);

        for (Named entry : inMemoryTestListener.elements) {
        	LOG.info("executiontime:" + ((DummyTask) entry).getExecutionTime() + ", name:" + entry.getName());
        }
        
        azzert(inMemoryTestListener.elements.size() == 7, "Expect 7 entries, received " + inMemoryTestListener.elements.size());

        Thread.sleep(6000);
        
        azzert(inMemoryTestListener.elements.size() == 13, "Expect 13 entries, received " + inMemoryTestListener.elements.size());

        
        /** Stress test*/
        
        for (int count = 0; count < 10000; count += 2) {
            scheduler.add(new DummyTask("", Integer.toString(count), "", now - 1000));
            scheduler.add(new DummyTask("", Integer.toString(count + 1), "", now + 2000));        	
        }

        Thread.sleep(6000);

        azzert(inMemoryTestListener.elements.size() == 10013, "Expect 10013 entries, received " + inMemoryTestListener.elements.size());
        
        
		LOG.info("Finished");
    }
	
}

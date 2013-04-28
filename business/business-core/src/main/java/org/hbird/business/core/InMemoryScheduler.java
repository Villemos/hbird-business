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
package org.hbird.business.core;

import java.util.SortedSet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.ProducerTemplate;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.IScheduled;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

/**
 * 
 * 
 * @author Gert Villemos
 * 
 */
public class InMemoryScheduler implements Runnable {

    /** The complete queue of Named objects. Is a multimap because multiple entries can have the same execution time. */
    protected TreeMultimap<Long, EntityInstance> queue = TreeMultimap.create(Ordering.<Long> natural(), Ordering.natural());

    /** The timestamp of the next object to be send. */
    protected long currentlyScheduled = 0;

    /** The threadpool. */
    protected ScheduledThreadPoolExecutor executor = null;

    /** The next scheduled task. */
    protected ScheduledFuture<?> pending = null;

    /** The route into which we inject the objects when they timeout. */
    protected ProducerTemplate inject = null;
    protected String injectUrl = "direct:injection";

    public InMemoryScheduler(ProducerTemplate inject) {
        executor = new ScheduledThreadPoolExecutor(1);
        this.inject = inject;
    }

    public InMemoryScheduler(ProducerTemplate inject, String injectUrl) {
        executor = new ScheduledThreadPoolExecutor(1);
        this.inject = inject;
        this.injectUrl = injectUrl;
    }

    /**
     * Adds a single object to the queue. May result in a rescheduling.
     * 
     * @param entry The object to be added.
     */
    public synchronized void add(IScheduled entry) {
        queue.put(entry.getExecutionTime(), (EntityInstance) entry);

        if (currentlyScheduled == 0 || entry.getExecutionTime() < currentlyScheduled) {
            run();
        }
    }

    public synchronized void clear() {
        queue.clear();
        stopCurrent();
    }

    protected void stopCurrent() {
        if (pending != null) {
            pending.cancel(false);
            pending = null;
        }
        currentlyScheduled = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public synchronized void run() {
        stopCurrent();

        while (queue.isEmpty() == false) {
            Long next = queue.keySet().first();
            long delay = next - System.currentTimeMillis();

            /** If delay less than 0, then send immediatly. */
            if (delay <= 0) {
                send(queue.get(next));
                queue.removeAll(next);
            }
            /** Schedule next run and then break. */
            else {
                currentlyScheduled = next;
                pending = executor.schedule(this, delay, TimeUnit.MILLISECONDS);
                break;
            }
        }
    }

    protected void send(SortedSet<EntityInstance> toSend) {
        for (EntityInstance entry : toSend) {
            inject.sendBody(injectUrl, entry);
        }
    }
    
    public void setInjectUrl(String url) {
    	this.injectUrl = url;
    }

    public String getInjectUrl() {
        return injectUrl;
    }

}

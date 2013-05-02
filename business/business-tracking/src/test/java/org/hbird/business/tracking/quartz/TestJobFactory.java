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
package org.hbird.business.tracking.quartz;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 *
 */
public class TestJobFactory extends SimpleJobFactory {

    private final JobListener listener;

    public TestJobFactory(JobListener listener) {
        this.listener = listener;
    }

    /**
     * @see org.quartz.simpl.SimpleJobFactory#newJob(org.quartz.spi.TriggerFiredBundle, org.quartz.Scheduler)
     */
    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler Scheduler) throws SchedulerException {
        Job job = super.newJob(bundle, Scheduler);
        if (job instanceof TestJob) {
            TestJob tj = (TestJob) job;
            tj.setJobListener(listener);
        }
        return job;
    }
}

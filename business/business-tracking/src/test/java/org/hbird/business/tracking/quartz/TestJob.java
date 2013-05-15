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

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.EntityInstance;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 */
public class TestJob implements Job {

    private JobListener listener;

    public void setJobListener(JobListener listener) {
        this.listener = listener;
    }

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        String id = data.getString(StandardArguments.ENTITY_ID);
        String issuer = data.getString(StandardArguments.ISSUED_BY);
        String name = data.getString(StandardArguments.NAME);
        String description = data.getString(StandardArguments.DESCRIPTION);
        EntityInstance entity = new EntityInstance(id, name) {
            private static final long serialVersionUID = -2566247043343833913L;
        };
        entity.setDescription(description);
        entity.setIssuedBy(issuer);
        entity.setTimestamp(System.currentTimeMillis());
        listener.record(entity);
    }
}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.util.Dates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 */
public class QuartzTest {

    private static final String GROUP = "GGG";
    private static final String JOB_A = "Job-A";
    private static final String JOB_B = "Job-B";
    private static final String JOB_C = "Job-C";
    private static final String TRIGGER_A = "Trigger-A";
    private static final String TRIGGER_B = "Trigger-B";
    private static final String TRIGGER_C = "Trigger-C";
    private static final String ISSUER = QuartzTest.class.getSimpleName();

    private Scheduler scheduler;

    private JobListener listener;

    private TestJobFactory factory;

    @Before
    public void setup() throws SchedulerException {
        listener = new JobListener();
        factory = new TestJobFactory(listener);
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.setJobFactory(factory);
        scheduler.start();
    }

    @After
    public void tearDown() throws SchedulerException {
        scheduler.shutdown();
    }

    @Test
    public void testSimple() throws Exception {
        long now = System.currentTimeMillis();
        long setupTime = 1000L * 1;

        JobDetail job1 = createJob(JOB_A, 1);
        JobDetail job2 = createJob(JOB_B, 2);
        JobDetail job3 = createJob(JOB_C, 3);

        Trigger trigger1 = createTrigger(TRIGGER_A, now + setupTime + 100);
        Trigger trigger2 = createTrigger(TRIGGER_B, now + setupTime + 200);
        Trigger trigger3 = createTrigger(TRIGGER_C, now + setupTime + 300);

        scheduler.scheduleJob(job1, trigger1);
        scheduler.scheduleJob(job2, trigger2);
        scheduler.scheduleJob(job3, trigger3);

        TriggerKey tk = new TriggerKey(TRIGGER_A, GROUP);
        JobKey jk = new JobKey(JOB_A, GROUP);
        assertEquals(job1, scheduler.getJobDetail(jk));
        assertEquals(trigger1, scheduler.getTrigger(tk));

        Thread.sleep(setupTime * 2);

        List<IEntityInstance> result = listener.getRecords();
        assertNotNull(result);
        assertEquals(3, result.size());

        IEntityInstance e = result.get(0);
        assertEquals(JOB_A, e.getName());
        assertEquals(JOB_A, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("Job #1", e.getDescription());
        long t1 = e.getTimestamp();

        e = result.get(1);
        assertEquals(JOB_B, e.getName());
        assertEquals(JOB_B, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("Job #2", e.getDescription());
        long t2 = e.getTimestamp();

        e = result.get(2);
        assertEquals(JOB_C, e.getName());
        assertEquals(JOB_C, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("Job #3", e.getDescription());
        long t3 = e.getTimestamp();

        assertTrue(Dates.toDefaultDateFormat(t1) + " <= " + Dates.toDefaultDateFormat(t2), t1 <= t2);
        assertTrue(Dates.toDefaultDateFormat(t2) + " <= " + Dates.toDefaultDateFormat(t3), t2 <= t3);
        assertNull(scheduler.getJobDetail(jk));
        assertNull(scheduler.getTrigger(tk));
    }

    @Test
    public void testTwoJobsAtSameMillisecond() throws Exception {
        long now = System.currentTimeMillis();
        long setupTime = 1000L * 1;

        JobDetail job1 = createJob(JOB_A, 1);
        JobDetail job2 = createJob(JOB_B, 2);

        Trigger trigger1 = createTrigger(TRIGGER_A, now + setupTime + 100);
        Trigger trigger2 = createTrigger(TRIGGER_B, now + setupTime + 100);

        scheduler.scheduleJob(job1, trigger1);
        scheduler.scheduleJob(job2, trigger2);

        TriggerKey tk = new TriggerKey(TRIGGER_A, GROUP);
        JobKey jk = new JobKey(JOB_A, GROUP);
        assertEquals(job1, scheduler.getJobDetail(jk));
        assertEquals(trigger1, scheduler.getTrigger(tk));

        Thread.sleep(setupTime * 2);

        List<IEntityInstance> result = listener.getRecords();
        assertNotNull(result);
        assertEquals(2, result.size());

        IEntityInstance e = result.get(0);
        assertEquals(JOB_A, e.getName());
        assertEquals(JOB_A, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("Job #1", e.getDescription());
        long t1 = e.getTimestamp();

        e = result.get(1);
        assertEquals(JOB_B, e.getName());
        assertEquals(JOB_B, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("Job #2", e.getDescription());
        long t2 = e.getTimestamp();

        assertTrue(Dates.toDefaultDateFormat(t1) + " <= " + Dates.toDefaultDateFormat(t2), t1 <= t2);
        assertNull(scheduler.getJobDetail(jk));
        assertNull(scheduler.getTrigger(tk));
    }

    @Test
    public void testRemoveTrigger() throws Exception {
        long now = System.currentTimeMillis();
        long setupTime = 1000L * 1;

        JobDetail job1 = createJob(JOB_A, 1);

        Trigger trigger1 = createTrigger(TRIGGER_A, now + setupTime + 100);

        scheduler.scheduleJob(job1, trigger1);

        TriggerKey tk = new TriggerKey(TRIGGER_A, GROUP);
        JobKey jk = new JobKey(JOB_A, GROUP);
        assertEquals(job1, scheduler.getJobDetail(jk));
        assertEquals(trigger1, scheduler.getTrigger(tk));

        scheduler.unscheduleJob(tk);
        assertNull(scheduler.getTrigger(tk));
        assertNull(scheduler.getJobDetail(jk));
        scheduler.deleteJob(jk);
        assertNull(scheduler.getJobDetail(jk));
        assertNull(scheduler.getTrigger(tk));

        Thread.sleep(setupTime * 2);

        List<IEntityInstance> result = listener.getRecords();
        assertNotNull(result);
        assertEquals(0, result.size());

        assertNull(scheduler.getJobDetail(jk));
        assertNull(scheduler.getTrigger(tk));
    }

    @Test
    public void testRescheduleTrigger() throws Exception {
        long now = System.currentTimeMillis();
        long setupTime = 1000L * 1;

        JobDetail job1 = createJob(JOB_A, 1);
        JobDetail job2 = createJob(JOB_B, 2);

        Trigger trigger1 = createTrigger(TRIGGER_A, now + setupTime + 1000 * 60 * 60); // one hour in the future
        Trigger trigger2 = createTrigger(TRIGGER_B, now + setupTime + 300);

        scheduler.scheduleJob(job1, trigger1);
        scheduler.scheduleJob(job2, trigger2);

        TriggerKey tk = new TriggerKey(TRIGGER_A, GROUP);
        JobKey jk = new JobKey(JOB_A, GROUP);
        assertEquals(job1, scheduler.getJobDetail(jk));
        assertEquals(trigger1, scheduler.getTrigger(tk));

        Trigger toChange = scheduler.getTrigger(tk);
        @SuppressWarnings("rawtypes")
        TriggerBuilder builder = toChange.getTriggerBuilder();
        Trigger newTrigger = builder.startAt(new Date(now + setupTime + 150)).build();
        scheduler.rescheduleJob(toChange.getKey(), newTrigger);
        assertEquals(newTrigger, scheduler.getTrigger(tk));
        assertEquals(job1, scheduler.getJobDetail(jk));

        Thread.sleep(setupTime * 2);

        List<IEntityInstance> result = listener.getRecords();
        assertNotNull(result);
        assertEquals(2, result.size());

        IEntityInstance e = result.get(0);
        assertEquals(JOB_A, e.getName());
        assertEquals(JOB_A, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("Job #1", e.getDescription());
        long t1 = e.getTimestamp();

        e = result.get(1);
        assertEquals(JOB_B, e.getName());
        assertEquals(JOB_B, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("Job #2", e.getDescription());
        long t2 = e.getTimestamp();

        assertTrue(Dates.toDefaultDateFormat(t1) + " <= " + Dates.toDefaultDateFormat(t2), t1 <= t2);
        assertNull(scheduler.getJobDetail(jk));
        assertNull(scheduler.getTrigger(tk));
    }

    @Test
    public void testUpdateJobDetails() throws Exception {
        long now = System.currentTimeMillis();
        long setupTime = 1000L * 1;

        JobDetail job1 = createJob(JOB_A, 1);

        Trigger trigger1 = createTrigger(TRIGGER_A, now + setupTime + 300);

        scheduler.scheduleJob(job1, trigger1);

        TriggerKey tk = new TriggerKey(TRIGGER_A, GROUP);
        Trigger t = scheduler.getTrigger(tk);
        JobKey jk = new JobKey(JOB_A, GROUP);
        JobDetail jd = scheduler.getJobDetail(jk);
        assertEquals(job1, jd);
        assertEquals(trigger1, t);

        JobBuilder jb = jd.getJobBuilder();
        JobDetail changed = jb
                .usingJobData(StandardArguments.NAME, JOB_B)
                .usingJobData(StandardArguments.DESCRIPTION, "This is changed value")
                .build();

        scheduler.deleteJob(jk);
        scheduler.scheduleJob(changed, t);
        assertEquals(changed, scheduler.getJobDetail(jk));
        assertNotSame(job1, scheduler.getJobDetail(jk));

        Thread.sleep(setupTime * 2);

        List<IEntityInstance> result = listener.getRecords();
        assertNotNull(result);
        assertEquals(1, result.size());

        IEntityInstance e = result.get(0);
        assertEquals(JOB_B, e.getName());
        assertEquals(JOB_A, e.getID());
        assertEquals(ISSUER, e.getIssuedBy());
        assertEquals("This is changed value", e.getDescription());

        assertNull(scheduler.getJobDetail(jk));
        assertNull(scheduler.getTrigger(tk));
    }

    Trigger createTrigger(String name, long time) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(name, GROUP)
                .startAt(new Date(time)).build();
    }

    JobDetail createJob(String name, int counter) {
        return JobBuilder
                .newJob(TestJob.class)
                .withIdentity(name, GROUP)
                .usingJobData(StandardArguments.NAME, name)
                .usingJobData(StandardArguments.ISSUED_BY, ISSUER)
                .usingJobData(StandardArguments.ENTITY_ID, name)
                .usingJobData(StandardArguments.DESCRIPTION, "Job #" + counter)
                .build();
    }

}

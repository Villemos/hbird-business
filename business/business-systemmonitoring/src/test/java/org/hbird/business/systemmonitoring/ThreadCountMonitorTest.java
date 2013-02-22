package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ThreadCountMonitorTest {

    private ThreadCountMonitor monitor;

    @Before
    public void setUp() throws Exception {
        monitor = new ThreadCountMonitor("test");
    }

    @Test
    public void testCheck() {
        assertNotNull(monitor.check());
    }
}

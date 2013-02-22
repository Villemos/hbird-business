package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class UptimeMonitorTest {

    private UptimeMonitor monitor;

    @Before
    public void setUp() throws Exception {
        monitor = new UptimeMonitor("test");
    }

    @Test
    public void testCheck() {
        assertNotNull(monitor.check());
    }
}

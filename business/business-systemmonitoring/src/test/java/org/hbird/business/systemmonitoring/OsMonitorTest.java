package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hbird.business.systemmonitoring.bean.OsMonitor;
import org.hbird.exchange.core.Label;
import org.junit.Before;
import org.junit.Test;

public class OsMonitorTest {

    private OsMonitor monitor;

    @Before
    public void setUp() throws Exception {
        monitor = new OsMonitor("test");
    }

    @Test
    public void testCheck() {
        Label info = monitor.check();
        assertNotNull(info);
        assertEquals("OS Info", info.getName());
        assertEquals("test/OS Info", info.getID());
        assertNotNull(info.getValue());
        assertTrue(info.getTimestamp() <= System.currentTimeMillis());
        assertNotNull(info.getDescription());
    }
}

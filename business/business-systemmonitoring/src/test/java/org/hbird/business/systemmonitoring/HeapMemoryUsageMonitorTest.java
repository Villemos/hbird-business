package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hbird.exchange.core.Parameter;
import org.junit.Before;
import org.junit.Test;

public class HeapMemoryUsageMonitorTest {

    private HeapMemoryUsageMonitor monitor;

    @Before
    public void setUp() throws Exception {
        monitor = new HeapMemoryUsageMonitor("test");
    }

    @Test
    public void testCheck() {
        Parameter[] result = monitor.check();
        assertEquals(2, result.length);
        assertNotNull(result[0]);
        assertNotNull(result[1]);
    }

}

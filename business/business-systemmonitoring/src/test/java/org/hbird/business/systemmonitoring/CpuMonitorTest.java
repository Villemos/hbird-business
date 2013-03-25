package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertNotNull;

import org.hbird.business.systemmonitoring.bean.CpuMonitor;
import org.junit.Before;
import org.junit.Test;

public class CpuMonitorTest {

    private CpuMonitor monitor;

    @Before
    public void setup() {
        monitor = new CpuMonitor("test");
    }

    @Test
    public void testCheck() {
        assertNotNull(monitor.check());
    }
}

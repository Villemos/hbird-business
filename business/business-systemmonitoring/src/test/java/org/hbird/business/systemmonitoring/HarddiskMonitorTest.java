package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.UnknownHostException;

import org.hbird.business.systemmonitoring.bean.HarddiskMonitor;
import org.hbird.exchange.core.Parameter;
import org.junit.Before;
import org.junit.Test;

public class HarddiskMonitorTest {

    private HarddiskMonitor monitor;

    @Before
    public void setup() {
        monitor = new HarddiskMonitor("test");
    }

    @Test
    public void testCheck() throws UnknownHostException {
        int roots = File.listRoots().length;
        Parameter[] result = monitor.check();
        assertEquals(roots * 3, result.length);
        for (Parameter p : result) {
            assertNotNull(p);
        }
    }
}

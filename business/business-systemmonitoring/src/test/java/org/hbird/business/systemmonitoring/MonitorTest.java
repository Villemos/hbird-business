package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hbird.business.core.naming.DefaultNaming;
import org.hbird.business.core.naming.INaming;
import org.hbird.business.systemmonitoring.bean.Monitor;
import org.junit.Before;
import org.junit.Test;

public class MonitorTest {

    private INaming naming;

    private Monitor monitor;

    @Before
    public void setUp() throws Exception {
        monitor = new Monitor("test");
        naming = new DefaultNaming();
    }

    @Test
    public void testSetNaming() {
        assertNotNull(monitor.getNaming());
        monitor.setNaming(naming);
        assertEquals(naming, monitor.getNaming());
        monitor.setNaming(null);
        assertNull(monitor.getNaming());
    }

    @Test
    public void testGetNaming() {
        testSetNaming();
    }
}

package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.api.IdBuilder;
import org.hbird.business.systemmonitoring.bean.HostInfo;
import org.hbird.business.systemmonitoring.bean.OsMonitor;
import org.hbird.exchange.core.Label;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OsMonitorTest {

    private static final String BASE_ID = "TEST";

    @Mock
    private IdBuilder builder;

    private InOrder inOrder;

    private OsMonitor monitor;

    @Before
    public void setup() {
        monitor = new OsMonitor(BASE_ID, builder);
        inOrder = inOrder(builder);
        when(builder.buildID(BASE_ID, OsMonitor.PARAMETER_RELATIVE_NAME)).thenReturn(BASE_ID + OsMonitor.PARAMETER_RELATIVE_NAME);
    }

    @Test
    public void testCheck() {
        Label info = monitor.check();
        assertNotNull(info);
        assertEquals(OsMonitor.PARAMETER_RELATIVE_NAME, info.getName());
        assertEquals(BASE_ID + OsMonitor.PARAMETER_RELATIVE_NAME, info.getID());
        assertEquals(HostInfo.getHostInfo(), info.getValue());
        assertTrue(info.getTimestamp() <= System.currentTimeMillis());
        assertEquals(OsMonitor.DESCRIPTION_OS_INFORMATION, info.getDescription());
        assertEquals(BASE_ID, info.getIssuedBy());
        assertEquals(BASE_ID, info.getApplicableTo());
        inOrder.verify(builder, times(1)).buildID(BASE_ID, OsMonitor.PARAMETER_RELATIVE_NAME);
        inOrder.verifyNoMoreInteractions();
    }
}

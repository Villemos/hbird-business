package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.api.IdBuilder;
import org.hbird.business.systemmonitoring.bean.Monitor;
import org.hbird.business.systemmonitoring.bean.ThreadCountMonitor;
import org.hbird.exchange.core.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ThreadCountMonitorTest {

    private static final String BASE_ID = "TEST";

    @Mock
    private IdBuilder builder;

    private InOrder inOrder;

    private ThreadCountMonitor monitor;

    @Before
    public void setup() {
        monitor = new ThreadCountMonitor(BASE_ID, builder);
        inOrder = inOrder(builder);
        when(builder.buildID(BASE_ID, ThreadCountMonitor.PARAMETER_RELATIVE_NAME)).thenReturn(BASE_ID + ThreadCountMonitor.PARAMETER_RELATIVE_NAME);
    }

    @Test
    public void testCheck() {
        Parameter p = monitor.check();
        assertNotNull(p);
        assertEquals(BASE_ID + ThreadCountMonitor.PARAMETER_RELATIVE_NAME, p.getID());
        assertEquals(ThreadCountMonitor.PARAMETER_RELATIVE_NAME, p.getName());
        assertEquals(ThreadCountMonitor.DESCRIPTION_THREAD_COUNT, p.getDescription());
        assertEquals(Monitor.UNIT_COUNT, p.getUnit());
        assertNotNull(p.getValue());
        assertEquals(BASE_ID, p.getIssuedBy());
        assertEquals(BASE_ID, p.getApplicableTo());
        inOrder.verify(builder, times(1)).buildID(BASE_ID, ThreadCountMonitor.PARAMETER_RELATIVE_NAME);
        inOrder.verifyNoMoreInteractions();
    }
}

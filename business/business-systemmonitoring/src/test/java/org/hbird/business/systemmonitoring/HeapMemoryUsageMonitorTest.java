package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.api.IdBuilder;
import org.hbird.business.systemmonitoring.bean.HeapMemoryUsageMonitor;
import org.hbird.business.systemmonitoring.bean.Monitor;
import org.hbird.exchange.core.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HeapMemoryUsageMonitorTest {

    private static final String BASE_ID = "TEST";

    @Mock
    private IdBuilder builder;

    private InOrder inOrder;

    private HeapMemoryUsageMonitor monitor;

    @Before
    public void setup() {
        monitor = new HeapMemoryUsageMonitor(BASE_ID, builder);
        inOrder = inOrder(builder);
        when(builder.buildID(BASE_ID, HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_HEAP_MEMORY)).thenReturn(
                BASE_ID + HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_HEAP_MEMORY);
        when(builder.buildID(BASE_ID, HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY)).thenReturn(
                BASE_ID + HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY);
    }

    @Test
    public void testCheck() {
        Parameter[] result = monitor.check();
        assertEquals(2, result.length);
        Parameter p = result[0];
        assertNotNull(p);
        assertEquals(BASE_ID + HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_HEAP_MEMORY, p.getID());
        assertEquals(HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_HEAP_MEMORY, p.getName());
        assertEquals(HeapMemoryUsageMonitor.DESCRIPTION_HEAP_MEMORY_USAGE, p.getDescription());
        assertEquals(Monitor.UNIT_BYTE, p.getUnit());
        assertEquals(BASE_ID, p.getIssuedBy());
        assertNotNull(p.getValue());
        assertEquals(BASE_ID, p.getApplicableTo());
        p = result[1];
        assertNotNull(p);
        assertEquals(BASE_ID + HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY, p.getID());
        assertEquals(HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY, p.getName());
        assertEquals(HeapMemoryUsageMonitor.DESCRIPTION_NON_HEAP_MEMORY_USAGE, p.getDescription());
        assertEquals(Monitor.UNIT_BYTE, p.getUnit());
        assertEquals(BASE_ID, p.getIssuedBy());
        assertNotNull(p.getValue());
        assertEquals(BASE_ID, p.getApplicableTo());

        inOrder.verify(builder, times(1)).buildID(BASE_ID, HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_HEAP_MEMORY);
        inOrder.verify(builder, times(1)).buildID(BASE_ID, HeapMemoryUsageMonitor.PARAMETER_RELATIVE_NAME_NON_HEAP_MEMORY);
    }
}

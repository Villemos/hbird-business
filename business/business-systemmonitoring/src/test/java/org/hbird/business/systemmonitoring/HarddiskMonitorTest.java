package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.File;

import org.hbird.business.api.IdBuilder;
import org.hbird.business.systemmonitoring.bean.HarddiskMonitor;
import org.hbird.business.systemmonitoring.bean.Monitor;
import org.hbird.exchange.core.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HarddiskMonitorTest {

    private static final String BASE_ID = "TEST";

    @Mock
    private IdBuilder builder;

    private HarddiskMonitor monitor;

    private InOrder inOrder;

    @Before
    public void setup() {
        monitor = new HarddiskMonitor(BASE_ID, builder);
        inOrder = inOrder(builder);
    }

    @Test
    public void testCheck() throws Exception {
        File[] roots = File.listRoots();
        for (File root : roots) {
            String rootName = root.getPath();
            when(builder.buildID(BASE_ID, rootName)).thenReturn(BASE_ID + rootName);
            when(builder.buildID(BASE_ID + rootName, HarddiskMonitor.PARAMETER_AVAILABLE_DISK_SPACE)).thenReturn(
                    BASE_ID + rootName + HarddiskMonitor.PARAMETER_AVAILABLE_DISK_SPACE);
            when(builder.buildID(BASE_ID + rootName, HarddiskMonitor.PARAMETER_FREE_DISK_SPACE)).thenReturn(
                    BASE_ID + rootName + HarddiskMonitor.PARAMETER_FREE_DISK_SPACE);
            when(builder.buildID(BASE_ID + rootName, HarddiskMonitor.PARAMETER_USED_DISK_SPACE)).thenReturn(
                    BASE_ID + rootName + HarddiskMonitor.PARAMETER_USED_DISK_SPACE);
        }

        Parameter[] result = monitor.check();
        assertEquals(roots.length * 3, result.length);
        for (int i = 0; i < result.length; i++) {
            Parameter p = result[i];
            String rootName = roots[i / 3].getPath();
            assertNotNull(p);
            assertNotNull(p.getValue());
            assertEquals(BASE_ID, p.getIssuedBy());
            assertEquals(BASE_ID + rootName, p.getApplicableTo());
            switch (i % 3) {
                case 0:
                    assertEquals(BASE_ID + rootName + HarddiskMonitor.PARAMETER_AVAILABLE_DISK_SPACE, p.getID());
                    assertEquals(HarddiskMonitor.PARAMETER_AVAILABLE_DISK_SPACE, p.getName());
                    assertEquals(HarddiskMonitor.DESCRIPTION_AVAILABLE_DISK_SPACE, p.getDescription());
                    assertEquals(Monitor.UNIT_BYTE, p.getUnit());
                    break;
                case 1:
                    assertEquals(BASE_ID + rootName + HarddiskMonitor.PARAMETER_FREE_DISK_SPACE, p.getID());
                    assertEquals(HarddiskMonitor.PARAMETER_FREE_DISK_SPACE, p.getName());
                    assertEquals(HarddiskMonitor.DESCRIPTION_FREE_DISK_SPACE, p.getDescription());
                    assertEquals(Monitor.UNIT_BYTE, p.getUnit());
                    break;
                case 2:
                    assertEquals(BASE_ID + rootName + HarddiskMonitor.PARAMETER_USED_DISK_SPACE, p.getID());
                    assertEquals(HarddiskMonitor.PARAMETER_USED_DISK_SPACE, p.getName());
                    assertEquals(HarddiskMonitor.DESCRIPTION_USED_DISK_SPACE, p.getDescription());
                    assertEquals(Monitor.UNIT_PERCENTAGE, p.getUnit());
                    break;
                default:
                    fail("unknown parameter");
            }
        }
        inOrder.verify(builder, times(4 * roots.length)).buildID(anyString(), anyString());
        inOrder.verifyNoMoreInteractions();
    }
}

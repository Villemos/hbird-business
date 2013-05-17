package org.hbird.business.systemmonitoring;

import static org.junit.Assert.assertEquals;

import org.hbird.business.api.IdBuilder;
import org.hbird.business.systemmonitoring.bean.Monitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MonitorTest {

    @Mock
    private IdBuilder idBuilder;

    private Monitor monitor;

    private InOrder inOrder;

    @Before
    public void setUp() throws Exception {
        monitor = new Monitor("test", idBuilder);
        inOrder = Mockito.inOrder(idBuilder);
    }

    @Test
    public void testGetIdBuilder() {
        assertEquals(idBuilder, monitor.getIdBuilder());
        inOrder.verifyNoMoreInteractions();
    }
}

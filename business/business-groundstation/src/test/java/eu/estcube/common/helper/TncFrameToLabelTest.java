/** 
 *
 */
package eu.estcube.common.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.core.naming.Base;
import org.hbird.business.core.naming.DefaultNaming;
import org.hbird.exchange.core.Label;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.estcube.domain.transport.tnc.TncFrame;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TncFrameToLabelTest {

    private static final String GROUND_STATION_NAME = "gs-name";

    private static final byte[] DATA = new byte[] { 0x0D, 0x0E, 0x0A, 0x0D, 0x0C, 0x00, 0x0D, 0x0E };

    private static final int PORT = 4;

    private static final Long NOW = System.currentTimeMillis();

    private static final String ISSUED_BY = "issuer";

    private TncFrameToLabel toLabel;

    @Mock
    private TncFrame frame;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        toLabel = new TncFrameToLabel(GROUND_STATION_NAME);
        inOrder = inOrder(frame);
        when(frame.getData()).thenReturn(DATA);
        when(frame.getTarget()).thenReturn(PORT);
    }

    /**
     * Test method for
     * {@link eu.estcube.common.helper.TncFrameToLabel#process(java.lang.Long, java.lang.String, eu.estcube.domain.transport.tnc.TncFrame)}
     * .
     */
    @Test
    public void testProcess() {
        Label label = toLabel.process(NOW, ISSUED_BY, frame);
        assertNotNull(label);
        assertEquals(ISSUED_BY, label.getIssuedBy());
        assertEquals(NOW.longValue(), label.getTimestamp());
        assertEquals(new DefaultNaming().createAbsoluteName(Base.GROUND_STATION, GROUND_STATION_NAME,
                TncFrameToLabel.RELATIVE_PARAMETER_NAME), label.getName());
        assertTrue(label.getDatasetidentifier().startsWith(ISSUED_BY));
        assertTrue(label.getDatasetidentifier().endsWith(String.valueOf(PORT)));
        assertEquals(TncFrameToLabel.DESCRIPTION, label.getDescription());
        assertEquals("0D 0E 0A 0D 0C 00 0D 0E", label.getValue());
        assertEquals(TncFrame.class.getSimpleName(), label.getType());
        inOrder.verify(frame, times(1)).getData();
        inOrder.verify(frame, times(1)).getTarget();
        inOrder.verifyNoMoreInteractions();
    }
}

package eu.estcube.common;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hbird.exchange.core.Named;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PrepareForInjectionTest {

    private static final String UUID = "UUID-asdfasd-fasd-fasdf213-123";
    private static final String NAME = "name";
    private static final String ISSUED_BY = "issuer";
    private static final String TYPE = "type";
    private static final String DATA_SET_ID = "data set id";
    private static final Long NOW = System.currentTimeMillis();

    private PrepareForInjection prep;

    @Mock
    private Exchange exchange;

    @Mock
    private Message in;

    @Mock
    private Message out;

    @Mock
    private Named named;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        prep = new PrepareForInjection();
        inOrder = inOrder(exchange, in, out, named);
    }

    /**
     * Test method for
     * {@link eu.estcube.common.PrepareForInjection#process(org.apache.camel.Exchange)}
     * .
     * 
     * @throws Exception
     */
    @Test
    public void testProcess() throws Exception {
        when(exchange.getIn()).thenReturn(in);
        when(exchange.getOut()).thenReturn(out);
        when(in.getBody(Named.class)).thenReturn(named);
        when(named.getUuid()).thenReturn(UUID);
        when(named.getName()).thenReturn(NAME);
        when(named.getIssuedBy()).thenReturn(ISSUED_BY);
        when(named.getType()).thenReturn(TYPE);
        when(named.getTimestamp()).thenReturn(NOW);
        prep.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(Named.class);

        inOrder.verify(named, times(1)).getUuid();
        inOrder.verify(out, times(1)).setHeader(Headers.UUID, UUID);
        inOrder.verify(named, times(1)).getName();
        inOrder.verify(out, times(1)).setHeader(Headers.NAME, NAME);
        inOrder.verify(named, times(1)).getIssuedBy();
        inOrder.verify(out, times(1)).setHeader(Headers.ISSUED_BY, ISSUED_BY);
        // inOrder.verify(named, times(1)).getClass(); // Mockito can't verify
        // final methods
        inOrder.verify(out, times(1)).setHeader(Headers.CLASS, named.getClass().getSimpleName());
        inOrder.verify(named, times(1)).getType();
        inOrder.verify(out, times(1)).setHeader(Headers.TYPE, TYPE);
        inOrder.verify(named, times(1)).getTimestamp();
        inOrder.verify(out, times(1)).setHeader(Headers.TIMESTAMP, NOW);
        inOrder.verifyNoMoreInteractions();
    }
}

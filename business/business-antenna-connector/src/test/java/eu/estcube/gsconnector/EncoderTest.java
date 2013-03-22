package eu.estcube.gsconnector;

import java.nio.charset.Charset;
import java.util.Arrays;

import junit.framework.TestCase;

import org.jboss.netty.channel.MessageEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import eu.estcube.domain.JMSConstants;

public class EncoderTest extends TestCase {

    private Encoder encoder;
    private MessageEvent message;

    @Before
    public void setUp() throws Exception {
        encoder = new Encoder();
        message = Mockito.mock(MessageEvent.class);

    }

    /**
     * Test method for
     * {@link eu.estcube.gsconnector.Encoder#checkEndLine(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)}
     * 
     * @throws Exception
     */
    @Test
    public void testcheckEndLineChannelHandlerContextMessageEvent() {
        Mockito.when(message.getMessage()).thenReturn("1");
        String str = "1\n";
        byte[] bytes = str.getBytes(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("1\n");
        str = "1\n";
        bytes = str.getBytes(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("");
        str = null;
        bytes = null;
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("1\\n");
        str = "1\\n\n";
        bytes = str.getBytes(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("11\n11");
        str = "11\n11\n";
        bytes = str.getBytes(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

    }

}

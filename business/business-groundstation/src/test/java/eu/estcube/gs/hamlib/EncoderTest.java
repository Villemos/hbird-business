package eu.estcube.gs.hamlib;

import java.nio.charset.Charset;
import java.util.Arrays;

import junit.framework.TestCase;

import org.jboss.netty.channel.MessageEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EncoderTest extends TestCase {

    private EncoderStringToBytes encoder;
    private MessageEvent message;

    @Override
    @Before
    public void setUp() throws Exception {
        encoder = new EncoderStringToBytes();
        message = Mockito.mock(MessageEvent.class);

    }

    /**
     * Test method for
     * {@link eu.estcube.gsconnector.Encoder#checkEndLine(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)}
     */
    @Test
    public void testcheckEndLineChannelHandlerContextMessageEvent() {
        Mockito.when(message.getMessage()).thenReturn("1");
        String str = "1\n";
        byte[] bytes = str.getBytes(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("1\n");
        str = "1\n";
        bytes = str.getBytes(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("");
        str = null;
        bytes = null;
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("1\\n");
        str = "1\\n\n";
        bytes = str.getBytes(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

        Mockito.when(message.getMessage()).thenReturn("11\n11");
        str = "11\n11\n";
        bytes = str.getBytes(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertTrue(Arrays.equals(bytes, encoder.checkEndLine(message)));

    }
}

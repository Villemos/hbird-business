package eu.estcube.gsconnector;

import java.nio.charset.Charset;

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

import eu.estcube.domain.JMSConstants;

public class DecoderToStringTest {

    private DecoderToString decoder = new DecoderToString();

    @Test
    public void testDecodeChannelHandlerContextChannelObject() throws Exception {

        String input = "x:\n" + "a:\tb\n" + "c:\td\n" + "e:\t\tf\n" + "o e:\t\tf\n" + "m: 1\n" + "\n"
            + JMSConstants.GS_DEVICE_END_MESSAGE + " 0\n";
        Object msg = new Object();
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        String decodedString = (String) decoder.decode(null, null, msg);
        Assert.assertEquals(decodedString, input);
    }
}

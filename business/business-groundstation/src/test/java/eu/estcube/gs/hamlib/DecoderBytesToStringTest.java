package eu.estcube.gs.hamlib;

import java.nio.charset.Charset;

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

public class DecoderBytesToStringTest {

    private final DecoderBytesToString decoder = new DecoderBytesToString();

    @Test
    public void testDecodeChannelHandlerContextChannelObject() throws Exception {

        String input = "x:\n" + "a:\tb\n" + "c:\td\n" + "e:\t\tf\n" + "o e:\t\tf\n" + "m: 1\n" + "\n"
                + HamlibConstants.DEVICE_END_MESSAGE + " 0\n";
        Object msg = new Object();
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        String decodedString = (String) decoder.decode(null, null, msg);
        Assert.assertEquals(decodedString, input);
    }
}

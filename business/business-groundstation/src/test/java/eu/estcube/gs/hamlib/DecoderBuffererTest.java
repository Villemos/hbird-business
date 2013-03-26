package eu.estcube.gs.hamlib;

import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Before;
import org.junit.Test;

public class DecoderBuffererTest extends TestCase {

    private DecoderBufferer decoder;

    @Override
    @Before
    public void setUp() throws Exception {
        decoder = new DecoderBufferer();
    }

    /**
     * Test method for
     * {@link eu.estcube.gsconnector.DecoderHamlib#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)}
     */
    @Test
    public void testHamlibDecoder() throws Exception {
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(new byte[] {});
        Object msg = new Object();

        String input = "a";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(null, decoder.decode(null, null, msg));

        input = "abc";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(null, decoder.decode(null, null, msg));

        input = " \n\nR\nPRT";
        buffer = ChannelBuffers.wrappedBuffer(new byte[] {});
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(null, decoder.decode(null, null, msg));

        input = "rprt \n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(null, decoder.decode(null, null, msg));

        input = "RPR  T\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(null, decoder.decode(null, null, msg));

        msg = new Object();
        decoder = new DecoderBufferer();
        input = HamlibConstants.DEVICE_END_MESSAGE + " 0\n";
        buffer = ChannelBuffers.wrappedBuffer(new byte[] {});
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(input, decoder.decode(null, null, msg));

        msg = new Object();
        decoder = new DecoderBufferer();
        input = "abs" + HamlibConstants.DEVICE_END_MESSAGE + " 0\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(input, decoder.decode(null, null, msg));

        msg = new Object();
        decoder = new DecoderBufferer();
        input = "abs " + HamlibConstants.DEVICE_END_MESSAGE + " 0\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(input, decoder.decode(null, null, msg));

        msg = new Object();
        decoder = new DecoderBufferer();
        input = "abs " + HamlibConstants.DEVICE_END_MESSAGE + " -11\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(input, decoder.decode(null, null, msg));

        msg = new Object();
        decoder = new DecoderBufferer();
        input = "abs " + HamlibConstants.DEVICE_END_MESSAGE + " 11\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = buffer.toString(Charset.forName(HamlibConstants.STRING_ENCODING));
        assertEquals(input, decoder.decode(null, null, msg));
    }

}

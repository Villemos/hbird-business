package eu.estcube.gsconnector;

import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Before;
import org.junit.Test;

import eu.estcube.domain.JMSConstants;

public class DecoderHamlibTest extends TestCase {

    private DecoderHamlib decoder;

    @Before
    public void setUp() throws Exception {
        decoder = new DecoderHamlib();
    }
    
    /**
     * Test method for {@link eu.estcube.gsconnector.DecoderHamlib#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)}.
     * @throws Exception 
     */
    @Test
    public void testHamlibDecoder()throws Exception{
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(new byte[] {});
        Object msg = new Object();
        
        String input = "a";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(null, decoder.decode(null, null, msg));
        
        input = "abc";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(null, decoder.decode(null, null, msg));     
        
        input = " \n\nR\nPRT";
        buffer = ChannelBuffers.wrappedBuffer(new byte[] {});
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(null, decoder.decode(null, null, msg));  
        
        input = "rprt \n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(null, decoder.decode(null, null, msg));  
        
        input = "RPR  T\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(null, decoder.decode(null, null, msg));  
        
        msg = new Object();
        decoder = new DecoderHamlib();
        input = JMSConstants.GS_DEVICE_END_MESSAGE+" 0\n";
        buffer = ChannelBuffers.wrappedBuffer(new byte[] {});
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(input, decoder.decode(null, null, msg));  
        
        msg = new Object();
        decoder = new DecoderHamlib();
        input = "abs"+JMSConstants.GS_DEVICE_END_MESSAGE+" 0\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(input, decoder.decode(null, null, msg));  
        
        msg = new Object();
        decoder = new DecoderHamlib();
        input = "abs "+JMSConstants.GS_DEVICE_END_MESSAGE+" 0\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(input, decoder.decode(null, null, msg));  
        

        msg = new Object();
        decoder = new DecoderHamlib();
        input = "abs "+JMSConstants.GS_DEVICE_END_MESSAGE+" -11\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(input, decoder.decode(null, null, msg));  
        

        msg = new Object();
        decoder = new DecoderHamlib();
        input = "abs "+JMSConstants.GS_DEVICE_END_MESSAGE+" 11\n";
        buffer = ChannelBuffers.wrappedBuffer(input.getBytes());
        msg = ((ChannelBuffer) buffer).toString(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
        assertEquals(input, decoder.decode(null, null, msg));  
    }

}

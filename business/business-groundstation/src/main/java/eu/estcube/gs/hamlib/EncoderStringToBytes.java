package eu.estcube.gs.hamlib;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import eu.estcube.domain.JMSConstants;

public class EncoderStringToBytes extends SimpleChannelHandler {

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {

        // encodes a string into bytes to be sent to Hamlib
        byte[] bytes = checkEndLine(e);
        if (bytes.length > 0) {
            ChannelBuffer buffer = ChannelBuffers.buffer(bytes.length);
            buffer.writeBytes(bytes);
            Channels.write(ctx, e.getFuture(), buffer);
        }
    }

    public byte[] checkEndLine(MessageEvent e) {
        String str = (String) e.getMessage().toString();
        if (str.length() == 0) {
            return null;
        }
        if (!str.endsWith("\n")) {
            str += "\n";
        }
        return str.getBytes(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
    }

}

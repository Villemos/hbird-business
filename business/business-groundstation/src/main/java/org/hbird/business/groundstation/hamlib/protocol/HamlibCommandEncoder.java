package org.hbird.business.groundstation.hamlib.protocol;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class HamlibCommandEncoder extends SimpleChannelHandler {

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {

        // encodes a string into bytes to be sent to Hamlib
        byte[] bytes = toBytes(e.getMessage());
        if (bytes != null && bytes.length > 0) {
            ChannelBuffer buffer = ChannelBuffers.buffer(bytes.length);
            buffer.writeBytes(bytes);
            Channels.write(ctx, e.getFuture(), buffer);
        }
    }

    byte[] toBytes(Object message) {
        if (message == null) {
            return null;
        }
        String str = message.toString();
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (!str.endsWith("\n")) {
            str += "\n";
        }
        return str.getBytes(Charset.forName(HamlibProtocolConstants.STRING_ENCODING));
    }

}

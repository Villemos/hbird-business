package org.hbird.business.groundstation.hamlib.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.component.netty.ChannelHandlerFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class HamlibResponseBufferer extends OneToOneDecoder implements ChannelHandlerFactory {

    protected static final Pattern MESSAGE_END_PATTERN = Pattern.compile(HamlibProtocolConstants.RESPONSE_END_MARKER + "[ ][+-]?\\d+\n");

    /** StringBuffer to collect lines until MESSAGE_END_PATTERN is found. */
    private final StringBuilder messageBuffer = new StringBuilder();

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel evt, Object msg) throws Exception {
        // put message lines together, buffer them until RPRT is reached and return the whole message
        String line = msg.toString();
        synchronized (messageBuffer) {
            messageBuffer.append(line);
            Matcher matcher = MESSAGE_END_PATTERN.matcher(line);
            if (matcher.find()) {
                // found the message end marker
                String message = messageBuffer.toString();
                messageBuffer.setLength(0); // clear the current buffer
                // return previous content of the buffer
                return message;
            }
        }
        // message end marker not found - return null; no message will be not yet sent down the line
        return null;
    }

    /**
     * @see org.apache.camel.component.netty.ChannelHandlerFactory#newChannelHandler()
     */
    @Override
    public ChannelHandler newChannelHandler() {
        return new HamlibResponseBufferer();
    }
}

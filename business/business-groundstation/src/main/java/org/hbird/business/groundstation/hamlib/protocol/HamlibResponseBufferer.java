package org.hbird.business.groundstation.hamlib.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class HamlibResponseBufferer extends OneToOneDecoder {

    protected static final Pattern MESSAGE_END_PATTERN = Pattern.compile(HamlibProtocolConstants.DEVICE_END_MESSAGE + "[ ]" + "[+-]?\\d+" + "\n");

    /** StringBuffer to collect lines until MESSAGE_END_PATTERN is found. */
    // TODO - 26.03.2013, kimmell - check if StringBuilder can be used here
    private final StringBuffer messageBuffer = new StringBuffer();

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel evt, Object msg) throws Exception {
        // puts message parts together, buffering them until RPRT is reached and
        // in the end returning the whole message
        String line = msg.toString();
        messageBuffer.append(line);
        Matcher matcher = MESSAGE_END_PATTERN.matcher(line);
        while (matcher.find()) { // TODO - 26.03.2013, kimmell - use if instead?
            // found the message end marker
            String message = messageBuffer.toString();
            messageBuffer.setLength(0); // clear the current buffer
            // return previous content of the buffer
            return message;
        }
        // message end marker not found - return null; no message will be not yet sent down the line
        return null;
    }
}

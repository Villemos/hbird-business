package eu.estcube.gs.hamlib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class DecoderBufferer extends OneToOneDecoder {

    protected static final Pattern MESSAGE_END_PATTERN = Pattern.compile(HamlibConstants.DEVICE_END_MESSAGE + "[ ]" + "[+-]?\\d+" + "\n");

    /** StringBuffer to collect messages until MESSAGE_END_PATTERN is found. */
    // TODO - 26.03.2013, kimmell - check if StringBuilder can be used here
    private final StringBuffer messageBuffer = new StringBuffer();

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel evt, Object msg) throws Exception {
        // puts message parts together, buffering them until RPRT is reached and
        // in the end returning the whole message
        String checkRPRT = msg.toString();
        messageBuffer.append(checkRPRT);
        Matcher matcher = MESSAGE_END_PATTERN.matcher(checkRPRT);
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

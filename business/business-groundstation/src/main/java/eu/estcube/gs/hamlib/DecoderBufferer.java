package eu.estcube.gs.hamlib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import eu.estcube.domain.JMSConstants;

public class DecoderBufferer extends OneToOneDecoder {

    private StringBuffer messageBuffer = new StringBuffer();

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel evt, Object msg) throws Exception {
        // puts message parts together, buffering them until RPRT is reached and
        // in the end returning the whole message
        String checkRPRT = msg.toString();
        messageBuffer.append(checkRPRT);
        Pattern pattern = Pattern.compile(JMSConstants.GS_DEVICE_END_MESSAGE + "[ ]" + "[+-]?\\d+" + "\n");
        Matcher matcher = pattern.matcher(checkRPRT);
        while (matcher.find()) {
            String message = messageBuffer.toString();
            messageBuffer = new StringBuffer();
            return message;
        }
        return null;

    }

}

package eu.estcube.gsconnector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import eu.estcube.domain.JMSConstants;

public class DecoderHamlib extends OneToOneDecoder {

    private StringBuffer messageBuffer = new StringBuffer();
    
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel evt, Object msg) throws Exception {
        String checkRPRT = msg.toString();
        messageBuffer.append(checkRPRT);
        Pattern pattern = Pattern.compile(JMSConstants.GS_DEVICE_END_MESSAGE + "[ ]" + "[+-]?\\d+" + "\n");
        Matcher matcher = pattern.matcher(checkRPRT);
        while (matcher.find()) {
            String message = messageBuffer.toString();
            messageBuffer.setLength(0);
            return message;
        }
        return null;

    }

}

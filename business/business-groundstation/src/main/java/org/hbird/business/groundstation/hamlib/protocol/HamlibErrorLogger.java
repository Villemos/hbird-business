package org.hbird.business.groundstation.hamlib.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HamlibErrorLogger extends OneToOneDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(HamlibErrorLogger.class);

    // Commands that do not return values respond with
    // the line "RPRT  x", where x is zero when successful,
    // otherwise is a negative number indicating
    // the error code.

    @Override
    protected Object decode(ChannelHandlerContext context, Channel channel, Object messageObject) throws Exception {
        String message = messageObject.toString();
        String errorCode = HamlibProtocolHelper.getErrorCode(message);
        if (errorCode != null) {
            String logString = escapeLineEnds(message);
            if ("0".equals(errorCode)) {
                LOG.trace("Hamlib response: OK - {}", logString);
            }
            else {
                LOG.warn("Hamlib response: Error; code {} - {}", errorCode, logString);
            }
        }
        return message;
    }

    static String escapeLineEnds(String str) {
        return str.replace("\n", "\\n");
    }
}

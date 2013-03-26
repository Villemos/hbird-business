package eu.estcube.gs.hamlib;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DecoderStringLogger extends OneToOneDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(DecoderStringLogger.class);

    // Commands that do not return values respond with
    // the line "RPRT  x", where x is zero when successful,
    // otherwise is a regative number indicating
    // the error code.

    @Override
    protected Object decode(ChannelHandlerContext arg0, Channel arg1, Object message) throws Exception {
        String[] list = message.toString().split("\n");
        String last = list[list.length - 1];
        String retcode = last.split(" ")[1];
        logMessage(retcode);
        return message;
    }

    private static void logMessage(String retcode) {
        String message;
        switch (Integer.parseInt(retcode)) {
            case 0:
                message = "Ok";
                break;
            case -1:
                message = "Nondescript error";
                break;
            default:
                message = "Unspecified error";
                break;
        }
        LOG.debug("Hamlib returned code {} - {}", retcode, message);
    }
}

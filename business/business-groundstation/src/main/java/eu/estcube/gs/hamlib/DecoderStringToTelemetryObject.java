package eu.estcube.gs.hamlib;

import org.hbird.exchange.core.State;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecoderStringToTelemetryObject extends OneToOneDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(DecoderStringToTelemetryObject.class);

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object message) {
        // creates a TelemetryObject out of a string

        LOG.debug("Message: {}", message.toString());
        String[] messageSplit = message.toString().split("\n");

        String[] name = messageSplit[0].split(":");

        System.out.println("name[0] = " + name[0]);
        System.out.println("state   = " + message.toString().contains("RPRT 0"));
        State label = new State("", "NativeCommandState", "Raw response from Hamlib", name[0], message.toString().contains("RPRT 0"));

        label.setTimestamp(System.currentTimeMillis());
        return label;
    }
}

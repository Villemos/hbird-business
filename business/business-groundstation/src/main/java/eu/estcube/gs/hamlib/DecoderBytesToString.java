package eu.estcube.gs.hamlib;

import java.nio.charset.Charset;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.string.StringDecoder;

import eu.estcube.domain.JMSConstants;

public class DecoderBytesToString extends StringDecoder {

    public DecoderBytesToString() {
        super(Charset.forName(JMSConstants.ENCODING_STRING_FORMAT));
    }

    public Object decode(ChannelHandlerContext ctx, Channel evt, Object msg) throws Exception {
        // decodes from bytes to a string
        return super.decode(ctx, evt, msg);
    }
}

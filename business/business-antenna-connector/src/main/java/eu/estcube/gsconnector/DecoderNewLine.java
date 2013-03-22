package eu.estcube.gsconnector;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;

import eu.estcube.domain.JMSConstants;

class DecoderNewLine extends DelimiterBasedFrameDecoder {

    protected DecoderNewLine() {
        super(JMSConstants.GS_MAX_FRAME_LENGTH, false, Delimiters.lineDelimiter());
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, Channel evt, ChannelBuffer arg2) throws Exception {
        return super.decode(ctx, evt, arg2);
    }
}
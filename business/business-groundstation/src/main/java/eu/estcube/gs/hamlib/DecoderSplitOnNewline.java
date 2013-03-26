package eu.estcube.gs.hamlib;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;

class DecoderSplitOnNewline extends DelimiterBasedFrameDecoder {

    protected DecoderSplitOnNewline() {
        super(HamlibConstants.MAX_FRAME_LENGTH, false, Delimiters.lineDelimiter());
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, Channel evt, ChannelBuffer arg2) throws Exception {
        // splits message from Hamlib on newline
        return super.decode(ctx, evt, arg2);
    }
}

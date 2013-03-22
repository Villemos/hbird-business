package eu.estcube.gsconnector;

import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public abstract class EncoderTelemetryObjectAbstract extends OneToOneEncoder{

    protected abstract Map<String, HamlibCommandBuilder> getCommandsHashMap();
    
   
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
        if (message == null) {
            return ChannelBuffers.EMPTY_BUFFER;
        }
        TelemetryCommand command = (TelemetryCommand) message;
        StringBuilder messageString = new StringBuilder();
        messageString.append(getCommandsHashMap().get(command.getCommandName()).createMessageString(command));
        return messageString;
    }
}

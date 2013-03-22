package eu.estcube.gsconnector;

import java.util.Date;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryParameter;

public abstract class DecoderTelemetryObjectAbstract extends OneToOneDecoder {

    @Value("${gsName}")
    private String gsName;

    private static final String HELP = "Commands (some may not be available for this rig):";

    private static final Logger LOG = LoggerFactory.getLogger(DecoderTelemetryObjectAbstract.class);

    protected abstract Map<String, String> getHamlibProtocolMap();

    protected abstract TelemetryObject createHelp(String[] messageSplit);

    protected abstract String getDeviceName();

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {

        String[] messageSplit = message.toString().split("\n");
        TelemetryObject telemetryObject;
        LOG.debug("Message: {}", message.toString());

        if (messageSplit[0].equals(HELP)) {
            telemetryObject = createHelp(messageSplit);
        } else {
            String[] name = messageSplit[0].split(":");
            telemetryObject = new TelemetryObject(hamlibToProtocol(name[0]), new Date());
            addParametersToTelemetryObject(messageSplit, telemetryObject);
        }

        telemetryObject.setSource(gsName);
        telemetryObject.setDevice(getDeviceName());
        return telemetryObject;
    }

    protected void addParametersToTelemetryObject(String[] messageSplit, TelemetryObject telemetryObject) {
        String[] messagePiece = { null, null };

        for (int i = 0; i < messageSplit.length; i++) {
            messageSplit[i] = messageSplit[i].replace(":\t\t", ":");
            messageSplit[i] = messageSplit[i].replace(":\t", ":");
            messageSplit[i] = messageSplit[i].replace(": ", ":");
            if (messageSplit[i].contains(":")) {
                messagePiece = messageSplit[i].split(":");
            } else if (messageSplit[i].contains(" ")) {
                messagePiece = messageSplit[i].split(" ");
            } else {
                continue;
            }
            messagePiece[0] = hamlibToProtocol(messagePiece[0]);
            if (messagePiece.length == 1) {
                telemetryObject.addParameter(new TelemetryParameter(messagePiece[0], ""));
                continue;
            }
            telemetryObject.addParameter(new TelemetryParameter(messagePiece[0], messagePiece[1]));
        }
    }

    protected String hamlibToProtocol(String hamlibName) {
        String protocolName = getHamlibProtocolMap().get(hamlibName);
        if (protocolName != null) {
            return protocolName;
        }
        return hamlibName;
    }

}

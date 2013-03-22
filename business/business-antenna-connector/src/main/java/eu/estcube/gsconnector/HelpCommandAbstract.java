package eu.estcube.gsconnector;

import java.util.Map;

import org.hbird.exchange.core.Parameter;

import eu.estcube.domain.JMSConstants;
import eu.estcube.domain.TelemetryObject;
import eu.estcube.domain.TelemetryParameter;

public abstract class HelpCommandAbstract {

    public TelemetryObject createHelpList(TelemetryObject telemetryObject, String[] messageSplit) {

        String[] messagePiece = { null, null, null };
        /*
         * i starts from 1 because, first line is help name, which is unnecessary.
         */
        for (int i = 1; i < messageSplit.length; i++) {
            if (messageSplit[i].contains(JMSConstants.GS_DEVICE_END_MESSAGE)) {
                break;
            }
            messagePiece = messageSplit[i].split(":");

            telemetryObject.addParameter(new TelemetryParameter(
            		getHelpHashMap().get(messagePiece[0]), 
            		messagePiece[1].substring((messagePiece[1].indexOf("(") + 1), messagePiece[1].indexOf(")"))));
            if (messagePiece.length == 3) {
                telemetryObject.addParameter(new TelemetryParameter(
                		getHelpHashMap().get(messagePiece[1].substring((messagePiece[1].length() - 1))), 
                        messagePiece[2].substring((messagePiece[2].indexOf("(") + 1), messagePiece[2].indexOf(")"))));
            }
        }

        return telemetryObject;
    }

    protected abstract Map<String, String> getHelpHashMap();
}

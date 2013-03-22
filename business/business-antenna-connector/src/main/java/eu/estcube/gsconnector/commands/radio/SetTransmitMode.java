package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetTransmitMode implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+X ");
        messageString.append(command.getParameter("TX Mode"));
        messageString.append(" ");
        messageString.append(command.getParameter("TX Passband"));
        messageString.append("\n");
        return messageString;
    }
}

package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetMemoryChannelNr implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+E ");
        messageString.append(command.getParameter("Memory#"));
        messageString.append("\n");
        return messageString;
    }
}

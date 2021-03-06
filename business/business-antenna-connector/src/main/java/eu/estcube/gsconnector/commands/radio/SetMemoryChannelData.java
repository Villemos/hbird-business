package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetMemoryChannelData implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+H ");
        messageString.append(command.getParameter("Channel"));
        messageString.append("\n");
        return messageString;
    }
}

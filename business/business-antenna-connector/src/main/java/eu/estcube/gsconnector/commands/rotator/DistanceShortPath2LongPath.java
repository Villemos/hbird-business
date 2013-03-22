package eu.estcube.gsconnector.commands.rotator;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class DistanceShortPath2LongPath implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+a ");
        messageString.append(command.getParameter("Short Path km"));
        messageString.append("\n");
        return messageString;
    }
}

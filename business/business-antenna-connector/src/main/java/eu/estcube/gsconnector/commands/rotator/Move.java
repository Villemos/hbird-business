package eu.estcube.gsconnector.commands.rotator;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class Move implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+M ");
        messageString.append(command.getParameter("Direction"));
        messageString.append(" ");
        messageString.append(command.getParameter("Speed"));
        messageString.append("\n");
        return messageString;
    }
}

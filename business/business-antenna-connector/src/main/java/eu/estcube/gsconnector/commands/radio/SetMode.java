package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetMode implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+M ");
        messageString.append(command.getParameter("Mode"));
        messageString.append(" ");
        messageString.append(command.getParameter("Passband"));
        messageString.append("\n");
        return messageString;
    }
}

package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetLevel implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+L ");
        messageString.append(command.getParameter("Level"));
        messageString.append(" ");
        messageString.append(command.getParameter("Level Value"));
        messageString.append("\n");
        return messageString;
    }
}

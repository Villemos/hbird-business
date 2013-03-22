package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetParm implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+P ");
        messageString.append(command.getParameter("Parm"));
        messageString.append(" ");
        messageString.append(command.getParameter("Parm Value"));
        messageString.append("\n");
        return messageString;
    }
}

package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetFunc implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+U ");
        messageString.append(command.getParameter("Func"));
        messageString.append(" ");
        messageString.append(command.getParameter("Func Status"));
        messageString.append("\n");
        return messageString;
    }
}

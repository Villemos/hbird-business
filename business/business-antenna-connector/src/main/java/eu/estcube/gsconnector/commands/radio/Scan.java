package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class Scan implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+g ");
        messageString.append(command.getParameter("Scan Fct"));
        messageString.append(" ");
        messageString.append(command.getParameter("Scan Channel"));
        messageString.append("\n");
        return messageString;
    }
}

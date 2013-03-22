package eu.estcube.gsconnector.commands.rotator;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class Dmm2Dec implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+E ");
        messageString.append(command.getParameter("Degrees"));
        messageString.append(" ");
        messageString.append(command.getParameter("Dec Minutes"));
        messageString.append(" ");
        messageString.append(command.getParameter("S/W"));
        messageString.append("\n");
        return messageString;
    }
}

package eu.estcube.gsconnector.commands.rotator;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class QRB implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+B ");
        messageString.append(command.getParameter("Lon 1"));
        messageString.append(" ");
        messageString.append(command.getParameter("Lat 1"));
        messageString.append(" ");
        messageString.append(command.getParameter("Lon 2"));
        messageString.append(" ");
        messageString.append(command.getParameter("Lat 2") + "\n");
        return messageString;
    }
}

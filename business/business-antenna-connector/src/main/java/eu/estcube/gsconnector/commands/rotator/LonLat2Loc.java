package eu.estcube.gsconnector.commands.rotator;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class LonLat2Loc implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+L ");
        messageString.append(command.getParameter("Longitude"));
        messageString.append(" ");
        messageString.append(command.getParameter("Latitude"));
        messageString.append(" ");
        messageString.append(command.getParameter("Loc Len"));
        messageString.append("\n");
        return messageString;
    }
}

package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class SetSplitVFO implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+S ");
        messageString.append(command.getParameter("Split"));
        messageString.append(" ");
        messageString.append(command.getParameter("TX VFO"));
        messageString.append("\n");
        return messageString;
    }
}

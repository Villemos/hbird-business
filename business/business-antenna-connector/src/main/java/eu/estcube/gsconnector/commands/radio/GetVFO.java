package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class GetVFO implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+v\n");
        return messageString;
    }
}

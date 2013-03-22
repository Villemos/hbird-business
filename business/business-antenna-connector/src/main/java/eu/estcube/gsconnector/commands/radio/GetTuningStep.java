package eu.estcube.gsconnector.commands.radio;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class GetTuningStep implements HamlibCommandBuilder {

    public StringBuilder createMessageString(TelemetryCommand command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+n\n");
        return messageString;
    }
}

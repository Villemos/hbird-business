package eu.estcube.gsconnector.commands.radio;

import org.hbird.exchange.core.Command;

import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class ConvertMW2Power implements HamlibCommandBuilder {

    public StringBuilder createMessageString(Command command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+4 ");
        messageString.append(command.getArgumentValue("Power mW"));
        messageString.append(" ");
        messageString.append(command.getArgumentValue("Frequency"));
        messageString.append(" ");
        messageString.append(command.getArgumentValue("Mode"));
        messageString.append("\n");
        return messageString;
    }
}

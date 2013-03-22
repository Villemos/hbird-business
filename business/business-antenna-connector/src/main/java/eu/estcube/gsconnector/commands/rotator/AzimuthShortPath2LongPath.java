package eu.estcube.gsconnector.commands.rotator;

import org.hbird.exchange.core.Command;

import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class AzimuthShortPath2LongPath implements HamlibCommandBuilder {

    public StringBuilder createMessageString(Command command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+A ");
        messageString.append(command.getArgumentValue("Short Path Deg"));
        messageString.append("\n");
        return messageString;
    }
}

package eu.estcube.gsconnector.commands.radio;

import org.hbird.exchange.core.Command;

import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class GetMode implements HamlibCommandBuilder {

    public StringBuilder createMessageString(Command command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+m\n");
        return messageString;
    }
}

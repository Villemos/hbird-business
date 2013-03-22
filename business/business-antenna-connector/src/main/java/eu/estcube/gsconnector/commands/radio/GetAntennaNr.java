package eu.estcube.gsconnector.commands.radio;

import org.hbird.exchange.core.Command;

import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class GetAntennaNr implements HamlibCommandBuilder {

    public StringBuilder createMessageString(Command command) {
        StringBuilder messageString = new StringBuilder();
        messageString.append("+y\n");
        return messageString;
    }
}

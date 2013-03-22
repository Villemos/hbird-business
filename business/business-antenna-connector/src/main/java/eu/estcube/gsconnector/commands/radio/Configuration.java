package eu.estcube.gsconnector.commands.radio;

import org.hbird.exchange.core.Command;

import eu.estcube.gsconnector.commands.HamlibCommandBuilder;

public class Configuration implements HamlibCommandBuilder {

    public StringBuilder createMessageString(Command command) {
        StringBuilder messageString = new StringBuilder();
        return messageString.append("+3\n");
    }
}
